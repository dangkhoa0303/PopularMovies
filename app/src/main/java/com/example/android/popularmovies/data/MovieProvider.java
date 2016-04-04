package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.android.popularmovies.data.MoviesContract.Movies;

/**
 * Created by Dell on 11/30/2015.
 */
public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_TITLE = 101;
    private static final int MOVIE_WITH_MOVIE_ID = 102;
    private static MoviesDB mOpenHelper;
    private static UriMatcher sURiMatcher = builURiMatcher();

    static UriMatcher builURiMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = MoviesContract.CONTENT_AUTHORITY; // com.example.android.popularmovies

        matcher.addURI(authority, MoviesContract.PATH_MOVIES, MOVIE); // com.example.android.popularmovies.movie
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/*", MOVIE_WITH_TITLE);
        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", MOVIE_WITH_MOVIE_ID);

        return matcher;
    }

    public MovieProvider() {

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int matcher = sURiMatcher.match(uri);

        switch (matcher) {
            case MOVIE:
                return Movies.CONTENT_TYPE;
            case MOVIE_WITH_TITLE:
                return Movies.CONTENT_ITEM_TYPE;
            case MOVIE_WITH_MOVIE_ID:
                return Movies.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        final int matcher = sURiMatcher.match(uri);

        Cursor retcur;

        switch (matcher) {
            case MOVIE: {
                retcur = mOpenHelper.getReadableDatabase().query(
                        Movies.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case MOVIE_WITH_MOVIE_ID: {
                retcur = mOpenHelper.getReadableDatabase().query(
                        Movies.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case MOVIE_WITH_TITLE: {
                retcur = mOpenHelper.getReadableDatabase().query(
                        Movies.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retcur.setNotificationUri(getContext().getContentResolver(), uri);
        return retcur;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sURiMatcher.match(uri);

        final Uri retURi;

        // insert into the table
        switch (match) {
            case MOVIE: {
                long _id = db.insert(Movies.TABLE_NAME, null, values);
                if (_id > 0) {
                    retURi = ContentUris.withAppendedId(Movies.CONTENT_URI, _id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }

                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return retURi;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sURiMatcher.match(uri);

        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";

        int rowsdeleted;

        switch (match) {
            case MOVIE:
                rowsdeleted = db.delete(Movies.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because a null deletes all rows
        if (rowsdeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsdeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sURiMatcher.match(uri);

        int rowsupdated;

        switch (match) {
            case MOVIE:
                rowsupdated = db.update(Movies.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsupdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsupdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int match = sURiMatcher.match(uri);

        switch (match) {
            case MOVIE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(Movies.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
