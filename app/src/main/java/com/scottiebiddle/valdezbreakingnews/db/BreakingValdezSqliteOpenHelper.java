package com.scottiebiddle.valdezbreakingnews.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by scottie on 4/13/15.
 */
public class BreakingValdezSqliteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 1;

    private static final String STORIES_NAME = "stories";

    private static final String CREATE_STORIES = "create table " + STORIES_NAME +
            "(_id integer primary key not null, " +
            " eventType varchar(255) not null, " +
            " eventBody varchar(2000) not null, " +
            " importDate long not null);";

    public BreakingValdezSqliteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STORIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion <= 1) {
            db.execSQL("drop table " + STORIES_NAME);
            db.execSQL(CREATE_STORIES);
        }
    }
}
