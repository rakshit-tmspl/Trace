package com.tmspl.trace.extra;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

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

    @Override
    public void onCreate() {
        super.onCreate();

        traceComponent = DaggerTraceComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(Constants.API_BASE_URL))
                .build();
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
