package com.superc.bannerdairy.ui.manage;


import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivitySonLevelBinding;
import com.superc.bannerdairy.lock.SonLevelLock;

//子代理，i县级a代理，门店代理，特约代理，零售代理
public class SonLevelActivity extends BaseActivity {


    private ActivitySonLevelBinding mBinding;
    private SonLevelLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_son_level);
        Bundle bundle =getIntent().getExtras();
        mLock = new SonLevelLock(this, mBinding,bundle);
        mBinding.setSonLevelLock(mLock);

    }
}
