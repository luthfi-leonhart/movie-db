package com.upwardproject.moviedb.model;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.upwardproject.moviedb.constant.MovieDbApi;
import com.upwardproject.moviedb.ui.movie.MovieFilter;
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

    public static void list(Context context, int filter, RequestParams params, final RemoteCallback.Load<List<Movie>> callback) {
        String url;
        switch (filter) {
            case MovieFilter.POPULAR:
                url = MovieDbApi.popularMoviesUrl;
                break;
            case MovieFilter.TOP_RATED:
                url = MovieDbApi.topRatedMoviesUrl;
                break;
            default:
                url = MovieDbApi.popularMoviesUrl;
        }

        ServerRestClient.get(context, url, params, new JsonDataReceiver() {
            @Override
            public void onLoadingSucceed(Object data) {
                JSONArray jsonArray = ((JSONObject) data).optJSONArray("results");

                if (jsonArray == null) {
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

    public static Movie getDataFromJson(JSONObject jsonObject) {
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

}
