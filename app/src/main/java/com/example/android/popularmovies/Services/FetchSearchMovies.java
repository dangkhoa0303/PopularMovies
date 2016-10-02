package com.example.android.popularmovies.Services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.UI.MainActivity;
import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.UI.SearchResultsActivity;

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
 * Created by Dell on 10/1/2016.
 */

public class FetchSearchMovies extends IntentService {
    public static String SEARCH = "search";
    public static String KEY_RESPONSE_SEARCH = "key_response_search";
    private ArrayList<MovieProperties> list;

    public FetchSearchMovies() {
        super("Search movies");
        list = new ArrayList<>();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String search = intent.getStringExtra(SEARCH);

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String json = null;

        try {
            String URL_BASE = "https://api.themoviedb.org/3/search/movie?";
            String API = "api_key";
            String QUERY = "query";

            Uri uriBuilder = Uri.parse(URL_BASE)
                    .buildUpon()
                    .appendQueryParameter(API, BuildConfig.OPEN_API)
                    .appendQueryParameter(QUERY, search)
                    .build();

            URL url = new URL(uriBuilder.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream==null) {
                return;
            }

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line=bufferedReader.readLine())!=null) {
                stringBuilder.append(line+"\n");
            }
            json = stringBuilder.toString();

        } catch (Exception e) {

        } finally {
            if (httpURLConnection!=null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader!=null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (json!=null) {
            list.addAll(getDataFromJsonString(json));
        } else {
            return;
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.setAction(SearchResultsActivity.SearchMoviesReceiver.SEARCH_MOVIES_LIST_RECEIVER);
        broadcastIntent.putParcelableArrayListExtra(KEY_RESPONSE_SEARCH, list);
        sendBroadcast(broadcastIntent);
    }

    protected ArrayList<MovieProperties> getDataFromJsonString(String jsonString) {
        ArrayList<MovieProperties> list = new ArrayList<>();

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
                list.add(movieProperties);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
