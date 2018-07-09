package com.superc.bannerdairy.ui.mine;


import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseFragment;
import com.superc.bannerdairy.databinding.FragmentBuyShowBinding;
import com.superc.bannerdairy.lock.BuyShowLock;

/*买家秀收藏界面*/
public class BuyShowFragment extends BaseFragment {


    private FragmentBuyShowBinding mBinging;
    private BuyShowLock mBuyShowLock;

    @Override
    protected View initBinding(LayoutInflater inflater, ViewGroup container) {
        mBinging = DataBindingUtil.inflate(inflater, R.layout.fragment_buy_show, container, false);
        mBuyShowLock = new BuyShowLock(this.getActivity(), mBinging);
        mBinging.setBuyshowLock(mBuyShowLock);
        return mBinging.getRoot();
    }

    @Override
    protected void init() {

    }
    /*由于点进来的时候就会加载(访问接口结束时会有提示)故此方法*/
    public void toGetData() {
        mBuyShowLock.init();
    }
}
