package com.upwardproject.moviedb.model;

import android.content.ContentUris;

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

public class MovieVideoRepository {

    public static void list(int id, RequestParams params, final RemoteCallback.Load<List<MovieVideo>> callback) {
        String url = ContentUris.withAppendedId(MovieDbApi.BASE_MOVIE_URI, id)
                .buildUpon()
                .appendPath(MovieDbApi.PATH_PARAM_VIDEOS)
                .toString();

        ServerRestClient.get(url, params, new JsonDataReceiver() {
            @Override
            public void onLoadingSucceed(Object data) {
                JSONArray jsonArray = ((JSONObject) data).optJSONArray("results");

                if (jsonArray == null || jsonArray.length() == 0) {
                    callback.onDataNotAvailable(null);
                    return;
                }

                List<MovieVideo> itemList = new ArrayList<>();
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

    public static MovieVideo getDataFromJson(JSONObject jsonObject) {
        if (jsonObject == null) return null;

        return new MovieVideo(JSONParser.optString(jsonObject, "id"),
                JSONParser.optString(jsonObject, "key"),
                JSONParser.optString(jsonObject, "name"));
    }

}
