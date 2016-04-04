package com.example.android.popularmovies.Properties;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dell on 11/13/2015.
 */
public class MovieProperties implements Parcelable {
    private String original_title;
    private String overview;
    private String poster_path;
    private String backdrop_path;
    private double vote_average;
    private int vote_count;
    private String release_date;

    private int id;

    // rating marked by user (saved in local database)
    //private double marks = 0;

    /*public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }*/

    public MovieProperties() {

    }

    protected MovieProperties(Parcel in) {
        original_title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        vote_average = in.readDouble();
        vote_count = in.readInt();
        release_date = in.readString();
        id = in.readInt();
    }

    public static final Creator<MovieProperties> CREATOR = new Creator<MovieProperties>() {
        @Override
        public MovieProperties createFromParcel(Parcel in) {
            return new MovieProperties(in);
        }

        @Override
        public MovieProperties[] newArray(int size) {
            return new MovieProperties[size];
        }
    };

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTitle() {
        return original_title;
    }

    public void setTitle(String original_title) {
        this.original_title = original_title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeDouble(vote_average);
        dest.writeInt(vote_count);
        dest.writeString(release_date);
        dest.writeInt(id);
    }
}
