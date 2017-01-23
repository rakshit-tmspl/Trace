package com.tmspl.trace.daggercomponent;

import com.tmspl.trace.activity.TraceActivity;
import com.tmspl.trace.api.API;
import com.tmspl.trace.daggermodel.ApiModule;
import com.tmspl.trace.daggermodel.AppModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface TraceComponent {
    API api();

    void inject(TraceActivity activity);
}
