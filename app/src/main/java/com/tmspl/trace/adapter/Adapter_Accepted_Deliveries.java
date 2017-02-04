package com.tmspl.trace.adapter;

import android.content.Context;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tmspl.trace.R;
import com.tmspl.trace.apimodel.PendingOrderBean;
import com.tmspl.trace.extra.Constants;

import java.util.List;

/**
 * Created by File.Server on 9/28/2015.
 */
public class Adapter_Accepted_Deliveries extends ArrayAdapter<PendingOrderBean> {

    Context context;
    List<PendingOrderBean> orderBeans;
    ViewHolder viewHolder;

    public Adapter_Accepted_Deliveries(Context context, int resource, List<PendingOrderBean> beans) {
        super(context, resource, beans);
        this.context = context;
        this.orderBeans = beans;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PendingOrderBean eventBean = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_item_for_dockier_accepted_deliveries
                    , parent, false);
            viewHolder = new ViewHolder();
            viewHolder.start = (TextView) convertView.findViewById(R.id.delivery_delivery_start_location);
            viewHolder.end = (TextView) convertView.findViewById(R.id.delivery_delivery_end_location);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.delivery_delivery_rs);
            viewHolder.count = (TextView) convertView.findViewById(R.id.delivery_delivery_txt);
            viewHolder.parcel_img = (ImageView) convertView.findViewById(R.id.delivery_delivery_parcel_img);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.start.setText(eventBean.getStart());
        viewHolder.end.setText(eventBean.getEnd());
        viewHolder.amount.setText("Rs. " + eventBean.getAmount());
        viewHolder.count.setText(eventBean.getCount() + " - Delivery");
        viewHolder.parcel_img.setTag(Constants.Image_IP + eventBean.getParcel_img());
        if (eventBean.getParcel_img().equals("noimage.jpg") || eventBean.getParcel_img().length() == 0) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //Log.e("Position-Order id", position + "-" + eventBean.getOrder_id());
            Picasso.with(context)
                    .load(R.drawable.photo)
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(viewHolder.parcel_img);
        } else {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //   Log.e("Position-Order id", position + "-" + eventBean.getOrder_id());
            Picasso.with(context)
                    .load(Constants.Image_IP + eventBean.getParcel_img())
                    .placeholder(R.drawable.photo)
                    .error(R.drawable.photo)
                    .into(viewHolder.parcel_img);

        }

        return convertView;
    }

    class ViewHolder {
        TextView start, end, amount, count;
        ImageView parcel_img;

    }
}
