package com.upwardproject.moviedb.ui.movie;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.model.MovieReview;
import com.upwardproject.moviedb.model.MovieVideo;
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

    public interface DetailView{
        Context getContext();

        void setVideosProgressIndicator(boolean active);

        void onVideosEmpty();

        void onVideosLoadError();

        void onVideosLoaded(List<MovieVideo> itemList);

        void setReviewsProgressIndicator(boolean active);

        void onReviewsEmpty();

        void onReviewsLoadError();

        void onReviewsLoaded(List<MovieReview> itemList);
    }

    public interface DetailPresenter{
        void loadVideos(int id, RequestParams params);

        void loadReviews(int id, RequestParams params);
    }
}
