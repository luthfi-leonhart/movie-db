package com.upwardproject.moviedb.ui.movie.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upwardproject.moviedb.R;
import com.upwardproject.moviedb.model.MovieReview;
import com.upwardproject.moviedb.ui.BaseListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dark on 04/03/2017.
 */

class MovieReviewAdapter extends BaseListAdapter<MovieReview> {
    private List<MovieReview> mItemList;

    MovieReviewAdapter(List<MovieReview> itemList) {
        mItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_review, parent, false);
        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieReviewViewHolder viewHolder = (MovieReviewViewHolder) holder;
        viewHolder.bindData(mItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_review_author_tv)
        TextView tvAuthor;
        @BindView(R.id.movie_review_content_tv)
        TextView tvContent;

        MovieReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(MovieReview data) {
            tvAuthor.setText(data.author);
            tvContent.setText(data.getContent());
        }
    }
}
