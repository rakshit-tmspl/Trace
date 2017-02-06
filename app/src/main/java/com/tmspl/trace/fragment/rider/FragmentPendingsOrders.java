package com.tmspl.trace.fragment.rider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tmspl.trace.R;
import com.tmspl.trace.activity.ridersactivity.DeliveryDetails;
import com.tmspl.trace.adapter.Adapter_Deliveries;
import com.tmspl.trace.apimodel.PendingOrderBean;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.MemoryCache;
import com.tmspl.trace.extra.MyApplication;
import com.tmspl.trace.extra.NetworkUtil;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

/**
 * Created by rakshit.sathwara on 1/25/2017.
 */

public class FragmentPendingsOrders extends Fragment {

    private Activity context;

    private static final String TAG = MyApplication.APP_TAG + FragmentPendingsOrders.class.getSimpleName();

    View rootView;
    ListView lst_deliveries;
    public static Adapter_Deliveries adapter_deliveries;
    public static ArrayList<PendingOrderBean> listBean;
    public static int flg = 1;
    public static int position = 0;
    String riderId;

    private TextView tvNothing;

    public static FragmentPendingsOrders newInstance(int index, Activity context) {
        FragmentPendingsOrders fragmentMap = new FragmentPendingsOrders();
        fragmentMap.context = context;
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragmentMap.setArguments(b);
        return fragmentMap;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        rootView = inflater.inflate(R.layout.fragment_pending_orders, container, false);

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();

        riderId = Preferences.getSavedPreferences(getActivity(), "rider_id");
        Log.e(TAG, "onCreateView:  riderId ->" + riderId);

        //tvNothing = (TextView) rootView.findViewById(R.id.tv_nothing);

        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        MemoryCache mr = new MemoryCache();
                        mr.clear();
                        if (Constants.isDash == 1) {
                            Constants.isDash = 0;
                            getActivity().finish();

                        } else {
                            getActivity().finish();
                        }

                        return false;
                    }
                }
                return false;
            }
        });

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pending Orders");
        listBean = new ArrayList<PendingOrderBean>();
        listBean.removeAll(listBean);
        MappingID();
        flg = 1;

        if (NetworkUtil.isInternetConnencted(context)) {
            new pending_order_list(context).execute();
        }


        return rootView;
    }

    private void MappingID() {
        lst_deliveries = (ListView) rootView.findViewById(R.id.lst_deliveries);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flg == 0) {
            flg = 1;
            listBean = new ArrayList<PendingOrderBean>();
            listBean.removeAll(listBean);
            if (NetworkUtil.isInternetConnencted(context)) {
                new pending_order_list(context).execute();
            }
        }
    }

    public class pending_order_list extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public pending_order_list(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Fetching Pending Deliveries..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

//                nameValuePairs.add(new BasicNameValuePair("lat_long", LocationService.rider_lat_long));
                nameValuePairs.add(new BasicNameValuePair("lat_long", "23.0326956,72.5590835"));
                nameValuePairs.add(new BasicNameValuePair("rider_id", riderId));
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "pending_order_list", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Updating Delivery Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Updating Delivery Goes Failed Please try again later\"}";
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

                        if (jobj1.getString("responseJson").equals("false") == false) {
                            listBean.removeAll(listBean);
                            JSONArray pendingArray = new JSONArray(jobj1.getString("responseJson"));
                            if (pendingArray.length() > 0) {
                                for (int i = 0; i < pendingArray.length(); i++) {
                                    JSONObject pendingObject = pendingArray.getJSONObject(i);
                                    String to_areaa[] = pendingObject.getString("to_area").split("\\*");
                                    String to_area = to_areaa[to_areaa.length - 1];
                                    PendingOrderBean obj = new PendingOrderBean(pendingObject.getString("image"), pendingObject.getString("from_area"), to_area, pendingObject.getString("total_deliveries"), pendingObject.getString("total_amount"), pendingObject.getString("order_id"));

                                    if (Preferences.getSavedPreferences(context, "rejected").indexOf(pendingObject.getString("order_id")) == -1) {

                                        listBean.add(obj);
                                    }
                                }

                                if (listBean.size() > 0) {
                                    //  tvNothing.setVisibility(View.GONE);
                                    // lst_deliveries.setVisibility(View.VISIBLE);
                                    adapter_deliveries = new Adapter_Deliveries(context, R.layout.custom_item_for_dockier_deliveries, listBean);
                                    lst_deliveries.setAdapter(adapter_deliveries);
                                    lst_deliveries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Constants.order_id = listBean.get(position).getOrder_id();
                                            FragmentPendingsOrders.position = position;
                                            context.startActivity(new Intent(context, DeliveryDetails.class));
                                        }
                                    });
                                } else {
                                  //  lst_deliveries.setVisibility(View.GONE);
                                    //  tvNothing.setVisibility(View.VISIBLE);
                                }


                            }
                        } else {
                            //Alert.ShowAlert(context,"No Pending Orders Found!");
                        }
                    } else {
                        Alert.showAlertWithFinish(context,
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