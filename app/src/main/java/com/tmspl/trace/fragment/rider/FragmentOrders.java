package com.tmspl.trace.fragment.rider;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmspl.trace.R;
import com.tmspl.trace.fragment.manageorder.FragmentCompleteOrders;
import com.tmspl.trace.fragment.manageorder.FragmentOnGoingOrders;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rakshit.sathwara on 1/25/2017.
 */

public class FragmentOrders extends Fragment {

    @BindView(R.id.viewpager_orders)
    ViewPager viewpagerOrders;
    @BindView(R.id.tab_manage_orders)
    TabLayout tabManageOrders;
    private Activity context;

    public static FragmentOrders newInstance(int index, Activity context) {
        FragmentOrders fragmentMap = new FragmentOrders();
        fragmentMap.context = context;
        Bundle b = new Bundle();
        b.putInt("index", index);
        fragmentMap.setArguments(b);
        return fragmentMap;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Your Orders");
        tabManageOrders.setupWithViewPager(viewpagerOrders);

        setupViewPager(viewpagerOrders);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
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
