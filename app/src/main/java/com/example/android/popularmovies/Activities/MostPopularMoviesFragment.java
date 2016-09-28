package com.example.android.popularmovies.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.popularmovies.Adapters.MoviesAdapter;
import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Util;
import com.example.android.popularmovies.service.FetchMoviesService;

import java.util.ArrayList;

/**
 * Created by Dell on 11/13/2015.
 */

public class MostPopularMoviesFragment extends Fragment {

    private MoviesAdapter adapter;
    private ArrayList<MovieProperties> list_movie;
    private ListView listView;
    private CustomSwipeRefreshLayout mSwipeRefreshLayout;

    private static String sort_by_popularity = "popularity.desc";
    private String type;

    private static String MOVIE_KEY = "movie"; // used for saved instance
    private static String TYPE = "USER_CHOOSING_TYPE";

    private PopularMoviesServiceReceiver receiver;

    public interface PopularMoviesCallBack {
        void onPopularMoviesItemSelected(MovieProperties movie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_KEY, list_movie);
        outState.putString(TYPE, type);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter(PopularMoviesServiceReceiver.POPULAR_MOVIES_LIST_RECEIVER);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new PopularMoviesServiceReceiver();
        getContext().registerReceiver(receiver, intentFilter);

        // if don't initialize adapter in onCreate, all the methods relating to adapter will cause error because if null object (adapter)
        adapter = new MoviesAdapter(getActivity(), new ArrayList<MovieProperties>());

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_KEY)) {
            list_movie = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
            type = savedInstanceState.getString(TYPE);
        } else {
            list_movie = new ArrayList<>();
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movies_layout, container, false);

        listView = (ListView) view.findViewById(R.id.listView);
        mSwipeRefreshLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
        mSwipeRefreshLayout.setListView(listView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(type);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((PopularMoviesCallBack) getActivity())
                        .onPopularMoviesItemSelected(list_movie.get(position));
            }
        });
        if (list_movie.size() == 0 && savedInstanceState == null) {
            type = sort_by_popularity;
            refresh(type);
        } else {
            updateUI(list_movie, adapter);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_movies_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void refresh(String type) {
        if (!Util.isNetworkAvailable(getContext())) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog
                    .setTitle("Oops! No internet connection!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
            Intent intent = new Intent(getActivity(), FetchMoviesService.class);
            intent.putExtra(FetchMoviesService.SORT_BY, type);
            getContext().startService(intent);
        }
    }

    private void updateUI(ArrayList<MovieProperties> list, MoviesAdapter adapter) {
        if (adapter != null) {
            adapter.clear();
        }
        adapter.addAll(list);
        listView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public class PopularMoviesServiceReceiver extends BroadcastReceiver {
        public static final String POPULAR_MOVIES_LIST_RECEIVER = "com.example.android.mostpopularmovies";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (list_movie != null) {
                list_movie.clear();
            }
            list_movie = intent.getParcelableArrayListExtra(FetchMoviesService.KEY_RESPONSE_POPULAR_MOVIES);
            updateUI(list_movie, adapter);
        }
    }
}