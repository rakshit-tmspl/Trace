package com.tmspl.trace.fcm;

import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.tmspl.trace.apimodel.DbModel;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.MyApplication;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rakshit.sathwara on 1/21/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyApplication.APP_TAG + MyFirebaseInstanceIDService.class.getSimpleName();

    Realm realm;
    DbModel dbModel;

    @Override
    public void onTokenRefresh() {
        //Get updated InstanceID token

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Log.e(TAG, "onTokenRefresh: " + refreshedToken);

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        dbModel = realm.createObject(DbModel.class);

        dbModel.setFcmToken(refreshedToken);

        realm.commitTransaction();

        RealmResults<DbModel> tagsBeanModelRealmResults = realm.where(DbModel.class).findAll();

        for (DbModel model1 : tagsBeanModelRealmResults) {
            Log.e(TAG, "IN Realm" + model1.getFcmToken());
        }

        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(Constants.FCM_INSTANCE_ID, refreshedToken)
                .apply();

    }
}
