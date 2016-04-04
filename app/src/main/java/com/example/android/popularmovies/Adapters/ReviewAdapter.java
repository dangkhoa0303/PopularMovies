package com.example.android.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Properties.Review;

import java.util.ArrayList;

/**
 * Created by Dell on 11/18/2015.
 */
public class ReviewAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Review> list;

    private Review review;

    public ReviewAdapter(Context context, ArrayList<Review> objects) {
        super(context, R.layout.list_review_item, objects);

        this.context = context;
        this.list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_review_item, parent, false);
        }

        review = list.get(position);

        TextView author_name_text = (TextView) convertView.findViewById(R.id.author_name_text);
        TextView content = (TextView) convertView.findViewById(R.id.content);

        author_name_text.setText(review.getAuthor_name());
        content.setText(review.getContent());

        return convertView;
    }
}
