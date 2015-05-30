package com.scottiebiddle.valdezbreakingnews.db;

import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.TableModelSpec;

/**
 * Created by scottie on 4/15/15.
 */
@TableModelSpec(tableName = "stories", className="Story")
public class StorySpec {

    public String eventType;
    public String eventBody;
    public long importTime;
    @ColumnSpec(constraints = "UNIQUE")
    public long storyId;

}
