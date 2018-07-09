package com.superc.bannerdairy.base;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import com.superc.cframework.base.ui.CBaseLock;

/**
 * 创建日期：2017/11/3 on 11:28
 * 描述：BaseLock
 * 作者：郭士超
 * QQ：1169380200
 */

public abstract class BaseLock<B extends ViewDataBinding> extends CBaseLock<B> {

    public BaseLock(Context context, B binding) {
        super(context, binding);
    }

    public BaseLock(Context context, B binding, Bundle bundle) {
        super(context, binding, bundle);
    }

}
