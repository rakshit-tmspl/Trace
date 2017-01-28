package com.tmspl.trace.fragment.manageorder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmspl.trace.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rakshit.sathwara on 1/25/2017.
 */

public class FragmentManageOrder extends Fragment {

    @BindView(R.id.viewpager_order)
    ViewPager viewpagerOrder;
    @BindView(R.id.tab_manage_order)
    TabLayout tabManageOrder;

    private Context context;

    public static FragmentManageOrder newInstance(int index, Context context) {
        FragmentManageOrder fragmentManageOrder = new FragmentManageOrder();
        fragmentManageOrder.context = context;
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragmentManageOrder.setArguments(b);
        return fragmentManageOrder;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_order, container, false);
        ButterKnife.bind(this, view);

        tabManageOrder.setupWithViewPager(viewpagerOrder);

        setupViewPager(viewpagerOrder);


        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentManageOrder.ViewPagerAdapter adapter = new FragmentManageOrder.ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new FragmentPendingOrders(), "Pending Orders");
        adapter.addFrag(new FragmentOnGoingOrders(), "On Going Orders");
        adapter.addFrag(new FragmentCompleteOrders(), "Completed Orders");
        viewPager.setAdapter(adapter);
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
