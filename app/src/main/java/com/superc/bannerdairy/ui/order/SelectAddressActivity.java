package com.superc.bannerdairy.ui.order;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivitySelectAddressBinding;
import com.superc.bannerdairy.lock.SelectAddLock;
//订单选择地址界面
public class SelectAddressActivity extends BaseActivity {
    private ActivitySelectAddressBinding mBinding;
    private SelectAddLock mLock;


    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_address);
        mLock = new SelectAddLock(this, mBinding,mBundle);
        mBinding.setSelectAddLock(mLock);
    }


}
