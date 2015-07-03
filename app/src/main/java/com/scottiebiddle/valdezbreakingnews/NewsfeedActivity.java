package com.scottiebiddle.valdezbreakingnews;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.scottiebiddle.valdezbreakingnews.adapter.NewsFeedAdapter;
import com.scottiebiddle.valdezbreakingnews.adapter.NewsFeedAdapter.OnFavoriteChangedListener;
import com.scottiebiddle.valdezbreakingnews.adapter.StoriesDao;
import com.scottiebiddle.valdezbreakingnews.db.Story;
import com.yahoo.squidb.data.SquidCursor;
import com.yahoo.squidb.sql.Query;


public class NewsfeedActivity extends Activity {

    private NewsFeedFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);
        mFragment = new NewsFeedFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mFragment)
                    .commit();
        }
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
    public static class NewsFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
            NewsStoryDataService.OnNewsStoryRefreshCompleteListener,
            LoaderManager.LoaderCallbacks<Cursor>,
            OnFavoriteChangedListener{

        public NewsFeedFragment() {
        }

        private SwipeRefreshLayout mSwipeRefreshLayout;
        private RecyclerView mRecyclerView;
        private NewsFeedAdapter mAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_newsfeed, container, false);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.newsFeed);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.app_700, R.color.app_500, R.color.accent_200, R.color.app_100);
            NewsStoryDataService nds = new NewsStoryDataService(getActivity());
            nds.setListener(this);
            nds.downloadAndPersistStories();
            return rootView;
        }

        @Override
        public void onStart() {
            super.onStart();
            mAdapter = new NewsFeedAdapter(null);
            mAdapter.setmListener(this);
            mRecyclerView.setAdapter(mAdapter);
            getLoaderManager().initLoader(1, null, this);
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onRefresh() {
            NewsStoryDataService nds = new NewsStoryDataService(getActivity());
            nds.setListener(this);
            nds.downloadAndPersistStories();
        }

        @Override
        public void onRefreshComplete() {
            mSwipeRefreshLayout.setRefreshing(false);
            getLoaderManager().restartLoader(1, null, this);

        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Cursor>(getActivity()) {

                @Override
                public Cursor loadInBackground() {
                    StoriesDao mStoriesDao = new StoriesDao(getContext());
                    return mStoriesDao.query(Story.class, Query.select(Story.PROPERTIES).orderBy(Story.IMPORT_TIME.desc()));
                }

                @Override
                public void onStartLoading() {
                    forceLoad();
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor((SquidCursor<Story>) data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }


        @Override
        public void onFavoriteChanged() {
            getLoaderManager().restartLoader(1, null, this);
        }
    }


}
