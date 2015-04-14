package com.scottiebiddle.valdezbreakingnews.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scottiebiddle.valdezbreakingnews.R;
import com.scottiebiddle.valdezbreakingnews.models.Story;

/**
 * Created by scottie on 4/13/15.
 */
public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder> {

    private Cursor mCursor;

    public NewsFeedAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public  NewsFeedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.newsfeed_list_item, viewGroup, false);
        return new NewsFeedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsFeedViewHolder viewHolder, int i) {
        mCursor.moveToPosition(i);
        Story story = Story.fromCursor(mCursor);
        viewHolder.mBodyText.setText(story.eventBody);
        viewHolder.mHeadlineText.setText(story.eventType);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    public static class NewsFeedViewHolder extends RecyclerView.ViewHolder {

        TextView mBodyText;
        TextView mHeadlineText;

        public NewsFeedViewHolder(View itemView) {
            super(itemView);
            mBodyText = (TextView) itemView.findViewById(R.id.body);
            mHeadlineText = (TextView) itemView.findViewById(R.id.headline);
        }
    }
}
