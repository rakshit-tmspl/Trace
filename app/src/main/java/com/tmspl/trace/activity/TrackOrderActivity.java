package com.tmspl.trace.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tmspl.trace.R;
import com.tmspl.trace.apimodel.maps_bean;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.GeocodeAddressIntentService;
import com.tmspl.trace.extra.MyApplication;
import com.tmspl.trace.extra.ServiceHandler;
import com.tmspl.trace.fragment.FragmentMap;
import com.tmspl.trace.fragment.SyncedMapFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

import static com.tmspl.trace.activity.Track_Order.startPosition;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.toRadians;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;
import static java.lang.StrictMath.toDegrees;

/**
 * Created by rakshit.sathwara on 2/15/2017.
 */

public class TrackOrderActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener,
        GoogleMap.OnCameraIdleListener {

    private static final String TAG = MyApplication.APP_TAG + FragmentMap.class.getSimpleName();
    final Handler mHandler = new Handler();
    private final GetAddressFromIntentService addressFromIntentService =
            new GetAddressFromIntentService();
    public GoogleMap mMap;
    public boolean isReady = false;
    private SyncedMapFragment mapFragment;
    private FragmentMap.LatLngInterpolator mLatLngInterpolator;
    private MapWrapperLayout mapWrapperLayout;
    private Context context;
    private LatLng currentLatLng;
    private AppCompatEditText etCurrentLocation;
    private AddressResultReceiver mResultReceiver;
    private LatLng onCameraChangedLatLng;
    private String rider_name, VehicleName, VehicleNumber, lat_long;
    static List<maps_bean> storelist;
    MarkerOptions mp1;
    Boolean mBooleancamerapostion = false;

    private HashMap<LatLng, Marker> courseMarkers = new HashMap<LatLng, Marker>();

    /*public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_order);

        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_wrapper_layout_track);

        ImageView iv_back = (ImageView) findViewById(R.id.iv_back_track);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mLatLngInterpolator = new FragmentMap.LatLngInterpolator.Linear();

        storelist = new ArrayList<maps_bean>();

        mapFragment = (SyncedMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_map_track);

        loadMapNow();
    }

    private void loadMapNow() {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        onCameraChangedLatLng = mMap.getCameraPosition().target;
        currentLatLng = mMap.getCameraPosition().target;
    }

    @Override
    public void onCameraIdle() {
        mHandler.removeCallbacks(addressFromIntentService);
        mHandler.postDelayed(addressFromIntentService, 800);
    }

    public String getCurrentLat() {
        return currentLatLng.latitude + "";
    }

    public String getCurrentLng() {
        return currentLatLng.longitude + "";
    }


    public interface LatLngInterpolator {
        public LatLng interpolate(float fraction, LatLng a, LatLng b);

        public class Linear implements FragmentMap.LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lng = (b.longitude - a.longitude) * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }

        public class LinearFixed implements FragmentMap.LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;

                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }

        public class Spherical implements FragmentMap.LatLngInterpolator {

            /* From github.com/googlemaps/android-maps-utils */
            @Override
            public LatLng interpolate(float fraction, LatLng from, LatLng to) {
                // http://en.wikipedia.org/wiki/Slerp
                double fromLat = toRadians(from.latitude);
                double fromLng = toRadians(from.longitude);
                double toLat = toRadians(to.latitude);
                double toLng = toRadians(to.longitude);
                double cosFromLat = cos(fromLat);
                double cosToLat = cos(toLat);

                // Computes Spherical interpolation coefficients.
                double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
                double sinAngle = sin(angle);
                if (sinAngle < 1E-6) {
                    return from;
                }
                double a = sin((1 - fraction) * angle) / sinAngle;
                double b = sin(fraction * angle) / sinAngle;

                // Converts from polar to vector and interpolate.
                double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
                double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
                double z = a * sin(fromLat) + b * sin(toLat);

                // Converts interpolated vector back to polar.
                double lat = atan2(z, sqrt(x * x + y * y));
                double lng = atan2(y, x);
                return new LatLng(toDegrees(lat), toDegrees(lng));
            }

            private double computeAngleBetween(double fromLat, double fromLng, double toLat, double toLng) {
                // Haversine's formula
                double dLat = fromLat - toLat;
                double dLng = fromLng - toLng;
                return 2 * asin(sqrt(pow(sin(dLat / 2), 2) +
                        cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2)));
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isReady = true;

        mMap = googleMap;

        //mapWrapperLayout.init(mMap, getPixelsFromDp(context, 39 + 20));

        if (ActivityCompat
                .checkSelfPermission(TrackOrderActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(TrackOrderActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        LatLng gujarat = new LatLng(23.012068, 72.5789153);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gujarat, 11));

        mResultReceiver = new AddressResultReceiver(null);

        mMap.setOnCameraChangeListener(this);

        mMap.setOnCameraIdleListener(this);

        new track_order(TrackOrderActivity.this).execute();

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                mMap.animateCamera(CameraUpdateFactory
                        .newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
            }
        });
    }

    class GetAddressFromIntentService implements Runnable {

        @Override
        public void run() {
            Intent intent = new Intent(TrackOrderActivity.this, GeocodeAddressIntentService.class);
            intent.putExtra(Constants.RECEIVER, mResultReceiver);
            intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);

            intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                    onCameraChangedLatLng.latitude);

            intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                    onCameraChangedLatLng.longitude);

            startService(intent);
        }
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                try {
                    //final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String address = resultData.getString(Constants.RESULT_DATA_KEY);
                            address = address.substring(0, address.length() - 2);
//                            etCurrentLocation.setText(address);
                        }
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }
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
                        mMap.addMarker(mp1);
                        mMap.addCircle(circleOptions);

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startPosition.latitude, startPosition.longitude), 12));
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
