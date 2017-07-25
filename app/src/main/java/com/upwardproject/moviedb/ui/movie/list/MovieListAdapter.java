package com.upwardproject.moviedb.ui.movie.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.upwardproject.moviedb.R;
import com.upwardproject.moviedb.constant.MovieDbApi;
import com.upwardproject.moviedb.model.Movie;
import com.upwardproject.moviedb.ui.ItemClickListener;
import com.upwardproject.moviedb.ui.BaseListAdapter;

import java.util.List;

/**
 * Created by Dark on 04/03/2017.
 */

class MovieListAdapter extends BaseListAdapter<Movie> {
    private List<Movie> mItemList;

    MovieListAdapter(List<Movie> itemList, ItemClickListener listener) {
        mItemList = itemList;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieViewHolder viewHolder = (MovieViewHolder) holder;
        viewHolder.bindData(mItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    private class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivPoster;
        private ItemClickListener mListener;

        private Movie data;

        MovieViewHolder(View itemView, ItemClickListener listener) {
            super(itemView);

            ivPoster = (ImageView) itemView;
            mListener = listener;
        }

        void bindData(Movie data) {
            this.data = data;

            Glide.with(itemView.getContext())
                    .load(MovieDbApi.BASE_POSTER_URL + data.getPosterPath())
                    .placeholder(R.drawable.loading_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivPoster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClicked(v, data, getAdapterPosition());
        }
    }
}
