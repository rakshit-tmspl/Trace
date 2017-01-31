package com.tmspl.trace.api;

import com.tmspl.trace.apimodel.LoginNewResponse;

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
    Call<LoginNewResponse> getLoginResponse(@Field("auth") String auth, @Field("mobile") String email,
                                            @Field("password") String password);

}
