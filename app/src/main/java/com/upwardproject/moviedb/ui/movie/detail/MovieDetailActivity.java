package com.upwardproject.moviedb.ui.movie.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.upwardproject.moviedb.R;
import com.upwardproject.moviedb.constant.MovieDbApi;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.ui.BaseActivity;
import com.upwardproject.moviedb.util.LDate;

public class MovieDetailActivity extends BaseActivity {

    private Toolbar toolbar;
    private ImageView ivBackdrop;
    private TextView tvTitle, tvReleaseDate, tvVoteAverage, tvOverview;

    private Movie movie;

    public static Intent newInstance(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(Movie.PARAM_MODEL, movie);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBackdrop = (ImageView) findViewById(R.id.movie_backdrop_iv);
        tvTitle = (TextView) findViewById(R.id.movie_title_tv);
        tvReleaseDate = (TextView) findViewById(R.id.movie_release_date_tv);
        tvVoteAverage = (TextView) findViewById(R.id.movie_vote_average_tv);
        tvOverview = (TextView) findViewById(R.id.movie_overview_tv);

        setToolbar(toolbar, null);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().hasExtra(Movie.PARAM_MODEL)) {
            movie = getIntent().getParcelableExtra(Movie.PARAM_MODEL);
            bindData();
        }
    }

    private void bindData() {
        Glide.with(this)
                .load(MovieDbApi.BASE_BACKDROP_URL + movie.getBackdropPath())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(ivBackdrop);

        tvTitle.setText(movie.getTitle());

        String releaseDate = LDate.getDetailedDate(movie.getReleaseDate(), LDate.defaultDateFormat(), LDate.dateFormatddMMMyyyy());
        tvReleaseDate.setText(String.format(getString(R.string.movie_release_date), releaseDate));

        tvVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
        Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.ic_star);
        tvVoteAverage.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

        tvOverview.setText(movie.getOverview());
    }
}
