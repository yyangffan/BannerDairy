package com.superc.bannerdairy.ui.mine;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseFragment;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.FragmentFlatLevelBinding;
import com.superc.bannerdairy.databinding.FragmentMyInviteBinding;
import com.superc.bannerdairy.lock.FlatFramentLock;
import com.superc.bannerdairy.lock.MyInviteFramentLock;
import com.superc.bannerdairy.lock.MyInviteLock;
import com.superc.bannerdairy.model.LevelItem;
import com.superc.bannerdairy.model.MyInviteItem;
import com.superc.bannerdairy.model.TeamProItem;
import com.superc.cframework.base.ui.CBaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

//我的邀请的激活列表

public class MyInviteFragment extends BaseFragment {
    private FragmentMyInviteBinding mBinding;
    private MyInviteFramentLock mLock;
    private List<MyInviteItem> mGoodsList;
    private CBaseRecyclerViewAdapter mAdapter;

    @Override
    protected View initBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_invite, container, false);
        mLock = new MyInviteFramentLock(mActivity, mBinding);
        mBinding.setMyInviteFramentLock(mLock);

        return mBinding.getRoot();
    }

    @Override
    protected void init() {
        GridLayoutManager ms = new GridLayoutManager(mActivity, 1);
        mBinding.rvInvite.setLayoutManager(ms);
        mBinding.rvInvite.addItemDecoration(new RecycleViewDivider(
                mActivity, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<MyInviteItem>();

        MyInviteItem item = new MyInviteItem("", "旗帜奶粉幼儿配方奶粉", "十八段", "￥ 260", "都买了");
        mGoodsList.add(item);
        mGoodsList.add(item);
        mGoodsList.add(item);

        mAdapter = new CBaseRecyclerViewAdapter(mActivity, mGoodsList, R.layout.item_my_invite, BR.myInviteLock);
        mBinding.rvInvite.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        mAdapter.setOnItemClickListener(new CBaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                //我的邀请
            }
        });
    }
}