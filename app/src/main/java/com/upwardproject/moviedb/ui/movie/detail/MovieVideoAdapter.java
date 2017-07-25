package com.upwardproject.moviedb.ui.movie.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upwardproject.moviedb.R;
import com.upwardproject.moviedb.model.MovieReview;
import com.upwardproject.moviedb.model.MovieVideo;
import com.upwardproject.moviedb.ui.BaseListAdapter;
import com.upwardproject.moviedb.util.IntentUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dark on 04/03/2017.
 */

class MovieVideoAdapter extends BaseListAdapter<MovieReview> {
    private List<MovieVideo> mItemList;

    MovieVideoAdapter(List<MovieVideo> itemList) {
        mItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_video, parent, false);
        return new MovieVideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MovieVideoViewHolder viewHolder = (MovieVideoViewHolder) holder;
        viewHolder.bindData(mItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    class MovieVideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_video_title_tv)
        TextView tvTitle;

        MovieVideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindData(final MovieVideo data) {
            tvTitle.setText(data.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.watchYoutubeVideo(itemView.getContext(), data.key);
                }
            });
        }
    }
}
