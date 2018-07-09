package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;

import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.databinding.ActivitySignBinding;

/**
 * 创建日期：2017/11/6 on 16:46
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class SignLock extends BaseLock<ActivitySignBinding> {

    public SignLock(Context context, ActivitySignBinding binding) {
        super(context, binding);
    }

    public SignLock(Context context, ActivitySignBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {

    }
}
