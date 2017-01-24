package com.tmspl.trace.api;

import android.content.Context;

import com.tmspl.trace.apimodel.LoginResponse;
import com.tmspl.trace.extra.MyApplication;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

public class API {

    private static final String TAG = MyApplication.APP_TAG + API.class.getSimpleName();
    Retrofit retrofit;
    private ApiInterface apiService;

    @Inject
    public API(Retrofit retrofit) {
        this.retrofit = retrofit;
        apiService = retrofit.create(ApiInterface.class);
    }

    public void loginUser(final Context context, final String auth, final String email, final String password,
                          final RetrofitCallbacks<LoginResponse> callback) {

        Call<LoginResponse> loginResponseCall = apiService.getLoginResponse(auth, email, password);

        loginResponseCall.enqueue(callback);

    }


}
