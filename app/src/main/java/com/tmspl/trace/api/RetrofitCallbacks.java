package com.tmspl.trace.api;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.widget.Toast;

import com.tmspl.trace.extra.MyApplication;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

public class RetrofitCallbacks<T> implements Callback<T> {
    private static final String TAG = MyApplication.APP_TAG + RetrofitCallbacks.class.getSimpleName();

    final Context context;

    public RetrofitCallbacks(Context context)
    {
        this.context = context;
    }

    @CallSuper
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        System.out.println("INSIDE ON RESPONSE");
        System.out.println("RESPONSE CODE:" + response.code());

        if (response.isSuccessful()) {

            if (response.code() != HttpURLConnection.HTTP_OK) {
                System.out.println(response.errorBody().toString());
            } else if (response.code() == HttpURLConnection.HTTP_OK) {
                System.out.println(response.body().toString());
            }
        }
    }

    @CallSuper
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (!call.isCanceled()) {
            if (!call.isCanceled())
            {
                Toast.makeText(context, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                System.out.println("INSIDE ON FAILURE");
                System.out.println(t.toString());
            }
        }
    }
}