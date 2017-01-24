package com.tmspl.trace.api;

import com.tmspl.trace.apimodel.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by rakshit.sathwara on 1/18/2017.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login_authentiocation")
    Call<LoginResponse> getLoginResponse(@Field("auth") String auth, @Field("email") String email,
                                         @Field("password") String password);

}
