package com.upwardproject.moviedb.ui.movie.list;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.upwardproject.moviedb.BuildConfig;
import com.upwardproject.moviedb.R;
import com.upwardproject.moviedb.constant.MovieDbApi;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.ui.BaseActivity;
import com.upwardproject.moviedb.ui.BaseListFragment;
import com.upwardproject.moviedb.ui.ItemClickListener;
import com.upwardproject.moviedb.ui.movie.MovieContract;
import com.upwardproject.moviedb.ui.movie.MovieFilter;
import com.upwardproject.moviedb.ui.movie.detail.MovieDetailActivity;
import com.upwardproject.moviedb.ui.movie.detail.MovieDetailFragment;
import com.upwardproject.moviedb.ui.widget.EmptyRecyclerView;
import com.upwardproject.moviedb.ui.widget.ItemOffsetDecoration;
import com.upwardproject.moviedb.util.ActivityUtil;
import com.upwardproject.moviedb.util.network.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends BaseListFragment implements MovieContract.ListView,
        ItemClickListener {
    private static final String PARAM_LIST = "movie_list";

    private Toolbar toolbar;

    private ArrayList<Movie> mItemList;
    private MovieListPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mPresenter = new MovieListPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        srlRefresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        rvList = (EmptyRecyclerView) view.findViewById(R.id.list);

        View emptyView = view.findViewById(R.id.empty_view);
        initEmptyView(emptyView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvList.setHasFixedSize(true);
        rvList.addItemDecoration(new ItemOffsetDecoration(getContext(), R.dimen.item_offset));

        int filter = mPresenter.getFilter();
        ((BaseActivity) getActivity()).setToolbar(toolbar,
                filter == MovieFilter.POPULAR ? R.string.movie_title_popular : R.string.movie_title_top_rated);

        if (savedInstanceState != null) {
            mItemList = savedInstanceState.getParcelableArrayList(PARAM_LIST);
            bindData();
        }

        if (mItemList != null) bindData();
        else loadData();
    }

    private void toggleFilter(int filter) {
        pageToLoad = 1;
        mPresenter.setFilter(filter);
        loadData();

        getActivity().setTitle(filter == MovieFilter.POPULAR ? R.string.movie_title_popular : R.string.movie_title_top_rated);
    }

    @Override
    public void loadData() {
        if (!NetworkUtil.isNetworkConnected(getContext())) {
            showConnectionError();
            return;
        }

        RequestParams params = new RequestParams();
        params.put(MovieDbApi.PARAM_API_KEY, BuildConfig.MOVIEDB_API_KEY);
        params.put(MovieDbApi.PARAM_PAGE, pageToLoad);

        mPresenter.loadMovies(params);
    }

    @Override
    public void onMovieListLoaded(List<Movie> movies) {
        if (movies == null) return;

        if (pageToLoad == 1 || adapter == null) {
            mItemList = new ArrayList<>(movies);
            bindData();
            return;
        }

        mItemList.addAll(movies);
        adapter.notifyDataSetChanged();
    }

    private void bindData() {
        adapter = new MovieListAdapter(mItemList, this);
        rvList.setAdapter(adapter);
        rvList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClicked(View view, Object data, int position) {
        Movie movie = (Movie) data;

        startActivity(MovieDetailActivity.newInstance(getContext(), movie));
//        ActivityUtil.replaceFragment(getFragmentManager(),
//                MovieDetailFragment.newInstance(movie),
//                R.id.container);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_movie_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (mPresenter != null) {
            switch (mPresenter.getFilter()) {
                case MovieFilter.POPULAR:
                    menu.findItem(R.id.ac_sort_popular).setChecked(true);
                    break;
                case MovieFilter.TOP_RATED:
                    menu.findItem(R.id.ac_sort_top_rated).setChecked(true);
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ac_sort_popular:
                toggleFilter(MovieFilter.POPULAR);
                item.setChecked(true);
                break;
            case R.id.ac_sort_top_rated:
                toggleFilter(MovieFilter.TOP_RATED);
                item.setChecked(true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean endlessLoaderEnabled() {
        return true;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        int orientation = getResources().getConfiguration().orientation;
        int spanSize = orientation == Configuration.ORIENTATION_PORTRAIT ? 3 : 4;
        return new GridLayoutManager(getContext(), spanSize);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PARAM_LIST, mItemList);
    }

}
