package com.upwardproject.moviedb.util.network;

/**
 * 02/18/2015 - Add Context parameter to get and post method
 */

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

public class ServerRestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static RequestHandle get(Context ctx, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Content-Type", "application/json");
        return client.get(ctx, url, params, responseHandler);
    }

    public static RequestHandle post(Context ctx, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        return client.post(ctx, url, params, responseHandler);
    }

    public static void setTimeout(int timeout) {
        client.setTimeout(timeout);
    }

    public static void cancelAllRequest(boolean cancel) {
        client.cancelAllRequests(cancel);
    }

    public static void cancelRequest(Context ctx, boolean mayInterruptIfRunning) {
        client.cancelRequests(ctx, mayInterruptIfRunning);
    }

    public static void cancelRequestsByTag(Object tag, boolean mayInterruptIfRunning) {
        client.cancelRequestsByTAG(tag, mayInterruptIfRunning);
    }

}
