package com.example.android.popularmovies.Activities;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Dell on 6/7/2016.
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    private GridView mGridView;

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public void setGridView(GridView gridView) {
        mGridView = gridView;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mGridView==null) {
            return true;
        }
        return mGridView.canScrollVertically(-100);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
