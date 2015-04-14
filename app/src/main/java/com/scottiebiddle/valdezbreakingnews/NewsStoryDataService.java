package com.scottiebiddle.valdezbreakingnews;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.scottiebiddle.valdezbreakingnews.db.StoriesDataSource;
import com.scottiebiddle.valdezbreakingnews.models.Story;

import org.json.JSONArray;

/**
 * Created by scottie on 4/13/15.
 * Object responsible for fetching and persisting news stories.
 */
public class NewsStoryDataService {
    public static final String BASE_URL = "http://10.0.1.23:9000";
    private Context mContext;
    private RequestQueue mQueue;

    public NewsStoryDataService(Context context) {
        mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
    }

    public void downloadAndPersistStories() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BASE_URL + "/stories",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Story[] stories = gson.fromJson(response.toString(), Story[].class);
                        StoriesDataSource dataSource = null;
                        try {
                            dataSource = new StoriesDataSource(mContext);
                            dataSource.open();
                            for(Story story : stories) {
                                dataSource.insertStory(story);
                            }
                        } finally {
                            if (dataSource != null) {
                                dataSource.close();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SCOTTIE", error.toString());
            }
        });
        mQueue.add(jsonArrayRequest);
    };

}
