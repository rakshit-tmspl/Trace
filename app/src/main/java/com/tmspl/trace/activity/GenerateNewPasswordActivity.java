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

public class GenerateNewPasswordActivity extends AppCompatActivity {

    EditText et_new_password, et_confirm_password;
    Button btn_verify;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_new_password);
        context = GenerateNewPasswordActivity.this;
        InitUI();
        InitAction();
    }

    private void InitUI() {

        et_new_password = (EditText) findViewById(R.id.et_new_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        btn_verify = (Button) findViewById(R.id.btn_verify);
    }

    private void InitAction() {
        btn_verify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (et_new_password.getText().toString().equals(et_confirm_password.getText().toString()) == false) {
                    Alert.ShowAlert(context, "Password Doesn't match!");
                } else {
                    if (et_new_password.length() < 6) {
                        Alert.ShowAlert(context, "Minimum length of password must be 6!");
                    } else {
                        new make_user_active(context).execute();
                    }
                }

            }
        });
    }

    public class make_user_active extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public make_user_active(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Updating Password..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("mobile", Preferences.getSavedPreferences(context, "mobile")));
                nameValuePairs.add(new BasicNameValuePair("password", et_new_password.getText().toString()));
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL + "update_password", ServiceHandler.POST, nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Updating Password goes failed please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Updating Password goes failed please try again later\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("RESPONSE PASSWORD-NEW", "onPostExecute: " + result);
            if (pd.isShowing())
                pd.dismiss();
            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Alert.ShowAlert(context, jobj1.getString("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {
                        finish();
                        startActivity(new Intent(context, LoginActivity.class));
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
