package com.tmspl.trace.extra;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

public class Preferences {
    public static String getSavedPreferences(Context context, String key) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            return sharedPreferences.getString(key, "");
        } catch (Exception e) {
            return "";
        }
    }

    public static void savePreferences(Context context, String key, String value) {
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {

        }
    }
}
