package com.tmspl.trace.fragment.addaddress;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.tmspl.trace.R;
import com.tmspl.trace.activity.CaptureActivity;
import com.tmspl.trace.adapter.AddressAdapter;
import com.tmspl.trace.apimodel.Address_bean;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.CustomRequest;
import com.tmspl.trace.extra.MyApplication;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Fragment_From_To_Address extends Fragment {

    public static Activity context;
    View rootView;
    public static ListView address_list;
    public static ArrayList<Address_bean> AddressList;
    public static Button btnCancel, btnDone;
    public static int listPosition = 0;
    public static ImageView addphoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    int flg = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        flg = 1;
        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_add_from_existing, container, false);

        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnDone = (Button) rootView.findViewById(R.id.btnDone);
        address_list = (ListView) rootView.findViewById(R.id.address_list);
        AddressList = new ArrayList<Address_bean>();
        addphoto = (ImageView) rootView.findViewById(R.id.order_photo);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (Constants.parcelBitmap != null) {
            addphoto.setImageBitmap(Constants.parcelBitmap);
        }

        addphoto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                takeImageDialog();
            }
        });


        btnDone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (AddressList.size() > 0) {
                        if (Constants.address_flag.equals("from")) {
                            Constants.order_from_address.setAddress_id(AddressList.get(listPosition).getKey());
                            Constants.order_from_address.setName(AddressList.get(listPosition).getName());
                            Constants.order_from_address.setAddress_line_1(AddressList.get(listPosition).getValue());
                            Constants.order_from_address.setLat_long(AddressList.get(listPosition).getLatLong());
                            Constants.data_city = AddressList.get(listPosition).getData_city();
                        } else {
                            Constants.order_to_address.get(Constants.toCount).setAddress_id(AddressList.get(listPosition).getKey());
                            Constants.order_to_address.get(Constants.toCount).setName(AddressList.get(listPosition).getName());
                            Constants.order_to_address.get(Constants.toCount).setAddress_line_1(AddressList.get(listPosition).getValue());
                            Constants.order_to_address.get(Constants.toCount).setLat_long(AddressList.get(listPosition).getLatLong());
                        }
                        getActivity().onBackPressed();
                    } else {
                        Alert.ShowAlert(getActivity(), "Please Enter New Address!");
                    }
                }
                return false;
            }
        });


        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // new getAddress(context).execute();
            if (flg == 1) {
                flg = 0;
                getAdd();
            }
        } else {
           /* if(Fragment_Place_Orders.pager==1)
            {
                getAdd();
            }*/
        }
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

    public void getAdd() {
        try {


            // Tag used to cancel the request
            String tag_json_obj = "json_obj_req";

            Listener<JSONObject> listener;

            String url = Constants.API_BASE_URL;
            if (Constants.address_flag.equals("from")) {
                if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                    url += "user_from_address";
                } else {
                    url += "depot_from_address";
                }
            } else {
                if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                    url += "user_to_address";
                } else {
                    url += "depot_to_address";
                }
            }


            HashMap<String, String> params = new HashMap<String, String>();
            if (Constants.address_flag.equals("from")) {
                if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                    params.put("user_id", Preferences.getSavedPreferences(context, "user_id"));
                } else {
                    params.put("depot_id", Preferences.getSavedPreferences(context, "depot_id"));
                }
            } else {
                if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                    params.put("user_id", Preferences.getSavedPreferences(context, "user_id"));
                } else {
                    params.put("depot_id", Preferences.getSavedPreferences(context, "depot_id"));
                }
            }
            params.put("auth", "4JW*BNtp2nX6AbJCAoksWi/1DHoJJGYw");


            final AlertDialog pDialog = new SpotsDialog(getActivity(), "Fetching Existing Address...");
            pDialog.show();

            CustomRequest jsonObjReq = new CustomRequest(Request.Method.POST,
                    url, params,
                    new Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            //   Log.e("From to response", response.toString());

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    //   Log.e("FROM TO", "Error: " + error.getMessage());
                    pDialog.dismiss();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {

                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    if (Constants.address_flag.equals("from")) {
                        if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                            params.put("user_id", Preferences.getSavedPreferences(context, "user_id"));
                        } else {
                            params.put("depot_id", Preferences.getSavedPreferences(context, "depot_id"));
                        }
                    } else {
                        if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                            params.put("user_id", Preferences.getSavedPreferences(context, "user_id"));
                        } else {
                            params.put("depot_id", Preferences.getSavedPreferences(context, "depot_id"));
                        }
                    }
                    params.put("auth", "4JW*BNtp2nX6AbJCAoksWi/1DHoJJGYw");

                    //     Log.e("come", "come");

                    return params;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Log.e(" NetworkResponse DATA", "parseNetworkResponse: " + response);
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(new JSONObject(jsonString),
                                HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (JSONException je) {
                        return Response.error(new ParseError(je));
                    }
                }

                @Override
                protected void deliverResponse(JSONObject response) {
                    // TODO Auto-generated method stub
                    try {
                        JSONObject jobj1 = new JSONObject(response.toString());

                        if (jobj1.has("error")) {
//                            Alert.ShowAlert(context, jobj1.getString("error"));
                        } else {
                            if (jobj1.getString("status").equals("1")) {
                                JSONArray AddressArray = new JSONArray(jobj1.getString("responseJson"));
                                for (int i = 0; i < AddressArray.length(); i++) {
                                    JSONObject address_object = AddressArray.getJSONObject(i);
                                    if (Constants.address_flag.equals("from")) {
                                        if (Constants.data_city.length() > 0) {
                                            //  if( address_object.getString("data_city").equals(Data.data_city)) {
                                            AddressList.add(new Address_bean(address_object.getString("key"), address_object.getString("name"), address_object.getString("value"), address_object.getString("lat_long"), address_object.getString("data_city")));
                                            //    }
                                        } else {
                                            AddressList.add(new Address_bean(address_object.getString("key"), address_object.getString("name"), address_object.getString("value"), address_object.getString("lat_long"), address_object.getString("data_city")));
                                        }

                                    } else {
                                        //   if( address_object.getString("data_city").equals(Data.data_city)) {
                                        AddressList.add(new Address_bean(address_object.getString("key"), address_object.getString("name"), address_object.getString("value"), address_object.getString("lat_long"), address_object.getString("data_city")));
                                        //   }
                                    }
                                }
                                AddressAdapter addressAdapter = new AddressAdapter(context, R.layout.list_view_item_address, AddressList, Fragment_From_To_Address.listPosition);
                                address_list.setAdapter(addressAdapter);
                                address_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                            }
                        }

                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    } catch (Exception e) {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        e.printStackTrace();
                    }


                }

            };


            MyApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class getAddress extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public getAddress(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Fetching Existing Address..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                String Service = "";
                if (Constants.address_flag.equals("from")) {
                    if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                        Service = "user_from_address";
                        nameValuePairs.add(new BasicNameValuePair("user_id", Preferences.getSavedPreferences(context, "user_id")));
                    } else {

                        Service = "depot_from_address";
                        nameValuePairs.add(new BasicNameValuePair("depot_id", Preferences.getSavedPreferences(context, "depot_id")));
                    }
                } else {
                    if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                        Service = "user_to_address";
                        nameValuePairs.add(new BasicNameValuePair("user_id", Preferences.getSavedPreferences(context, "user_id")));
                    } else {
                        Service = "depot_to_address";
                        nameValuePairs.add(new BasicNameValuePair("depot_id", Preferences.getSavedPreferences(context, "depot_id")));
                    }
                }
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL + Service, ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Fetching Address Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Fetching Address Goes Failed Please try again later\"}";
            }
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jobj1 = new JSONObject(result);

                if (jobj1.has("error")) {
//                    Alert.ShowAlert(context, jobj1.getString("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {

                        JSONArray AddressArray = new JSONArray(jobj1.getString("responseJson"));
                        for (int i = 0; i < AddressArray.length(); i++) {
                            JSONObject address_object = AddressArray.getJSONObject(i);
                            if (Constants.address_flag.equals("from")) {
                                if (Constants.data_city.length() > 0) {
                                    //  if( address_object.getString("data_city").equals(Data.data_city)) {
                                    AddressList.add(new Address_bean(address_object.getString("key"), address_object.getString("name"), address_object.getString("value"), address_object.getString("lat_long"), address_object.getString("data_city")));
                                    //    }
                                } else {
                                    AddressList.add(new Address_bean(address_object.getString("key"), address_object.getString("name"), address_object.getString("value"), address_object.getString("lat_long"), address_object.getString("data_city")));
                                }

                            } else {
                                //   if( address_object.getString("data_city").equals(Data.data_city)) {
                                AddressList.add(new Address_bean(address_object.getString("key"), address_object.getString("name"), address_object.getString("value"), address_object.getString("lat_long"), address_object.getString("data_city")));
                                //   }
                            }
                        }
                        AddressAdapter addressAdapter = new AddressAdapter(context, R.layout.list_view_item_address, AddressList, Fragment_From_To_Address.listPosition);
                        address_list.setAdapter(addressAdapter);
                        address_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Constants.parcelBitmap != null) {
                addphoto.setImageBitmap(Constants.parcelBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



