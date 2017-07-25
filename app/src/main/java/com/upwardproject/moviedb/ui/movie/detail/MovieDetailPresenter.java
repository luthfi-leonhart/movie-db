package com.upwardproject.moviedb.ui.movie.detail;

import com.loopj.android.http.RequestParams;
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

class MovieDetailPresenter implements MovieContract.DetailPresenter {

    private MovieContract.DetailView view;

    MovieDetailPresenter(MovieContract.DetailView view) {
        this.view = view;
    }

    @Override
    public void loadVideos(int id, RequestParams params) {
        view.setVideosProgressIndicator(true);

        MovieVideoRepository.list(view.getContext(), id, params, new RemoteCallback.Load<List<MovieVideo>>() {
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

        MovieReviewRepository.list(view.getContext(), id, params, new RemoteCallback.Load<List<MovieReview>>() {
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
}
