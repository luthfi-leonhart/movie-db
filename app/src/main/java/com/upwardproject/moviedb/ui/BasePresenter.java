package com.upwardproject.moviedb.ui;

/**
 * Created by Dark on 28/07/2017.
 */

public interface BasePresenter<V> {
    void attachView(V view);

    void detachView();
}
