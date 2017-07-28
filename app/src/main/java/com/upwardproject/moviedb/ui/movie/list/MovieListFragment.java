package com.upwardproject.moviedb.ui.movie.list;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
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
import com.upwardproject.moviedb.data.DatabaseContract;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.ui.BaseActivity;
import com.upwardproject.moviedb.ui.BaseListFragment;
import com.upwardproject.moviedb.ui.ItemClickListener;
import com.upwardproject.moviedb.ui.movie.MovieContract;
import com.upwardproject.moviedb.ui.movie.MovieFilter;
import com.upwardproject.moviedb.ui.movie.detail.MovieDetailActivity;
import com.upwardproject.moviedb.ui.widget.ItemOffsetDecoration;
import com.upwardproject.moviedb.util.network.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MovieListFragment extends BaseListFragment implements MovieContract.ListView,
        ItemClickListener {

    private static final String PARAM_LIST = "movie_list";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Unbinder unbinder;

    private ArrayList<Movie> mItemList;
    private MovieListPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        mPresenter = new MovieListPresenter(sp, getLoaderManager());
        mPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);

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
        int textResId = 0;
        switch (filter) {
            case MovieFilter.POPULAR:
                textResId = R.string.movie_title_popular;
                break;
            case MovieFilter.TOP_RATED:
                textResId = R.string.movie_title_top_rated;
                break;
            case MovieFilter.FAVORITE:
                textResId = R.string.movie_title_favorite;
                break;
        }

        ((BaseActivity) getActivity()).setToolbar(toolbar, textResId);

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

        int textResId;
        switch (filter) {
            case MovieFilter.POPULAR:
                textResId = R.string.movie_title_popular;
                break;
            case MovieFilter.TOP_RATED:
                textResId = R.string.movie_title_top_rated;
                break;
            case MovieFilter.FAVORITE:
                textResId = R.string.movie_title_favorite;
                break;
            default:
                throw new RuntimeException("Filter index not found");
        }

        getActivity().setTitle(textResId);
    }

    @Override
    public void loadData() {
        if (mPresenter.getFilter() == MovieFilter.FAVORITE && pageToLoad > 1) {
            // No need to load another data for favorites, because we already retrieved them all
            pageToLoad = 1;
            return;
        }

        if (!NetworkUtil.isNetworkConnected(getContext())) {
            showConnectionError();
            return;
        }

        RequestParams params = new RequestParams();
        params.put(MovieDbApi.PARAM_API_KEY, BuildConfig.MOVIEDB_API_KEY);
        params.put(MovieDbApi.PARAM_PAGE, pageToLoad);

        mPresenter.loadMovies(mPresenter.getFilter(), params);
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

    @Override
    public void showEmpty(String message) {
        if (isAdded() && mPresenter.getFilter() == MovieFilter.FAVORITE) {
            emptyViewHolder.setMessage(message != null ? message : getString(R.string.movie_favorite_empty));
            emptyViewHolder.setIcon(R.drawable.ic_star_outline);
            emptyViewHolder.showButton(false);
            rvList.setAsEmpty();
            return;
        }

        emptyViewHolder.showButton(true);
        super.showEmpty(message);
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
    }

    @Override
    public CursorLoader getFavoriteMovieLoader(LoaderManager.LoaderCallbacks callbacks) {
        return new CursorLoader(getContext(),
                DatabaseContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
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
                case MovieFilter.FAVORITE:
                    menu.findItem(R.id.ac_sort_favorite).setChecked(true);
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
            case R.id.ac_sort_favorite:
                // TODO get favorite list
                toggleFilter(MovieFilter.FAVORITE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.detachView();
    }
}
