package com.example.android.popularmovies.Services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import com.example.android.popularmovies.UI.MostPopularMoviesFragment;
import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.Properties.MovieProperties;

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
 * Created by Dell on 8/11/2016.
 */
public class FetchMoviesService extends IntentService {
    public static String SORT_BY = "SORT_BY";
    public static String KEY_RESPONSE_POPULAR_MOVIES = "key_response_popular_movies";
    private ArrayList<MovieProperties> list_movie;

    public FetchMoviesService() {
        super("PopularMovies");
        list_movie = new ArrayList<>();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String type = intent.getStringExtra(SORT_BY);

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String json = null;
        try {
            String URL_BASE = "https://api.themoviedb.org/3/discover/movie?";
            String SORT = "sort_by";
            String API = "api_key";

            Uri uriBuilder = Uri.parse(URL_BASE)
                    .buildUpon()
                    .appendQueryParameter(SORT, type)
                    .appendQueryParameter(API, BuildConfig.OPEN_API)
                    .build();

            URL url = new URL(uriBuilder.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream == null) {
                // do nothing here
                return;
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

        if (json != null) {
            list_movie.addAll(getDataFromJsonString(json));
        } else
            return;

        Intent broadcastIntent = new Intent();
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.setAction(MostPopularMoviesFragment.PopularMoviesServiceReceiver.POPULAR_MOVIES_LIST_RECEIVER);
        broadcastIntent.putParcelableArrayListExtra(KEY_RESPONSE_POPULAR_MOVIES, list_movie);
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
