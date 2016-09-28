package com.example.android.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Properties.MovieProperties;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Dell on 11/13/2015.
 */
public class MoviesAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<MovieProperties> movies;

    public MoviesAdapter(Context context, ArrayList<MovieProperties> movies) {
        super(context, R.layout.list_movie_item, movies);
        this.context = context;
        this.movies = movies;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (position == 0) {
                convertView = LayoutInflater.from(this.context).inflate(R.layout.list_movie_item_special, parent, false);
            } else {
                convertView = LayoutInflater.from(this.context).inflate(R.layout.list_movie_item, parent, false);
            }
        }

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.item_movie_thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.item_movie_title);
        TextView review = (TextView) convertView.findViewById(R.id.item_movie_review);
        TextView summary = (TextView) convertView.findViewById(R.id.item_movie_summary);

        Picasso.with(context)
                .load(movies.get(position).getPoster_path())
                .error(R.drawable.app_movie_icon)
                .into(thumbnail);

        title.setText(movies.get(position).getTitle());
        review.setText(movies.get(position).getVote_average() + "/10.0");
        summary.setText(movies.get(position).getOverview());

        return convertView;
    }
}
