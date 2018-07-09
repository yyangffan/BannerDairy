package com.superc.bannerdairy.ui.order;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityBuyRecordBinding;
import com.superc.bannerdairy.lock.BuyRecordLock;

public class BuyRecordActivity extends BaseActivity {

    private ActivityBuyRecordBinding mBinding;
    private BuyRecordLock mBuyRecordLock;


    @Override
    protected void initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_buy_record);
        mBuyRecordLock=new BuyRecordLock(mBinding,this);
        mBinding.setBuyRecordLock(mBuyRecordLock);
    }
}
