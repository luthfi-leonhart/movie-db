package com.upwardproject.moviedb.ui.movie;

import android.os.Bundle;

import com.upwardproject.moviedb.R;
import com.upwardproject.moviedb.ui.BaseActivity;
import com.upwardproject.moviedb.ui.movie.list.MovieListFragment;
import com.upwardproject.moviedb.util.ActivityUtil;

public class MovieActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
                    new MovieListFragment(),
                    R.id.container,
                    false);
        }
    }
}
