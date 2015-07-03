package com.scottiebiddle.valdezbreakingnews.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        viewHolder.mRootView.setOnClickListener(new View.OnClickListener() {
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

        viewHolder.mRootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, story.getEventType() + "\n" + story.getEventBody());
                viewHolder.mStoryContainer.getContext().startActivity(share);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }


    public static class NewsFeedViewHolder extends RecyclerView.ViewHolder {

        View mRootView;
        TextView mBodyText;
        TextView mHeadlineText;
        TextView mDateline;
//        Button mShareButton;
        ImageView mFavoriteButton;
        RelativeLayout mStoryContainer;

        public NewsFeedViewHolder(View itemView) {
            super(itemView);
            mRootView = itemView.findViewById(R.id.root_view);
            mBodyText = (TextView) itemView.findViewById(R.id.body);
            mHeadlineText = (TextView) itemView.findViewById(R.id.headline);
            mDateline = (TextView) itemView.findViewById(R.id.event_date);
//            mShareButton = (Button) itemView.findViewById(R.id.share_button);
            mFavoriteButton = (ImageView) itemView.findViewById(R.id.favorite_button);
            mStoryContainer = (RelativeLayout) itemView.findViewById(R.id.story_container);
        }
    }
}
