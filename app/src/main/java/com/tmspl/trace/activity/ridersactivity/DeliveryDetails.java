package com.tmspl.trace.activity.ridersactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmspl.trace.R;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.NetworkUtil;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;
import com.tmspl.trace.fragment.rider.FragmentPendingsOrders;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class DeliveryDetails extends AppCompatActivity {

    public static LinearLayout from_add, to_add_1, to_add_2, to_add_3;
    public static TextView txt_from_add, txt_to_add_1, txt_to_add_2, txt_to_add_3;
    public static TextView delivery_total_amount_txt, delivery_delivery_txt, delivery_accept_txt, delivery_calcel_txt;
    public static ImageView parcel_img;
    public static Activity context;


    public static TextView txt_total_amt, txt_total_deliveries, delivery_deliver_pickup_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);
        context = DeliveryDetails.this;

        from_add = (LinearLayout) findViewById(R.id.from_address);
        to_add_1 = (LinearLayout) findViewById(R.id.to_address_1);
        to_add_2 = (LinearLayout) findViewById(R.id.to_address_2);
        to_add_3 = (LinearLayout) findViewById(R.id.to_address_3);

        txt_from_add = (TextView) findViewById(R.id.accepted_from_address);
        txt_to_add_1 = (TextView) findViewById(R.id.accepted_to_address_1);
        txt_to_add_2 = (TextView) findViewById(R.id.accepted_to_address_2);
        txt_to_add_3 = (TextView) findViewById(R.id.accepted_to_address_3);
        delivery_total_amount_txt = (TextView) findViewById(R.id.delivery_total_amount_txt);
        delivery_delivery_txt = (TextView) findViewById(R.id.delivery_delivery_txt);
        delivery_deliver_pickup_txt = (TextView) findViewById(R.id.delivery_deliver_pickup_txt);
        delivery_accept_txt = (TextView) findViewById(R.id.delivery_accept_txt);
        delivery_calcel_txt = (TextView) findViewById(R.id.delivery_calcel_txt);


        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delivery_calcel_txt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Preferences.savePreferences(context, "rejected", FragmentPendingsOrders.listBean.get(FragmentPendingsOrders.position).getOrder_id() + ",");
                finish();
                FragmentPendingsOrders.listBean.remove(FragmentPendingsOrders.position);
                FragmentPendingsOrders.adapter_deliveries.notifyDataSetChanged();


            }
        });

        parcel_img = (ImageView) findViewById(R.id.delivery_parcel_img);


        if (NetworkUtil.isInternetConnencted(context)) {
            new view_order_detail(context).execute();
        } else {
            Alert.showInternetConnError(context);
        }

    }

    public class view_order_detail extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public view_order_detail(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Fetching Addresses..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("order_id",
                        Constants.order_id));

                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "view_order_detail", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Fetching Addresses Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Fetching Addresses Goes Failed Please try again later\"}";
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

                        JSONArray detailArray = new JSONArray(
                                jobj1.getString("responseJson"));

                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        final JSONObject object_to_1 = detailArray.getJSONObject(0);

                        if (object_to_1.getString("image").equals("noimage.jpg") || object_to_1.getString("image").length() == 0) {
                            Picasso.with(context)
                                    .load(R.drawable.photo)
                                    .placeholder(R.drawable.photo)
                                    .error(R.drawable.photo)
                                    .into(parcel_img);
                        } else {
                            Picasso.with(context)
                                    .load(Constants.Image_IP + object_to_1.getString("image"))
                                    .placeholder(R.drawable.photo)
                                    .error(R.drawable.photo)
                                    .into(parcel_img);

                        }


                        delivery_total_amount_txt.setText(Math.round(Float.parseFloat(object_to_1.getString("total_amount"))) + " Rs.");

                        String deliverydate = object_to_1.getString("pickup_date_time");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sdf.parse(deliverydate);
                        sdf = new SimpleDateFormat("HH:mm");
                        delivery_deliver_pickup_txt.setText(sdf.format(date));


                        delivery_delivery_txt.setText(detailArray.length() + "");
                        String strFrom = "";
                        if (object_to_1.getString("payment_method").equals("1")) {
                            strFrom = object_to_1.getString("from_address") + "\nPickup Time : " + sdf.format(date) + "\n" + "Collect Money From Pickup Location!";
                        } else {
                            strFrom = object_to_1.getString("from_address") + "\nPickup Time : " + sdf.format(date) + "\n" + "Payment has been done!";
                        }

                        txt_from_add.setText(strFrom);
                        txt_to_add_1.setText(object_to_1.getString("to_address"));

                        if (detailArray.length() > 1) {
                            to_add_2.setVisibility(View.VISIBLE);
                            JSONObject object_to_2 = detailArray.getJSONObject(1);
                            txt_to_add_2.setText(object_to_2.getString("to_address"));

                        }

                        if (detailArray.length() > 2) {
                            to_add_3.setVisibility(View.VISIBLE);
                            JSONObject object_to_3 = detailArray.getJSONObject(2);
                            txt_to_add_3.setText(object_to_3.getString("to_address"));

                        }
                        delivery_accept_txt.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub

                                new accept_order(context).execute();
                            }
                        });

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

    public class accept_order extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public accept_order(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Updating Order..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("rider_id", Preferences.getSavedPreferences(context, "rider_id")));
                nameValuePairs.add(new BasicNameValuePair("order_id", Constants.order_id));


                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "accept_order", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Updating Order Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Updating Order Goes Failed Please try again later\"}";
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
                        FragmentPendingsOrders.flg = 0;
                        Alert.showAlertWithFinish(context, "Thank You for accepting order!\nOrder is placed under \"Orders\"!");


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