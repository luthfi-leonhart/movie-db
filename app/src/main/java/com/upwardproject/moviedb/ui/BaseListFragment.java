package com.upwardproject.moviedb.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.upwardproject.moviedb.R;
import com.upwardproject.moviedb.ui.widget.EndlessRecyclerViewOnScrollListener;

public abstract class BaseListFragment extends BaseFragment implements BaseContract.RemoteView, SwipeRefreshLayout.OnRefreshListener {
    protected final int LIMIT = 50;
    private final String PARAM_PAGE = "page";

    protected SwipeRefreshLayout srlRefresh;
    protected RecyclerView rvList;

    protected int pageToLoad = 1;
    protected BaseListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) pageToLoad = savedInstanceState.getInt(PARAM_PAGE);
        else pageToLoad = 1;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        srlRefresh.setColorSchemeResources(R.color.colorAccent);
        srlRefresh.setOnRefreshListener(this);

        rvList.setLayoutManager(getLayoutManager());
        if (endlessLoaderEnabled()) {
            rvList.addOnScrollListener(new EndlessRecyclerViewOnScrollListener(rvList.getLayoutManager()) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    pageToLoad = page + 1;
                    loadData();
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        pageToLoad = 1;
        loadData();
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        srlRefresh.post(new Runnable() {
            @Override
            public void run() {
                srlRefresh.setRefreshing(active);
            }
        });
    }

    protected void showConnectionError() {
        if (isAdded()) {
            rvList.setVisibility(View.GONE);
            setProgressIndicator(false);
            Toast.makeText(getContext(), R.string.error_network_unavailable, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showEmpty(String message) {
        if (isAdded() && pageToLoad == 1) {
            rvList.setVisibility(View.GONE);
            Toast.makeText(getContext(), message != null ? message : getString(R.string.error_data_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showError() {
        if (isAdded()) {
            Toast.makeText(getContext(), R.string.error_loading_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(PARAM_PAGE, pageToLoad);
    }

    public abstract void loadData();

    protected abstract boolean endlessLoaderEnabled();

    protected abstract RecyclerView.LayoutManager getLayoutManager();
}
