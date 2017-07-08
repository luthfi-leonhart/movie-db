package com.upwardproject.moviedb.ui.movie.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.upwardproject.moviedb.R;
import com.upwardproject.moviedb.constant.MovieDbApi;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.ui.BaseActivity;
import com.upwardproject.moviedb.util.LDate;

public class MovieDetailFragment extends Fragment {

    private Toolbar toolbar;
    private ImageView ivBackdrop;
    private TextView tvTitle, tvReleaseDate, tvVoteAverage, tvOverview;

    private Movie movie;

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle b = new Bundle();
        b.putParcelable(Movie.PARAM_MODEL, movie);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movie = getArguments().getParcelable(Movie.PARAM_MODEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ivBackdrop = (ImageView) view.findViewById(R.id.movie_backdrop_iv);
        tvTitle = (TextView) view.findViewById(R.id.movie_title_tv);
        tvReleaseDate = (TextView) view.findViewById(R.id.movie_release_date_tv);
        tvVoteAverage = (TextView) view.findViewById(R.id.movie_vote_average_tv);
        tvOverview = (TextView) view.findViewById(R.id.movie_overview_tv);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((BaseActivity) getActivity()).setToolbar(toolbar, null);
        ActionBar ab = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        bindData();
    }

    private void bindData() {
        Glide.with(getContext())
                .load(MovieDbApi.BASE_BACKDROP_URL + movie.getBackdropPath())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(ivBackdrop);

        tvTitle.setText(movie.getTitle());

        String releaseDate = LDate.getDetailedDate(movie.getReleaseDate(), LDate.defaultDateFormat(), LDate.dateFormatddMMMyyyy());
        tvReleaseDate.setText(String.format(getString(R.string.movie_release_date), releaseDate));

        tvVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
        Drawable drawable = AppCompatResources.getDrawable(getContext(), R.drawable.ic_star);
        tvVoteAverage.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

        tvOverview.setText(movie.getOverview());
    }

//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList(PARAM_LIST, mItemList);
//    }

}
