package com.tmspl.trace.api;

import com.tmspl.trace.apimodel.AddData;
import com.tmspl.trace.apimodel.LoginNewResponse;
import com.tmspl.trace.apimodel.RiderPendingOrderResponse;

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

    @FormUrlEncoded
    @POST("add_data")
    Call<AddData> ADD_DATA_CALL(@Field("first_name") String fName, @Field("email") String email,
                                @Field("mobile") String contact, @Field("auth") String auth, @Field("image") String image,
                                @Field("password") String password, @Field("is_validate_user") String isValid);

    @FormUrlEncoded
    @POST("pending_order_list")
    Call<RiderPendingOrderResponse> RIDER_PENDING_ORDER_RESPONSE_CALL(@Field("auth") String auth,
                                                                      @Field("lat_long") String latlan,
                                                                      @Field("rider_id") String riderId);
}
