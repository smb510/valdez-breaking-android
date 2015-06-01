package com.scottiebiddle.valdezbreakingnews.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scottiebiddle.valdezbreakingnews.R;
import com.scottiebiddle.valdezbreakingnews.db.Story;
import com.yahoo.squidb.data.SquidCursor;

import java.text.DateFormat;
import java.util.Date;


/**
 * Created by scottie on 4/13/15.
 */
public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder> {

    private SquidCursor<Story> mCursor;
    public boolean isDown = false;

    public NewsFeedAdapter(SquidCursor<Story> cursor) {
        mCursor = cursor;
    }

    public interface OnFavoriteChangedListener {
        void onFavoriteChanged();
    }

    private OnFavoriteChangedListener mListener;

    public void setmListener(OnFavoriteChangedListener mListener) {
        this.mListener = mListener;
    }

    public void swapCursor(SquidCursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public  NewsFeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsfeed_list_item, viewGroup, false);
        return new NewsFeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final NewsFeedViewHolder viewHolder, int i) {
        mCursor.moveToPosition(i);
        final Story story = new Story(mCursor);
        viewHolder.mBodyText.setText(story.getEventBody());
        viewHolder.mHeadlineText.setText(story.getEventType());
        long importDate = story.getImportTime();
        Date date = new Date(importDate);
        viewHolder.mDateline.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(date));
        if (story.isFavorite()) {
            viewHolder.mFavoriteButton.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            viewHolder.mFavoriteButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        viewHolder.mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, story.getEventType() + "\n" + story.getEventBody());
                i.setType("text/plain");
                v.getContext().startActivity(Intent.createChooser(i, "Share Story"));
            }
        });

        viewHolder.mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoriesDao dao = new StoriesDao(v.getContext());
                if (story.isFavorite()) {
                    story.setIsFavorite(false);
                    dao.persist(story);
                } else {
                    story.setIsFavorite(true);
                    dao.persist(story);
                }
                if (mListener != null) {
                    mListener.onFavoriteChanged();
                }
            }
        });

        viewHolder.mStoryContainer.setOnTouchListener(new View.OnTouchListener() {

            private float downTouchX;

            private float deltaXThreshold = viewHolder.mActionsContainer.getWidth();
            private float width = viewHolder.mStoryContainer.getWidth();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isDown = true;
                        downTouchX = event.getX();
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        Log.d("SCOTTIE", "cancel!");
                    case MotionEvent.ACTION_UP:
                        isDown = false;
                        float translationX = viewHolder.mStoryContainer.getTranslationX();
                        if (translationX <= (-1.0f * deltaXThreshold) / 2.0f) {
                            viewHolder.mStoryContainer.animate()
                                    .translationX(-1 * deltaXThreshold)
                                    .setInterpolator(new OvershootInterpolator())
                                    .setDuration(100)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            viewHolder.mStoryContainer.setTranslationX(-1 * deltaXThreshold);
                                        }
                                    })
                                    .start();
                        } else {
                            viewHolder.mStoryContainer.animate()
                                    .translationX(0)
                                    .setInterpolator(new OvershootInterpolator())
                                    .setDuration(100)
                                    .withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            viewHolder.mStoryContainer.setTranslationX(0);
                                        }
                                    })
                                    .start();
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        float delta = event.getX() - downTouchX;
                        if (delta < 0) {
                            float maxTranslation = -1 * (deltaXThreshold);
                            float translation = Math.min(0, Math.max(delta, maxTranslation));
                            viewHolder.mStoryContainer.setTranslationX(translation);
                        } else if (viewHolder.mStoryContainer.getTranslationX() < 0) {
                            float currentTranslation = viewHolder.mStoryContainer.getTranslationX();
                            viewHolder.mStoryContainer.setTranslationX(Math.min(0, currentTranslation + delta));

                        }
                        return true;

                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }


    public static class NewsFeedViewHolder extends RecyclerView.ViewHolder {

        TextView mBodyText;
        TextView mHeadlineText;
        TextView mDateline;
        ImageButton mShareButton;
        ImageButton mFavoriteButton;
        LinearLayout mActionsContainer;
        RelativeLayout mStoryContainer;

        public NewsFeedViewHolder(View itemView) {
            super(itemView);
            mBodyText = (TextView) itemView.findViewById(R.id.body);
            mHeadlineText = (TextView) itemView.findViewById(R.id.headline);
            mDateline = (TextView) itemView.findViewById(R.id.event_date);
            mShareButton = (ImageButton) itemView.findViewById(R.id.share_button);
            mFavoriteButton = (ImageButton) itemView.findViewById(R.id.favorite_button);
            mActionsContainer = (LinearLayout) itemView.findViewById(R.id.action_container);
            mStoryContainer = (RelativeLayout) itemView.findViewById(R.id.story_container);
        }
    }
}
