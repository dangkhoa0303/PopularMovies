package com.example.android.popularmovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.R;

public class FavoriteMoviesActivity extends AppCompatActivity implements FavoriteMoviesFragment.CallBack {

    private boolean mTwoPane;

    private static String DETAIL_TAG = "favorite_movie_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        if (findViewById(R.id.movie_detail_container) != null ) {

            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailFragment(), DETAIL_TAG)
                        .commit();
            }
        }
        else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    @Override
    public void onListItemSelected(MovieProperties movie) {

        if (mTwoPane) {

            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.PACKAGE, movie);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAIL_TAG)
                    .commit();
        }
        else {

            Intent intent = new Intent(this, DetailActivity.class);

            Bundle bundle = new Bundle();

            bundle.putParcelable("Movie_Package_Intent", movie);

            intent.putExtra("Package", bundle);

            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
