package com.upwardproject.moviedb.constant;

import com.upwardproject.moviedb.BuildConfig;

public class MovieDbApi {
    public static final String PARAM_API_KEY = "api_key",
            PARAM_PAGE = "page";

    private static final String BASE_MOVIE_URL = BuildConfig.BASE_URL + "3/movie/";
    public static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185",
            BASE_BACKDROP_URL = "http://image.tmdb.org/t/p/w780";

    public static final String popularMoviesUrl = BASE_MOVIE_URL + "popular",
            topRatedMoviesUrl = BASE_MOVIE_URL + "top_rated";
}
