package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivitySettingBinding;
import com.superc.bannerdairy.lock.SettingLock;


public class SettingActivity extends BaseActivity {


    private ActivitySettingBinding mBinding;
    private SettingLock mSettingLock;

    @Override
    protected void initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting);
        mSettingLock=new SettingLock(this,mBinding);
        mBinding.setSetLock(mSettingLock);
    }

}
