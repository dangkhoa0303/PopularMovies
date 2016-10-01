package com.example.android.popularmovies.UI;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Dell on 6/7/2016.
 */
public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    private ListView mListView;

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

    @Override
    public boolean canChildScrollUp() {
        if (mListView==null) {
            return true;
        }
        return mListView.canScrollVertically(-100);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
