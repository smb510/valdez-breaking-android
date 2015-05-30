package com.scottiebiddle.valdezbreakingnews.adapter;

import android.content.Context;

import com.scottiebiddle.valdezbreakingnews.db.StoriesDatabase;
import com.yahoo.squidb.data.DatabaseDao;

/**
 * Created by scottie on 5/27/15.
 */
public class StoriesDao extends DatabaseDao {

    public StoriesDao(Context context) {
        super(new StoriesDatabase(context));
    }
}
