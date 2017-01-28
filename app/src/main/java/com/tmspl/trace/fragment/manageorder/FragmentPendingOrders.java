package com.tmspl.trace.fragment.manageorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tmspl.trace.R;
import com.tmspl.trace.activity.AcceptedDeliveriesActivity;
import com.tmspl.trace.adapter.Adapter_Accepted_Deliveries;
import com.tmspl.trace.apimodel.PendingOrderBean;
import com.tmspl.trace.extra.Constants;
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

public class FragmentPendingOrders extends Fragment {

    public static Activity context;
    public static ArrayList<PendingOrderBean> PendingList;
    View rootView;
    ListView lst_deliveries;
    Adapter_Accepted_Deliveries adapter_deliveries;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();

        PendingList = new ArrayList<PendingOrderBean>();

        if (NetworkUtil.isInternetConnencted(context)) {
            new history_list(context).execute();
        }
        rootView = inflater.inflate(R.layout.fragment_pending_orders, container, false);
//        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Orders");
        lst_deliveries = (ListView) rootView.findViewById(R.id.lst_deliveries);
        Constants.isSignCom = 0;
        return rootView;

    }

    public class history_list extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;

        public history_list(Activity context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context, "Fetching Deliveries..");
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
                if (Preferences.getSavedPreferences(context, "usertype").equals("1")) {
                    nameValuePairs.add(new BasicNameValuePair("user_id",
                            Preferences.getSavedPreferences(context, "user_id")));
                    Service = "user_specific_history_list";
                } else {
                    nameValuePairs.add(new BasicNameValuePair("depot_id",
                            Preferences.getSavedPreferences(context, "depot_id")));
                    Service = "depot_specific_history_list";
                }
                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + Service, ServiceHandler.POST,
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
                    //  Alert.ShowAlert(context, jobj1.getString("error"));
                } else {
                    if (jobj1.getInt("status") == 1) {

                        JSONObject responseObject = new JSONObject(jobj1.getString("responseJson"));
                        if (responseObject.getString("pending").equals("false")) {

                        } else {
                            JSONArray pendingArray = new JSONArray(responseObject.getString("pending"));
                            //     Log.e("Length", pendingArray.length() + "");
                            if (pendingArray.length() > 0) {
                                //     Log.e("Length1", pendingArray.length() + "");
                                for (int i = 0; i < pendingArray.length(); i++) {
                                    //    Log.e("Length" + i, pendingArray.length() + "");
                                    JSONObject pendingObject = pendingArray.getJSONObject(i);
                                    String to_areaa[] = pendingObject.getString("to_area").split("\\*");
                                    String to_area = to_areaa[to_areaa.length - 1];
                                    PendingOrderBean obj = new PendingOrderBean(pendingObject.getString("image"), pendingObject.getString("from_area"), to_area, pendingObject.getString("total_deliveries"), Math.round(Double.parseDouble(pendingObject.getString("total_amount"))) + "", pendingObject.getString("order_id"));
                                    PendingList.add(obj);
                                    //    Log.e("Record", PendingList.get(i).getAmount());
                                }
                            }


                            adapter_deliveries = new Adapter_Accepted_Deliveries(context, R.layout.custom_item_for_dockier_accepted_deliveries, PendingList);
                            lst_deliveries.setAdapter(adapter_deliveries);
                            lst_deliveries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Constants.order_id = PendingList.get(position).getOrder_id();
                                    Constants.isCompleted = 0;
                                    context.startActivity(new Intent(context, AcceptedDeliveriesActivity.class));
                                }
                            });

                        }
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



