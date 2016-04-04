package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.MoviesContract.Movies;

/**
 * Created by Dell on 11/30/2015.
 */
public class MoviesDB extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "movie.db";
    public static final int DATABASE_VERSION = 1;

    public MoviesDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE "
                + Movies.TABLE_NAME
                + " ( "
                + Movies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Movies.MOVIE_ID + " INTEGER NOT NULL, "
                + Movies.TITLE + " TEXT NOT NULL, "
                + Movies.OVERVIEW + " TEXT, "
                + Movies.POSTER_PATH + " TEXT, "
                + Movies.BACKDROP_PATH + " TEXT, "
                + Movies.VOTE_AVERAGE + " DOUBLE NOT NULL, "
                + Movies.VOTE_COUNT + " INTEGER NOT NULL, "
                + Movies.RELEASE_DATE + " TEXT, "
                //+ Movies.MARKED_BY_USER + " DOUBLE NOT NULL, "
                + " UNIQUE ( " + Movies.MOVIE_ID + " ) ON CONFLICT REPLACE );"; // set the movie_id to be unique

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Movies.TABLE_NAME);
        onCreate(db);
    }
}
