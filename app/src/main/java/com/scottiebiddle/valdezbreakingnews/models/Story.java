package com.scottiebiddle.valdezbreakingnews.models;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by scottie on 4/13/15.
 */
public class Story {

    public long id;
    public String eventType;
    public String eventBody;
    public long importDate;
//    public boolean isBroadcast;

    public static final String[] COLUMNS = {
            "_id",
            "eventType",
            "eventBody",
            "importDate",
    };

    public static final String TABLE_NAME = "stories";


    public Story(long id, String eventType, String eventBody, long importDate, boolean isBroadcast) {
        this.id = id;
        this.eventType = eventType;
        this.eventBody = eventBody;
        this.importDate = importDate;
    }

    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("_id", id);
        values.put("eventType", eventType);
        values.put("eventBody", eventBody);
        values.put("importDate", importDate);
        return values;
    }

    public Story() {}

    public static Story fromCursor(Cursor cursor) {
        Story story = new Story();
        int index = cursor.getColumnIndex("_id");
        story.id = cursor.getLong(index);
        index = cursor.getColumnIndex("eventType");
        story.eventType = cursor.getString(index);
        index = cursor.getColumnIndex("eventBody");
        story.eventBody = cursor.getString(index);
        index = cursor.getColumnIndex("importDate");
        story.importDate = cursor.getLong(index);
        return story;
    }

}
