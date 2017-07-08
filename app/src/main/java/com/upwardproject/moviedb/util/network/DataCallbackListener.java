package com.upwardproject.moviedb.util.network;

public interface DataCallbackListener {
    void onLoadingSucceed(Object data);

    void onLoadingEmpty(String message);

    void onLoadingFailed();
}
