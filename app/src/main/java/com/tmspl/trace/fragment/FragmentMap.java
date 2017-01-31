package com.tmspl.trace.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tmspl.trace.R;
import com.tmspl.trace.activity.MapWrapperLayout;
import com.tmspl.trace.apimodel.maps_bean;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.GeocodeAddressIntentService;
import com.tmspl.trace.extra.MyApplication;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;
import com.tmspl.trace.fragment.addaddress.FragmentSetDestination;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.toRadians;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;
import static java.lang.StrictMath.toDegrees;


/**
 * Created by rakshit.sathwara on 1/23/2017.
 */

public class FragmentMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener,
        GoogleMap.OnCameraIdleListener {
    private static final String TAG = MyApplication.APP_TAG + FragmentMap.class.getSimpleName();
    final Handler mHandler = new Handler();
    private final GetAddressFromIntentService addressFromIntentService =
            new GetAddressFromIntentService();
    public GoogleMap mMap;
    public boolean isReady = false;
    private SyncedMapFragment mapFragment;
    private LatLngInterpolator mLatLngInterpolator;
    private MapWrapperLayout mapWrapperLayout;
    private Context context;
    private LatLng currentLatLng;
    private AppCompatEditText etCurrentLocation;
    private AddressResultReceiver mResultReceiver;
    private LatLng onCameraChangedLatLng;
    private String rider_name, VehicleName, VehicleNumber, lat_long;
    static List<maps_bean> storelist;

    private HashMap<LatLng, Marker> courseMarkers = new HashMap<LatLng, Marker>();


    String[] web = {"Document", "Parcle", "Boxes", "Fragile", "Other"};
    int[] imageId = {R.drawable.ic_boxes, R.drawable.ic_document, R.drawable.ic_parcel,
            R.drawable.ic_fragile

    };
    LinearLayout ll_boxes, ll_document, ll_fragile, ll_parcel;
    ImageView iv_boxes, iv_document, iv_fragile, iv_parcle;

    public boolean isBoxes = false, isDocument = false, isFragile = false, isParcel = false;

    public static FragmentMap newInstance(int index, Context context) {
        FragmentMap fragmentMap = new FragmentMap();
        fragmentMap.context = context;
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragmentMap.setArguments(b);
        return fragmentMap;
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);

        mapWrapperLayout = (MapWrapperLayout) view.findViewById(R.id.map_wrapper_layout);

        mLatLngInterpolator = new LatLngInterpolator.Linear();

        storelist = new ArrayList<maps_bean>();

//        etCurrentLocation = (AppCompatEditText) view.findViewById(R.id.et_currentLocation);

        mapFragment = (SyncedMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.frag_map);

        iv_boxes = (ImageView) view.findViewById(R.id.iv_box);
        iv_document = (ImageView) view.findViewById(R.id.iv_document);
        iv_fragile = (ImageView) view.findViewById(R.id.iv_fragile);
        iv_parcle = (ImageView) view.findViewById(R.id.iv_parcle);


        ll_boxes = (LinearLayout) view.findViewById(R.id.ll_boxes);
        ll_document = (LinearLayout) view.findViewById(R.id.ll_document);
        ll_fragile = (LinearLayout) view.findViewById(R.id.ll_fragile);
        ll_parcel = (LinearLayout) view.findViewById(R.id.ll_parcel);


        InitAction();

        loadMapNow();

        return view;
    }

    public void placeCurrentLocationMarker(final LatLng latLng) {
        if (isReady) {

            currentLatLng = latLng;

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            Intent intent = new Intent(getActivity(), GeocodeAddressIntentService.class);
            intent.putExtra(Constants.RECEIVER, mResultReceiver);
            intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);

            intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                    latLng.latitude);
            intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                    latLng.longitude);

            getActivity().startService(intent);

        }
    }

    private void loadMapNow() {
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        isReady = true;

        mMap = googleMap;

        mapWrapperLayout.init(mMap, getPixelsFromDp(context, 39 + 20));

        if (ActivityCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(getActivity(),
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

        new getTrackData(getActivity()).execute();

/*        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener()
        {
            @Override
            public void onMyLocationChange(Location location)
            {
                mMap.animateCamera(CameraUpdateFactory
                        .newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
            }
        });*/


    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
//        etCurrentLocation.setText("Fetching location...");
        onCameraChangedLatLng = mMap.getCameraPosition().target;
        currentLatLng = mMap.getCameraPosition().target;

    }

    @Override
    public void onCameraIdle() {
        mHandler.removeCallbacks(addressFromIntentService);
        mHandler.postDelayed(addressFromIntentService, 800);

    }
//
//    public String getCurrentPlace() {
//        return etCurrentLocation.getText().toString();
//    }

    public String getCurrentLat() {
        return currentLatLng.latitude + "";
    }

    public String getCurrentLng() {
        return currentLatLng.longitude + "";
    }

    public interface LatLngInterpolator {
        public LatLng interpolate(float fraction, LatLng a, LatLng b);

        public class Linear implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lng = (b.longitude - a.longitude) * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }

        public class LinearFixed implements LatLngInterpolator {
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

        public class Spherical implements LatLngInterpolator {

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

    class GetAddressFromIntentService implements Runnable {

        @Override
        public void run() {
            Intent intent = new Intent(getActivity(), GeocodeAddressIntentService.class);
            intent.putExtra(Constants.RECEIVER, mResultReceiver);
            intent.putExtra(Constants.FETCH_TYPE_EXTRA, Constants.USE_ADDRESS_LOCATION);

            intent.putExtra(Constants.LOCATION_LATITUDE_DATA_EXTRA,
                    onCameraChangedLatLng.latitude);

            intent.putExtra(Constants.LOCATION_LONGITUDE_DATA_EXTRA,
                    onCameraChangedLatLng.longitude);

            getActivity().startService(intent);
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
                    getActivity().runOnUiThread(new Runnable() {
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        }
    }

    public class getTrackData extends AsyncTask<String, String, String> {

        Activity context;


        public getTrackData(Activity context) {
            this.context = context;
            Log.e(TAG, "getTrackData: " + "IN GETTRACKDATA");
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

                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                        + "getTrackData", ServiceHandler.POST, nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Signup Failed Please try again later\"}";
                } else {
                    Log.e(TAG, "getTrackData: " + "IN GETTRACKDATA");
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
                Log.e(TAG, "onPostExecute: " + result);

                if (jobj1.has("error")) {
                    //     Alert.ShowAlert(context, jobj1.getString("error"));
                } else {

                    if (jobj1.getInt("status") == 1) {

                        JSONArray jsonArray = jobj1.optJSONArray("responseJson");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            rider_name = jsonObject.optString("company_name");
                            lat_long = jsonObject.getString("lat_long").replaceAll(" ", "");
                            if (lat_long.length() != 0) {
                                StringTokenizer tokens = new StringTokenizer(lat_long, ",");
                                String first_lat = tokens.nextToken();
                                String second_long = tokens.nextToken();

                                double lat = Double.parseDouble(first_lat);
                                double longi = Double.parseDouble(second_long);
                                VehicleName = jsonObject.getString("category");
                                VehicleNumber = jsonObject.getString("mobile");

                                maps_bean bean = new maps_bean();

                                bean.setName(rider_name);
                                bean.setVehicleName(VehicleName);
                                bean.setVehicleNumber(VehicleNumber);
                                bean.setLatlong(new LatLng(lat, longi));
                                storelist.add(bean);

                            }
                            addItemsToMap(storelist);
                        }
                    } else {
                        Alert.showAlertWithFinish(context, jobj1.getString("responseMessage"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void addItemsToMap(List<maps_bean> items) {
        // TODO Auto-generated method stub
        if (this.mMap != null) {

            Log.e(TAG, "addItemsToMap: " + "IN addItemsToMap");

            LatLngBounds bounds = this.mMap.getProjection().getVisibleRegion().latLngBounds;

            for (maps_bean item : items) {

                if (bounds.contains(new LatLng(item.getLatlong().latitude, item
                        .getLatlong().longitude))) {

                    if (!courseMarkers.containsKey(item.getLatlong())) {

                        Marker m = mMap.addMarker(new MarkerOptions()
                                .position(item.getLatlong())
                                .title(item.getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_depot))
                                .snippet(item.getVehicleNumber() + "-" + item.getVehicleName()));
                        this.courseMarkers.put(item.getLatlong(), m);

                        m.showInfoWindow();

                    }
                } else {

                    if (courseMarkers.containsKey(item.getLatlong())) {

                        courseMarkers.get(item.getLatlong()).remove();

                        courseMarkers.remove(item.getLatlong());
                    }
                }
            }
        }
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                LinearLayout info = new LinearLayout(getActivity());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getActivity());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                final TextView snippet = new TextView(getActivity());
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


    public void InitAction() {
        iv_boxes.setBackgroundResource(R.drawable.ic_boxes);
        iv_document.setBackgroundResource(R.drawable.ic_document);
        iv_fragile.setBackgroundResource(R.drawable.ic_fragile);
        iv_parcle.setBackgroundResource(R.drawable.ic_parcel);

        Constants.material_type_id = "";

        ll_boxes.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isBoxes == false) {
                    ll_boxes.setBackgroundColor(getActivity().getResources().getColor(android.R.color.darker_gray));
                    isBoxes = true;

                    Constants.material_type_id += "1,";

                    if (Constants.material_type_id.length() != 0) {
                        isParcel = false;
                        isDocument = false;
                        isFragile = false;
                        isBoxes = false;
                        Preferences.savePreferences(getActivity(), "SFLG", "1");
                        Fragment fragmentS1 = new FragmentSetDestination();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentS1).addToBackStack(null).commit();
                    } else {
                        Alert.ShowAlert(getActivity(), "Please Select Material Type!");
                    }

                    ll_document.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isDocument = false;
                    ll_fragile.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isFragile = false;
                    ll_parcel.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isParcel = false;

                } else {
                    ll_boxes.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isBoxes = false;
                }

            }
        });
        ll_document.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isDocument == false) {
                    ll_document.setBackgroundColor(getActivity().getResources().getColor(android.R.color.darker_gray));
                    isDocument = true;

                    Constants.material_type_id += "2,";

                    if (Constants.material_type_id.length() != 0) {
                        isParcel = false;
                        isDocument = false;
                        isFragile = false;
                        isBoxes = false;
                        Preferences.savePreferences(getActivity(), "SFLG", "1");
                        Fragment fragmentS1 = new FragmentSetDestination();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentS1).addToBackStack(null).commit();
                    } else {
                        Alert.ShowAlert(getActivity(), "Please Select Material Type!");
                    }

                    ll_boxes.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isBoxes = false;
                    ll_fragile.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isFragile = false;
                    ll_parcel.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isParcel = false;


                } else {
                    ll_document.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isDocument = false;
                }
            }
        });
        ll_fragile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isFragile == false) {
                    ll_fragile.setBackgroundColor(getActivity().getResources().getColor(android.R.color.darker_gray));
                    isFragile = true;


                    ll_boxes.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isBoxes = false;
                    ll_document.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isDocument = false;
                    ll_parcel.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isParcel = false;


                } else {
                    ll_fragile.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isFragile = false;
                }
            }
        });
        ll_parcel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isParcel == false) {
                    ll_parcel.setBackgroundColor(getActivity().getResources().getColor(android.R.color.darker_gray));
                    isParcel = true;

                    Constants.material_type_id += "20,";

                    if (Constants.material_type_id.length() != 0) {
                        isParcel = false;
                        isDocument = false;
                        isFragile = false;
                        isBoxes = false;
                        Preferences.savePreferences(getActivity(), "SFLG", "1");
                        Fragment fragmentS1 = new FragmentSetDestination();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentS1).addToBackStack(null).commit();
                    } else {
                        Alert.ShowAlert(getActivity(), "Please Select Material Type!");
                    }

                    ll_boxes.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isBoxes = false;
                    ll_fragile.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isFragile = false;
                    ll_document.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isDocument = false;

                } else {
                    ll_parcel.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                    isParcel = false;
                }
            }
        });
    }

}
