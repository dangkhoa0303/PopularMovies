package com.example.android.popularmovies.Properties;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell on 11/18/2015.
 */
public class Review implements Parcelable {
    private String author_name;
    private String content;

    public Review () {

    }

    protected Review(Parcel in) {
        author_name = in.readString();
        content = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author_name);
        dest.writeString(content);
    }
}
