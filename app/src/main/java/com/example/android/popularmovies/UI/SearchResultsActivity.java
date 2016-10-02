package com.example.android.popularmovies.UI;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.android.popularmovies.Adapters.MoviesAdapter;
import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Services.FetchSearchMovies;
import com.example.android.popularmovies.Util;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {
    private SearchMoviesReceiver receiver;
    private Toolbar toolbar;
    private ListView mListView;
    private ProgressBar progressBar;

    private String query;
    private ArrayList<MovieProperties> list;
    private MoviesAdapter adapter;

    private static String SAVE_LIST = "save list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_results);
        toolbar = (Toolbar) findViewById(R.id.toolBarResults);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBarResults);
        progressBar.setVisibility(View.INVISIBLE);
        mListView = (ListView) findViewById(R.id.listViewResults);
        mListView.setAdapter(adapter);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            query = intent.getDataString();
        }

        adapter = new MoviesAdapter(this, new ArrayList<MovieProperties>(), MoviesAdapter.SEARCH_MOVIES_ITEM);

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVE_LIST)) {
            list = savedInstanceState.getParcelableArrayList(SAVE_LIST);
            updateUI(list);
        } else {
            list = new ArrayList<>();
            search(query);
        }

        IntentFilter intentFilter = new IntentFilter(SearchMoviesReceiver.SEARCH_MOVIES_LIST_RECEIVER);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new SearchMoviesReceiver();
        registerReceiver(receiver, intentFilter);

        toolbar.setTitle(query);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_LIST, list);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void search(String query) {
        if (Util.isNetworkAvailable(this)) {
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(this, FetchSearchMovies.class);
            intent.putExtra(FetchSearchMovies.SEARCH, query);
            startService(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Oops! No internet connection!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private void updateUI(ArrayList<MovieProperties> list) {
        if (adapter != null) {
            adapter.clear();
        }
        adapter.addAll(list);
        mListView.setAdapter(adapter);
        if (list.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No results found!")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    public class SearchMoviesReceiver extends BroadcastReceiver {
        public static final String SEARCH_MOVIES_LIST_RECEIVER = "com.example.android.searchmovies";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (list != null) {
                list.clear();
            }
            list = intent.getParcelableArrayListExtra(FetchSearchMovies.KEY_RESPONSE_SEARCH);
            updateUI(list);
        }
    }
}
