package com.tmspl.trace.extra;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationService extends Service {

    public static String rider_lat_long;
    private static final long MIN_TIME_BETWEEN_WIFI_REPORTS = 45 * 1000; // 45 seconds
    public static final String BROADCAST_ACTION = "Hello World";
    static Location mCurrentLocation;
    static LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    Intent intent;
    int counter = 0;
    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // this is where we deal with the data sent from the battery.
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Log.e("Bettry Level", "->" + level);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        Log.e("Location", "on bind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Location", "on Create");
        intent = new Intent(BROADCAST_ACTION);

    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e("Location", "on Start");
        listener = new MyLocationListener();
        getNewLocation();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    private void getNewLocation() {
        this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 600000, 10000, listener);
            //locationManager.requestLocationUpdates(provider,1000, 0, listener);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {

                bestLocation = l;
            }
        }
        if (bestLocation != null) {
            double lat = bestLocation.getLatitude();
            double lng = bestLocation.getLongitude();
            Log.e("Location", "From Service" + lat + "---" + lng);
            if (NetworkUtil.isInternetConnencted(this) && Preferences.getSavedPreferences(this, "rider_id").length()>0) {
                new update_rider_track(Preferences.getSavedPreferences(this, "rider_id"), lat + "," + lng).execute();
            }
        } else {
            Log.e("Location", "Location is not available");
        }
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(final Location loc) {
            getNewLocation();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e("Location", "Location is disable");
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    public class update_rider_track extends AsyncTask<String, String, String> {

        String rider_id, lat_long;

        public update_rider_track(String rider_id, String lat_lon) {
            this.rider_id = rider_id;
            this.lat_long = lat_lon;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("rider_id", this.rider_id));
                nameValuePairs.add(new BasicNameValuePair("lat_lon", this.lat_long));

                rider_lat_long=this.lat_long+"";
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL + "update_rider_track", ServiceHandler.POST, nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Update Location Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Login Failed Please try again later\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Log.e("Location Service", jobj1.getString("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {

                        Log.e("Location Service", jobj1.getString("responseMessage"));


                    } else {
                        Log.e("Location Service", jobj1.getString("responseMessage"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
