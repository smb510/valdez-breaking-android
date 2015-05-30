package com.scottiebiddle.valdezbreakingnews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yahoo.squidb.data.AbstractDatabase;
import com.yahoo.squidb.sql.Table;

/**
 * Created by scottie on 4/15/15.
 */
public class StoriesDatabase extends AbstractDatabase {
    /**
     * Create a new AbstractDatabase. Subclasses should be sure to call super() in their constructors. Context must not
     * be null. Generally, you should call this constructor with an application context to avoid keeping a reference
     * to an Activity.
     *
     * @param context the Context
     */
    public StoriesDatabase(Context context) {
        super(context);
    }

    @Override
    protected String getName() {
        return "breaking-valdez.db";
    }

    @Override
    protected int getVersion() {
        return 1;
    }

    @Override
    protected Table[] getTables() {
        return new Table[]{ Story.TABLE };
    }

    @Override
    protected void onTablesCreated(SQLiteDatabase db) {

    }

    @Override
    protected boolean onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return false;
    }
}
