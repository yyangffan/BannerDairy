package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.databinding.ActivityTeamPerformanceBinding;
import com.superc.bannerdairy.ui.manage.TeamPerFragment;
import com.superc.bannerdairy.ui.manage.TeamPerformanceActivity;
import com.superc.bannerdairy.ui.mine.MyInviteActivity;
import com.superc.bannerdairy.ui.mine.MyInviteFragment;
import com.superc.bannerdairy.ui.order.MyFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amorr on 2017/11/22.
 * 团队业绩界面
 */

public class TeamPerLock extends BaseLock<ActivityTeamPerformanceBinding> {
    private String[] titles = { "今日", "本周","本月","上个月"};
    private List<Fragment> fragments;
    private int page=0;
    private MyFragmentPagerAdapter adapter;

    public TeamPerLock(Context context, ActivityTeamPerformanceBinding binding) {
        super(context, binding);
    }

    public TeamPerLock(Context context, ActivityTeamPerformanceBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock=new AppBarLock(mContext, R.string.team_achieve);
        appBarLock.barData.isLeft=true;
        appBarLock.barData.titleLeft="返回";
        appBarLock.barData.imsLeft=mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);


        fragments = new ArrayList<>();

        fragments.add(new TeamPerFragment(1));
        fragments.add(new TeamPerFragment(2));
        fragments.add(new TeamPerFragment(3));
        fragments.add(new TeamPerFragment(4));

        titles = new  String[]{ "今日", "本周","本月","上个月"};

        adapter = new MyFragmentPagerAdapter(((TeamPerformanceActivity) mContext).getSupportFragmentManager(), fragments,titles);
        mBinding.viewpager.setAdapter(adapter);
        mBinding.viewpager.setCurrentItem(page);
        mBinding.viewpager.setOffscreenPageLimit(4);
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
        //防止点击的时候出现中间的条目
//        mBinding.orderTabLayout.setOnTabSelectedListener(new MyOnTabSelectedListener(mBinding.orderViewPager));
        mBinding.tabs.setTabMode(TabLayout.MODE_FIXED);
    }
}
