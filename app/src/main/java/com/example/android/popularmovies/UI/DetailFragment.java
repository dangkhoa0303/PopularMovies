package com.example.android.popularmovies.UI;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Adapters.ReviewAdapter;
import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.Data.MoviesContract;
import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.Properties.Review;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Util;
import com.squareup.picasso.Picasso;

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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 12/5/2015.
 */
public class DetailFragment extends Fragment {

    // uri:  content://com.example.android.popularmovies/movie --> access to table "movie" in the database through
    // content provider and content resolver
    private Uri authority = MoviesContract.Movies.CONTENT_URI;
    // variable used to check whether the movie is in favorite list or not
    private boolean check_marked_favorite = false;
    // MovieProperties object
    private MovieProperties movie;
    // used to check if there is bundle arguments from activity
    // in tablet mode, at the first time lanched, no movie is sent to this fragment
    // therefore, no movie id is available for the Asynctask to do => app crashes
    private boolean checkHasValidMovie = false;
    // review list
    private ArrayList<Review> review_list = new ArrayList<>();
    // URL which is used to pass to the Intent in order to launcher implicitly the trailer
    private String TrailerRequestURL = null;
    // adapter to populate the review list
    private ReviewAdapter adapter;
    // key which is used to save instance for the review list
    private String LIST_REVIEW = "list_review";
    // key which is used to pass into the Bundle when the Activity send data to this fragment
    public static String PACKAGE = "Movie_package";
    // progressBar in Review List
    private ProgressBar progressBar;

    // using Butter Knife to cast
    @InjectView(R.id.collapseToolBar)
    CollapsingToolbarLayout titleToolBar;
    @InjectView(R.id.title_text)
    TextView title;
    @InjectView(R.id.release_date_text)
    TextView release_date;
    @InjectView(R.id.overview_text)
    TextView overview;
    @InjectView(R.id.user_rating_text)
    TextView user_rating;

    @InjectView(R.id.poster_thumbnail)
    ImageView poster;
    @InjectView(R.id.backdrop_image)
    ImageView backdrop;

    @InjectView(R.id.mark_favorite)
    ImageView mark_favorite;
    @InjectView(R.id.ratingBar)
    RatingBar rateBar;

    // save state for the review list
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_REVIEW, review_list);
    }

    public void setDetailForLayout() {

        title.setText(movie.getTitle());
        release_date.setText(movie.getRelease_date());
        user_rating.setText(String.valueOf(movie.getVote_average()) + "/10.0");
        overview.setText(movie.getOverview());

        titleToolBar.setTitle(movie.getTitle());

        Picasso.with(getContext())
                .load(movie.getBackdrop_path())
                .placeholder(R.drawable.movie_icon)
                .error(R.drawable.movie_icon)
                .into(backdrop);
        backdrop.setAlpha(0.8f);

        Picasso.with(getContext())
                .load(movie.getPoster_path())
                .placeholder(R.drawable.movie_icon)
                .error(R.drawable.movie_icon)
                .resize(300, 400)
                .into(poster);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        movie = new MovieProperties();

        // get arguments bundle from the activity
        // this fragment can be in MainActivity (HandSet devices) or in DetailActivity (Tablets)
        // therefore, the bundle can be got from MainActivity or from DetailActivity
        Bundle arguments = getArguments();

        // if there is data (which is a MovieProperties object), get it and put it into movie variable
        if (arguments != null) {

            movie = arguments.getParcelable(DetailFragment.PACKAGE);
            checkHasValidMovie = true;

        } else {

            Cursor cursor = queryMovies();
            cursor.moveToFirst();

            if (cursor.getCount() == 1) {
                movie.setTitle(cursor.getString(FavoriteMoviesFragment.COL_TITLE));
                movie.setOverview(cursor.getString(FavoriteMoviesFragment.COL_OVERVIEW));
                movie.setId(cursor.getInt(FavoriteMoviesFragment.COL_MOVIE_ID));
                movie.setRelease_date(cursor.getString(FavoriteMoviesFragment.COL_RELEASE_DATE));
                movie.setVote_count(cursor.getInt(FavoriteMoviesFragment.COL_VOTE_COUNT));
                movie.setVote_average(cursor.getDouble(FavoriteMoviesFragment.COL_VOTE_AVERAGE));
                movie.setPoster_path(cursor.getString(FavoriteMoviesFragment.COL_POSTER_PATH));
                movie.setBackdrop_path(cursor.getString(FavoriteMoviesFragment.COL_BACKDROP_PATH));

                checkHasValidMovie = true;

            } else {
                movie.setTitle("Title");
                movie.setOverview("Overview");
                movie.setId(0);
                movie.setRelease_date("Release_date");
                movie.setVote_count(10);
                movie.setPoster_path(null);
                movie.setBackdrop_path(null);
                movie.setVote_average(10.0f);
            }

        }
        // create view for this fragment
        View view = inflater.inflate(R.layout.detail_fragment_new, container, false);

        adapter = new ReviewAdapter(getContext(), new ArrayList<Review>());

        // check saved state
        if (savedInstanceState != null) {
            review_list = savedInstanceState.getParcelableArrayList(LIST_REVIEW);
            adapter.addAll(review_list);
        }

        //viewHolder = new ViewHolder(view);
        ButterKnife.inject(this, view);

        if (!checkHasValidMovie) {
            mark_favorite.setEnabled(false);
        }

        setDetailForLayout();

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage(movie.getTitle());

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        Marking_favorite_event();

        return view;
    }

    // checking internet connection, if there is, get the json string and return the trailer URL
    @Override
    public void onStart() {
        super.onStart();

        if (checkHasValidMovie) {
            // if there is no internet connection, these codes can not be executed to get trailer link and review list
            if (Util.isNetworkAvailable(getContext())) {
                GetJsonKeyTask_trailer task = new GetJsonKeyTask_trailer();
                task.execute(String.valueOf(movie.getId()));

                /*GetJsonKeyTask_reviews task_reviews = new GetJsonKeyTask_reviews();
                task_reviews.execute(String.valueOf(movie.getId()));*/
            } else {
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (checkHasValidMovie) {
            if (Util.isNetworkAvailable(getContext())) {
                if (TrailerRequestURL == null) {
                    GetJsonKeyTask_trailer task = new GetJsonKeyTask_trailer();
                    task.execute(String.valueOf(movie.getId()));
                }
                if (review_list == null) {
                    GetJsonKeyTask_reviews task_reviews = new GetJsonKeyTask_reviews();
                    task_reviews.execute(String.valueOf(movie.getId()));
                }
            }
        }
    }

    @OnClick(R.id.trailer_button)
    public void Trailer() {
        // if there is no internet connection, a Toast will appear to announce to users
        if (Util.isNetworkAvailable(getContext())) {
            if (TrailerRequestURL != null) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(TrailerRequestURL));

                if (i.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(i);
                }
            } else {
                Toast.makeText(getContext(), "No trailer available for this movie", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.review_button)
    public void Reviews() {

        if (!Util.isNetworkAvailable(getContext()) && review_list.size() == 0) {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            showReviewDialog();
        }
    }

    // rating event
    @OnClick(R.id.ratingBar)
    public void Rating() {
        rateBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

            }
        });
    }

    public void Marking_favorite_event() {
        // query content provider and check if the current movie is in th database of favorite movies or not
        // if yes, set the star button to be pressed
        try {
            if (currentMovie(movie).getCount() == 1) {
                mark_favorite.setImageResource(R.drawable.like_icon);
                check_marked_favorite = true;
            }
        } finally {
            currentMovie(movie).close();
        }

//        mark_favorite.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                if (fromUser && rating == 1.0f) {
//                    Toast.makeText(getContext(), "Marked as favorite movie", Toast.LENGTH_SHORT).show();
//                    Uri uri_insert = getContext().getContentResolver().insert(authority, insert(movie));
//                } else {
//                    Toast.makeText(getContext(), "Unmarked as favorite movie", Toast.LENGTH_SHORT).show();
//                    int movie_deleted = delete(movie);
//                }
//            }
//        });
        mark_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_marked_favorite) {
                    mark_favorite.setImageResource(R.drawable.not_like_icon);
                    Toast.makeText(getContext(), "Unliked", Toast.LENGTH_SHORT).show();
                    int movie_deleted = delete(movie);
                    check_marked_favorite = false;
                } else {
                    mark_favorite.setImageResource(R.drawable.like_icon);
                    Toast.makeText(getContext(), "Liked", Toast.LENGTH_SHORT).show();
                    Uri uri_insert = getContext().getContentResolver().insert(authority, insert(movie));
                    check_marked_favorite = true;
                }
            }
        });
    }

    // insert method using ContentValues
    public ContentValues insert(MovieProperties movie) {
        ContentValues values = new ContentValues();

        values.put(MoviesContract.Movies.MOVIE_ID, movie.getId());
        values.put(MoviesContract.Movies.TITLE, movie.getTitle());
        values.put(MoviesContract.Movies.OVERVIEW, movie.getOverview());
        values.put(MoviesContract.Movies.POSTER_PATH, movie.getPoster_path());
        values.put(MoviesContract.Movies.BACKDROP_PATH, movie.getBackdrop_path());
        values.put(MoviesContract.Movies.VOTE_AVERAGE, movie.getVote_average());
        values.put(MoviesContract.Movies.VOTE_COUNT, movie.getVote_count());
        values.put(MoviesContract.Movies.RELEASE_DATE, movie.getRelease_date());

        return values;
    }

    // query the exact movie id and delete from the ContentProvider
    public int delete(MovieProperties movie) {
        int deleted = getContext().getContentResolver().delete(
                authority,
                MoviesContract.Movies.MOVIE_ID + " = ? ",
                new String[]{String.valueOf(movie.getId())}
        );
        return deleted;
    }

    // query the current movie to check if it is in the database or not
    public Cursor currentMovie(MovieProperties movie) {
        Cursor retCur = getContext().getContentResolver().query(
                authority,
                null,
                MoviesContract.Movies.MOVIE_ID + " = ? ",
                new String[]{String.valueOf(movie.getId())},
                null
        );
        return retCur;
    }

    public Cursor queryMovies() {

        String sortOrder = MoviesContract.Movies.VOTE_AVERAGE + " DESC LIMIT 1";

        Cursor retCur = getContext().getContentResolver().query(
                authority,
                FavoriteMoviesFragment.CURSOR_COLUMNS,
                null,
                null,
                sortOrder
        );
        return retCur;
    }

    // pop up a dialog of review list
    public void showReviewDialog() {
        LayoutInflater inflater = getLayoutInflater(null);
        View view = inflater.inflate(R.layout.reviews_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        AlertDialog review_dialog = builder.create();
        review_dialog.show();

        progressBar = (ProgressBar) review_dialog.findViewById(R.id.Progressbar_Review);
        //progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);

        if (Util.isNetworkAvailable(getContext())) {

            review_list.clear();

            Log.v("SIZE", "" + review_list.size());

            GetJsonKeyTask_reviews task_reviews = new GetJsonKeyTask_reviews();
            task_reviews.setProgressBar(progressBar);
            task_reviews.execute(String.valueOf(movie.getId()));
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }

        ListView listView_review = (ListView) review_dialog.findViewById(R.id.listView_reiviews);
        listView_review.setAdapter(adapter);

    }


    /**
     * this class returns string (trailer youtube link), which is used to launch trailer by using intent (youtube or web)
     */

    public class GetJsonKeyTask_trailer extends AsyncTask<String, Void, String> {

        public String GetTrailerKey(String json) {

            String LIST = "results";
            String KEY = "key";

            String trailerKey = null;

            try {

                JSONObject videos = new JSONObject(json);
                JSONArray results = videos.getJSONArray(LIST);

                JSONObject trailer = results.getJSONObject(0);

                if (trailer != null) {
                    trailerKey = trailer.getString(KEY);
                } else {
                    trailerKey = null;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return trailerKey;
        }

        public String YoutubeRequest(String key) {

            String BASE_LINK = "https://www.youtube.com/watch?v=";

            StringBuilder builder = new StringBuilder(BASE_LINK);
            builder.append(key);

            if (key == null) {
                return null;
            }

            return builder.toString();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection_trailer = null;
            BufferedReader reader_trailer = null;
            String jSonString_trailer = null;

            try {

                URL url_trailer = new URL("http://api.themoviedb.org/3/movie/" + params[0] + "/videos?api_key=" + BuildConfig.OPEN_API);

                urlConnection_trailer = (HttpURLConnection) url_trailer.openConnection();
                urlConnection_trailer.setRequestMethod("GET");
                urlConnection_trailer.connect();

                InputStream inputStream_trailer = urlConnection_trailer.getInputStream();

                if (inputStream_trailer == null) {
                    // do nothing here
                }

                reader_trailer = new BufferedReader(new InputStreamReader(inputStream_trailer));
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader_trailer.readLine()) != null) {
                    builder.append(line + "\n");
                }
                jSonString_trailer = builder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection_trailer != null) {
                    urlConnection_trailer.disconnect();
                }
                if (reader_trailer != null) {
                    try {
                        reader_trailer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return YoutubeRequest(GetTrailerKey(jSonString_trailer));
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            TrailerRequestURL = result;
        }
    }

    /**
     * this class returns an arraylist<Review> which contains list of reviews
     */


    public class GetJsonKeyTask_reviews extends AsyncTask<String, Integer, ArrayList<Review>> {

        ProgressBar bar;
        public void setProgressBar(ProgressBar bar) {
            this.bar = bar;
        }
        // get an object Review through jsonString
        public ArrayList<Review> getReviewList(String json) {

            String LIST = "results";
            String AUTHOR = "author";
            String CONTENT = "content";
            ArrayList<Review> list = new ArrayList<>();
            try {
                JSONObject reviews = new JSONObject(json);
                JSONArray list_review = reviews.getJSONArray(LIST);
                if (list_review.length() != 0) {
                    for (int i = 0; i < list_review.length(); i++) {
                        Review review = new Review();
                        JSONObject object = list_review.getJSONObject(i);

                        review.setAuthor_name(object.getString(AUTHOR));
                        review.setContent(object.getString(CONTENT));

                        list.add(review);
                    }
                } else {
                    Review review = new Review();
                    review.setAuthor_name("No reviews");
                    review.setContent("");

                    list.add(review);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }
        // return arraylist<Review>

        @Override
        protected ArrayList<Review> doInBackground(String... params) {

            HttpURLConnection urlConnection_review = null;
            BufferedReader reader_review = null;
            String jSonString_review = null;
            try {
                URL url_review = new URL("https://api.themoviedb.org/3/movie/" + params[0] + "/reviews?api_key=" + BuildConfig.OPEN_API);
                urlConnection_review = (HttpURLConnection) url_review.openConnection();
                urlConnection_review.setRequestMethod("GET");
                urlConnection_review.connect();

                InputStream inputStream_review = urlConnection_review.getInputStream();
                if (inputStream_review == null) {
                    // do nothing here
                }
                reader_review = new BufferedReader(new InputStreamReader(inputStream_review));

                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader_review.readLine()) != null) {
                    builder.append(line + "\n");
                }
                jSonString_review = builder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection_review != null) {
                    urlConnection_review.disconnect();
                }
                if (reader_review != null) {
                    try {
                        reader_review.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return getReviewList(jSonString_review);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (this.bar != null) {
                bar.setProgress(values[0]);
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            super.onPostExecute(reviews);
            review_list = reviews;
            if (reviews != null) {
                adapter.clear();
                adapter.addAll(reviews);
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
