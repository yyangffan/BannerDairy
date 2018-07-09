package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;

import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.databinding.FragmentFlatLevelBinding;

/**
 * Created by Amorr on 2017/11/18.
 * 平级推广
 */

public class FlatFramentLock extends BaseLock<FragmentFlatLevelBinding>{
    public FlatFramentLock(Context context, FragmentFlatLevelBinding binding) {
        super(context, binding);
    }

    public FlatFramentLock(Context context, FragmentFlatLevelBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {

    }
}
