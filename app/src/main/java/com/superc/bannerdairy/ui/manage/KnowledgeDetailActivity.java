package com.superc.bannerdairy.ui.manage;

import android.app.Activity;
import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityKnowledgeDetailBinding;
import com.superc.bannerdairy.lock.KnowledgeDetailLock;

//传播知识详情界面

public class KnowledgeDetailActivity extends BaseActivity {
    private ActivityKnowledgeDetailBinding mBinding;
    private KnowledgeDetailLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_knowledge_detail);
        mLock = new KnowledgeDetailLock(this, mBinding,mBundle);
        mBinding.setKnowledgeDetailLock(mLock);


    }


}
