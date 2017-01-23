package com.tmspl.trace.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tmspl.trace.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rakshit.sathwara on 1/23/2017.
 */

public class FragmentSetDestination extends Fragment {


    @BindView(R.id.tv_sender_name)
    TextView tvSenderName;
    @BindView(R.id.tv_sender_address)
    TextView tvSenderAddress;
    @BindView(R.id.linear_current_location)
    LinearLayout linearCurrentLocation;
    @BindView(R.id.tv_receiver_name)
    TextView tvReceiverName;
    @BindView(R.id.tv_receiver_address)
    TextView tvReceiverAddress;
    @BindView(R.id.linear_destination_location)
    LinearLayout linearDestinationLocation;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_destination, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
