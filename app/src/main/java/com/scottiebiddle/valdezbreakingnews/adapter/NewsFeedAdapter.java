package com.scottiebiddle.valdezbreakingnews.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public NewsFeedAdapter(SquidCursor<Story> cursor) {
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
        Story story = new Story(mCursor);
        viewHolder.mBodyText.setText(story.getEventBody());
        viewHolder.mHeadlineText.setText(story.getEventType());
        long importDate = story.getImportTime();
        Date date = new Date(importDate);
        viewHolder.mDateline.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(date));

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    public static class NewsFeedViewHolder extends RecyclerView.ViewHolder {

        TextView mBodyText;
        TextView mHeadlineText;
        TextView mDateline;

        public NewsFeedViewHolder(View itemView) {
            super(itemView);
            mBodyText = (TextView) itemView.findViewById(R.id.body);
            mHeadlineText = (TextView) itemView.findViewById(R.id.headline);
            mDateline = (TextView) itemView.findViewById(R.id.event_date);
        }
    }
}
