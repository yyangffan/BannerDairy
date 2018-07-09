package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;

import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.databinding.FragmentMyInviteBinding;

/**
 * Created by Amorr on 2017/11/22.
 * 我的邀请界面
 */

public class MyInviteFramentLock extends BaseLock<FragmentMyInviteBinding> {
    public MyInviteFramentLock(Context context, FragmentMyInviteBinding binding) {
        super(context, binding);
    }

    public MyInviteFramentLock(Context context, FragmentMyInviteBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {

    }
}
