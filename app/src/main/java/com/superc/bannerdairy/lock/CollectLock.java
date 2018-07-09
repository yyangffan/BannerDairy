package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.androidkun.xtablayout.XTabLayout;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.databinding.ActivityMyCollectBinding;
import com.superc.bannerdairy.ui.mine.BuyShowFragment;
import com.superc.bannerdairy.ui.mine.GoodsCollectFragment;
import com.superc.bannerdairy.ui.mine.MyCollectActivity;
import com.superc.bannerdairy.ui.order.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amorr on 2017/11/20.
 */

public class CollectLock extends BaseLock<ActivityMyCollectBinding> {

    private String[] titles = null;
    private List<Fragment> fragments;
    private int page = 0;
    private MyFragmentPagerAdapter adapter;
    private BuyShowFragment mE;
    private GoodsCollectFragment mE1;


    public CollectLock(Context context, ActivityMyCollectBinding binding) {
        super(context, binding);
    }

    public CollectLock(Context context, ActivityMyCollectBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.my_collect);
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        mBinding.titleBar.setAppBarLock(appBarLock);


        fragments = new ArrayList<>();
        mE1 = new GoodsCollectFragment();
        fragments.add(mE1);//商品收藏
        mE = new BuyShowFragment();
        fragments.add(mE);//买家秀收藏
        titles = new String[]{"商品收藏", "买家秀收藏"};
        adapter = new MyFragmentPagerAdapter(((MyCollectActivity) mContext).getSupportFragmentManager(), fragments, titles);
        mBinding.viewpager.setAdapter(adapter);
        mBinding.viewpager.setCurrentItem(page);
        mBinding.viewpager.setOffscreenPageLimit(4);
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
        //防止点击的时候出现中间的条目
//        mBinding.orderTabLayout.setOnTabSelectedListener(new MyOnTabSelectedListener(mBinding.orderViewPager));
        mBinding.tabs.setTabMode(TabLayout.MODE_FIXED);
        mBinding.tabs.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
               /*由于点进来的时候就会加载(因为在调用接口的时候会有提示)故此方法*/
                if(tab.getPosition()==1) {
                    mE.toGetData();
                    mBinding.viewpager.setCurrentItem(1);
                }else {
                    mE1.toGetData();
                    mBinding.viewpager.setCurrentItem(0);
                }
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });

    }
}
