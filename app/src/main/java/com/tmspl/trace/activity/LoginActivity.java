package com.tmspl.trace.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.tmspl.trace.R;
import com.tmspl.trace.api.API;
import com.tmspl.trace.api.RetrofitCallbacks;
import com.tmspl.trace.apimodel.LoginResponse;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.MyApplication;
import com.tmspl.trace.extra.Preferences;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends TraceActivity implements Validator.ValidationListener {

    private static final String TAG = MyApplication.APP_TAG + LoginActivity.class.getSimpleName();

    @Inject
    API api;

    @NotEmpty
    @Email
    @BindView(R.id.et_username)
    EditText etUsername;

    @NotEmpty
    @BindView(R.id.et_password)
    EditText etPassword;

    @BindView(R.id.btn_sign_in)
    Button btnSignIn;

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        api = ((MyApplication) getApplicationContext()).getTraceComponent().api();

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

    }

    /**
     * Called when all {@link Rule}s pass.
     */
    @Override
    public void onValidationSucceeded() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        api.loginUser(LoginActivity.this, Constants.AUTH, email, password, new OtpVerificationCallback(LoginActivity.this));
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

    private class OtpVerificationCallback extends RetrofitCallbacks<LoginResponse> {
        public OtpVerificationCallback(Context loginActivity) {
            super();
        }

        @Override
        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
            super.onResponse(call, response);

            if (response.isSuccessful()) {

                LoginResponse.ResponseJsonBean responseJsonBean = new LoginResponse.ResponseJsonBean();

                int userId = responseJsonBean.getUsertype();

                LoginResponse.ResponseJsonBean.QueryResultBean bean = new LoginResponse.ResponseJsonBean.QueryResultBean();

                Preferences.savePreferences(LoginActivity.this, "userType", String.valueOf(userId));
                Preferences.savePreferences(LoginActivity.this, "password", bean.getPassword());

                if (userId == 1) {
                    Preferences.savePreferences(LoginActivity.this, "firstName", bean.getFirstName());
                    Preferences.savePreferences(LoginActivity.this, "lastName", bean.getLastName());
                    Preferences.savePreferences(LoginActivity.this, "email", bean.getEmail());
                    Preferences.savePreferences(LoginActivity.this, "mobile", bean.getMobile());
                    Preferences.savePreferences(LoginActivity.this, "userId", String.valueOf(userId));
                    Preferences.savePreferences(LoginActivity.this, "userType", "1");
                    finish();
                }

                Toast.makeText(LoginActivity.this, "Login", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "something wrong", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<LoginResponse> call, Throwable t) {
            super.onFailure(call, t);

            Log.i(TAG, "ERROR ON login");
            Log.i(TAG, "call.toString(): " + call.toString());
            Log.i(TAG, "t.toString(): " + t.toString());

            Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
        }
    }
}
