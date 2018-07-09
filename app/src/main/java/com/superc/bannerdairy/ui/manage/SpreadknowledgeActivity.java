package com.superc.bannerdairy.ui.manage;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivitySpreadknowledgeBinding;
import com.superc.bannerdairy.lock.KnowledgeLock;

//传播知识界面
public class SpreadknowledgeActivity extends BaseActivity {
    private ActivitySpreadknowledgeBinding mBinding;
    private KnowledgeLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_spreadknowledge);
        mLock = new KnowledgeLock(this, mBinding,mBundle);
        mBinding.setKnowledgeLock(mLock);


    }
}
