package com.superc.bannerdairy.ui.home;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseFragment;
import com.superc.bannerdairy.databinding.FragmentHomeBinding;
import com.superc.bannerdairy.lock.HomeLock;

/**
 * 创建日期：2017/11/7 on 15:52
 * 描述：
 */

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding mBinding;
    private HomeLock mLock;

    @Override
    protected View initBinding(LayoutInflater inflater, ViewGroup container) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        mLock = new HomeLock(mActivity, mBinding);
        mBinding.setHomeLock(mLock);

        return mBinding.getRoot();
    }

    @Override
    protected void init() {
        
    }

    public void refreshData() {
        mLock.httpGoods();
    }

}
