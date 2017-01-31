package com.tmspl.trace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tmspl.trace.R;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class ForgotPasswordActivity extends AppCompatActivity {

    public static EditText txt_enter_code;
    Button btn_verify;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        context = ForgotPasswordActivity.this;
        InitUI();
        InitAction();
    }

    private void InitUI() {

        txt_enter_code = (EditText) findViewById(R.id.et_entercode);
        btn_verify = (Button) findViewById(R.id.btn_verify);
    }

    private void InitAction() {
        btn_verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (txt_enter_code.getText().toString().length() > 0) {
                    new generate_forgot_password_key(context).execute();
                } else {
                    Alert.ShowAlert(context, "Please Enter Email Id!");
                }

            }
        });
    }

    public class generate_forgot_password_key extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public generate_forgot_password_key(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Confirming Email Address..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("mobile", txt_enter_code.getText().toString()));
                Preferences.savePreferences(context, "mobile", txt_enter_code.getText().toString());
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL + "generate_forgot_password_key", ServiceHandler.POST, nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"OTP Verification goes failed please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"OTP Verification goes failed please try again later\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("RESPONSE FORGOTPASSWORD", "onPostExecute: " + result);

            if (pd.isShowing())
                pd.dismiss();
            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Alert.ShowAlert(context, jobj1.getString("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {
                        Preferences.savePreferences(context, "forgot", "1");
                        Preferences.savePreferences(context, "otp", jobj1.getString("responseJson"));
                        startActivity(new Intent(context, OtpVerificationActivity.class));
                    } else {
                        Alert.ShowAlert(context, jobj1.getString("responseMessage"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
