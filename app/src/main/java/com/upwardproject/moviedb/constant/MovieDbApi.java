package com.upwardproject.moviedb.constant;

import android.net.Uri;

import com.upwardproject.moviedb.BuildConfig;

public class MovieDbApi {
    public static final String PARAM_API_KEY = "api_key",
            PARAM_PAGE = "page";

    public static final String PATH_PARAM_REVIEWS = "reviews",
            PATH_PARAM_VIDEOS = "videos";

    public static final Uri BASE_MOVIE_URI = new Uri.Builder()
            .scheme("https")
            .authority(BuildConfig.AUTHORITY)
            .appendPath("3")
            .appendPath("movie")
            .build();

    public static final String BASE_POSTER_URL = new Uri.Builder()
            .scheme("http")
            .authority("image.tmdb.org")
            .appendPath("t")
            .appendPath("p")
            .appendPath("w185")
            .toString();

    public static final String BASE_BACKDROP_URL = new Uri.Builder()
            .scheme("http")
            .authority("image.tmdb.org")
            .appendPath("t")
            .appendPath("p")
            .appendPath("w780")
            .toString();

    public static final String popularMoviesUrl = BASE_MOVIE_URI.buildUpon().appendPath("popular").toString(),
            topRatedMoviesUrl = BASE_MOVIE_URI.buildUpon().appendPath("top_rated").toString();
}
