package com.upwardproject.moviedb.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dark on 25/07/2017.
 */

public class MovieReview implements Parcelable {
    public final String id;
    public final String author;
    private String content;
    public final String url;

    public MovieReview(String id, String author, String url) {
        this.id = id;
        this.author = author;
        this.url = url;
    }

    public MovieReview setContent(String content) {
        this.content = content;
        return this;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.url);
    }

    protected MovieReview(Parcel in) {
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel source) {
            return new MovieReview(source);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
}
