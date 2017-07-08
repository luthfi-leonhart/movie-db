package com.upwardproject.moviedb.util;

import org.json.JSONObject;

/**
 * Created by Dark on 07/03/2017.
 */

public class JSONParser {
    /**
     * Return the value mapped by the given key, or {@code null} if not present or null.
     */
    public static String optString(JSONObject json, String key) {
        // http://code.google.com/p/android/issues/detail?id=13830
        if (json.isNull(key))
            return null;
        else
            return json.optString(key, null);
    }

    public static long optLong(JSONObject json, String key) {
        // http://code.google.com/p/android/issues/detail?id=13830
        if (json.isNull(key))
            return 0;
        else
            return Long.parseLong(json.optString(key, null));
    }
}
