package com.example.android.popularmovies.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dell on 11/30/2015.
 */
public class MoviesContract {

    // define the authority for URI
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movie";

    public static final class Movies implements BaseColumns {

        // Uri used to access table movie in database - root URI
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        // table name
        public static final String TABLE_NAME = "movie";

        // id, set to autocrement
        public static final String _ID = "_id";

        public static final String MOVIE_ID = "movie_id";

        public static final String TITLE = "original_title";

        public static final String OVERVIEW = "overview";

        public static final String POSTER_PATH = "poster_path";

        public static final String BACKDROP_PATH = "backdrop_path";

        public static final String VOTE_AVERAGE = "vote_average";

        public static final String VOTE_COUNT = "vote_count";

        public static final String RELEASE_DATE = "release_date";

        // added on 19/02/2016
       // public static final String MARKED_BY_USER = "marks";
    }
}
