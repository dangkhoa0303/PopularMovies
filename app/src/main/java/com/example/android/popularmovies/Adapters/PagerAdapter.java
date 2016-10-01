package com.example.android.popularmovies.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.popularmovies.UI.FavoriteMoviesFragment;
import com.example.android.popularmovies.UI.MostPopularMoviesFragment;
import com.example.android.popularmovies.UI.UpComingMoviesFragment;

/**
 * Created by Dell on 9/18/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MostPopularMoviesFragment tab1 = new MostPopularMoviesFragment();
                return tab1;
            case 1:
                UpComingMoviesFragment tab2 = new UpComingMoviesFragment();
                return tab2;
            case 2:
                FavoriteMoviesFragment tab3 = new FavoriteMoviesFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
