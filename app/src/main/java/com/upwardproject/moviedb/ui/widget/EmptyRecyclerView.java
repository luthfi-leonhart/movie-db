package com.upwardproject.moviedb.ui.widget;

/**
 * Source : http://stackoverflow.com/questions/27414173/equivalent-of-listview-setemptyview-in-recyclerview
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class EmptyRecyclerView extends RecyclerView {
	private View emptyView;
	private int zeroCount = 0;
	
	final private AdapterDataObserver observer = new AdapterDataObserver() {
	    @Override
	    public void onChanged() {
	        checkIfEmpty();
	    }
	
	    @Override
	    public void onItemRangeInserted(int positionStart, int itemCount) {
	        checkIfEmpty();
	    }
	
	    @Override
	    public void onItemRangeRemoved(int positionStart, int itemCount) {
	        checkIfEmpty();
	    }
	};
	
	public EmptyRecyclerView(Context context) {
	    super(context);
	}
	
	public EmptyRecyclerView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}
	
	public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	}
	
	public void checkIfEmpty() {
	    if (emptyView != null && getAdapter() != null) {
	        final boolean emptyViewVisible = getAdapter().getItemCount() == zeroCount;
	        emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
	        setVisibility(emptyViewVisible ? GONE : VISIBLE);
	    }
	}
	
	@Override
	public void setAdapter(Adapter adapter) {
	    final Adapter oldAdapter = getAdapter();
	    if (oldAdapter != null) {
	        oldAdapter.unregisterAdapterDataObserver(observer);
	    }
	    super.setAdapter(adapter);
	    if (adapter != null) {
	        adapter.registerAdapterDataObserver(observer);
	    }
	
	    checkIfEmpty();
	}
	
	public void setEmptyView(View emptyView) {
	    this.emptyView = emptyView;
	    checkIfEmpty();
	}

	public void setAsEmpty(){
		emptyView.setVisibility(VISIBLE);
		setVisibility(GONE);
	}

	// The value to show if the adapter item count is empty
	// ex : 1 is empty if the adapter has a header 
	public void setZeroCount(int value){
		zeroCount = value;
	}
}