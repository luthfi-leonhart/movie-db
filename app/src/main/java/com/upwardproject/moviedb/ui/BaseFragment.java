package com.upwardproject.moviedb.ui;

import android.content.Context;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    public Context getApplicationContext() {
        return getContext().getApplicationContext();
    }
}
