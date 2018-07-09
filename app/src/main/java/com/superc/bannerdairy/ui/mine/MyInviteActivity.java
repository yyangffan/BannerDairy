package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Activity;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityMyInviteBinding;
import com.superc.bannerdairy.databinding.ActivityMyTeamBinding;
import com.superc.bannerdairy.lock.MyInviteLock;
import com.superc.bannerdairy.lock.MyTeamLock;

//我的邀请
public class MyInviteActivity extends BaseActivity {
    private ActivityMyInviteBinding mBinding;
    private MyInviteLock mLock;

    @Override
    protected void initBinding() {
// 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_invite);
        mLock = new MyInviteLock(this, mBinding);
        mBinding.setMyInviteLock(mLock);
    }
}