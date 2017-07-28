package com.upwardproject.moviedb.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Dark on 27/07/2017.
 */

public class DatabaseContract {
    /*
     * The "Content authority" is a name for the entire content provider, similar to the
     * relationship between a domain name and its website. A convenient string to use for the
     * content authority is the package name for the app, which is guaranteed to be unique on the
     * Play Store.
     */
    public static final String CONTENT_AUTHORITY = "com.upwardproject.moviedb";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for the app.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /*
     * Possible paths that can be appended to BASE_CONTENT_URI to form valid URI's that Sunshine
     * can handle. For instance,
     *
     *     content://com.example.android.sunshine/movie/
     *     [           BASE_CONTENT_URI         ][ PATH_MOVIE ]
     *
     * is a valid path for looking at weather data.
     *
     *      content://com.upwardproject.moviedb/givemeroot/
     *
     * will fail, as the ContentProvider hasn't been given any information on what to do with
     * "givemeroot". At least, let's hope not. Don't be that dev, reader. Don't be that dev.
     */
    public static final String PATH_MOVIE = "movie";

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the movie table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        /* Used internally as the name of our movie table. */
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VOTE_AVG = "vote_average";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
