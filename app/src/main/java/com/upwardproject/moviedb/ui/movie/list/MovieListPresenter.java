package com.upwardproject.moviedb.ui.movie.list;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.loopj.android.http.RequestParams;
import com.upwardproject.moviedb.constant.MovieDbApi;
import com.upwardproject.moviedb.constant.Settings;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.model.MovieRepository;
import com.upwardproject.moviedb.ui.movie.MovieContract;
import com.upwardproject.moviedb.ui.movie.MovieFilter;
import com.upwardproject.moviedb.util.network.RemoteCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dark on 07/07/2017.
 */

class MovieListPresenter implements MovieContract.ListAction {

    private final int ID_FAVORITE_MOVIES_LOADER = 12;

    private MovieContract.ListView view;
    private SharedPreferences sp;
    private LoaderManager loaderManager;

    MovieListPresenter(SharedPreferences sp, LoaderManager loaderManager) {
        this.sp = sp;
        this.loaderManager = loaderManager;
    }

    @Override
    public void setFilter(int filter) {
        if (sp != null) {
            sp.edit()
                    .putInt(Settings.CURRENT_FILTER.name(), filter)
                    .apply();
        }
    }

    @Override
    public int getFilter() {
        if (sp == null) return 0;

        return sp.getInt(Settings.CURRENT_FILTER.name(), MovieFilter.POPULAR);
    }

    @Override
    public void loadMovies(int filter, RequestParams params) {
        view.setProgressIndicator(true);

        RemoteCallback.Load<List<Movie>> callback = new RemoteCallback.Load<List<Movie>>() {
            @Override
            public void onDataLoaded(List<Movie> data) {
                view.onMovieListLoaded(data);
                view.setProgressIndicator(false);
            }

            @Override
            public void onFailed() {
                view.showError();
                view.setProgressIndicator(false);
            }

            @Override
            public void onDataNotAvailable(String message) {
                view.showEmpty(message);
                view.setProgressIndicator(false);
            }
        };

        switch (filter) {
            case MovieFilter.POPULAR:
                MovieRepository.getListFromRemote(MovieDbApi.popularMoviesUrl, params, callback);
                break;
            case MovieFilter.TOP_RATED:
                MovieRepository.getListFromRemote(MovieDbApi.topRatedMoviesUrl, params, callback);
                break;
            case MovieFilter.FAVORITE:
                loadFavoriteMovies(callback);
                break;
            default:
                throw new RuntimeException("Filter index not found");
        }
    }

    private void loadFavoriteMovies(final RemoteCallback.Load<List<Movie>> callback) {
        loaderManager.initLoader(ID_FAVORITE_MOVIES_LOADER, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                if (id == ID_FAVORITE_MOVIES_LOADER) return view.getFavoriteMovieLoader(this);

                throw new RuntimeException("Loader id " + id + " not found");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data.getCount() == 0) {
                    callback.onDataNotAvailable(null);
                    return;
                }

                List<Movie> itemList = new ArrayList<>();

                if (data.getPosition() == data.getCount()) data.moveToPosition(-1);

                while (data.moveToNext()) {
                    Movie movie = MovieRepository.getCompleteDataFromCursor(data);
                    itemList.add(movie);
                }

                callback.onDataLoaded(itemList);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
            }
        });
    }

    @Override
    public void attachView(MovieContract.ListView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
