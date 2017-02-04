package com.tmspl.trace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmspl.trace.R;
import com.tmspl.trace.activity.ridersactivity.RiderHomeActivity;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.NetworkUtil;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import dmax.dialog.SpotsDialog;

public class AcceptedDeliveriesActivity extends AppCompatActivity {

    public static String logo, rider_image;
    public static LinearLayout from_add, to_add_1, to_add_2, to_add_3;
    public static TextView txt_from_add, txt_to_add_1, txt_to_add_2, txt_to_add_3;
    public static ImageView parcel_img, accepted_make_call;
    public static ImageView done2, done3, iv_next;
    public static Activity context;
    public static ImageView done1;

    public static String rider_name, rider_vehicle_name, rider_vehicle_number, secret_code, order_track_id;

    public static String to_id, to_rs, to_count, to_address;


    public static Button btn_track_order;

    //view Details from rider
    TextView tvViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_deliveries);
        context = AcceptedDeliveriesActivity.this;

        if (Constants.order_id == null) {
            finish();
            startActivity(new Intent(this, UserHomeActivity.class));
        }

        from_add = (LinearLayout) findViewById(R.id.from_address);
        to_add_1 = (LinearLayout) findViewById(R.id.to_address_1);
//        to_add_2 = (LinearLayout) findViewById(R.id.to_address_2);
//        to_add_3 = (LinearLayout) findViewById(R.id.to_address_3);

        txt_from_add = (TextView) findViewById(R.id.accepted_from_address);
        txt_to_add_1 = (TextView) findViewById(R.id.accepted_to_address_1);
//        txt_to_add_2 = (TextView) findViewById(R.id.accepted_to_address_2);
//        txt_to_add_3 = (TextView) findViewById(R.id.accepted_to_address_3);

        done1 = (ImageView) findViewById(R.id.accept_img_1);
        done2 = (ImageView) findViewById(R.id.accept_img_2);
        done3 = (ImageView) findViewById(R.id.accept_img_3);


        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        parcel_img = (ImageView) findViewById(R.id.accepted_parcel_img);
        accepted_make_call = (ImageView) findViewById(R.id.accepted_make_call);
        iv_next = (ImageView) findViewById(R.id.iv_next);

        btn_track_order = (Button) findViewById(R.id.btn_track_order);


        if (Preferences.getSavedPreferences(context, "usertype").equals("3")) {
            iv_next.setVisibility(View.INVISIBLE);
            btn_track_order.setText("Track Destination");
        }

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
                        logo = object_to_1.getString("image");


                        if (Preferences.getSavedPreferences(context, "usertype").equals("3")) {
                            accepted_make_call.setVisibility(View.VISIBLE);
                        } else {
                            accepted_make_call.setVisibility(View.GONE);
                        }
                        accepted_make_call.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                Uri number;
                                try {
                                    number = Uri.parse("tel:" +
                                            object_to_1.getString("umobile"));
                                    Intent dial = new Intent(
                                            Intent.ACTION_DIAL, number);
                                    startActivity(dial);
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        });
                        if (object_to_1.getString("hasrider").equals("0") || Constants.isSignCom == 0) {
                            iv_next.setVisibility(View.INVISIBLE);
                        }
                        iv_next.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                try {
                                    if (object_to_1.getString("hasrider").equals("0")) {
                                        iv_next.setVisibility(View.INVISIBLE);
                                    } else {
                                        rider_name = object_to_1.getString("rider_name");
                                        rider_image = object_to_1.getString("rider_image");
                                        rider_vehicle_name = object_to_1.getString("vehicle_name");
                                        rider_vehicle_number = object_to_1.getString("vehicle_number");

                                        if (Constants.isCompleted == 1) {
                                            startActivity(new Intent(context, InvoiceActivity.class));
                                        } else {
                                            startActivity(new Intent(context, Rider_details.class));
                                        }

                                    }
                                } catch (JSONException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        });
                        secret_code = object_to_1.getString("secret_code");
                        order_track_id = object_to_1.getString("order_track_id");

                        String deliverydate = object_to_1.getString("pickup_date_time");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sdf.parse(deliverydate);
                        sdf = new SimpleDateFormat("HH:mm");
                        String strFrom = "";
                        if (object_to_1.getString("payment_method").equals("1")) {
                            strFrom = object_to_1.getString("from_address") + "\nPickup Time : " + sdf.format(date) + "\n" + "Collect Money From Pickup Location!";
                        } else {
                            strFrom = object_to_1.getString("from_address") + "\nPickup Time : " + sdf.format(date) + "\n" + "Payment has been done!";
                        }

                        txt_from_add.setText(strFrom);

                        txt_to_add_1.setText(object_to_1.getString("to_address"));
                        btn_track_order.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                context.startActivity(new Intent(context, Track_Order.class));
                            }
                        });
                        if (Preferences.getSavedPreferences(context, "usertype").equals("3") && object_to_1.getString("status").equals("2") == false) {
                            to_add_1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    // TODO Auto-generated method stub
                                    try {
                                        to_id = object_to_1.getString("to_id");
                                        to_rs = object_to_1.getString("individual_price");
                                        to_count = "1";
                                        to_address = object_to_1.getString("to_address");
                                        startActivity(new Intent(context, CompleteDeliveryActivity.class));
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        if (Preferences.getSavedPreferences(context, "usertype").equals("3") == false && object_to_1.getString("status").equals("2")) {
                            to_add_1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View arg0) {
                                    // TODO Auto-generated method stub

                                    try {
                                        to_id = object_to_1.getString("to_id");
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    // startActivity(new Intent(context, Rider_Complite.class));
                                }
                            });
                        }
                        if (object_to_1.getString("status").equals("2") == false) {
                            done1.setVisibility(View.GONE);
                        } else {
                            deliverydate = object_to_1.getString("delivery_date_time");
                            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            date = sdf.parse(deliverydate);
                            sdf = new SimpleDateFormat("HH:mm");
                            txt_to_add_1.setText(txt_to_add_1.getText() + "\nDelivery Time : " + sdf.format(date));
                        }

                        if (detailArray.length() > 1) {
                            to_add_2.setVisibility(View.VISIBLE);

                            final JSONObject object_to_2 = detailArray.getJSONObject(1);
                            txt_to_add_2.setText(object_to_2.getString("to_address"));
                            if (object_to_2.getString("status").equals("2") == false) {
                                done2.setVisibility(View.GONE);

                            } else {
                                deliverydate = object_to_2.getString("delivery_date_time");
                                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                date = sdf.parse(deliverydate);
                                sdf = new SimpleDateFormat("HH:mm");
                                txt_to_add_2.setText(txt_to_add_2.getText() + "\nDelivery Time : " + sdf.format(date));
                            }

                            if (Preferences.getSavedPreferences(context, "usertype").equals("3") && object_to_2.getString("status").equals("2") == false) {
                                to_add_2.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        // TODO Auto-generated method stub
                                        try {
                                            to_id = object_to_2.getString("to_id");
                                            to_rs = object_to_2.getString("individual_price");
                                            to_count = "2";
                                            to_address = object_to_2.getString("to_address");
                                            startActivity(new Intent(context, CompleteDeliveryActivity.class));
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            if (Preferences.getSavedPreferences(context, "usertype").equals("3") == false && object_to_2.getString("status").equals("2")) {
                                to_add_2.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        // TODO Auto-generated method stub
                                        try {
                                            to_id = object_to_2.getString("to_id");
                                            //  startActivity(new Intent(context, Rider_Complite.class));
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }

                        if (detailArray.length() > 2) {
                            to_add_3.setVisibility(View.VISIBLE);
                            final JSONObject object_to_3 = detailArray.getJSONObject(2);
                            txt_to_add_3.setText(object_to_3.getString("to_address"));
                            if (object_to_3.getString("status").equals("2") == false) {
                                done3.setVisibility(View.GONE);
                            } else {
                                deliverydate = object_to_3.getString("delivery_date_time");
                                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                date = sdf.parse(deliverydate);
                                sdf = new SimpleDateFormat("HH:mm");
                                txt_to_add_3.setText(txt_to_add_3.getText() + "\nDelivery Time : " + sdf.format(date));
                            }
                            if (Preferences.getSavedPreferences(context, "usertype").equals("3") && object_to_3.getString("status").equals("2") == false) {
                                to_add_3.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        // TODO Auto-generated method stub
                                        try {
                                            to_id = object_to_3.getString("to_id");
                                            to_rs = object_to_3.getString("individual_price");
                                            to_count = "3";
                                            to_address = object_to_3.getString("to_address");
                                            startActivity(new Intent(context, CompleteDeliveryActivity.class));
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                            if (Preferences.getSavedPreferences(context, "usertype").equals("3") == false && object_to_3.getString("status").equals("2")) {
                                to_add_3.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View arg0) {
                                        // TODO Auto-generated method stub
                                        try {
                                            to_id = object_to_3.getString("to_id");
                                            // startActivity(new Intent(context, Rider_Complite.class));
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Preferences.getSavedPreferences(AcceptedDeliveriesActivity.this, "usertype").equals("3")) {
            if (Constants.flgDelivery == 1) {
                startActivity(new Intent(AcceptedDeliveriesActivity.this, RiderHomeActivity.class));

            } else {
                Constants.flgDelivery = 0;
            }
        } else {
            // startActivity(new Intent(AcceptedDeliveriesActivity.this,UserHomeActivity.class));
        }
    }
}