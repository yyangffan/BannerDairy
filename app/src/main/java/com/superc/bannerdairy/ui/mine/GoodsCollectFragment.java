package com.superc.bannerdairy.ui.mine;


import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseFragment;
import com.superc.bannerdairy.databinding.FragmentGoodsCollectBinding;
import com.superc.bannerdairy.lock.GoodsColLock;

/*商品收藏界面*/
public class GoodsCollectFragment extends BaseFragment {


    private FragmentGoodsCollectBinding mBinding;
    private GoodsColLock mColLock;

    @Override
    protected View initBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_goods_collect, container, false);
        mColLock = new GoodsColLock(this.getActivity(), mBinding);
        mBinding.setGoodsLock(mColLock);
        return mBinding.getRoot();
    }

    @Override
    protected void init() {

    }

    public void toGetData() {
        mColLock.init();
    }

}
