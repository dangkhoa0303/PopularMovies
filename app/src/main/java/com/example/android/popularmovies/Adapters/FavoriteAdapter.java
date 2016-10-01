package com.example.android.popularmovies.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.UI.FavoriteMoviesFragment;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 12/8/2015.
 */
public class FavoriteAdapter extends CursorAdapter {


    public static class ViewHolder {

        @InjectView(R.id.poster_imageView)
        public ImageView poster_image;
        @InjectView(R.id.movie_title)
        public TextView title_text;
        @InjectView(R.id.release_date)
        public TextView release_date_text;
        @InjectView(R.id.vote_average_fragment)
        public TextView vote_average_text;

        public ViewHolder(View view) {

            ButterKnife.inject(this, view);

        }
    }

    public FavoriteAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_favorite_movie_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        // set tag for viewHolder in order to use it in the another method - bindView
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String poster_path = cursor.getString(FavoriteMoviesFragment.COL_BACKDROP_PATH);
        Picasso.with(context)
                .load(poster_path)
                .placeholder(R.drawable.movie_icon)
                .error(R.drawable.movie_icon)
                .into(viewHolder.poster_image);

        String title = cursor.getString(FavoriteMoviesFragment.COL_TITLE);
        viewHolder.title_text.setText(title);

        String release_date = cursor.getString(FavoriteMoviesFragment.COL_RELEASE_DATE);
        viewHolder.release_date_text.setText(release_date);

        String vote_average = cursor.getString(FavoriteMoviesFragment.COL_VOTE_AVERAGE);
        viewHolder.vote_average_text.setText(vote_average + "/10.0");

    }
}
