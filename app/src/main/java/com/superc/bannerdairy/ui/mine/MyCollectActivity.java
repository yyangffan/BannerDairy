package com.superc.bannerdairy.ui.mine;


import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityMyCollectBinding;
import com.superc.bannerdairy.lock.CollectLock;

//我的收藏界面

public class MyCollectActivity extends BaseActivity {
    private ActivityMyCollectBinding mBinding;
    private CollectLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_collect);
        mLock = new CollectLock(this, mBinding);
        mBinding.setCollectLock(mLock);


    }
}
