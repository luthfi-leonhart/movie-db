package com.upwardproject.moviedb.ui;

import android.content.res.Resources;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by Dark on 29/05/2016.
 */
public class BaseActivity extends AppCompatActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void setToolbar(Toolbar toolbar, CharSequence title) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);

            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayShowTitleEnabled(title != null);
                if (title != null) setTitle(title);
            }
        }
    }

    public void setToolbar(Toolbar toolbar, int titleResId) {
        try {
            setToolbar(toolbar, getText(titleResId));
        } catch (Resources.NotFoundException e){
            setToolbar(toolbar, null);
        }
    }

    public void setLogo(int logoResId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setLogo(logoResId);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
