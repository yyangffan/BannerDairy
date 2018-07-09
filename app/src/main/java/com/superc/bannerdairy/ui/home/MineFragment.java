package com.superc.bannerdairy.ui.home;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseFragment;
import com.superc.bannerdairy.databinding.FragmentMineBinding;
import com.superc.bannerdairy.lock.MineLock;

/**
 * 创建日期：2017/11/9 on 9:47
 * 描述：
 */

public class MineFragment extends BaseFragment {

    private FragmentMineBinding mBinding;
    private MineLock mLock;

    @Override
    protected View initBinding(LayoutInflater inflater, ViewGroup container) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mine, container, false);
        mLock = new MineLock(mActivity, mBinding);
        mBinding.setMineLock(mLock);
        return mBinding.getRoot();
    }

    @Override
    protected void init() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mLock!=null) {
            mLock.setMsg();
        }
    }
}
