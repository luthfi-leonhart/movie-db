package com.upwardproject.moviedb.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Dark on 25/07/2017.
 */

public class MovieVideo implements Parcelable {
    public final String id;
    public final String key;
    public final String title;

    public MovieVideo(String id, String key, String title) {
        this.id = id;
        this.key = key;
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.title);
    }

    protected MovieVideo(Parcel in) {
        this.id = in.readString();
        this.key = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<MovieVideo> CREATOR = new Parcelable.Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel source) {
            return new MovieVideo(source);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };
}
