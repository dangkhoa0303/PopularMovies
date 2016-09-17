package com.example.android.popularmovies.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.popularmovies.Adapters.GridViewAdapter;
import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.Network.CheckNetworkConnection;
import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MoviesContract;
import com.example.android.popularmovies.service.FetchMoviesService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dell on 11/13/2015.
 */

public class MoviesFragment extends Fragment {

    private GridViewAdapter adapter;
    private ArrayList<MovieProperties> list_movie;
    private GridView gridView;
    private CustomSwipeRefreshLayout mSwipeRefreshLayout;

    private static String sort_by_popularity = "popularity.desc", sort_by_rates = "vote_average.desc";
    private String sort_by;

    private static String MOVIE_KEY = "movie"; // used for saved instance
    private static String SORT_KEY = "SORT_ORDER";

    private CheckNetworkConnection checkNetworkConnection;
    private MoviesServiceReceiver receiver;

    public interface CallBack {
        public void onItemSelected(MovieProperties movie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIE_KEY, list_movie);
        outState.putString(SORT_KEY, sort_by);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter(MoviesServiceReceiver.MOVIES_LIST_RECEIVER);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new MoviesServiceReceiver();
        getContext().registerReceiver(receiver, intentFilter);

        checkNetworkConnection = new CheckNetworkConnection(getActivity());
        // if don't initialize adapter in onCreate, all the methods relating to adapter will cause error because if null object (adapter)
        adapter = new GridViewAdapter(getActivity(), new ArrayList<MovieProperties>());

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_KEY)) {
            list_movie = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
            sort_by = savedInstanceState.getString(SORT_KEY);
        } else {
            list_movie = new ArrayList<>();
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_new, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);
        mSwipeRefreshLayout = (CustomSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.red);
        mSwipeRefreshLayout.setGridView(gridView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(sort_by);
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((CallBack) getActivity())
                        .onItemSelected(list_movie.get(position));
            }
        });
        if (list_movie.size()==0 && savedInstanceState==null) {
            sort_by = sort_by_popularity;
            refresh(sort_by);
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
        inflater.inflate(R.menu.menu_movies_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.most_popular) {
            sort_by = sort_by_popularity;
            refresh(sort_by);
        }

        return super.onOptionsItemSelected(item);
    }

    public void refresh(String sort_by) {
        if (checkNetworkConnection.isNetworkAvailable()) {
            mSwipeRefreshLayout.setRefreshing(true);
            Intent intent = new Intent(getActivity(), FetchMoviesService.class);
            intent.putExtra(FetchMoviesService.SORT_BY, sort_by);
            getContext().startService(intent);
        } else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog
                    .setTitle("Oops! No internet connection!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();
            //mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void updateUI(ArrayList<MovieProperties> list, GridViewAdapter adapter) {
        if (adapter != null) {
            adapter.clear();
        }
        adapter.addAll(list);
        gridView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public class MoviesServiceReceiver extends BroadcastReceiver {
        public static final String MOVIES_LIST_RECEIVER = "com.example.android.movies";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (list_movie != null) {
                list_movie.clear();
            }
            list_movie = intent.getParcelableArrayListExtra(FetchMoviesService.KEY_RESPONSE);
            updateUI(list_movie, adapter);
        }
    }
}
