package com.tmspl.trace.activity.homeactivity.module;

import com.tmspl.trace.activity.homeactivity.HomeActivity;
import com.tmspl.trace.activity.homeactivity.HomeActivityScope;
import com.tmspl.trace.activity.homeactivity.adapter.DemoAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dagger.Module;
import dagger.Provides;

/**
 * Created by rakshit.sathwara on 1/19/2017.
 */

@Module
public class HomeActivityModule {

    final HomeActivity homeActivity;
    String[] arry = {"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"};


    public HomeActivityModule(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }

    @Provides
    @HomeActivityScope
    public DemoAdapter demoAdapter() {
        return new DemoAdapter(homeActivity, stringList);
    }

    List<String> stringList = new ArrayList<String>(Arrays.asList(arry));
}
