package com.example.android.popularmovies.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Dell on 11/18/2015.
 */
public class CheckNetworkConnection {

    private Context context;

    public CheckNetworkConnection (Context context) {
        this.context = context;
    }

    // check whether there is internet connection
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
