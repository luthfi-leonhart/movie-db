package com.upwardproject.moviedb.ui.movie.list;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.loopj.android.http.RequestParams;
import com.upwardproject.moviedb.constant.Settings;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.model.MovieRepository;
import com.upwardproject.moviedb.ui.movie.MovieContract;
import com.upwardproject.moviedb.ui.movie.MovieFilter;
import com.upwardproject.moviedb.util.network.RemoteCallback;

import java.util.List;

/**
 * Created by Dark on 07/07/2017.
 */

public class MovieListPresenter implements MovieContract.ListPresenter {
    private MovieContract.ListView view;

    public MovieListPresenter(MovieContract.ListView view) {
        this.view = view;
    }

    @Override
    public void setFilter(int filter) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        sp.edit()
                .putInt(Settings.CURRENT_FILTER.name(), filter)
                .apply();
    }

    @Override
    public int getFilter() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        return sp.getInt(Settings.CURRENT_FILTER.name(), MovieFilter.POPULAR);
    }

    @Override
    public void loadMovies(RequestParams params) {
        view.setProgressIndicator(true);

        MovieRepository.list(view.getContext(), getFilter(), params, new RemoteCallback.Load<List<Movie>>() {
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
        });
    }
}
