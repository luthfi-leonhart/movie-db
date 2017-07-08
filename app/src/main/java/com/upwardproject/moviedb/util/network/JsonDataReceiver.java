package com.upwardproject.moviedb.util.network;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public abstract class JsonDataReceiver extends JsonHttpResponseHandler implements DataCallbackListener {

    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        onLoadingSucceed(response);
    }

    public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response) {
        onLoadingFailed();
    }

    public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
        onLoadingFailed();
    }
}
