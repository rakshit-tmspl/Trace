package com.tmspl.trace.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.tmspl.trace.R;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.CheckEditValue;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.HttpHelper;
import com.tmspl.trace.extra.NetworkUtil;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;
import com.tmspl.trace.extra.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 10;
    private static final int CAMERA_REQUEST = 20;
    public static Bitmap gbitmap = null;
    EditText et_fullname, et_username, et_password, et_mobile,
            et_confirm_password;
    Button btn_register;
    ImageView user_registration;
    HttpHelper httphelper;
    Utility utility;
    Boolean isInternetPresent = false;
    JSONObject Responce;
    String body;
    Activity context;

    public static Bitmap registerImage = null;
    public static int registerFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        context = UserRegistrationActivity.this;
        InitUI();
        InitAction();
    }

    private void InitUI() {

        et_fullname = (EditText) findViewById(R.id.et_fullname);
        et_username = (EditText) findViewById(R.id.et_username_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_mobile = (EditText) findViewById(R.id.et_mobilenumber);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        user_registration = (ImageView) findViewById(R.id.user_registration);
        btn_register = (Button) findViewById(R.id.btn_register);
    }

    private void InitAction() {
        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (CheckEditValue.isInValid(et_fullname)) {
                    Alert.ShowAlert(context, "Please Enter Full Name!");
                } else if (CheckEditValue.isInValid(et_username) || CheckEditValue.isValidEmail(et_username) == false) {
                    Alert.ShowAlert(context, "Please Enter Valid Email!");
                } else if (et_mobile.getText().toString().charAt(0) == '0' || et_mobile.getText().toString().length() < 10) {
                    Alert.ShowAlert(context, "Mobile number can not start with 0!");
                } else if (CheckEditValue.isInValid(et_password) || et_password.getText().toString().length() < 6) {
                    Alert.ShowAlert(context, "Password length must be more than 5!");
                } else if (et_confirm_password.getText().toString().equals(et_password.getText().toString()) == false) {
                    Alert.ShowAlert(context, "Password doesn't match!");
                } else {
                    if (NetworkUtil.isInternetConnencted(context)) {
                        Constants.ut = 1;
                        if (gbitmap != null) {
                            Constants.rphoto = Utility.encodeTobase64(gbitmap);
                            Log.e("IMAGE", "onClick: " + Constants.rphoto);
                        }
                        Constants.rfitst_name = et_fullname.getText().toString();
                        Constants.remail = et_username.getText().toString();
                        Constants.rmobile = et_mobile.getText().toString();
                        Constants.rpassword = et_password.getText().toString();
                        new check_otp(context).execute();

                    } else {
                        Alert.showInternetConnError(context);
                    }
                }
                // new sendImage(context).execute();
            }
        });

        user_registration = (ImageView) findViewById(R.id.user_registration);
        user_registration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
              /*  gbitmap = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.user_demo);*/
//                show_camera_gallery_popup(v);
                takeImageDialog();
            }
        });
    }

    public void takeImageDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UserRegistrationActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Constants.camFlg = 1;
                    startActivity(new Intent(UserRegistrationActivity.this, CaptureActivity.class));
                } else if (items[item].equals("Choose from Library")) {
                    Constants.camFlg = 0;
                    startActivity(new Intent(UserRegistrationActivity.this, CaptureActivity.class));
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void show_camera_gallery_popup(View v) {

        final String[] CType = new String[]{"CAMERA", "GALLERY"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Select Profile Picture")
                .setItems(CType, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 1) {

                            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, GALLERY_REQUEST);
                        } else {
                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            user_registration.setImageURI(data.getData());
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap croped_pic = (Bitmap) data.getExtras().get("data");
            user_registration.setImageBitmap(croped_pic);
        } else if (requestCode == 0) {
            Bitmap bitmp_user_img = BitmapFactory.decodeResource(getResources(), R.drawable.rider_registration);
            user_registration.setImageBitmap(bitmp_user_img);
        }
        user_registration.buildDrawingCache();
        gbitmap = user_registration.getDrawingCache();
    }*/

    public void InitConnection() {
        httphelper = new HttpHelper(context);
        utility = new Utility(context);
        isInternetPresent = utility.isConnectingToInternet();
    }

    public class check_otp extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public check_otp(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Confirming Signup..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new
                        ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("email", et_username.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("mobile", et_mobile.getText().toString()));

                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL + "check_otp", ServiceHandler.POST, nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Sign up Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Sign up Failed Please try again later\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (pd.isShowing())
                pd.dismiss();
            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Alert.ShowAlert(context, jobj1.getString
                            ("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {

                        JSONObject ResponseObj = new JSONObject
                                (jobj1.getString("responseJson"));


                        String otp = ResponseObj.getString("otp");
                        Preferences.savePreferences(context, "otp",
                                otp);

                        finish();
                        startActivity(new Intent(context,
                                OtpVerificationActivity.class));
                    } else {
                        Alert.ShowAlert(context, jobj1.getString
                                ("responseMessage"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (user_registration != null) {
                user_registration.setImageBitmap(Constants.parcelBitmap);
                Log.e("Image Value -->", "onResume: " + Constants.parcelBitmap);
                Log.e("Image BASE -->", "onResume: " + Utility.encodeTobase64(Constants.parcelBitmap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class sendImage extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Confirming Signup..");
            pd.show();
            pd.setCancelable(false);

        }

        public sendImage(Activity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new
                        ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("image", Utility.encodeTobase64(Constants.parcelBitmap)));

                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL + "save_image", ServiceHandler.POST, nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Sign up Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Sign up Failed Please try again later\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (pd.isShowing())
                pd.dismiss();
            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
                    Alert.ShowAlert(context, jobj1.getString
                            ("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {

                        JSONObject ResponseObj = new JSONObject
                                (jobj1.getString("responseJson"));
                    } else {
                        Alert.ShowAlert(context, jobj1.getString
                                ("responseMessage"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
