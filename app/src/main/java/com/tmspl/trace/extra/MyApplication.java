package com.tmspl.trace.extra;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.tmspl.trace.daggercomponent.DaggerTraceComponent;
import com.tmspl.trace.daggercomponent.TraceComponent;
import com.tmspl.trace.daggermodel.ApiModule;
import com.tmspl.trace.daggermodel.AppModule;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

public class MyApplication extends MultiDexApplication {

    public static final String APP_TAG = "TRACE";

    private static final String TAG = APP_TAG + MyApplication.class.getSimpleName();

    private TraceComponent traceComponent;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        traceComponent = DaggerTraceComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(Constants.API_BASE_URL))
                .build();
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

    public TraceComponent getTraceComponent() {
        return traceComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
