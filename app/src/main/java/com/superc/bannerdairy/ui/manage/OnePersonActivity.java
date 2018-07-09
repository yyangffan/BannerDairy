package com.superc.bannerdairy.ui.manage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityOnePersonBinding;
import com.superc.bannerdairy.lock.OnePersonLock;

//县级代理里面的详情页面
public class OnePersonActivity extends BaseActivity {

    private ActivityOnePersonBinding mBinding;
    private OnePersonLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_one_person);
        Bundle bundle =getIntent().getExtras();

        mLock = new OnePersonLock(this, mBinding,bundle);
        mBinding.setOnePersonLock(mLock);

    }

}
