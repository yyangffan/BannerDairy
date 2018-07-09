package com.superc.bannerdairy.ui.login;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityDemoBinding;
import com.superc.bannerdairy.databinding.ActivityLoginBinding;
import com.superc.bannerdairy.lock.DemoLock;
import com.superc.bannerdairy.lock.LoginLock;
import com.superc.bannerdairy.ui.home.MainActivity;

/**
 * 创建日期：2017/10/30 on 13:55
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding mBinding;
    private LoginLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mLock = new LoginLock(this, mBinding);
        mBinding.setLoginLock(mLock);

    }

    @Override
    protected void init() {

    }


}
