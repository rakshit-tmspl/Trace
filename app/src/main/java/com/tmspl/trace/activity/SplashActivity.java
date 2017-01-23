package com.tmspl.trace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.tmspl.trace.R;
import com.tmspl.trace.extra.Preferences;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Preferences.getSavedPreferences(SplashActivity.this, "logout").equals("1")) {
            Preferences.savePreferences(SplashActivity.this, "logout", "");
            finish();
        } else {

            new CountDownTimer(5000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //call to my UI thread every one second


                }

                public void onFinish() {
                    //final call to my UI thread after 90 seconds
                    finish();
                    if (Preferences.getSavedPreferences(SplashActivity.this, "usertype").equals("1") ||
                            Preferences.getSavedPreferences(SplashActivity.this, "usertype").equals("2")) {
                        startActivity(new Intent(SplashActivity.this, UserHomeActivity.class));

                    } else if (Preferences.getSavedPreferences(SplashActivity.this, "usertype").equals("3")) {
                        startActivity(new Intent(SplashActivity.this, RiderHomeActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    }
                }
            }.start();
        }
    }
}
