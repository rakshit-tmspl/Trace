package com.tmspl.trace.fragment.addaddress;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tmspl.trace.R;
import com.tmspl.trace.activity.CaptureActivity;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.CheckEditValue;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;
import com.tmspl.trace.extra.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

/**
 * Created by rakshit.sathwara on 2/14/2017.
 */

public class UpdateUserProfile extends Fragment {

    public static Activity context;
    View rootView;
    public static int mob = 0;
    EditText edt_password, user_name, user_mobile, user_email;
    ImageView user_image;
    Button btnCancel, btnSave;
    public static String new_pass = "";
    public static int picChange = 0;

    private String newPass, oldPass;

    public static UpdateUserProfile newInstance(int index, Activity context) {
        UpdateUserProfile updateUserProfile = new UpdateUserProfile();
        updateUserProfile.context = context;
        Bundle b = new Bundle();
        b.putInt("index", index);
        updateUserProfile.setArguments(b);
        return updateUserProfile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_update_user_profile, container, false);
        edt_password = (EditText) rootView.findViewById(R.id.user_password);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Edit Profile");

        user_name = (EditText) rootView.findViewById(R.id.user_name);
        user_email = (EditText) rootView.findViewById(R.id.user_email);
        user_mobile = (EditText) rootView.findViewById(R.id.user_mobile);
        user_image = (ImageView) rootView.findViewById(R.id.user_image);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);

        if (Preferences.getSavedPreferences(getActivity(), "facebook_id").length() > 0 || Preferences.getSavedPreferences(getActivity(), "google_id").length() > 0) {
            edt_password.setVisibility(View.GONE);
        }
        edt_password.setKeyListener(null);

        user_name.setText(Preferences.getSavedPreferences(context, "first_name"));
        user_email.setText(Preferences.getSavedPreferences(context, "email"));
        user_mobile.setText(Preferences.getSavedPreferences(context, "mobile"));
        user_image.setImageBitmap(Constants.image);
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picChange = 1;
                takeImageDialog();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckEditValue.isInValid(user_name)) {
                    Alert.ShowAlert(getActivity(), "Please Enter Name!");
                } else if (CheckEditValue.isInValid(user_email) || CheckEditValue.isValidEmail(user_email) == false) {
                    Alert.ShowAlert(getActivity(), "Please Enter Valid Email Address!");
                } else if (user_mobile.getText().toString().charAt(0) == '0' || user_mobile.getText().toString().length() < 10) {
                    Alert.ShowAlert(context, "Mobile number can not start with 0!");
                } else {
                    new update_user_profile(getActivity()).execute();
                }
            }
        });

        edt_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edt_password.getRight() - edt_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        generate_dialog();
                        return true;
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    public void generate_dialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_for_change_password, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        final EditText old_password, new_password, confirm_new_password;

        old_password = (EditText) dialogView.findViewById(R.id.old_password);
        new_password = (EditText) dialogView.findViewById(R.id.new_password);
        confirm_new_password = (EditText) dialogView.findViewById(R.id.confirm_new_password);

        Button done = (Button) dialogView.findViewById(R.id.btn_don);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");

                    String passwordToHash = old_password.getText().toString();
                    md.update(passwordToHash.getBytes());
                    byte[] bytes = md.digest();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < bytes.length; i++) {
                        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
                    }
                    String sec = sb.toString();
                    Log.e("old", Preferences.getSavedPreferences(getActivity(), "password"));
                    Log.e("new", sec.toString());


                    String old = Preferences.getSavedPreferences(getActivity(), "password");
                    String newString = sec.toString();

                    Log.e("OLD PASSWORD", "onClick: " + old);

                    if (old.equals(newString)) {
                        Log.e("PASSWORD", "onClick: " + "OLD PASSWORD MATCH");
                    }


                    if (Preferences.getSavedPreferences(getActivity(), "password").equalsIgnoreCase(sec.toString())) {
                        if (new_password.getText().toString().equals(confirm_new_password.getText().toString())) {
                            if (new_password.getText().toString().length() > 5) {
                                new_pass = new_password.getText().toString();
                                alertDialog.dismiss();
                            } else {
                                Alert.ShowAlert(getActivity(), "Minimum password length must be 6!");
                            }
                        } else {
                            Alert.ShowAlert(getActivity(), "Password does not match!");
                        }
                        newPass = new_password.getText().toString().trim();
                        oldPass = old_password.getText().toString().trim();
                        new ChangePassword(context).execute();
                    } else {
                        Alert.ShowAlert(getActivity(), "Old password does not match!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        Button cancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void takeImageDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                Constants.camSender = 1;
                if (items[item].equals("Take Photo")) {
                    Constants.camFlg = 1;
                    startActivity(new Intent(getActivity(), CaptureActivity.class));
                } else if (items[item].equals("Choose from Library")) {
                    Constants.camFlg = 0;
                    startActivity(new Intent(getActivity(), CaptureActivity.class));
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.image != null) {
            user_image.setImageBitmap(Constants.image);
        }
    }

    public class update_user_profile extends AsyncTask<String, String, String> {

        Activity context;
        android.app.AlertDialog pd;

        public update_user_profile(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Updating Profile ..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id", Preferences.getSavedPreferences(getActivity(), "user_id")));

                nameValuePairs.add(new BasicNameValuePair("first_name", user_name.getText().toString()));

                if (Preferences.getSavedPreferences(getActivity(), "mobile").equals(user_mobile.getText().toString()) == false) {
                    mob = 1;
                    Preferences.savePreferences(getActivity(), "mobile", user_mobile.getText().toString());
                    nameValuePairs.add(new BasicNameValuePair("mobile", user_mobile.getText().toString()));
                }
//                if (Preferences.getSavedPreferences(getActivity(), "email").equals(user_email.getText().toString()) == false) {
                nameValuePairs.add(new BasicNameValuePair("email", user_email.getText().toString()));
//                }


                if (new_pass.equals("") == false) {

                    nameValuePairs.add(new BasicNameValuePair("password", new_pass));
                    new_pass = "";
                }
                if (picChange == 1) {
                    picChange = 0;
                    nameValuePairs.add(new BasicNameValuePair("image", Utility.encodeTobase64(Constants.image)));
                }


                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL + "update_profile", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Updating Profile Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Updating Profile Goes Failed Please try again later\"}";
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
                        Toast.makeText(getActivity(), "Profile updated successfully!", Toast.LENGTH_LONG).show();
                        getFragmentManager().popBackStack();
                    } else {
                        if (mob == 1) {
                            mob = 0;
                            Preferences.savePreferences(getActivity(), "mobile", "");
                        }
                        Alert.ShowAlert(context,
                                jobj1.getString("responseMessage"));
                    }
                }

                if (pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                if (pd.isShowing())
                    pd.dismiss();
                e.printStackTrace();
            }
        }
    }

    private class ChangePassword extends AsyncTask<String, String, String> {

        Activity context;
        android.app.AlertDialog pd;

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Updating Profile ..");
            pd.show();
            pd.setCancelable(false);

        }

        public ChangePassword(Activity context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("old_password", oldPass));
                nameValuePairs.add(new BasicNameValuePair("mobile", Preferences.getSavedPreferences(getActivity(), "mobile")));
                nameValuePairs.add(new BasicNameValuePair("new_password", newPass));
                nameValuePairs.add(new BasicNameValuePair("auth", Constants.AUTH));
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL + "change_password", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Updating Profile Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Updating Profile Goes Failed Please try again later\"}";
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
                        Toast.makeText(getActivity(), jobj1.getString("jsonResponse"), Toast.LENGTH_LONG).show();
                        getFragmentManager().popBackStack();
                    } else {
//                        if (mob == 1) {
//                            mob = 0;
//                            Preferences.savePreferences(getActivity(), "mobile", "");
//                        }
                        Alert.ShowAlert(context,
                                jobj1.getString("responseMessage"));
                    }
                }

                if (pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                if (pd.isShowing())
                    pd.dismiss();
                e.printStackTrace();
            }
        }

    }
}