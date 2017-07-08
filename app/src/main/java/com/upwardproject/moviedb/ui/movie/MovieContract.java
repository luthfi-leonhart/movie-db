package com.upwardproject.moviedb.ui.movie;

import com.loopj.android.http.RequestParams;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.ui.BaseContract;

import java.util.List;

/**
 * Created by Dark on 07/07/2017.
 */

public class MovieContract {
    public interface ListView extends BaseContract.RemoteView{
        void onMovieListLoaded(List<Movie> movies);
    }

    public interface ListPresenter{
        void setFilter(int filter);

        int getFilter();

        void loadMovies(RequestParams params);
    }
}
