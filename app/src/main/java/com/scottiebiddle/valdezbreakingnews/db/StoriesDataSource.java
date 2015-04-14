package com.scottiebiddle.valdezbreakingnews.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.scottiebiddle.valdezbreakingnews.models.Story;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by scottie on 4/13/15.
 */
public class StoriesDataSource {

    private Set<Cursor> mCursors;

    // Database fields
    private SQLiteDatabase database;
    private BreakingValdezSqliteOpenHelper dbHelper;


    public StoriesDataSource(Context context) {
        dbHelper = new BreakingValdezSqliteOpenHelper(context);
        mCursors = new HashSet<>();
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertStory(Story story) {
        long result = database.insertWithOnConflict("stories", null, story.toContentValues(), SQLiteDatabase.CONFLICT_IGNORE);
        if (result > 0) {

        }
        return result;
    }

    public Cursor getStories() {
        Cursor cursor = database.query(Story.TABLE_NAME, Story.COLUMNS, null, null, null, null, "_id ASC");
        return cursor;
    }


}
