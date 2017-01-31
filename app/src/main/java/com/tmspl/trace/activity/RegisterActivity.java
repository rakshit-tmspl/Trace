package com.tmspl.trace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.tmspl.trace.R;

public class RegisterActivity extends AppCompatActivity {

    LinearLayout iv_user, iv_rider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InitUl();
        InitAction();

    }

    private void InitUl() {
        iv_user = (LinearLayout) findViewById(R.id.layout_user);
        iv_rider = (LinearLayout) findViewById(R.id.layout_rider);
    }

    private void InitAction() {

        iv_user.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent_user = new Intent(RegisterActivity.this,
                        UserRegistrationActivity.class);
                startActivity(intent_user);

            }
        });
        iv_rider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent_user = new Intent(RegisterActivity.this,
                        RiderRegistrationActivity.class);
                startActivity(intent_user);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }
}
