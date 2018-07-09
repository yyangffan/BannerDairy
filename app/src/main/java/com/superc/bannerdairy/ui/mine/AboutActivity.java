package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Activity;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityAboutBinding;
import com.superc.bannerdairy.databinding.ActivityInviteBinding;
import com.superc.bannerdairy.lock.AboutLock;
import com.superc.bannerdairy.lock.InviteLock;

public class AboutActivity extends BaseActivity {
    //关于我们界面
    private ActivityAboutBinding mBinding;
    private AboutLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        mLock = new AboutLock(this, mBinding);
        mBinding.setAboutLock(mLock);


    }

}
