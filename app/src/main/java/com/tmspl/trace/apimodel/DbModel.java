package com.tmspl.trace.apimodel;

import io.realm.RealmObject;

/**
 * Created by rakshit.sathwara on 2/7/2017.
 */

public class DbModel extends RealmObject {

    String fcmToken, password;

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
