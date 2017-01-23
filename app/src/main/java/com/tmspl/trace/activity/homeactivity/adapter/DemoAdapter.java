package com.tmspl.trace.activity.homeactivity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tmspl.trace.R;

import java.util.List;

/**
 * Created by rakshit.sathwara on 1/19/2017.
 */

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.DemoVH> {

    Context context;
    List<String> arry;

    public DemoAdapter(final Context context, final List<String> arry) {
        this.context = context;
        this.arry = arry;
    }

    @Override
    public DemoVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_row_demo_view, parent, false);
        return new DemoVH(view);
    }


    @Override
    public void onBindViewHolder(DemoVH holder, int position) {

        String value = arry.get(position);

        holder.textView.setText(value);
    }

    @Override
    public int getItemCount() {
        return arry.size();
    }

    public class DemoVH extends RecyclerView.ViewHolder {

        TextView textView;

        public DemoVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_demo);
        }
    }
}
