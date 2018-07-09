package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityInviteBinding;
import com.superc.bannerdairy.lock.InviteLock;

//我的邀请界面
public class InviteActivity extends BaseActivity {

    private ActivityInviteBinding mBinding;
    private InviteLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_invite);
        mLock = new InviteLock(this, mBinding);
        mBinding.setInviteLock(mLock);

    }
}
