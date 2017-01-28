package com.tmspl.trace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class Rider_Complite extends Activity {
    public static ImageView rider_complete_image;
    public static TextView order_track_id;
    public static RatingBar rate;
    public static Activity context;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_complite);
        context = Rider_Complite.this;
        InitUI();
        InitAction();

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(context, AcceptedDeliveriesActivity.class));

            }
        });
    }

    private void InitUI() {
        btn_submit = (Button) findViewById(R.id.btn_submit);
        rider_complete_image = (ImageView) findViewById(R.id.rider_complete_img);
        order_track_id = (TextView) findViewById(R.id.txt_dockier_order_track_id);
        rate = (RatingBar) findViewById(R.id.ratingBar1);
    }

    private void InitAction() {
        try {
            if (Preferences.getSavedPreferences(this, "usertype").equals("3")) {
                rider_complete_image.setImageBitmap(RiderHomeActivity.getPhoto(Preferences.getSavedPreferences(this, "r_image")));
            } else {
                if (AcceptedDeliveriesActivity.rider_image.equals("noimage.jpg") || AcceptedDeliveriesActivity.rider_image.length() == 0) {
                    Picasso.with(this)
                            .load(R.drawable.icon_rider_image)
                            .placeholder(R.drawable.icon_rider_image)
                            .error(R.drawable.icon_rider_image)
                            .into(rider_complete_image);
                } else {
                    Picasso.with(this)
                            .load(Constants.Image_IP + AcceptedDeliveriesActivity.rider_image)
                            .placeholder(R.drawable.icon_rider_image)
                            .error(R.drawable.icon_rider_image)
                            .into(rider_complete_image);

                }
            }
            order_track_id.setText(AcceptedDeliveriesActivity.order_track_id);
            btn_submit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (NetworkUtil.isInternetConnencted(context)) {
                        new make_rate(context).execute();
                    } else {
                        Alert.showInternetConnError(context);
                    }
                }
            });
        }catch(Exception e)
        {

        }
    }

    public class make_rate extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public make_rate(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context,"Updating Feedback..");
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
                nameValuePairs.add(new BasicNameValuePair("rate", rate.getSecondaryProgress() / 2 + ""));

                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "make_rate", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Updating Feedback Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Updating Feedback Goes Failed Please try again later\"}";
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

                        Alert.showAlertWithFinish(context, "Thank you for feedback!");
                        startActivity(new Intent(context, AcceptedDeliveriesActivity.class));
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
        finish();
    }
}
