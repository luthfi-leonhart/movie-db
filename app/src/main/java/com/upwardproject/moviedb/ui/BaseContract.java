package com.upwardproject.moviedb.ui;

import android.content.Context;

/**
 * Created by Dark on 06/06/2016.
 */
public class BaseContract {

    public interface BaseView {
        Context getContext();

        Context getApplicationContext();
    }

    public interface RemoteView extends BaseView {
        void setProgressIndicator(final boolean active);

        void showEmpty(String message);

        void showError();
    }
}
