package com.tmspl.trace.api;

import android.content.Context;

import com.tmspl.trace.apimodel.AddData;
import com.tmspl.trace.apimodel.LoginNewResponse;
import com.tmspl.trace.apimodel.RiderPendingOrderResponse;
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


    private API() {
        apiService = ApiClient.getClient().create(ApiInterface.class);
    }

    //Get the only object available
    public static API getInstance() {
        return instance;
    }

    public void loginUser(final Context context, final String auth, final String email, final String password,
                          final String fcmToken, final RetrofitCallbacks<LoginNewResponse> callback) {

        Call<LoginNewResponse> loginResponseCall = apiService.getLoginResponse(auth, email, password, fcmToken);

        loginResponseCall.enqueue(callback);

    }

    public void addData(final Context context, final String auth, final String fName, final String email,
                        final String contact, final String password, final String isValid, final String image,
                        final RetrofitCallbacks callback) {
        Call<AddData> addDataCall = apiService.ADD_DATA_CALL(fName, email, contact, auth, image, password, isValid);
        addDataCall.enqueue(callback);
    }


    public void riderPendingOrder(final Context context, final String auth, final String latLan, final String riderId,
                                  final RetrofitCallbacks callback) {
        Call<RiderPendingOrderResponse> riderPendingOrderResponseCall = apiService.RIDER_PENDING_ORDER_RESPONSE_CALL(auth,
                latLan, riderId);
        riderPendingOrderResponseCall.enqueue(callback);

    }
}
