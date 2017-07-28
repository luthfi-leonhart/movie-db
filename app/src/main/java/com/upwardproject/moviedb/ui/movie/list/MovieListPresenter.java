package com.upwardproject.moviedb.ui.movie.list;

import android.content.SharedPreferences;
import android.database.Cursor;

import com.loopj.android.http.RequestParams;
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

    private MovieContract.ListView view;
    private SharedPreferences sp;

    MovieListPresenter(SharedPreferences sp) {
        this.sp = sp;
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
    public void loadMoviesFromRemote(int filter, RequestParams params) {
        view.setProgressIndicator(true);

        MovieRepository.getListFromRemote(filter, params, new RemoteCallback.Load<List<Movie>>() {
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

    @Override
    public void loadMoviesFromLocal(Cursor cursor) {
        if (cursor.getCount() == 0) {
            view.showEmpty(null);
            view.setProgressIndicator(false);
            return;
        }

        List<Movie> itemList = new ArrayList<>();

        if (cursor.getPosition() == cursor.getCount()) cursor.moveToPosition(-1);

        while (cursor.moveToNext()) {
            Movie movie = MovieRepository.getCompleteDataFromCursor(cursor);
            itemList.add(movie);
        }

        view.onMovieListLoaded(itemList);
        view.setProgressIndicator(false);
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
