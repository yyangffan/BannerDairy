package com.superc.bannerdairy.ui.order;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Amorr on 2017/11/16.
 * 我的订单列表的adapter
 * 我的推广列表adapter通用
 */

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private String[] tabTitles;
    private List<Fragment> fragments;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, String[] tabTitles) {
        super(fm);
        this.fragments = fragments;
        this.tabTitles = tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
