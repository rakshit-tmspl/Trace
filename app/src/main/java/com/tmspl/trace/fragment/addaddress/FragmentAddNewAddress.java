package com.tmspl.trace.fragment.addaddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.tmspl.trace.activity.CaptureActivity;
import com.tmspl.trace.R;
import com.tmspl.trace.apimodel.AreaBean;
import com.tmspl.trace.apimodel.CityBean;
import com.tmspl.trace.apimodel.GoogleAreaBean;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.CheckEditValue;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.MyApplication;
import com.tmspl.trace.extra.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by rakshit.sathwara on 1/24/2017.
 */

public class FragmentAddNewAddress extends Fragment implements Validator.ValidationListener, AdapterView.OnItemClickListener {

    private static final String TAG = MyApplication.APP_TAG + FragmentAddNewAddress.class.getSimpleName();

    public static ImageView ivAddNewAddressImage;
    public static EditText etNewaddName;
    public static EditText etNewaddContact;
    public static EditText etNewaddAddress;
    public static EditText etNewaddCity;
    public static AutoCompleteTextView autotvNewaddArea;
    public static EditText etNewaddPincode;
    public static Button btnNewaddCancel;
    public static Button btnNewaddConfirm;

    private Validator validator;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    public GoogleAreaBean r;

    private static final String LATLONG_API_BASE = "https://maps.googleapis.com/maps/api/place/details";

    private static final String API_KEY = "AIzaSyDFQhe5gw1uaM_Ha7DWlmXZtxvHKAhtVJE";
    public static String city_key = "", area_key, lat_long;

    private static final int GALLERY_REQUEST = 10;
    private static final int CAMERA_REQUEST = 20;

    public static Activity context;

    public static ArrayList<CityBean> cityList;
    public static ArrayList<AreaBean> areaList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_address, container, false);

        ivAddNewAddressImage = (ImageView) view.findViewById(R.id.iv_add_new_address_image);
        etNewaddCity = (EditText) view.findViewById(R.id.et_newadd_city);
        etNewaddName = (EditText) view.findViewById(R.id.et_newadd_name);
        etNewaddAddress = (EditText) view.findViewById(R.id.et_newadd_address);
        etNewaddPincode = (EditText) view.findViewById(R.id.et_newadd_pincode);
        autotvNewaddArea = (AutoCompleteTextView) view.findViewById(R.id.autotv_newadd_area);
        etNewaddContact = (EditText) view.findViewById(R.id.et_newadd_contact);
        btnNewaddConfirm = (Button) view.findViewById(R.id.btn_newadd_confirm);
        btnNewaddCancel = (Button) view.findViewById(R.id.btn_newadd_cancel);

        r = new GoogleAreaBean();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();

        autotvNewaddArea.setAdapter(new GooglePlacesAutocompleteAdapter(getActivity(), R.layout.auto_list_item));
        autotvNewaddArea.setOnItemClickListener(this);

        feelConstants();
        initAction();

        validator = new Validator(this);
        validator.setValidationListener(this);


        return view;
    }


    @Override
    public void onValidationSucceeded() {

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

    }

    private void feelConstants() {
        if (Constants.address_flag.equals("from")) {
            if (Constants.order_from_address.getName() != null && Constants.order_from_address.getAddress_id().equals("0")) {
                etNewaddName.setText(Constants.order_from_address.getName());
                etNewaddAddress.setText(Constants.order_from_address.getAddress_line_1());
                // new_address_to_address_line_2.setText(Constants.order_from_address.getAddress_line_2());
                city_key = Constants.order_from_address.getCity_key();

                area_key = Constants.order_from_address.getArea_key();
                autotvNewaddArea.setText(Constants.order_from_address.getArea_value());

                r.setState(Constants.order_from_address.getState());
                r.setLat_long(Constants.order_from_address.getLat_long());
                lat_long = r.getLat_long();

                etNewaddCity.setText(Constants.order_from_address.getCity_value());
                etNewaddContact.setText(Constants.order_from_address.getMobile());
                etNewaddPincode.setText(Constants.order_from_address.getPin());
            }
        } else {
            if (Constants.order_to_address.getName() != null && Constants.order_to_address.getAddress_id().equals("0")) {
                etNewaddName.setText(Constants.order_to_address.getName());
                etNewaddAddress.setText(Constants.order_to_address.getAddress_line_1());
                //new_address_to_address_line_2.setText(Constants.order_to_address.get(Constants.toCount).getAddress_line_2());
                city_key = Constants.order_to_address.getCity_key();

                area_key = Constants.order_to_address.getArea_key();
                autotvNewaddArea.setText(Constants.order_to_address.getArea_value());

                r.setState(Constants.order_to_address.getState());
                r.setLat_long(Constants.order_to_address.getLat_long());
                lat_long = r.getLat_long();

                etNewaddCity.setText(Constants.order_to_address.getCity_value());
                etNewaddContact.setText(Constants.order_to_address.getMobile());
                etNewaddPincode.setText(Constants.order_to_address.getPin());
            }
        }
    }

    public void initAction() {
        new all_city(getActivity()).execute();

        etNewaddPincode.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etNewaddPincode.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddName.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddContact.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddAddress.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddCity.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(autotvNewaddArea.getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        }, 200);

        btnNewaddCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etNewaddPincode.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddName.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddContact.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddAddress.getWindowToken(), 0);
                    // imm.hideSoftInputFromWindow(new_address_to_address_line_2.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddCity.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(autotvNewaddArea.getWindowToken(), 0);
                    getActivity().onBackPressed();
                } catch (Exception e) {
                    getActivity().onBackPressed();
                }
            }
        });

        btnNewaddConfirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etNewaddPincode.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddName.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddContact.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddAddress.getWindowToken(), 0);
                    // imm.hideSoftInputFromWindow(new_address_to_address_line_2.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(etNewaddCity.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(autotvNewaddArea.getWindowToken(), 0);
                } catch (Exception e) {

                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (etNewaddPincode.getText().length() == 0) {
                        etNewaddPincode.setText(" ");
                    }

                    if (CheckEditValue.isInValid(etNewaddName)) {
                        Alert.ShowAlert(getActivity(), "Please Enter Name!");
                    } else if (CheckEditValue.isInValid(etNewaddAddress)) {
                        Alert.ShowAlert(getActivity(), "Please Enter Address!");
                    } /*else if (CheckEditValue.isInValid(new_address_to_address_line_2)) {
                            Alert.ShowAlert(getActivity(), "Please Enter Address Line 2!");
                        }*/ else if (CheckEditValue.isInValid(etNewaddCity)) {
                        Alert.ShowAlert(getActivity(), "Please Select City!");
                    } else if (CheckEditValue.isInValid(autotvNewaddArea)) {
                        Alert.ShowAlert(getActivity(), "Please Select Area!");
                    } else if ((CheckEditValue.isInValid(etNewaddContact) || etNewaddContact.getText().length() < 10)) {
                        Alert.ShowAlert(getActivity(), "Please Enter Valid Mobile Number!");
                    } else {
                        if (r != null) {
                            if (r.getLat_long() != null) {
                                if (Constants.address_flag.equals("from")) {
                                    Constants.order_from_address.setAddress_id("0");
                                    Constants.order_from_address.setName(etNewaddName.getText().toString());
                                    Constants.order_from_address.setAddress_line_1(etNewaddAddress.getText().toString());
                                    // Constants.order_from_address.setAddress_line_2(new_address_to_address_line_2.getText().toString());
                                    Constants.order_from_address.setCity_key(city_key);
                                    Constants.order_from_address.setArea_key(autotvNewaddArea.getText() + "*" + r.getLat_long());

                                    Constants.order_from_address.setCity_value(etNewaddCity.getText().toString());
                                    Constants.order_from_address.setArea_value(autotvNewaddArea.getText().toString());

                                    Constants.order_from_address.setLat_long(r.getLat_long());
                                    Constants.order_from_address.setState(r.getState());
                                    Constants.order_from_address.setMobile(etNewaddContact.getText().toString());
                                    Constants.order_from_address.setPin(etNewaddPincode.getText().toString());
                                } else {
                                    Constants.order_to_address.setAddress_id("0");
                                    Constants.order_to_address.setName(etNewaddName.getText().toString());
                                    Constants.order_to_address.setAddress_line_1(etNewaddAddress.getText().toString());
                                    // Constants.order_to_address.get(Constants.toCount).setAddress_line_2(new_address_to_address_line_2.getText().toString());
                                    Constants.order_to_address.setCity_key(city_key);
                                    Constants.order_to_address.setArea_key(autotvNewaddArea.getText() + "*" + r.getLat_long());

                                    Constants.order_to_address.setCity_value(etNewaddCity.getText().toString());
                                    Constants.order_to_address.setArea_value(autotvNewaddArea.getText().toString());

                                    Constants.order_to_address.setLat_long(r.getLat_long());
                                    Constants.order_to_address.setState(r.getState());
                                    Constants.order_to_address.setMobile(etNewaddContact.getText().toString());
                                    Constants.order_to_address.setPin(etNewaddPincode.getText().toString());
                                }
                                getActivity().onBackPressed();
                            } else {
                                Alert.ShowAlert(getActivity(), "Please select nearest area from suggestions!");
                            }
                        } else {
                            Alert.ShowAlert(getActivity(), "Please select nearest area from suggestions!");
                        }

                    }
                }
                return false;
            }
        });
        if (Constants.parcelBitmap != null) {
            ivAddNewAddressImage.setImageBitmap(Constants.parcelBitmap);
        }

        ivAddNewAddressImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                takeImageDialog();
            }
        });


        /*area.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (city_key.length() == 0) {
                        Alert.ShowAlert(getActivity(), "Please Select City!");
                    } else {
                        areaList.removeAll(areaList);
                        areaList.clear();
                        new area(context).execute();
                    }

                }
                return false;
            }
        });*/
    }

    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);


        r = (GoogleAreaBean) adapterView.getItemAtPosition(position);
        new getLatLong(getActivity()).execute();

    }


    public void takeImageDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
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

    public class all_city extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public all_city(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Fetching City..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "all_city", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Fetching Cities Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Fetching Cities Goes Failed Please try again later\"}";
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

                        String res = jobj1.getString("responseJson");
                        if (res.equals("false") == false) {
                            JSONArray city_array = new JSONArray(res);
                            for (int i = 0; i < city_array.length(); i++) {
                                JSONObject city_object = city_array.getJSONObject(i);
                                cityList.add(new CityBean(city_object.getString("key"), city_object.getString("value")));
                            }


                           /* if (Constants.order_from_address.getCity_key() != null || Constants.Constants_city.length()>0) {

                                if(Constants.Constants_city.length()>0)
                                {
                                    for(int i=0;i<cityList.size();i++)
                                    {
                                        if(Constants.Constants_city.equals(cityList.get(i).getKey().split("\\*")[0]))
                                        {
                                            city_key = cityList.get(i).getKey();
                                            String input = cityList.get(i).getValue();
                                            city.setText(input.substring(0, 1).toUpperCase() + input.substring(1));
                                            break;
                                        }
                                    }

                                }
                                else {
                                    city_key = Constants.order_from_address.getCity_key();
                                    String input = Constants.order_from_address.getCity_value();
                                    city.setText(input.substring(0, 1).toUpperCase() + input.substring(1));
                                }
                            } else {*/
                            etNewaddCity.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    // TODO Auto-generated method stub

                                    if (event.getAction() == MotionEvent.ACTION_UP) {
                                        final String[] City_array = new String[cityList.size()];
                                        for (int c = 0; c < cityList.size(); c++) {
                                            City_array[c] = cityList.get(c).getValue();
                                        }
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                                        builder.setTitle("Select City")
                                                .setItems(City_array, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        city_key = cityList.get(which).getKey().toLowerCase();
                                                        Constants.data_city = city_key.split("\\*")[0];
                                                        String input = cityList.get(which).getValue();
                                                        etNewaddCity.setText(input.substring(0, 1).toUpperCase() + input.substring(1));

                                                    }
                                                });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();
                                    }
                                    return false;
                                }
                            });
                            //    }


                        }

                    } else {
                        Alert.ShowAlert(context,
                                jobj1.getString("responseMessage"));
                    }
                }

                if (pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class area extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public area(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Fetching Area..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub


            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("city_id", city_key));
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "area", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Fetching Area Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();


            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                if (result != null) {
                    JSONArray city_array = new JSONArray(result);
                    for (int i = 0; i < city_array.length(); i++) {
                        JSONObject ara_object = city_array.getJSONObject(i);
                        areaList.add(new AreaBean(ara_object.getString("key"), ara_object.getString("value"), ara_object.getString("lat_long")));
                    }
                    final String[] Area_array = new String[areaList.size()];
                    for (int c = 0; c < areaList.size(); c++) {
                        Area_array[c] = areaList.get(c).getValue();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
                    builder.setTitle("Select Area")
                            .setItems(Area_array, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    area_key = areaList.get(which).getKey().toLowerCase();
                                    lat_long = areaList.get(which).getLatLong();
                                    String input = areaList.get(which).getValue();
                                    autotvNewaddArea.setText(input.substring(0, 1).toUpperCase() + input.substring(1));
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    //   }
                    //}
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

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Constants.parcelBitmap != null) {
                ivAddNewAddressImage.setImageBitmap(Constants.parcelBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList<GoogleAreaBean> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public GoogleAreaBean getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the Constants to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }


    }

    public static ArrayList<GoogleAreaBean> autocomplete(String input) {
        ArrayList<GoogleAreaBean> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8") + "," + etNewaddCity.getText());
            Log.e("URL : ", sb.toString());
            URL url = new URL(sb.toString());
            if (etNewaddCity.getText().length() > 0) {
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } else {
                //Toast.makeText(context, "Please select city!", Toast.LENGTH_SHORT).show();
                Alert.ShowAlert(context, "Please Select City!");
            }
        } catch (MalformedURLException e) {
            Log.e("New Address", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("New Address", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.e("Json Constants", jsonObj.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<GoogleAreaBean>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(new GoogleAreaBean(predsJsonArray.getJSONObject(i).getString("description"), predsJsonArray.getJSONObject(i).getString("reference"), ""));
            }
        } catch (JSONException e) {
            Log.e("New Address", "Cannot process JSON results", e);
        }

        return resultList;
    }

    class getLatLong extends AsyncTask<String, String, String> {

        Activity context;
        android.app.AlertDialog pd;

        public getLatLong(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Please wait...");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                StringBuilder sb = new StringBuilder(LATLONG_API_BASE + OUT_JSON);
                sb.append("?key=" + API_KEY);
                sb.append("&reference=" + r.getReference());
                //   Log.e("URL : ", sb.toString());
                URL url = new URL(sb.toString());
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }

            } catch (MalformedURLException e) {
                //  Log.e("New Address", "Error processing Places API URL", e);
                return "";
            } catch (IOException e) {
                //    Log.e("New Address", "Error connecting to Places API", e);
                return "";
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }

            }
            return jsonResults.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //  Log.e("Response", s);
            try {
                JSONObject resultObj = new JSONObject(s);
                resultObj = new JSONObject(resultObj.getString("result"));

                JSONObject ll = new JSONObject(new JSONObject(resultObj.getString("geometry")).getString("location"));
                r.setLat_long(ll.getString("lat") + "," + ll.getString("lng"));

                r.setState(" ");

                //Toast.makeText(getActivity(), r.getLat_long(), Toast.LENGTH_SHORT).show();
                if (pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                if (pd.isShowing())
                    pd.dismiss();
            }
        }
    }
}
