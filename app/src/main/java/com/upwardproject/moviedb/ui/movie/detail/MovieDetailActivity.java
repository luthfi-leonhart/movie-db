package com.upwardproject.moviedb.ui.movie.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loopj.android.http.RequestParams;
import com.upwardproject.moviedb.BuildConfig;
import com.upwardproject.moviedb.R;
import com.upwardproject.moviedb.constant.MovieDbApi;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.model.MovieReview;
import com.upwardproject.moviedb.model.MovieVideo;
import com.upwardproject.moviedb.ui.BaseActivity;
import com.upwardproject.moviedb.ui.movie.MovieContract;
import com.upwardproject.moviedb.util.LDate;
import com.upwardproject.moviedb.util.network.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends BaseActivity implements MovieContract.DetailView {

    private static final String PARAM_REVIEWS = "reviews";
    private static final String PARAM_REVIEWS_STATE = "reviews_state";
    private static final String PARAM_VIDEOS = "videos";
    private static final String PARAM_VIDEOS_STATE = "videos_state";

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.movie_backdrop_iv)
    ImageView ivBackdrop;
    @BindView(R.id.movie_favorite_iv)
    ImageView ivFavorite;
    @BindView(R.id.movie_title_tv)
    TextView tvTitle;
    @BindView(R.id.movie_release_date_tv)
    TextView tvReleaseDate;
    @BindView(R.id.movie_vote_average_tv)
    TextView tvVoteAverage;
    @BindView(R.id.movie_overview_tv)
    TextView tvOverview;
    @BindView(R.id.movie_videos_empty_tv)
    TextView tvEmptyVideos;
    @BindView(R.id.movie_videos_progressbar)
    ProgressBar pbVideos;
    @BindView(R.id.movie_videos_rv)
    RecyclerView rvVideos;
    @BindView(R.id.movie_reviews_empty_tv)
    TextView tvEmptyReviews;
    @BindView(R.id.movie_reviews_progressbar)
    ProgressBar pbReviews;
    @BindView(R.id.movie_reviews_rv)
    RecyclerView rvReviews;

    private MenuItem itemFavorite;

    private MovieDetailPresenter mPresenter;

    private Movie mMovie;
    private ArrayList<MovieVideo> mVideos;
    private ArrayList<MovieReview> mReviews;

    private int actionBarHeight;

    public static Intent newInstance(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(Movie.PARAM_MODEL, movie);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mPresenter = new MovieDetailPresenter(getContentResolver());
        mPresenter.attachView(this);

        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        initViews();

        if (getIntent().hasExtra(Movie.PARAM_MODEL)) {
            mMovie = getIntent().getParcelableExtra(Movie.PARAM_MODEL);

            bindData(mMovie);

            if (savedInstanceState == null) {
                loadVideos(mMovie.getId());
                loadReviews(mMovie.getId());
            }
        }

        toggleFavoriteIcon();
    }

    private void initViews() {
        setToolbar(toolbar, null);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShown = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset > actionBarHeight) {
                    if (itemFavorite != null) itemFavorite.setVisible(false);
                    isShown = false;
                } else if (!isShown) {
                    if (itemFavorite != null) itemFavorite.setVisible(true);
                    isShown = true;
                }
            }
        });

        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });

        rvVideos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvVideos.setHasFixedSize(true);
        rvVideos.setNestedScrollingEnabled(false);

        rvReviews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvReviews.setNestedScrollingEnabled(false);

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.list_divider));
        rvReviews.addItemDecoration(divider);

        tvEmptyVideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadVideos(mMovie.getId());
            }
        });

        tvEmptyReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadReviews(mMovie.getId());
            }
        });
    }

    private void bindData(Movie movie) {
        Glide.with(this)
                .load(MovieDbApi.BASE_BACKDROP_URL + movie.getBackdropPath())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(ivBackdrop);

        tvTitle.setText(movie.getTitle());
        String releaseDate = LDate.getDetailedDate(movie.getReleaseDate(), LDate.defaultDateFormat(), LDate.dateFormatddMMMyyyy());
        tvReleaseDate.setText(releaseDate);
        tvVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
        tvOverview.setText(movie.getOverview());
    }

    private void setConnectionError(TextView textView) {
        textView.setText(R.string.error_network_unavailable_retry);
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(PARAM_REVIEWS, mReviews);
        outState.putParcelable(PARAM_REVIEWS_STATE, rvReviews.getLayoutManager().onSaveInstanceState());
        outState.putParcelableArrayList(PARAM_VIDEOS, mVideos);
        outState.putParcelable(PARAM_VIDEOS_STATE, rvVideos.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Parcelable reviewsState = savedInstanceState.getParcelable(PARAM_REVIEWS_STATE);
        rvReviews.getLayoutManager().onRestoreInstanceState(reviewsState);
        mReviews = savedInstanceState.getParcelableArrayList(PARAM_REVIEWS);

        if (mReviews != null) onReviewsLoaded(mReviews);
        else loadReviews(mMovie.getId());

        Parcelable videosState = savedInstanceState.getParcelable(PARAM_VIDEOS_STATE);
        rvVideos.getLayoutManager().onRestoreInstanceState(videosState);
        mVideos = savedInstanceState.getParcelableArrayList(PARAM_VIDEOS);
        onVideosLoaded(mVideos);

        if (mVideos != null) onVideosLoaded(mVideos);
        else loadVideos(mMovie.getId());
    }

    /*
     * LOAD VIDEOS
     */

    private void loadVideos(int id) {
        if (!NetworkUtil.isNetworkConnected(this)) {
            setConnectionError(tvEmptyVideos);
            return;
        }

        RequestParams params = new RequestParams();
        params.put(MovieDbApi.PARAM_API_KEY, BuildConfig.MOVIEDB_API_KEY);

        mPresenter.loadVideos(id, params);
    }

    @Override
    public void setVideosProgressIndicator(boolean active) {
        pbVideos.setVisibility(active ? View.VISIBLE : View.GONE);
        if (active) tvEmptyVideos.setVisibility(View.GONE);
    }

    @Override
    public void onVideosEmpty() {
        tvEmptyVideos.setText(R.string.movie_videos_empty);
        tvEmptyVideos.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVideosLoadError() {
        tvEmptyVideos.setText(R.string.movie_videos_error);
        tvEmptyVideos.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVideosLoaded(List<MovieVideo> itemList) {
        mVideos = new ArrayList<>(itemList);
        rvVideos.setAdapter(new MovieVideoAdapter(mVideos));
        rvVideos.setVisibility(View.VISIBLE);
    }

    /*
     * LOAD REVIEWS
     */

    private void loadReviews(int id) {
        if (!NetworkUtil.isNetworkConnected(this)) {
            setConnectionError(tvEmptyReviews);
            return;
        }

        RequestParams params = new RequestParams();
        params.put(MovieDbApi.PARAM_API_KEY, BuildConfig.MOVIEDB_API_KEY);
        params.put(MovieDbApi.PARAM_PAGE, 1);

        mPresenter.loadReviews(id, params);
    }

    @Override
    public void setReviewsProgressIndicator(boolean active) {
        pbReviews.setVisibility(active ? View.VISIBLE : View.GONE);
        if (active) tvEmptyReviews.setVisibility(View.GONE);
    }

    @Override
    public void onReviewsEmpty() {
        tvEmptyReviews.setText(R.string.movie_reviews_empty);
        tvEmptyReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReviewsLoadError() {
        tvEmptyReviews.setText(R.string.movie_reviews_error);
        tvEmptyReviews.setVisibility(View.VISIBLE);
    }

    @Override
    public void onReviewsLoaded(List<MovieReview> itemList) {
        mReviews = new ArrayList<>(itemList);
        rvReviews.setAdapter(new MovieReviewAdapter(mReviews));
        rvReviews.setVisibility(View.VISIBLE);
    }

    /*
     * FAVORITE HANDLING
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        itemFavorite = menu.findItem(R.id.ac_favorite);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isFavorite = mPresenter.isFavoriteMovie(mMovie.getId());

        int iconResId = isFavorite ? R.drawable.ic_star : R.drawable.ic_star_outline;
        int textResId = isFavorite ? R.string.movie_favorite_remove : R.string.movie_favorite_set;

        itemFavorite.setIcon(iconResId);
        itemFavorite.setTitle(textResId);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ac_favorite) {
            toggleFavorite();
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleFavorite() {
        if (mPresenter.isFavoriteMovie(mMovie.getId())) {
            mPresenter.removeFromFavorite(mMovie.getId());
        } else mPresenter.setAsFavorite(mMovie);

        toggleFavoriteIcon();
    }

    private void toggleFavoriteIcon() {
        boolean isFavorite = mPresenter.isFavoriteMovie(mMovie.getId());

        int iconResId = isFavorite ? R.drawable.ic_star : R.drawable.ic_star_outline;
        int textResId = isFavorite ? R.string.movie_favorite_remove : R.string.movie_favorite_set;

        ivFavorite.setImageResource(iconResId);
        ivFavorite.setContentDescription(getString(textResId));

        supportInvalidateOptionsMenu();
    }
}
