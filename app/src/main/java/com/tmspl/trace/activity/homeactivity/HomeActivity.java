package com.tmspl.trace.activity.homeactivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tmspl.trace.R;
import com.tmspl.trace.activity.LoginActivity;
import com.tmspl.trace.activity.SplashActivity;
import com.tmspl.trace.apimodel.DbModel;
import com.tmspl.trace.extra.MemoryCache;
import com.tmspl.trace.extra.MyApplication;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.fragment.FragmentMap;
import com.tmspl.trace.fragment.addaddress.UpdateUserProfile;
import com.tmspl.trace.fragment.manageorder.FragmentManageOrder;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PermissionListener {

    FragmentMap map;
    FragmentTransaction ft;

    String fcmKey;
    private Realm realm;
    private static final String TAG = MyApplication.APP_TAG + HomeActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();

        RealmResults<DbModel> tagsBeanModelRealmResults = realm.where(DbModel.class).findAll();

        for (DbModel model1 : tagsBeanModelRealmResults) {
            Log.e(TAG, "IN Realm RIDER-HOME" + model1.getFcmToken());
            fcmKey = model1.getFcmToken();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        new TedPermission(this)
                .setPermissionListener(this)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WAKE_LOCK)
                .check();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_placeOrder) {
            Preferences.savePreferences(this, "SFLG", "1");
            fragment = FragmentMap.newInstance(0, this);
        } else if (id == R.id.drawer_myOrder) {
            fragment = FragmentManageOrder.newInstance(1, this);
        } else if (id == R.id.drawer_profile) {
            fragment = UpdateUserProfile.newInstance(2, HomeActivity.this);
        } else if (id == R.id.drawer_type) {
            //Become Rider clear SharedPreferences and memory and set user data.
            try {
                String userType = Preferences.getSavedPreferences(HomeActivity.this, "usertype");

                Log.e(TAG, "onNavigationItemSelected: HOMEACTIVITY" + userType);

                if (userType.equals("1")) {
                    //Preferences.savePreferences(HomeActivity.this, "usertype", "3");

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    //Preferences.savePreferences(HomeActivity.this, "usertype", "3");
                    Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
/*
                String contact = Preferences.getSavedPreferences(HomeActivity.this, "mobile");
                String password = Preferences.getSavedPreferences(HomeActivity.this, "password");

                Log.e(TAG, "onNavigationItemSelected: contact" + contact);
                Log.e(TAG, "onNavigationItemSelected: password" + password);

                API.getInstance().loginUser(HomeActivity.this, Constants.AUTH, contact, password, fcmKey,
                        new RetrofitCallbacks<LoginNewResponse>(HomeActivity.this) {
                            @Override
                            public void onResponse(Call<LoginNewResponse> call, Response<LoginNewResponse> response) {
                                super.onResponse(call, response);

                                if (response.isSuccessful()) {

                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    MemoryCache memoryCache = new MemoryCache();
                                    memoryCache.clear();


                                    LoginNewResponse.ResponseJsonBean responseJsonBean = response.body().getResponseJson();
                                    int user = responseJsonBean.getRiderUsertype();

                                    List<LoginNewResponse.ResponseJsonBean.RiderQueryResultBean> userList = responseJsonBean
                                            .getRiderQueryResult();

                                    Preferences.savePreferences(HomeActivity.this, "usertype", String.valueOf(user));

                                    for (LoginNewResponse.ResponseJsonBean.RiderQueryResultBean riderBean : userList) {
                                        Log.e(TAG, "onResponse: USER-ID" + riderBean.getRiderId());

                                        Preferences.savePreferences(HomeActivity.this, "first_name", riderBean.getFirstName());
                                        Preferences.savePreferences(HomeActivity.this, "last_name", riderBean.getLastName());
                                        Preferences.savePreferences(HomeActivity.this, "email", riderBean.getEmail());
                                        Preferences.savePreferences(HomeActivity.this, "mobile", riderBean.getMobile());
                                        Preferences.savePreferences(HomeActivity.this, "user_id", riderBean.getRiderId());
                                        Preferences.savePreferences(HomeActivity.this, "password", riderBean.getPassword());
                                    }

                                    finish();
                                    startActivity(new Intent(HomeActivity.this, RiderHomeActivity.class));
                                }
                            }

                            @Override
                            public void onFailure(Call<LoginNewResponse> call, Throwable t) {
                                super.onFailure(call, t);
                                Log.i(TAG, t.toString());
                                Log.i(TAG, call.toString());
                                Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.drawer_logout) {
            try {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                MemoryCache memoryCache = new MemoryCache();
                memoryCache.clear();

                if (Integer.valueOf(Build.VERSION.SDK_INT) > 16) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            map = FragmentMap.newInstance(0, this);
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

    @Override
    public void onPermissionGranted() {
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, FragmentMap.newInstance(0, this));
        ft.commit();
    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
        Toast.makeText(this, "Permission Denided", Toast.LENGTH_SHORT).show();
    }
}
