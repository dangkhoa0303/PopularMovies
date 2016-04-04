package com.example.android.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Dell on 11/13/2015.
 */
public class GridViewAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<MovieProperties> movies;

    public GridViewAdapter(Context context, ArrayList<MovieProperties> movies) {
        super(context, R.layout.gridview_image_item, movies);
        this.context = context;
        this.movies = movies;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.gridview_image_item, parent, false);
        }
        Picasso.with(context)
                .load(movies.get(position).getPoster_path())
                .placeholder(R.drawable.movie_icon)
                .error(R.drawable.movie_icon)
                .resize(1000, 1000)
                .into((ImageView) convertView);
        return convertView;
    }
}
