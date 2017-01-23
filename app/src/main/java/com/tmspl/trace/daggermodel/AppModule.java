package com.tmspl.trace.daggermodel;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

@Module
public class AppModule {

    Application mApplication;

    public AppModule(Application application)
    {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication()
    {
        return mApplication;
    }
}
