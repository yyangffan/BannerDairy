package com.superc.bannerdairy.ui.login;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityForgetPasswordBinding;
import com.superc.bannerdairy.lock.AppBarLock;
import com.superc.bannerdairy.lock.ForgetPasswordLock;

/**
 * 创建日期：2017/11/6 on 17:55
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class ForgetPasswordActivity extends BaseActivity {

    private ActivityForgetPasswordBinding mBinding;
    private ForgetPasswordLock mLock;

    @Override
    protected void initBinding() {

        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        mLock = new ForgetPasswordLock(this, mBinding);
        mBinding.setForgetPasswordLock(mLock);


    }

    @Override
    protected void init() {
        AppBarLock appBarLock=new AppBarLock(getContext(), R.string.forget_title);
        appBarLock.barData.isLeft=true;
        appBarLock.barData.titleLeft="返回";
        appBarLock.barData.imsLeft=getContext().getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
    }


}