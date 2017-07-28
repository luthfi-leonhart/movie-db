package com.upwardproject.moviedb.ui;

/**
 * Created by Dark on 06/06/2016.
 */
public class BaseContract {

    public interface RemoteView {
        void setProgressIndicator(final boolean active);

        void showEmpty(String message);

        void showError();
    }
}
