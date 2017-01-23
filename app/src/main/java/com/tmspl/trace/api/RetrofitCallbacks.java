package com.tmspl.trace.api;

import android.support.annotation.CallSuper;

import com.tmspl.trace.extra.MyApplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

public class RetrofitCallbacks<T> implements Callback<T>
{
    private static final String TAG = MyApplication.APP_TAG + RetrofitCallbacks.class.getSimpleName();

    @CallSuper
    @Override
    public void onResponse(Call<T> call, Response<T> response)
    {

    }

    @CallSuper
    @Override
    public void onFailure(Call<T> call, Throwable t)
    {
        if (!call.isCanceled())
        {

        }
    }
}