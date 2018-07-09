package com.superc.bannerdairy.ui.login;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivitySignBinding;
import com.superc.bannerdairy.lock.SignLock;

/**
 * 创建日期：2017/11/6 on 17:33
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class SignActivity extends BaseActivity {

    private ActivitySignBinding mBinding;
    private SignLock mLock;

    @Override
    protected void initBinding() {

        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign);
        mLock = new SignLock(this, mBinding);
        mBinding.setSignLock(mLock);


    }

    @Override
    protected void init() {

    }


}
