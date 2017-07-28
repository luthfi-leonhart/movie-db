package com.upwardproject.moviedb.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.loopj.android.http.RequestParams;
import com.upwardproject.moviedb.data.DatabaseContract;
import com.upwardproject.moviedb.util.network.JsonDataReceiver;
import com.upwardproject.moviedb.util.network.RemoteCallback;
import com.upwardproject.moviedb.util.network.ServerRestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark on 08/07/2017.
 */

public class MovieRepository {
    public static void getListFromRemote(String url, RequestParams params, final RemoteCallback.Load<List<Movie>> callback) {
        ServerRestClient.get(url, params, new JsonDataReceiver() {
            @Override
            public void onLoadingSucceed(Object data) {
                JSONArray jsonArray = ((JSONObject) data).optJSONArray("results");

                if (jsonArray == null || jsonArray.length() == 0) {
                    callback.onDataNotAvailable(null);
                    return;
                }

                List<Movie> itemList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    itemList.add(getDataFromJson(jsonObject));
                }

                callback.onDataLoaded(itemList);
            }

            @Override
            public void onLoadingEmpty(String message) {
                callback.onDataNotAvailable(message);
            }

            @Override
            public void onLoadingFailed() {
                callback.onFailed();
            }
        });
    }

    private static Movie getDataFromJson(JSONObject jsonObject) {
        if (jsonObject == null) return null;

        return new Movie(jsonObject.optInt("id"), jsonObject.optString("title"), jsonObject.optString("original_title"))
                .setPosterPath(jsonObject.optString("poster_path"))
                .setBackdropPath(jsonObject.optString("backdrop_path"))
                .setOverview(jsonObject.optString("overview"))
                .setReleaseDate(jsonObject.optString("release_date"))
                .setVoteCount(jsonObject.optInt("vote_count"))
                .setVoteAverage(jsonObject.optDouble("vote_average"))
                .setPopularity(jsonObject.optDouble("popularity"));
    }

    /*
     * LOCAL
     */

    public static boolean isExistInLocal(ContentResolver resolver, int movieId) {
        Uri uri = ContentUris.withAppendedId(DatabaseContract.MovieEntry.CONTENT_URI, movieId);
        String[] projection = {DatabaseContract.MovieEntry._ID};

        Cursor cursor = resolver.query(uri, projection, null, null, null);

        if (cursor == null) return false;

        boolean exist = cursor.getCount() == 1;
        cursor.close();

        return exist;
    }

    public static void saveToLocal(ContentResolver resolver, Movie movie) {
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MovieEntry._ID, movie.getId());
        values.put(DatabaseContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        values.put(DatabaseContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        values.put(DatabaseContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        values.put(DatabaseContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        values.put(DatabaseContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        values.put(DatabaseContract.MovieEntry.COLUMN_VOTE_AVG, movie.getVoteAverage());
        values.put(DatabaseContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        values.put(DatabaseContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        values.put(DatabaseContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        resolver.insert(DatabaseContract.MovieEntry.CONTENT_URI, values);
    }

    public static void deleteFromLocal(ContentResolver resolver, int movieId) {
        Uri uri = ContentUris.withAppendedId(DatabaseContract.MovieEntry.CONTENT_URI, movieId);
        resolver.delete(uri, null, null);
    }

    public static Movie getCompleteDataFromCursor(Cursor data) {
        return new Movie(data.getInt(data.getColumnIndex(DatabaseContract.MovieEntry._ID)),
                data.getString(data.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_TITLE)),
                data.getString(data.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_ORIGINAL_TITLE)))
                .setPosterPath(data.getString(data.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_POSTER_PATH)))
                .setBackdropPath(data.getString(data.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_BACKDROP_PATH)))
                .setOverview(data.getString(data.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_OVERVIEW)))
                .setReleaseDate(data.getString(data.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_RELEASE_DATE)))
                .setVoteCount(data.getInt(data.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_VOTE_COUNT)))
                .setVoteAverage(data.getDouble(data.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_VOTE_AVG)))
                .setPopularity(data.getDouble(data.getColumnIndex(DatabaseContract.MovieEntry.COLUMN_POPULARITY)));
    }
}
