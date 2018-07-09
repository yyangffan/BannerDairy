package com.superc.bannerdairy.lock;

import android.content.Context;

import com.superc.bannerdairy.base.BaseLock;
import com.superc.cframework.base.ui.CBaseLock;
import com.superc.bannerdairy.databinding.FragmentDemoBinding;
import com.superc.bannerdairy.model.Demo;

/**
 * 创建日期：2017/10/31 on 14:24
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class DemLock extends BaseLock<FragmentDemoBinding> {

    private Demo mDemo;

    public DemLock(Context context, FragmentDemoBinding binding) {
        super(context, binding);
    }

    @Override
    protected void init() {
        mDemo = new Demo("Super丶C", "20");
    }

    public Demo getDemo() {
        return mDemo;
    }
}
