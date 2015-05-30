package com.scottiebiddle.valdezbreakingnews;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.scottiebiddle.valdezbreakingnews.adapter.StoriesDao;
import com.scottiebiddle.valdezbreakingnews.models.Story;
import com.yahoo.squidb.sql.Query;
import com.yahoo.squidb.sql.TableStatement;

import org.json.JSONArray;

/**
 * Created by scottie on 4/13/15.
 * Object responsible for fetching and persisting news stories.
 */
public class NewsStoryDataService {
    public static final String BASE_URL = "http://evening-oasis-4196.herokuapp.com";
    private Context mContext;
    private RequestQueue mQueue;
    private OnNewsStoryRefreshCompleteListener mListener;

    public NewsStoryDataService(Context context) {
        mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
    }

    public void downloadAndPersistStories() {

        StoriesDao dao = new StoriesDao(mContext);
        Query query = Query.select(com.scottiebiddle.valdezbreakingnews.db.Story.STORY_ID)
                .from(com.scottiebiddle.valdezbreakingnews.db.Story.TABLE)
                .orderBy(com.scottiebiddle.valdezbreakingnews.db.Story.STORY_ID.desc())
                .limit(1);
        com.scottiebiddle.valdezbreakingnews.db.Story story = dao.fetchByQuery(com.scottiebiddle.valdezbreakingnews.db.Story.class, query);
        long last = -1;
        if (story != null) {
            last = story.getStoryId();
        }


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BASE_URL + "/stories?last=" + last,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Story[] stories = gson.fromJson(response.toString(), Story[].class);
                        StoriesDao dataSource = null;
                            dataSource = new StoriesDao(mContext);
                            int inserted = 0;
                            for(Story story : stories) {
                                com.scottiebiddle.valdezbreakingnews.db.Story dbStory = new com.scottiebiddle.valdezbreakingnews.db.Story();
                                dbStory.setEventBody(story.eventBody);
                                dbStory.setEventType(story.eventType);
                                dbStory.setImportTime(story.importDate);
                                dbStory.setStoryId(story.id);
                                inserted += (dataSource.persistWithOnConflict(dbStory, TableStatement.ConflictAlgorithm.IGNORE) ? 1 : 0);

                            }
                            if (inserted > 0) {
                                Toast.makeText(mContext, inserted + " New Stories Found!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, "Up to Date!", Toast.LENGTH_LONG).show();
                            }
                            if (mListener != null) {
                                mListener.onRefreshComplete();
                            }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SCOTTIE", error.toString());
                Toast.makeText(mContext, "Network error!", Toast.LENGTH_LONG).show();
                if (mListener != null) {
                    mListener.onRefreshComplete();
                }
            }
        });
        mQueue.add(jsonArrayRequest);
    };

    public interface OnNewsStoryRefreshCompleteListener {
        void onRefreshComplete();
    }

    public void setListener(OnNewsStoryRefreshCompleteListener mListener) {
        this.mListener = mListener;
    }

}
