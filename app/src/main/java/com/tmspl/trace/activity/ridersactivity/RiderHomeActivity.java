package com.tmspl.trace.activity.ridersactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.tmspl.trace.R;
import com.tmspl.trace.activity.LoginActivity;
import com.tmspl.trace.activity.homeactivity.HomeActivity;
import com.tmspl.trace.api.API;
import com.tmspl.trace.api.RetrofitCallbacks;
import com.tmspl.trace.apimodel.LoginNewResponse;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.LocationService;
import com.tmspl.trace.extra.MemoryCache;
import com.tmspl.trace.extra.MyApplication;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.fragment.rider.FragmentOrders;
import com.tmspl.trace.fragment.rider.FragmentPendingsOrders;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RiderHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentPendingsOrders fragmentPendingsOrders;
    FragmentTransaction ft;

    private static final String TAG = MyApplication.APP_TAG + RiderHomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            boolean IsGPS = hasGPSDevice(this);

            if (IsGPS) {
                showSettingsAlert();
            } else {
                startService();
            }
        }

        setContentView(R.layout.activity_rider_home3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, FragmentPendingsOrders.newInstance(0, this));
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.rider_home, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//
//        Fragment fragment = null;
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_pending_orders) {
            fragment = FragmentPendingsOrders.newInstance(0, this);
        } else if (id == R.id.nav_orders) {
            fragment = FragmentOrders.newInstance(1, this);
        } else if (id == R.id.nav_earnings) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_become_user) {
            //Become user clear SharedPreferences and memory and set user data.
            try {

                String contact = Preferences.getSavedPreferences(RiderHomeActivity.this, "mobile");
                String password = Preferences.getSavedPreferences(RiderHomeActivity.this, "password");

                Log.e(TAG, "onNavigationItemSelected: contact" + contact);
                Log.e(TAG, "onNavigationItemSelected: password" + password);

                API.getInstance().loginUser(RiderHomeActivity.this, Constants.AUTH, contact, password,
                        new RetrofitCallbacks<LoginNewResponse>(RiderHomeActivity.this) {
                            @Override
                            public void onResponse(Call<LoginNewResponse> call, Response<LoginNewResponse> response) {
                                super.onResponse(call, response);

                                if (response.isSuccessful()) {

                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RiderHomeActivity.this);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    MemoryCache memoryCache = new MemoryCache();
                                    memoryCache.clear();


                                    LoginNewResponse.ResponseJsonBean responseJsonBean = response.body().getResponseJson();
                                    int user = responseJsonBean.getUserUsertype();

                                    List<LoginNewResponse.ResponseJsonBean.UserQueryResultBean> userList = responseJsonBean
                                            .getUserQueryResult();

                                    Preferences.savePreferences(RiderHomeActivity.this, "usertype", String.valueOf(user));

                                    for (LoginNewResponse.ResponseJsonBean.UserQueryResultBean userBean : userList) {
                                        Log.e(TAG, "onResponse: USER-ID" + userBean.getUserId());

                                        Preferences.savePreferences(RiderHomeActivity.this, "first_name", userBean.getFirstName());
                                        Preferences.savePreferences(RiderHomeActivity.this, "last_name", userBean.getLastName());
                                        Preferences.savePreferences(RiderHomeActivity.this, "email", userBean.getEmail());
                                        Preferences.savePreferences(RiderHomeActivity.this, "mobile", userBean.getMobile());
                                        Preferences.savePreferences(RiderHomeActivity.this, "user_id", userBean.getUserId());
                                        Preferences.savePreferences(RiderHomeActivity.this, "password", userBean.getPassword());
                                    }

                                    finish();
                                    startActivity(new Intent(RiderHomeActivity.this, HomeActivity.class));
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginNewResponse> call, Throwable t) {
                                super.onFailure(call, t);
                                Log.i(TAG, t.toString());
                                Log.i(TAG, call.toString());
                                Toast.makeText(RiderHomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_logout) {
            try {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RiderHomeActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                MemoryCache memoryCache = new MemoryCache();
                memoryCache.clear();

                if (Integer.valueOf(Build.VERSION.SDK_INT) > 16) {
                    Intent intent = new Intent(RiderHomeActivity.this, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(RiderHomeActivity.this, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            fragmentPendingsOrders = FragmentPendingsOrders.newInstance(0, this);
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        dialog.cancel();
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

    public void startService() {
        startService(new Intent(this, LocationService.class));
    }

    public static Bitmap getPhoto(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
