package com.example.android.popularmovies.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/*
onCreate will launch first, next is onCreateView, and then onStart is called.
 */

public class MoviesFragment extends Fragment {

    private GridViewAdapter adapter; // custom adapter for GridView
    public static ArrayList<MovieProperties> list_movie = new ArrayList<>(); // list_movies contains all the movies' information
    private GridView gridView;
    public String sort_by_popularity = "popularity.desc", sort_by_rates = "vote_average.desc";
    public String sort_by;
    private String MOVIE_KEY = "movie"; // used for saved instance
    private CheckNetworkConnection checkNetworkConnection;
    private ProgressBar progressBar;
    public boolean sortCheck;
    public String SORT_KEY = "SORT_ORDER";

    public interface CallBack {
        public void onItemSelected(MovieProperties movie);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkNetworkConnection = new CheckNetworkConnection(getActivity());
        // if don't initialize adpater in onCreate, all the methods relating to adapter will cause error because if null object (adapter)
        adapter = new GridViewAdapter(getActivity(), new ArrayList<MovieProperties>());

        if (savedInstanceState != null) {
            list_movie = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
            adapter.addAll(list_movie);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_KEY, list_movie); // saved the list of movies on the first time run with INTERNET CONNECTION
        outState.putBoolean(SORT_KEY, sortCheck);
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
            UpdateTask(sort_by);
            sortCheck = true;
        }
        if (id == R.id.highest_rated) {
            sort_by = sort_by_rates;
            UpdateTask(sort_by);
            sortCheck = false;
        }
        return super.onOptionsItemSelected(item);
    }

    public void UpdateTask(String sort_by) {
        if (checkNetworkConnection.isNetworkAvailable()) { // if there is internet connection, execute
            MovieTask movieTask = new MovieTask();
            progressBar.setVisibility(View.VISIBLE);
            movieTask.setProgressBar(progressBar);
            movieTask.execute(sort_by);
        } else { // otherwise, show a dialog to announce user about internet connection

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
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);

        progressBar = (ProgressBar) view.findViewById(R.id.Progressbar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), list_movie.get(position).getTitle(), Toast.LENGTH_SHORT).show();

                ((CallBack) getActivity())
                        .onItemSelected(list_movie.get(position));
            }
        });

        if (list_movie.size() == 0) {
            if (savedInstanceState != null && savedInstanceState.containsKey(SORT_KEY)) {
                if (savedInstanceState.getBoolean(SORT_KEY)) {
                    sort_by = sort_by_popularity;
                    UpdateTask(sort_by);
                } else {
                    sort_by = sort_by_rates;
                    UpdateTask(sort_by);
                }
            } else {
                sort_by = sort_by_popularity;
                UpdateTask(sort_by);
            }
            //sortCheck = true;
        } else {
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    public class MovieTask extends AsyncTask<String, Integer, ArrayList<MovieProperties>> {

        ProgressBar bar;
        public void setProgressBar(ProgressBar bar) {
            this.bar = bar;
        }

        // this method is used to get information from the json string
        protected ArrayList<MovieProperties> getDataFromJsonString(String jsonString) {
            list_movie = new ArrayList<>();

            String LIST = "results";
            String TITLE = "original_title";
            String OVERVIEW = "overview";
            String POSTER_PATH = "poster_path";
            String BACKDROP_PATH = "backdrop_path";
            String VOTE_AVERAGE = "vote_average";
            String VOTE_COUNT = "vote_count";
            String RELEASE_DATE = "release_date";

            String ID = "id";

            try {
                JSONObject theMovieDb = new JSONObject(jsonString);
                JSONArray movies = theMovieDb.getJSONArray(LIST); // get the list of json objects

                for (int i = 0; i < movies.length(); i++) {
                    JSONObject movie = movies.getJSONObject(i);
                    MovieProperties movieProperties = new MovieProperties();

                    // set attributes for specific movie
                    movieProperties.setTitle(movie.getString(TITLE));

                    // some movies don't have overview
                    if (!movie.getString(OVERVIEW).equals("null")) {
                        movieProperties.setOverview(movie.getString(OVERVIEW));
                    } else {
                        movieProperties.setOverview("No content");
                    }

                    // set the complete url path for the poster of the movie
                    String BASE_URL = "http://image.tmdb.org/t/p/";

                    StringBuilder builder = new StringBuilder(BASE_URL);
                    builder.append("w342/").append(movie.getString(POSTER_PATH));
                    movieProperties.setPoster_path(builder.toString());

                    StringBuilder builder1 = new StringBuilder(BASE_URL);
                    builder1.append("w500/").append(movie.getString(BACKDROP_PATH));
                    movieProperties.setBackdrop_path(builder1.toString());

                    movieProperties.setVote_average(movie.getDouble(VOTE_AVERAGE));
                    movieProperties.setVote_count(movie.getInt(VOTE_COUNT));

                    movieProperties.setId(movie.getInt(ID));

                    // some movies don't have release date
                    if (!movie.getString(RELEASE_DATE).equals("null")) {
                        movieProperties.setRelease_date(movie.getString(RELEASE_DATE));
                    } else {
                        movieProperties.setRelease_date("Unknown");
                    }
                    list_movie.add(movieProperties);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list_movie;
        }

        @Override
        /**
         * do in background
         * get json string
         * return a list of movies' information
         */
        protected ArrayList<MovieProperties> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String json = null;

            try {

                String URL_BASE = "http://api.themoviedb.org/3/discover/movie?";
                String SORT = "sort_by";
                String API = "api_key";

                Uri uriBuilder = Uri.parse(URL_BASE).buildUpon()
                        .appendQueryParameter(SORT, params[0])
                        .appendQueryParameter(API, BuildConfig.OPEN_API)
                        .build();

                URL url = new URL(uriBuilder.toString());

                Log.v("HTPP::::", uriBuilder.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null) {
                    // do nothing here
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                json = builder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return getDataFromJsonString(json);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // set progress for progressBar
            if (this.bar != null) {
                bar.setProgress(values[0]);
            }
        }

        @Override
        /**
         * after executing do in background, set movies posters for the GridView using custom adapter
         */
        protected void onPostExecute(ArrayList<MovieProperties> movieProperties) {
            super.onPostExecute(movieProperties);

            list_movie = movieProperties;

            // if list is not empty, clear the previous data in the adapter and replaced with new one
            if (movieProperties != null) {
                adapter.clear();
                for (MovieProperties movie : movieProperties) {
                    adapter.add(movie);
                }
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
