package com.tmspl.trace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.tmspl.trace.R;
import com.tmspl.trace.activity.ridersactivity.RiderHomeActivity;
import com.tmspl.trace.extra.LocationService;
import com.tmspl.trace.extra.MyApplication;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    private static final String TAG = MyApplication.APP_TAG + LoginActivity.class.getSimpleName();

    @NotEmpty
    @BindView(R.id.et_username)
    EditText etUsername;

    @NotEmpty
    @Password
    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.btn_sign_in)
    Button btnSignIn;

    Validator validator;
    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

    }

    /**
     * Called when all {@link Rule}s pass.
     */
    @Override
    public void onValidationSucceeded() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        startService();
        startActivity(new Intent(LoginActivity.this, RiderHomeActivity.class));
//        API.getInstance().loginUser(LoginActivity.this, Constants.AUTH, email, password, new RetrofitCallbacks<LoginNewResponse>(LoginActivity.this) {
//            @Override
//            public void onResponse(Call<LoginNewResponse> call, Response<LoginNewResponse> response) {
//                super.onResponse(call, response);
//
//                if (response.isSuccessful()) {
//
//                    if (response.body() == null) {
//                        Toast.makeText(LoginActivity.this, "Null", Toast.LENGTH_SHORT).show();
//                    } else {
//                        LoginNewResponse.ResponseJsonBean responseJsonBean = response.body().getResponseJson();
//
//                        int user = responseJsonBean.getUserUsertype();
//                        int rider = responseJsonBean.getRiderUsertype();
////
//                        Log.e(TAG, "onResponse: " + rider);
//
//                        List<LoginNewResponse.ResponseJsonBean.UserQueryResultBean> userList = responseJsonBean
//                                .getUserQueryResult();
//
//                        List<LoginNewResponse.ResponseJsonBean.RiderQueryResultBean> riderList = responseJsonBean
//                                .getRiderQueryResult();
//
//                        Preferences.savePreferences(LoginActivity.this, "usertype", String.valueOf(user));
//
//                        if (Constants.ut == 3) {
//
//                            Preferences.savePreferences(LoginActivity.this, "usertype", String.valueOf(rider));
//
//                            for (LoginNewResponse.ResponseJsonBean.RiderQueryResultBean rider1 : riderList) {
//                                Log.e(TAG, "onResponse: RIDER_ID " + rider1.getRiderId());
//                                Preferences.savePreferences(LoginActivity.this, "rider_id", rider1.getRiderId());
//                                Preferences.savePreferences(LoginActivity.this, "first_name", rider1.getFirstName());
//                                Preferences.savePreferences(LoginActivity.this, "last_name", rider1.getFirstName());
//                                Preferences.savePreferences(LoginActivity.this, "mobile", rider1.getMobile());
//                                Preferences.savePreferences(LoginActivity.this, "email", rider1.getEmail());
//                                Preferences.savePreferences(LoginActivity.this, "rider_id", rider1.getRiderId());
//                            }
//
//                            Preferences.savePreferences(LoginActivity.this, "usertype", "3");
//                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                            StrictMode.setThreadPolicy(policy);
////                            if (record.getString("rider_image").equals("noimage.jpg")) {
////                                logo = BitmapFactory.decodeResource(getResources(), R.drawable.rider_name);
////                            } else {
////                                logo = MyImageLoader.getImageFromUrl(context, Constant.Image_IP + record.getString("rider_image"));
////                            }
//
//                            //  Preferences.savePreferences(context, "r_image", Utility.encodeTobase64(logo));
//
//
//                            startService();
//                        } else {
//                            for (LoginNewResponse.ResponseJsonBean.UserQueryResultBean user1 : userList) {
//                                Log.e(TAG, "onResponse: user_id : " + user1.getUserId());
//                                Preferences.savePreferences(LoginActivity.this, "first_name", user1.getFirstName());
//                                Preferences.savePreferences(LoginActivity.this, "last_name", user1.getLastName());
//                                Preferences.savePreferences(LoginActivity.this, "email", user1.getEmail());
//                                Preferences.savePreferences(LoginActivity.this, "mobile", user1.getMobile());
//                                Preferences.savePreferences(LoginActivity.this, "user_id", user1.getUserId());
//                            }
//                            Preferences.savePreferences(LoginActivity.this, "usertype", "1");
//                            finish();
//                        }
//
////                        if (user == 1) {
////                            for (LoginNewResponse.ResponseJsonBean.UserQueryResultBean user1 : userList) {
////                                Log.e(TAG, "onResponse: user_id : " + user1.getUserId());
////                                Preferences.savePreferences(LoginActivity.this, "first_name", user1.getFirstName());
////                                Preferences.savePreferences(LoginActivity.this, "last_name", user1.getLastName());
////                                Preferences.savePreferences(LoginActivity.this, "email", user1.getEmail());
////                                Preferences.savePreferences(LoginActivity.this, "mobile", user1.getMobile());
////                                Preferences.savePreferences(LoginActivity.this, "user_id", user1.getUserId());
////                            }
////                            Preferences.savePreferences(LoginActivity.this, "usertype", "1");
////                            finish();
////                        } else if (rider == 3) {
////
////                        }
//                        finish();
//
//                        Constants.isLogin = 1;
//                        if (Preferences.getSavedPreferences(LoginActivity.this, "usertype").equals("3") == false) {
//                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                        } else if (Preferences.getSavedPreferences(LoginActivity.this, "usertype").equals("3")) {
//                            startActivity(new Intent(LoginActivity.this, RiderHomeActivity.class));
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginNewResponse> call, Throwable t) {
//                super.onFailure(call, t);
//
//                Log.i(TAG, t.toString());
//                Log.i(TAG, call.toString());
//                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * Called when one or several {@link Rule}s fail.
     *
     * @param errors List containing references to the {@link View}s and
     *               {@link Rule}s that failed.
     */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof AutoCompleteTextView) {
                ((AutoCompleteTextView) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startService() {
        startService(new Intent(this, LocationService.class));
    }
}
