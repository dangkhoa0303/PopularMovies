package com.example.android.popularmovies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.popularmovies.Adapters.PagerAdapter;
import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.R;

public class MainActivity extends AppCompatActivity implements MostPopularMoviesFragment.PopularMoviesCallBack,
        UpComingMoviesFragment.UpComingMoviesCallBack, FavoriteMoviesFragment.FavoriteMoviesCallBack {
    //    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";
//    private static String DETAIL_TAG = "favorite_movie_detail";
//    private boolean mTwoPane;
    public static String parcelable = "Movie_Package_Intent", transpackage = "Package";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        if (findViewById(R.id.movie_detail_container) != null) {
//            mTwoPane = true;
//            if (savedInstanceState == null) {
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.movie_detail_container, new DetailFragment(), DETAIL_FRAGMENT_TAG)
//                        .commit();
//            }
//        } else {
//            mTwoPane = false;
//            getSupportActionBar().setElevation(0f);
//        }

        setContentView(R.layout.activity_main_official);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_popular));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_upcoming_unselected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_favorite_unselected));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        toolbar.setTitle(R.string.popular_movies);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                String title;
                switch (tab.getPosition()) {
                    case 0:
                        title = getString(R.string.popular_movies);
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_popular);
                        break;
                    case 1:
                        title = getString(R.string.upcoming_movies);
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_upcoming);
                        break;
                    case 2:
                        title = getString(R.string.favorite_movies);
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite);
                        break;
                    default:
                        title = null;
                }
                toolbar.setTitle(title);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_popular_unselected);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_upcoming_unselected);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_favorite_unselected);
                        break;
                    default:
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.favorite_movies) {
//            Intent intent = new Intent(this, FavoriteMoviesActivity.class);
//            startActivity(intent);
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPopularMoviesItemSelected(MovieProperties movie) {
//        if (mTwoPane) {
//            Bundle args = new Bundle();
//            args.putParcelable(DetailFragment.PACKAGE, movie);
//
//            DetailFragment fragment = new DetailFragment();
//            fragment.setArguments(args);
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.movie_detail_container, fragment, DETAIL_FRAGMENT_TAG)
//                    .commit();
//        } else {
//            Intent i = new Intent(this, DetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("Movie_Package_Intent", movie);
//            i.putExtra("Package", bundle);
//            startActivity(i);
//        }
        Intent i = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(parcelable, movie);
        i.putExtra(transpackage, bundle);
        startActivity(i);
    }

    @Override
    public void onUpComingMoviesItemSelected(MovieProperties movie) {
//        if (mTwoPane) {
//            Bundle args = new Bundle();
//            args.putParcelable(DetailFragment.PACKAGE, movie);
//
//            DetailFragment fragment = new DetailFragment();
//            fragment.setArguments(args);
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.movie_detail_container, fragment, DETAIL_FRAGMENT_TAG)
//                    .commit();
//        } else {
//            Intent i = new Intent(this, DetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("Movie_Package_Intent", movie);
//            i.putExtra("Package", bundle);
//            startActivity(i);
//        }
        Intent i = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(parcelable, movie);
        i.putExtra(transpackage, bundle);
        startActivity(i);
    }

    @Override
    public void onListItemSelected(MovieProperties movie) {
//        if (mTwoPane) {
//
//            Bundle args = new Bundle();
//            args.putParcelable(DetailFragment.PACKAGE, movie);
//
//            DetailFragment fragment = new DetailFragment();
//            fragment.setArguments(args);
//
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.movie_detail_container, fragment, DETAIL_TAG)
//                    .commit();
//        }
//        else {
//            Intent intent = new Intent(this, DetailActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable("Movie_Package_Intent", movie);
//            intent.putExtra("Package", bundle);
//            startActivity(intent);
//        }
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(parcelable, movie);
        intent.putExtra(transpackage, bundle);
        startActivity(intent);
    }
}
