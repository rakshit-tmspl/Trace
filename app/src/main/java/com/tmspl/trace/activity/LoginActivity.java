package com.tmspl.trace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.tmspl.trace.R;
import com.tmspl.trace.activity.homeactivity.HomeActivity;
import com.tmspl.trace.activity.ridersactivity.RiderHomeActivity;
import com.tmspl.trace.api.API;
import com.tmspl.trace.api.RetrofitCallbacks;
import com.tmspl.trace.apimodel.LoginNewResponse;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.LocationService;
import com.tmspl.trace.extra.MyApplication;
import com.tmspl.trace.extra.Preferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

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
    @BindView(R.id.rb_user)
    AppCompatRadioButton rbUser;
    @BindView(R.id.rb_rider)
    AppCompatRadioButton rbRider;
    @BindView(R.id.rg_user_type)
    RadioGroup rgUserType;

    public static String type;
    private String password;

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


        rgUserType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_user) {
                    type = "user";
                } else {
                    type = "rider";
                }

            }
        });

    }

    /**
     * Called when all {@link Rule}s pass.
     */
    @Override
    public void onValidationSucceeded() {
        String email = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();


        //Login Api
        API.getInstance().loginUser(LoginActivity.this, Constants.AUTH, email, password, new RetrofitCallbacks<LoginNewResponse>(LoginActivity.this) {
            @Override
            public void onResponse(Call<LoginNewResponse> call, Response<LoginNewResponse> response) {
                super.onResponse(call, response);

                if (response.isSuccessful()) {

                    if (response.body() == null) {
                        Toast.makeText(LoginActivity.this, "Null", Toast.LENGTH_SHORT).show();
                    } else {
                        LoginNewResponse.ResponseJsonBean responseJsonBean = response.body().getResponseJson();

                        int user = responseJsonBean.getUserUsertype();
                        int rider = responseJsonBean.getRiderUsertype();

                        List<LoginNewResponse.ResponseJsonBean.UserQueryResultBean> userList = responseJsonBean
                                .getUserQueryResult();

                        List<LoginNewResponse.ResponseJsonBean.RiderQueryResultBean> riderList = responseJsonBean
                                .getRiderQueryResult();


                        Log.e(TAG, "onResponse: " + type);

                        if (type.equals("user")) {

                            //set USER Data and go to User side Activity (HomeActivity)
                            Preferences.savePreferences(LoginActivity.this, "usertype", String.valueOf(user));

                            //getting user data from UserQueryResultBean list

                            for (LoginNewResponse.ResponseJsonBean.UserQueryResultBean userBean : userList) {
                                Log.e(TAG, "onResponse: USER-ID" + userBean.getUserId());

                                Preferences.savePreferences(LoginActivity.this, "first_name", userBean.getFirstName());
                                Preferences.savePreferences(LoginActivity.this, "last_name", userBean.getLastName());
                                Preferences.savePreferences(LoginActivity.this, "email", userBean.getEmail());
                                Preferences.savePreferences(LoginActivity.this, "mobile", userBean.getMobile());
                                Preferences.savePreferences(LoginActivity.this, "user_id", userBean.getUserId());
                                Preferences.savePreferences(LoginActivity.this, "password", password);
                            }

                            finish();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {

                            //set RIDER Data and go to Rider side Activity (RiderHomeActivity)
                            Preferences.savePreferences(LoginActivity.this, "usertype", String.valueOf(rider));

                            //Getting rider data from RiderQueryResultBean list
                            for (LoginNewResponse.ResponseJsonBean.RiderQueryResultBean riderBean : riderList) {
                                Log.e(TAG, "onResponse: RIDER-ID" + riderBean.getRiderId());

                                Preferences.savePreferences(LoginActivity.this, "first_name", riderBean.getFirstName());
                                Preferences.savePreferences(LoginActivity.this, "last_name", riderBean.getLastName());
                                Preferences.savePreferences(LoginActivity.this, "mobile", riderBean.getMobile());
                                Preferences.savePreferences(LoginActivity.this, "email", riderBean.getEmail());
                                Preferences.savePreferences(LoginActivity.this, "rider_id", riderBean.getRiderId());
                            }

                            startService();
                            finish();
                            startActivity(new Intent(LoginActivity.this, RiderHomeActivity.class));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginNewResponse> call, Throwable t) {
                super.onFailure(call, t);

                Log.i(TAG, t.toString());
                Log.i(TAG, call.toString());
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
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
            if (view instanceof EditText) {
                ((TextView) (EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void startService() {
        startService(new Intent(this, LocationService.class));
    }
}
