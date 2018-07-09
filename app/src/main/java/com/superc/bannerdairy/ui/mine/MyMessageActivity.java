package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityMyMessageBinding;
import com.superc.bannerdairy.lock.MyMessageLock;

/**
 * 创建日期：2017/11/9 on 16:18
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class MyMessageActivity extends BaseActivity {

    private ActivityMyMessageBinding mBinding;
    private MyMessageLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_message);
        mLock = new MyMessageLock(this, mBinding);
        mBinding.setMyMessageLock(mLock);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLock.page=1;
        mLock.getMessage();
    }
}
