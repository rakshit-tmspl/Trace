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
import android.widget.Toast;

import com.tmspl.trace.R;
import com.tmspl.trace.activity.homeactivity.HomeActivity;
import com.tmspl.trace.api.API;
import com.tmspl.trace.api.RetrofitCallbacks;
import com.tmspl.trace.apimodel.AddData;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.MyApplication;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Response;

public class OtpVerificationActivity extends AppCompatActivity {

    private static final String TAG = MyApplication.APP_TAG + LoginActivity.class.getSimpleName();

    EditText txt_enter_code;
    Button btn_verify;
    Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        context = OtpVerificationActivity.this;
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
                try {
                    if (txt_enter_code.getText().toString().equals(Preferences.getSavedPreferences(context, "otp")) && txt_enter_code.getText().toString()
                            .length() > 0) {
                        if (Preferences.getSavedPreferences(context, "forgot").equals("1")) {
                            Preferences.savePreferences(context, "forgot", "");
                            finish();
                            startActivity(new Intent(context, GenerateNewPasswordActivity.class));
                        } else {

                            API.getInstance().addData(OtpVerificationActivity.this, Constants.AUTH, Constants.rfitst_name,
                                    Constants.remail, Constants.rmobile, Constants.rpassword, "y", Constants.rphoto,
                                    new RetrofitCallbacks(OtpVerificationActivity.this) {
                                        @Override
                                        public void onResponse(Call call, Response response) {
                                            super.onResponse(call, response);

                                            if (response.isSuccessful()) {
                                                if (response.body() == null) {
                                                    Toast.makeText(OtpVerificationActivity.this, "Something wrong!",
                                                            Toast.LENGTH_SHORT).show();
                                                } else {

                                                    AddData addData = (AddData) response.body();
                                                    Log.e(TAG, "onResponse: " + addData.getResponseMessage());

                                                    AddData.ResponseJsonBean responseJsonBean = ((AddData) response.body()).getResponseJson();
                                                    List<AddData.ResponseJsonBean.RiderQueryResultBean> riderQueryResultBeen = responseJsonBean.getRiderQueryResult();
                                                    List<AddData.ResponseJsonBean.UserQueryResultBean> userQueryResultBeen = responseJsonBean.getUserQueryResult();

                                                    if (Constants.ut == 1) {
                                                        for (AddData.ResponseJsonBean.UserQueryResultBean user : userQueryResultBeen) {
                                                            Log.e(TAG, "onResponse: " + user.getUserId());
                                                            Preferences.savePreferences(context, "usertype", "1");
                                                            Preferences.savePreferences(context, "first_name", user.getFirstName());
                                                            Preferences.savePreferences(context, "email", user.getEmail());
                                                            Preferences.savePreferences(context, "mobile", user.getMobile());
                                                            Preferences.savePreferences(context, "user_id", user.getUserId());
                                                            Preferences.savePreferences(context, "password", Constants.rpassword);
                                                            finish();
                                                            startActivity(new Intent(context, HomeActivity.class));
                                                        }
                                                    } else if (Constants.ut == 3) {
                                                        Alert.showAlertWithFinish(context, "Please contact head office and complete your physical verification process !!");
                                                    } else {
                                                        Toast.makeText(OtpVerificationActivity.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call call, Throwable t) {
                                            super.onFailure(call, t);
                                            Log.i(TAG, t.toString());
                                            Log.i(TAG, call.toString());
                                            Toast.makeText(OtpVerificationActivity.this, "Something went wrong",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            //new register_entity(context).execute();
                        }
                    } else {
                        Alert.ShowAlert(context, "Please Enter valid OTP!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    public class register_entity extends AsyncTask<String, String,
            String> {

        Activity context;
        AlertDialog pd;

        public register_entity(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Please wait..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                ServiceHandler sh = new ServiceHandler();
                String Service = "";
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                if (Constants.ut == 1) {
                    if (Constants.rphoto.length() != 0) {
                        nameValuePairs.add(new BasicNameValuePair("image", Constants.rphoto));
                    }
                    nameValuePairs.add(new BasicNameValuePair("first_name", Constants.rfitst_name));
                    nameValuePairs.add(new BasicNameValuePair("email", Constants.remail));
                    nameValuePairs.add(new BasicNameValuePair("mobile", Constants.rmobile));
                    nameValuePairs.add(new BasicNameValuePair("password", Constants.rpassword));
                    nameValuePairs.add(new BasicNameValuePair("is_validate_user", "y"));
                    //nameValuePairs.add(new BasicNameValuePair("gcm_id", Preferences.getSavedPreferences(context, "gcm_id")));
                    Service = "add_data";
                } else if (Constants.ut == 2) {
                    if (Constants.rphoto.length() != 0) {
                        nameValuePairs.add(new BasicNameValuePair("company_logo", Constants.rphoto));
                    }
                    nameValuePairs.add(new BasicNameValuePair("company_name", Constants.rfitst_name));
                    nameValuePairs.add(new BasicNameValuePair("email", Constants.remail));
                    nameValuePairs.add(new BasicNameValuePair("mobile", Constants.rmobile));
                    nameValuePairs.add(new BasicNameValuePair("password", Constants.rpassword));
                    nameValuePairs.add(new BasicNameValuePair("category", Constants.rcategory));
                    nameValuePairs.add(new BasicNameValuePair("is_validate_user", "y"));
                    //nameValuePairs.add(new BasicNameValuePair("gcm_id", Preferences.getSavedPreferences(context, "gcm_id")));
                    Service = "add_depot_data";
                } else if (Constants.ut == 3) {
                    if (Constants.rphoto.length() != 0) {
                        nameValuePairs.add(new BasicNameValuePair("image", Constants.rphoto));
                    }
                    nameValuePairs.add(new BasicNameValuePair("first_name", Constants.rfitst_name));
                    nameValuePairs.add(new BasicNameValuePair("email", Constants.remail));
                    nameValuePairs.add(new BasicNameValuePair("mobile", Constants.rmobile));
                    nameValuePairs.add(new BasicNameValuePair("password", Constants.rpassword));
                    nameValuePairs.add(new BasicNameValuePair("is_validate_user", "y"));
                    // nameValuePairs.add(new BasicNameValuePair("gcm_id", Preferences.getSavedPreferences(context, "gcm_id")));
                    Service = "add_data";
                }

                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                        + Service, ServiceHandler.POST, nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Registration goes failed please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Registration goes failed please try again later\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Log.e("Response", "onPostExecute: " + result);
            if (pd.isShowing())
                pd.dismiss();
            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Alert.ShowAlert(context, jobj1.getString
                            ("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {

                        JSONObject ResponseObj = new JSONObject(jobj1.getString("responseJson"));
                        JSONObject record = ResponseObj.getJSONObject("record");

                        if (Constants.ut == 1) {
                            Preferences.savePreferences(context, "usertype", "1");
                            Preferences.savePreferences(context, "first_name", record.getString("first_name"));
                            Preferences.savePreferences(context, "email", record.getString("email"));
                            Preferences.savePreferences(context, "mobile", record.getString("mobile"));
                            Preferences.savePreferences(context, "user_id", record.getString("user_id"));
                            Preferences.savePreferences(context, "password", record.getString("password"));
                            finish();
                            startActivity(new Intent(context, HomeActivity.class));
                        } else if (Constants.ut == 2) {
                            Preferences.savePreferences(context, "usertype", "2");
                            Preferences.savePreferences(context, "company_name", record.getString("company_name"));
                            Preferences.savePreferences(context, "email", record.getString("email"));
                            Preferences.savePreferences(context, "mobile", record.getString("mobile"));
                            Preferences.savePreferences(context, "depot_id", record.getString("depot_id"));
                            Preferences.savePreferences(context, "category", record.getString("category"));
                            Preferences.savePreferences(context, "password", record.getString("password"));
                            finish();
                            startActivity(new Intent(context, UserHomeActivity.class));
                        } else if (Constants.ut == 3) {
                            Alert.showAlertWithFinish(context, "Please contact head office and complete your physical verification process !!");
                        }

                    } else {
                        Alert.ShowAlert(context, jobj1.getString
                                ("responseMessage"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
