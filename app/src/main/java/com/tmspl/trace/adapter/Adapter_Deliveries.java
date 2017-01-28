package com.tmspl.trace.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmspl.trace.R;
import com.tmspl.trace.activity.AcceptedDeliveriesActivity;
import com.tmspl.trace.apimodel.PendingOrderBean;
import com.tmspl.trace.extra.Alert;
import com.tmspl.trace.extra.Constants;
import com.tmspl.trace.extra.Preferences;
import com.tmspl.trace.extra.ServiceHandler;
import com.tmspl.trace.fragment.rider.FragmentPendingsOrders;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by File.Server on 9/28/2015.
 */
public class Adapter_Deliveries extends BaseAdapter {

    Activity context;
    List<PendingOrderBean> orderBeans;

    public Adapter_Deliveries(Activity context, int resource, List<PendingOrderBean> beans) {
        this.context = context;
        this.orderBeans = beans;

    }

    @Override
    public int getCount() {
        return orderBeans.size();
    }

    @Override
    public PendingOrderBean getItem(int position) {
        return orderBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final PendingOrderBean eventBean = getItem(position);
        final ViewHolder viewHolder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_item_for_dockier_deliveries, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.start = (TextView) convertView.findViewById(R.id.delivery_delivery_start_location);
            viewHolder.end = (TextView) convertView.findViewById(R.id.delivery_delivery_end_location);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.delivery_delivery_rs);
            viewHolder.count = (TextView) convertView.findViewById(R.id.delivery_delivery_txt);
            viewHolder.parcel_img = (ImageView) convertView.findViewById(R.id.delivery_delivery_parcel_img);
            viewHolder.delivery_accept_txt=(TextView)convertView.findViewById(R.id.delivery_accept_txt);
            viewHolder.delivery_calcel_txt=(TextView)convertView.findViewById(R.id.delivery_calcel_txt);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.parcel_img.setTag(Constants.Image_IP + eventBean.getParcel_img());
        if (eventBean.getParcel_img().equals("noimage.jpg") || eventBean.getParcel_img().length() == 0) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
           // Log.e("Position-Order id", position + "-" + eventBean.getOrder_id());
            Picasso.with(context)
                    .load(R.drawable.photo)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(viewHolder.parcel_img);
        } else {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
       //     Log.e("Position-Order id", position + "-" + eventBean.getOrder_id());
            Picasso.with(context)
                    .load(Constants.Image_IP + eventBean.getParcel_img())
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(viewHolder.parcel_img);

        }

        viewHolder.start.setText(eventBean.getStart());
        viewHolder.end.setText(eventBean.getEnd());
        viewHolder.amount.setText("Rs. " + Math.round(Float.valueOf(eventBean.getAmount())));
        viewHolder.count.setText(eventBean.getCount());
        viewHolder.delivery_accept_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.order_id=eventBean.getOrder_id();
                new accept_order(context,position).execute();
            }
        });
        viewHolder.delivery_calcel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.order_id=eventBean.getOrder_id();
                new cancel_order(context,position).execute();

                Preferences.savePreferences(context,"rejected",eventBean.getOrder_id()+",");

            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView start, end, amount, count,delivery_accept_txt,delivery_calcel_txt;
        ImageView parcel_img;

    }

    public class accept_order extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;
        int position;
        public accept_order(Activity context, int position) {
            this.context = context; this.position=position;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context,"Updating Order..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("rider_id", Preferences.getSavedPreferences(context, "rider_id")));
                nameValuePairs.add(new BasicNameValuePair("order_id", Constants.order_id));


                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "accept_order", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Updating Order Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Updating Order Goes Failed Please try again later\"}";
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
                        FragmentPendingsOrders.flg=0;
                        FragmentPendingsOrders.listBean.remove(position);
                        FragmentPendingsOrders.adapter_deliveries.notifyDataSetChanged();
                        Alert.showAlertWithPause(context, "Thank You for accepting order!\nOrder is placed under \"Orders\"!");
                        context.startActivity(new Intent(context, AcceptedDeliveriesActivity.class));

                    } else {
                        Alert.ShowAlert(context,
                                jobj1.getString("responseMessage"));
                    }
                }

                if (pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
                if (pd.isShowing())
                    pd.dismiss();
            }
        }
    }


    public class cancel_order extends AsyncTask<String, String, String> {

        Activity context;
        AlertDialog pd;
        int position;
        public cancel_order(Activity context, int position) {
            this.context = context;
            this.position=position;
        }

        @Override
        protected void onPreExecute() {
            pd = new SpotsDialog(context,"Updating Order..");
            pd.show();
            pd.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {
                ServiceHandler sh = new ServiceHandler();

                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("rider_id", Preferences.getSavedPreferences(context, "rider_id")));
                nameValuePairs.add(new BasicNameValuePair("order_id", Constants.order_id));


                String jsonResponse = sh.makeServiceCall(Constants.API_BASE_URL
                                + "rider_cancel_order", ServiceHandler.POST,
                        nameValuePairs);
                if (jsonResponse.equals("")) {
                    return "{\"error\":\"Updating Order Goes Failed Please try again later\"}";
                } else {
                    return jsonResponse;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"error\":\"Updating Order Goes Failed Please try again later\"}";
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
                        FragmentPendingsOrders.flg=0;
                        FragmentPendingsOrders.listBean.remove(position);
                        FragmentPendingsOrders.adapter_deliveries.notifyDataSetChanged();

                    } else {
                        Alert.ShowAlert(context,
                                jobj1.getString("responseMessage"));
                    }
                }

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
