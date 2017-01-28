package com.tmspl.trace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tmspl.trace.R;
import com.tmspl.trace.apimodel.maps_bean;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.GPSTracker;
import com.tmspl.trace.extra.NetworkUtil;
import com.tmspl.trace.extra.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

public class Track_Order extends FragmentActivity implements LocationListener, OnMapReadyCallback,
        OnClickListener, OnInfoWindowClickListener {

    static LatLng startPosition;
    static List<maps_bean> storelist;
    Button btn_next;
    private GoogleMap map;
    MarkerOptions mp1;
    Boolean mBooleancamerapostion = false;
    GPSTracker gps;
    private String rider_name, VehicleName;
    private String lat_long;
    private HashMap<LatLng, Marker> courseMarkers = new HashMap<LatLng, Marker>();

    SupportMapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_order);
        gps = new GPSTracker(Track_Order.this);
        storelist = new ArrayList<maps_bean>();

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

      /*  map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);
        }

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    boolean IsGPS = hasGPSDevice(Track_Order.this);

                    if (IsGPS) {
                        showSettingsAlert();
                    }
                }
                return false;
            }
        });
        if (gps.canGetLocation()) {
            if (NetworkUtil.isInternetConnencted(Track_Order.this)) {
                new track_order(Track_Order.this).execute();
            }
        }

        map.setOnCameraChangeListener(new OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraposition) {
                // TODO Auto-generated method stub

                if (mBooleancamerapostion) {
                    addItemsToMap(storelist);
                }
            }
        });*/
        try {
            switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)) {
                case ConnectionResult.SUCCESS:
                    mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.track_order_map);
                    mapFragment.getMapAsync(this);
                    // Gets to GoogleMap from the MapView and does initialization stuff
                    if (map != null) {
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        map.setMyLocationEnabled(true);

                        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                            @Override
                            public boolean onMyLocationButtonClick() {
                                LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                                    Location location = mgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                    if (location != null) {
                                        onLocationChanged(location);
                                    }

                                    boolean IsGPS = hasGPSDevice(Track_Order.this);

                                    if (IsGPS) {
                                        showSettingsAlert();
                                    }
                                }
                                return false;
                            }
                        });
                        if (gps.canGetLocation()) {
                            startPosition = new LatLng(gps.getLatitude(), gps.getLongitude());
                            if (NetworkUtil.isInternetConnencted(this)) {
                                new track_order(Track_Order.this).execute();
                            }
                        }

                        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                            @Override
                            public void onCameraChange(CameraPosition cameraposition) {
                                // TODO Auto-generated method stub

                                if (mBooleancamerapostion) {
                                    addItemsToMap(storelist);
                                }
                            }
                        });

                    }

                    break;
                case ConnectionResult.SERVICE_MISSING:
                    Toast.makeText(Track_Order.this, "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                    break;
                case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                    Toast.makeText(Track_Order.this, "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(Track_Order.this, GooglePlayServicesUtil.isGooglePlayServicesAvailable(Track_Order.this), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                LinearLayout info = new LinearLayout(Track_Order.this);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(Track_Order.this);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                final TextView snippet = new TextView(Track_Order.this);
                snippet.setTextColor(Color.BLUE);


                snippet.setText(marker.getSnippet());


                String str = snippet.getText().toString().split("-")[0].trim();
                if (str.length() > 0) {
                    Uri number;
                    try {
                        number = Uri.parse("tel:" + str);
                        Intent dial = new Intent(
                                Intent.ACTION_DIAL, number);
                        startActivity(dial);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Track_Order.this);

        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    protected void addItemsToMap(List<maps_bean> items) {
        // TODO Auto-generated method stub
        if (this.map != null) {

            LatLngBounds bounds = this.map.getProjection().getVisibleRegion().latLngBounds;

            for (maps_bean item : items) {

                if (bounds.contains(new LatLng(item.getLatlong().latitude, item
                        .getLatlong().longitude))) {

                    if (!courseMarkers.containsKey(item.getLatlong())) {

                        this.courseMarkers.put(
                                item.getLatlong(),
                                this.map.addMarker(new MarkerOptions()
                                        .position(item.getLatlong())
                                        .title(item.getName())
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_user))
                                        .snippet(item.getVehicleNumber() + "-" + item.getVehicleName())));

                        //
                    }
                } else {

                    if (courseMarkers.containsKey(item.getLatlong())) {

                        courseMarkers.get(item.getLatlong()).remove();

                        courseMarkers.remove(item.getLatlong());
                    }
                }
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public class track_order extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public track_order(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "please wait..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("order_track_id",
                        AcceptedDeliveriesActivity.order_track_id));
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                        + "track_order", ServiceHandler.POST, nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Signup Failed Please try again later\"}";
                } else {


                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Signup Failed Please try again later\"}";
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

                        JSONObject resObject = new JSONObject(jobj1.getString("responseJson"));
                        JSONArray jsonArray = new JSONArray(resObject.getString("data"));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            rider_name = jsonObject.optString("to_name");
                            lat_long = jsonObject.getString("delivery_lat_long");
                            StringTokenizer tokens = new StringTokenizer(lat_long, ",");
                            String first_lat = tokens.nextToken();
                            String second_long = tokens.nextToken();

                            double lat = Double.parseDouble(first_lat);
                            double longi = Double.parseDouble(second_long);
                            VehicleName = jsonObject.getString("to_address");


                            maps_bean bean = new maps_bean();

                            bean.setName(rider_name);
                            bean.setVehicleNumber("");
                            bean.setVehicleName(VehicleName);
                            bean.setLatlong(new LatLng(lat, longi));
                            storelist.add(bean);

                        }

                        double radiusInMeters = 100.0;
                        int strokeColor = 0xffff0000; // red outline
                        int shadeColor = 0x44ff0000; // opaque red fill
                        JSONObject rider = jsonArray.getJSONObject(0);

                        StringTokenizer tokens = new StringTokenizer(rider.getString("lat_long"), ",");
                        String first_lat1 = tokens.nextToken();
                        String second_long1 = tokens.nextToken();

                        double lat = Double.parseDouble(first_lat1);
                        double longi = Double.parseDouble(second_long1);


                        startPosition = new LatLng(lat, longi);


                        CircleOptions circleOptions = new CircleOptions()
                                .center(startPosition).radius(radiusInMeters)
                                .fillColor(shadeColor).strokeColor(strokeColor)
                                .strokeWidth(8);
                        mp1 = new MarkerOptions();
                        mp1.position(new LatLng(startPosition.latitude, startPosition.longitude));
                        //  mp1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        mp1.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_dockier));
                        mp1.draggable(true);
                        mp1.alpha(0.7f);
                        map.addMarker(mp1);
                        map.addCircle(circleOptions);

                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startPosition.latitude, startPosition.longitude), 12));
                        mBooleancamerapostion = true;
                        if (pd.isShowing())
                            pd.dismiss();
                    } else {
                        if (pd.isShowing())
                            pd.dismiss();
                        Alert.showAlertWithFinish(context,
                                jobj1.getString("responseMessage"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
