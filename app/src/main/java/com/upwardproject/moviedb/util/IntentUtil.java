package com.upwardproject.moviedb.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Dark on 25/07/2017.
 */

public class IntentUtil {
    /**
     * Courtesy: https://stackoverflow.com/a/12439378/2178568
     * @param context Context
     * @param id Youtube key id
     */
    public static void watchYoutubeVideo(Context context, String id) {
        try {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            context.startActivity(webIntent);
        }
    }
}
