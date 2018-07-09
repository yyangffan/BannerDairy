package com.superc.bannerdairy.ui.home;


import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseFragment;
import com.superc.bannerdairy.databinding.FragmentFashionSellerBinding;
import com.superc.bannerdairy.lock.FashionLock;

//买家秀
public class FashionSellerFragment extends BaseFragment {
    private FragmentFashionSellerBinding mBinding;
    private FashionLock mLock;

    @Override
    protected View initBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_fashion_seller, container, false);
        mLock = new FashionLock(mActivity, mBinding);
        mBinding.setAllFashin(mLock);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mLock!=null) {
            mLock.httpKnow();
        }
    }

    @Override
    protected void init() {
    }
}
