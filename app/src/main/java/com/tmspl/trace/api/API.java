package com.tmspl.trace.api;

import android.content.Context;

import com.tmspl.trace.apimodel.LoginNewResponse;
import com.tmspl.trace.extra.MyApplication;

import retrofit2.Call;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

public class API {

    private static final String TAG = MyApplication.APP_TAG + API.class.getSimpleName();
    //create an object of SingleObject
    private static API instance = new API();
    private ApiInterface apiService;


    private API()
    {
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    //Get the only object available
    public static API getInstance()
    {
        return instance;
    }

    public void loginUser(final Context context, final String auth, final String email, final String password,
                          final RetrofitCallbacks<LoginNewResponse> callback) {

        Call<LoginNewResponse> loginResponseCall = apiService.getLoginResponse(auth, email, password);

        loginResponseCall.enqueue(callback);

    }


}
