package com.upwardproject.moviedb.model;

import android.content.ContentUris;
import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.upwardproject.moviedb.constant.MovieDbApi;
import com.upwardproject.moviedb.util.JSONParser;
import com.upwardproject.moviedb.util.network.JsonDataReceiver;
import com.upwardproject.moviedb.util.network.RemoteCallback;
import com.upwardproject.moviedb.util.network.ServerRestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark on 25/07/2017.
 */

public class MovieReviewRepository {

    public static void list(Context context, int id, RequestParams params, final RemoteCallback.Load<List<MovieReview>> callback) {
        String url = ContentUris.withAppendedId(MovieDbApi.BASE_MOVIE_URI, id)
                .buildUpon()
                .appendPath(MovieDbApi.PATH_PARAM_REVIEWS)
                .toString();

        ServerRestClient.get(context, url, params, new JsonDataReceiver() {
            @Override
            public void onLoadingSucceed(Object data) {
                JSONArray jsonArray = ((JSONObject) data).optJSONArray("results");

                if (jsonArray == null || jsonArray.length() == 0) {
                    callback.onDataNotAvailable(null);
                    return;
                }

                List<MovieReview> itemList = new ArrayList<>();
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

    public static MovieReview getDataFromJson(JSONObject jsonObject) {
        if (jsonObject == null) return null;

        return new MovieReview(JSONParser.optString(jsonObject, "id"),
                JSONParser.optString(jsonObject, "author"),
                JSONParser.optString(jsonObject, "url"))
                .setContent(JSONParser.optString(jsonObject, "content"));
    }

}
