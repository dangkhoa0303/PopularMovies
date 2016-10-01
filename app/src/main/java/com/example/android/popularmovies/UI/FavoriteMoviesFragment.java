package com.example.android.popularmovies.UI;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.popularmovies.Adapters.FavoriteAdapter;
import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Data.MoviesContract;

/**
 * Created by Dell on 12/8/2015.
 */
public class FavoriteMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static FavoriteAdapter mAdapter;
    private Uri authority = MoviesContract.Movies.CONTENT_URI;
    private static String SELECTED_KEY = "selected_postion";
    private static int LOADER = 0;
    private int mPosition;
    private ListView mlistView;

    public static final String[] CURSOR_COLUMNS = {
            MoviesContract.Movies.TABLE_NAME + "." + MoviesContract.Movies._ID,
            MoviesContract.Movies.MOVIE_ID,
            MoviesContract.Movies.TITLE,
            MoviesContract.Movies.OVERVIEW,
            MoviesContract.Movies.POSTER_PATH,
            MoviesContract.Movies.BACKDROP_PATH,
            MoviesContract.Movies.VOTE_AVERAGE,
            MoviesContract.Movies.VOTE_COUNT,
            MoviesContract.Movies.RELEASE_DATE
    };

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_OVERVIEW = 3;
    public static final int COL_POSTER_PATH = 4;
    public static final int COL_BACKDROP_PATH = 5;
    public static final int COL_VOTE_AVERAGE = 6;
    public static final int COL_VOTE_COUNT = 7;
    public static final int COL_RELEASE_DATE = 8;


    public interface FavoriteMoviesCallBack {
        void onListItemSelected(MovieProperties movie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }

        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mAdapter = new FavoriteAdapter(getActivity(), null, 0);

        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);

        mlistView = (ListView) view.findViewById(R.id.listView_favorite_movie);
        mlistView.setAdapter(mAdapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    MovieProperties movieProperties = new MovieProperties();
                    movieProperties.setId(cursor.getInt(cursor.getColumnIndex(MoviesContract.Movies.MOVIE_ID)));
                    movieProperties.setTitle(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.TITLE)));
                    movieProperties.setPoster_path(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.POSTER_PATH)));
                    movieProperties.setBackdrop_path(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.BACKDROP_PATH)));
                    movieProperties.setRelease_date(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.RELEASE_DATE)));
                    movieProperties.setOverview(cursor.getString(cursor.getColumnIndex(MoviesContract.Movies.OVERVIEW)));
                    movieProperties.setVote_average(cursor.getDouble(cursor.getColumnIndex(MoviesContract.Movies.VOTE_AVERAGE)));
                    movieProperties.setVote_count(cursor.getInt(cursor.getColumnIndex(MoviesContract.Movies.VOTE_COUNT)));
                    ((FavoriteMoviesCallBack) getActivity()).onListItemSelected(movieProperties);
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = MoviesContract.Movies.VOTE_AVERAGE + " DESC";
        return new CursorLoader(getActivity(),
                authority,
                CURSOR_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if (mPosition != ListView.INVALID_POSITION) {
            mlistView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
