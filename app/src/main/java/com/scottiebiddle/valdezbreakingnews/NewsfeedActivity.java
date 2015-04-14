package com.scottiebiddle.valdezbreakingnews;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.scottiebiddle.valdezbreakingnews.adapter.NewsFeedAdapter;
import com.scottiebiddle.valdezbreakingnews.db.StoriesDataSource;


public class NewsfeedActivity extends Activity {

    private PlaceholderFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        mFragment = new PlaceholderFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mFragment)
                    .commit();
        }
        new NewsStoryDataService(this).downloadAndPersistStories();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_newsfeed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        private RecyclerView mRecyclerView;
        private Cursor mCursor;
        private StoriesDataSource mDataSource;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.newsFeed);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            StoriesDataSource mDataSource = new StoriesDataSource(getActivity());
            mDataSource.open();
            mCursor = mDataSource.getStories();
            NewsFeedAdapter adapter = new NewsFeedAdapter(mCursor);
            mRecyclerView.setAdapter(adapter);

        }

        @Override
        public void onStop() {
            super.onStop();
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
            if (mDataSource != null) {
                mDataSource.close();
            }
        }

    }
}
