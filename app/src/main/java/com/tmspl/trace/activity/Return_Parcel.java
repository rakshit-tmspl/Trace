package com.tmspl.trace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmspl.trace.R;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.NetworkUtil;
import com.tmspl.trace.extra.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class Return_Parcel extends Activity {


    public static ImageView complete_delivery_parcel_img;
    public static TextView complete_price_txt, complete_count_txt;
    public static Activity context;
    public static Button btn_track_order;
    public static EditText complete_secret_edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.return_parcel);
        context = Return_Parcel.this;
        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        complete_secret_edt = (EditText) findViewById(R.id.complete_reason_edt);
        complete_delivery_parcel_img = (ImageView) findViewById(R.id.complete_delivery_parcel_img);
        complete_price_txt = (TextView) findViewById(R.id.complete_price_txt);
        complete_count_txt = (TextView) findViewById(R.id.complete_count_txt);
        btn_track_order = (Button) findViewById(R.id.btn_track_order);

        if (AcceptedDeliveriesActivity.logo.equals("noimage.jpg") || AcceptedDeliveriesActivity.logo.length() == 0) {
            Picasso.with(context)
                    .load(R.drawable.photo)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(complete_delivery_parcel_img);
        } else {
            Picasso.with(context)
                    .load(Constants.Image_IP + AcceptedDeliveriesActivity.logo)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(complete_delivery_parcel_img);

        }

        complete_price_txt.setText("Rs. " + AcceptedDeliveriesActivity.to_rs);
        complete_count_txt.setText(AcceptedDeliveriesActivity.to_count);
        btn_track_order.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (complete_secret_edt.getText().toString().length() > 0) {
                    if (NetworkUtil.isInternetConnencted(context)) {
                        new make_return(context).execute();
                    } else {
                        Alert.showInternetConnError(context);
                    }
                } else {
                    Alert.ShowAlert(context, "Please enter reason");
                }
            }
        });
    }

    public class make_return extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public make_return(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Updating Delivery..");

            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id",
                        AcceptedDeliveriesActivity.to_id));

                nameValuePairs.add(new BasicNameValuePair("return_note",
                        complete_secret_edt.getText().toString()));
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "make_return", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Updating Delivery Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Updating Delivery Goes Failed Please try again later\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Alert.ShowAlert(context, jobj1.getString("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {

                        String res = jobj1.getString("responseJson");
                        if (res.equals("1")) {
                            Alert.showAlertWithFinish(context, "Return note added successfully!");
                            startActivity(new Intent(context, AcceptedDeliveriesActivity.class));
                        } else {
                            Alert.showError(context);
                        }


                    } else {
                        Alert.ShowAlert(context,
                                jobj1.getString("responseMessage"));
                    }
                }

                if (pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
