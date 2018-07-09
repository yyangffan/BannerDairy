package com.superc.bannerdairy.ui.demo;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityDemoBinding;
import com.superc.bannerdairy.lock.DemoLock;

/**
 * 创建日期：2017/10/30 on 13:55
 * 描述：一个Demo
 * 作者：郭士超
 * QQ：1169380200
 */

public class DemoActivity extends BaseActivity {

    private ActivityDemoBinding mBinding;
    private DemoLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_demo);
        mLock = new DemoLock(this, mBinding);
        mBinding.setDemoLock(mLock);

    }

    @Override
    protected void init() {

    }


}
