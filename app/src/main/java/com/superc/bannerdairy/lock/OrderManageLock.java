package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.databinding.ActivityOrderManageBinding;
import com.superc.bannerdairy.ui.order.MyFragmentPagerAdapter;
import com.superc.bannerdairy.ui.order.OrderFragment;
import com.superc.bannerdairy.ui.order.OrderManageActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amorr on 2017/11/16.
 */

public class OrderManageLock extends BaseLock<ActivityOrderManageBinding> {
    private String[] titles = {"待付款", "待审核", "待发货", "已发货"};
    private List<Fragment> fragments;
    private int page = 0;
    private MyFragmentPagerAdapter adapter;
    private OrderFragment mE;
    private OrderFragment mE1;
    private OrderFragment mE2;
    private OrderFragment mE3;
    private String mWhat="";

    public OrderManageLock(Context context, ActivityOrderManageBinding binding) {
        super(context, binding);
    }

    public OrderManageLock(Context context, ActivityOrderManageBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }


    @Override
    protected void init() {
        if(mBundle!=null) {
            mWhat = mBundle.getString("what");
            page=Integer.parseInt(mWhat==null||mWhat.equals("")?"0":mWhat);
        }
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.order_title);
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        mBinding.titleBar.setAppBarLock(appBarLock);

        initData();


    }

    private void initData() {
        fragments = new ArrayList<>();
        //0：未付款1：待自提2已提货）
        mE = new OrderFragment(1);
        mE1 = new OrderFragment(2);
        mE2 = new OrderFragment(3);
        mE3 = new OrderFragment(4);
        fragments.add(mE);
        fragments.add(mE1);
        fragments.add(mE2);
        fragments.add(mE3);
        titles = new String[]{"待付款", "待审核", "待发货", "已发货"};
        adapter = new MyFragmentPagerAdapter(((OrderManageActivity) mContext).getSupportFragmentManager(), fragments, titles);
        mBinding.orderViewPager.setAdapter(adapter);
        mBinding.orderViewPager.setCurrentItem(page);
        mBinding.orderViewPager.setOffscreenPageLimit(4);
        mBinding.orderTabLayout.setupWithViewPager(mBinding.orderViewPager);
        //防止点击的时候出现中间的条目
//        mBinding.orderTabLayout.setOnTabSelectedListener(new MyOnTabSelectedListener(mBinding.orderViewPager));
        mBinding.orderTabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    public void getMsgAgin() {
        mE1.getDataAgin(2);
        mE2.getDataAgin(3);
        mE3.getDataAgin(4);
    }

    public void oneGetMsgAgin() {
        mE.getDataAgin(1);
    }
}
