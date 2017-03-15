package com.tmspl.trace.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmspl.trace.R;
import com.tmspl.trace.activity.ridersactivity.RiderHomeActivity;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.Preferences;


public class Rider_details extends Activity {
    TextView txt_rider_name, txt_bike_name, txt_bike_number, txt_dockier_code;
    Button btn_share_code, btn_track_order;
    ImageView rider_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_details);
        InitUI();
        InitAction();
    }

    private void InitUI() {

        txt_rider_name = (TextView) findViewById(R.id.txt_ridername);
        txt_bike_name = (TextView) findViewById(R.id.txt_bike_name);
        txt_bike_number = (TextView) findViewById(R.id.txt_bike_number);
        txt_dockier_code = (TextView) findViewById(R.id.txt_dockier_code);
        rider_image = (ImageView) findViewById(R.id.rider_image);

        txt_rider_name.setText(AcceptedDeliveriesActivity.rider_name);
        txt_bike_name.setText(AcceptedDeliveriesActivity.rider_vehicle_name);
        txt_bike_number.setText(AcceptedDeliveriesActivity.rider_vehicle_number);
        txt_dockier_code.setText(AcceptedDeliveriesActivity.secret_code);
        if (Preferences.getSavedPreferences(this, "usertype").equals("3")) {
            rider_image.setImageBitmap(RiderHomeActivity.getPhoto(Preferences.getSavedPreferences(this, "r_image")));
        } else {
            if (AcceptedDeliveriesActivity.rider_image.equals("noimage.jpg") || AcceptedDeliveriesActivity.rider_image.length() == 0) {
                Picasso.with(this)
                        .load(R.drawable.icon_rider_image)
                        .placeholder(R.drawable.icon_rider_image)
                        .error(R.drawable.icon_rider_image)
                        .into(rider_image);
            } else {
                Picasso.with(this)
                        .load(Constants.IMAGE_URL + AcceptedDeliveriesActivity.rider_image)
                        .placeholder(R.drawable.icon_rider_image)
                        .error(R.drawable.icon_rider_image)
                        .into(rider_image);

            }
        }


        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_share_code = (Button) findViewById(R.id.btn_share_code);
        btn_track_order = (Button) findViewById(R.id.btn_track_order);
    }

    private void InitAction() {
        btn_share_code.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is secret code for your order id " + AcceptedDeliveriesActivity.order_track_id + " : " + AcceptedDeliveriesActivity.secret_code);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        btn_track_order.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(Rider_details.this, TrackOrderActivity.class));

            }
        });
    }
}
