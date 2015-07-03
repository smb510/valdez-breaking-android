package com.scottiebiddle.valdezbreakingnews.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by scottie on 7/2/15.
 */
public class StoriesItemViewHelper extends ItemTouchHelper {
    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p/>
     */
    public StoriesItemViewHelper() {
        super(new SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {


                Log.d("SCOTTIEBUG", "swiped: " + direction);
            }
        });
    }
}
