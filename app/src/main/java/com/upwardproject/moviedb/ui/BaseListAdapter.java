package com.upwardproject.moviedb.ui;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Dark on 06/01/2017.
 */

public abstract class BaseListAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected ItemClickListener mListener;
}
