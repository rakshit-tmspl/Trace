package com.tmspl.trace.extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rakshit.sathwara on 1/21/2017.
 */

public class NetworkConnectionDetector extends BroadcastReceiver {

    public static final String CONNECTIVITY_CHANGED = "io.fusionbit.khalaiyo.CONNECTIVITY_CHANGED";

    @Override
    public void onReceive(Context context, Intent intent) {


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            context.sendBroadcast(new Intent(CONNECTIVITY_CHANGED).putExtra("is_active", true));
        } else {
            context.sendBroadcast(new Intent(CONNECTIVITY_CHANGED).putExtra("is_active", false));
        }

    }

    public static boolean isInternetOn(final Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
