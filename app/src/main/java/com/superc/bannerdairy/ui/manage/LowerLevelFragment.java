package com.superc.bannerdairy.ui.manage;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseFragment;
import com.superc.bannerdairy.databinding.FragmentLevelLowerBinding;
import com.superc.bannerdairy.lock.LevelLock;

//下级推广界面
public class LowerLevelFragment extends BaseFragment {
    private FragmentLevelLowerBinding mBinding;
    private LevelLock mLock;

    @Override
    protected View initBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_level_lower, container, false);
        mLock = new LevelLock(mActivity, mBinding);
        mBinding.setLevelLock(mLock);
        return mBinding.getRoot();
    }

    @Override
    protected void init() {

    }
}