package com.upwardproject.moviedb.ui.widget;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.upwardproject.moviedb.R;

/**
 * Created by Dark on 25/11/2016.
 */

public class EmptyViewHolder {

    private View rootView;
    private TextView tvTitle, tvMessage;
    private Button button;
    private ImageView icon;

    public EmptyViewHolder(View view) {
        rootView = view;

        icon = (ImageView) view.findViewById(R.id.empty_image_iv);
//        tvTitle = (TextView) view.findViewById(R.id.empty_title_tv);
        tvMessage = (TextView) view.findViewById(R.id.empty_message_tv);
        button = (Button) view.findViewById(R.id.empty_button);
    }
//
//    public void init(int iconResId, String text, View.OnClickListener listener) {
//        updateIcon(iconResId);
//        setMessage(text);
//        setClickListener(listener);
//    }

    public EmptyViewHolder setTitleTextColor(int color) {
        tvTitle.setTextColor(color);
        return this;
    }

    public EmptyViewHolder setTitle(String text) {
        if (text != null) tvTitle.setText(text);
        return this;
    }

    public EmptyViewHolder setTitle(int textResId) {
        tvTitle.setText(textResId);
        return this;
    }

    public EmptyViewHolder setMessageTextColor(int color) {
        tvMessage.setTextColor(color);
        return this;
    }

    public EmptyViewHolder setMessage(String text) {
        if (text != null) tvMessage.setText(text);
        return this;
    }

    public EmptyViewHolder setMessage(int textResId) {
        tvMessage.setText(textResId);
        return this;
    }

    public EmptyViewHolder setIcon(int iconResId) {
        if (iconResId != 0) {
            icon.setVisibility(View.VISIBLE);
            icon.setImageResource(iconResId);
        }

        return this;
    }

    public EmptyViewHolder showIcon(boolean show) {
        icon.setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    public EmptyViewHolder setButtonText(String text) {
        button.setText(text);
        return this;
    }

    public EmptyViewHolder setClickListener(View.OnClickListener listener) {
        if (listener != null) {
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(listener);
        } else button.setVisibility(View.GONE);

        return this;
    }

    public EmptyViewHolder showButton(boolean show) {
        button.setVisibility(show ? View.VISIBLE : View.GONE);
        return this;
    }

    public void hide() {
        rootView.setVisibility(View.GONE);
    }

    public void show() {
        rootView.setVisibility(View.VISIBLE);
    }
}
