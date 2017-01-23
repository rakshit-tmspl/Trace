package com.tmspl.trace.fcm;

import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.MyApplication;

/**
 * Created by rakshit.sathwara on 1/21/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyApplication.APP_TAG + MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        //Get updated InstanceID token

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e(TAG, "onTokenRefresh: " + refreshedToken);

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(Constants.FCM_INSTANCE_ID, refreshedToken)
                .apply();

    }
}
