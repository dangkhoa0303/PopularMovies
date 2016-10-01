package com.example.android.popularmovies.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.popularmovies.Adapters.PagerAdapter;
import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Services.FetchSearchMovies;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MostPopularMoviesFragment.PopularMoviesCallBack,
        UpComingMoviesFragment.UpComingMoviesCallBack, FavoriteMoviesFragment.FavoriteMoviesCallBack, SearchView.OnQueryTextListener {

//    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";
//    private static String DETAIL_TAG = "favorite_movie_detail";
//    private boolean mTwoPane;
    public static String parcelable = "Movie_Package_Intent", transpackage = "Package";
    private SearchMoviesReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter(SearchMoviesReceiver.SEARCH_MOVIES_LIST_RECEIVER);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new SearchMoviesReceiver();
        registerReceiver(receiver, intentFilter);

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
                        break;
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
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(this, FetchSearchMovies.class);
        intent.putExtra(FetchSearchMovies.SEARCH, query);
        startService(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class SearchMoviesReceiver extends BroadcastReceiver {
        public static final String SEARCH_MOVIES_LIST_RECEIVER = "com.example.android.searchmovies";

        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<MovieProperties> list = intent.getParcelableArrayListExtra(FetchSearchMovies.KEY_RESPONSE_SEARCH);
            Toast.makeText(getApplicationContext(), ""+list.size(), Toast.LENGTH_SHORT).show();
        }
    }
}
