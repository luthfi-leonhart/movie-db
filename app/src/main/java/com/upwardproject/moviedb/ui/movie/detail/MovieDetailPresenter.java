package com.upwardproject.moviedb.ui.movie.detail;

import android.content.ContentResolver;

import com.loopj.android.http.RequestParams;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.model.MovieRepository;
import com.upwardproject.moviedb.model.MovieReview;
import com.upwardproject.moviedb.model.MovieReviewRepository;
import com.upwardproject.moviedb.model.MovieVideo;
import com.upwardproject.moviedb.model.MovieVideoRepository;
import com.upwardproject.moviedb.ui.movie.MovieContract;
import com.upwardproject.moviedb.util.network.RemoteCallback;

import java.util.List;

/**
 * Created by Dark on 25/07/2017.
 */

class MovieDetailPresenter implements MovieContract.DetailAction {

    private MovieContract.DetailView view;
    private ContentResolver resolver;

    MovieDetailPresenter(ContentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void loadVideos(int id, RequestParams params) {
        view.setVideosProgressIndicator(true);

        MovieVideoRepository.list(id, params, new RemoteCallback.Load<List<MovieVideo>>() {
            @Override
            public void onDataLoaded(List<MovieVideo> data) {
                view.onVideosLoaded(data);
                view.setVideosProgressIndicator(false);
            }

            @Override
            public void onFailed() {
                view.onVideosLoadError();
                view.setVideosProgressIndicator(false);
            }

            @Override
            public void onDataNotAvailable(String message) {
                view.onVideosEmpty();
                view.setVideosProgressIndicator(false);
            }
        });
    }

    @Override
    public void loadReviews(int id, RequestParams params) {
        view.setReviewsProgressIndicator(true);

        MovieReviewRepository.list(id, params, new RemoteCallback.Load<List<MovieReview>>() {
            @Override
            public void onDataLoaded(List<MovieReview> data) {
                view.onReviewsLoaded(data);
                view.setReviewsProgressIndicator(false);
            }

            @Override
            public void onFailed() {
                view.onReviewsLoadError();
                view.setReviewsProgressIndicator(false);
            }

            @Override
            public void onDataNotAvailable(String message) {
                view.onReviewsEmpty();
                view.setReviewsProgressIndicator(false);
            }
        });
    }

    @Override
    public boolean isFavoriteMovie(int movieId) {
        return MovieRepository.isExistInLocal(resolver, movieId);
    }

    @Override
    public void setAsFavorite(Movie movie) {
        MovieRepository.saveToLocal(resolver, movie);
    }

    @Override
    public void removeFromFavorite(int movieId) {
        MovieRepository.deleteFromLocal(resolver, movieId);
    }

    @Override
    public void attachView(MovieContract.DetailView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
