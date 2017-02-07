package com.tmspl.trace.extra;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

public class MyApplication extends MultiDexApplication {

    public static final String APP_TAG = "TRACE";

    private static final String TAG = APP_TAG + MyApplication.class.getSimpleName();


    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static MyApplication mInstance;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        Realm.init(this);

        final RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .name(Constants.REALM_DATABASE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfig);

      /*  traceComponent = DaggerTraceComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(Constants.API_BASE_URL_TRACE))
                .build();*/
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static boolean hasNetwork() {
        return mInstance.checkIfHasNetwork();
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
