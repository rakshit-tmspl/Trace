package com.tmspl.trace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tmspl.trace.R;
import com.tmspl.trace.apimodel.Address_bean;
import com.tmspl.trace.fragment.addaddress.FragmentAddAddressFromExisting;

import java.util.List;

import static com.tmspl.trace.fragment.addaddress.FragmentAddAddressFromExisting.listPosition;


public class AddressAdapter extends ArrayAdapter<Address_bean> {

    Context context;
    List<Address_bean> mAddress_bean;
    private int mSelectedVariation;
    private int selected = -1;

    public AddressAdapter(Context context, int resource, List<Address_bean> beans, int SelectedVariation) {
        super(context, resource, beans);
        this.context = context;
        this.mAddress_bean = beans;
        this.mSelectedVariation = SelectedVariation;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Address_bean eventBean = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_view_item_address, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.address_list_layout = (LinearLayout) convertView.findViewById(R.id.address_list_layout);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.address_radio = (RadioButton) convertView.findViewById(R.id.address_radio);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(eventBean.getName() + " - " + eventBean.getValue());


        if (mSelectedVariation == position) viewHolder.address_radio.setChecked(true);
        else viewHolder.address_radio.setChecked(false);

        viewHolder.address_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAddAddressFromExisting.listPosition = position;
                mSelectedVariation = position;
                AddressAdapter.this.notifyDataSetChanged();
            }
        });

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentAddAddressFromExisting.listPosition = position;
                mSelectedVariation = position;
                AddressAdapter.this.notifyDataSetChanged();
            }
        });


        return convertView;
    }

    class ViewHolder {
        TextView title;
        RadioButton address_radio;
        LinearLayout address_list_layout;

    }
}