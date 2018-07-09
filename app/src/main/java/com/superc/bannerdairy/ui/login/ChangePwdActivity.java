package com.superc.bannerdairy.ui.login;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityChangePwdBinding;
import com.superc.bannerdairy.lock.AppBarLock;
import com.superc.bannerdairy.lock.ChangPwdLock;

/********************************************************************
  @version: 1.0.0
  @description: 修改密码
  @author: user
  @time: 2018/5/4 16:40
  @变更历史:
********************************************************************/
public class ChangePwdActivity extends BaseActivity {

    private ActivityChangePwdBinding mBinding;
    private ChangPwdLock mChangPwdLock;


    @Override
    protected void initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_pwd);
        mChangPwdLock=new ChangPwdLock(this,mBinding);
        mBinding.setLock(mChangPwdLock);

    }

    @Override
    protected void init() {
        AppBarLock appBarLock=new AppBarLock(getContext(), "修改密码");
        appBarLock.barData.isLeft=true;
        appBarLock.barData.titleLeft="返回";
        appBarLock.barData.imsLeft=getContext().getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
    }

}
