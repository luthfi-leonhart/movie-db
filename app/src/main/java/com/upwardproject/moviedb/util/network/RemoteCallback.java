package com.upwardproject.moviedb.util.network;

/**
 * Created by Dark on 07/03/2017.
 */

public class RemoteCallback {

    public interface Load<T> {
        void onDataLoaded(T data);

        void onFailed();

        void onDataNotAvailable(String message);
    }

    public interface Save<T> {
        void onDataSaved(T data);

        void onSaveFailed(String message);
    }

    public interface Update<T> {
        void onDataUpdated(T data);

        void onUpdateFailed(String message);
    }

    public interface Delete<T> {
        void onDataDeleted(T data);

        void onDeleteFailed(String message);
    }
}
