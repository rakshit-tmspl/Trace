package com.tmspl.trace.activity.homeactivity.component;

import com.tmspl.trace.activity.homeactivity.HomeActivityScope;
import com.tmspl.trace.activity.homeactivity.module.HomeActivityModule;

import dagger.Component;

/**
 * Created by rakshit.sathwara on 1/19/2017.
 */


@HomeActivityScope
@Component(modules = HomeActivityModule.class)
public interface HomeActivityComponent {

//    void injectHomeActivity(HomeActivity homeActivity);
}
