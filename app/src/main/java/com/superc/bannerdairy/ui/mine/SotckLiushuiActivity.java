package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivitySotckLiushuiBinding;
import com.superc.bannerdairy.lock.StockLiushuiLock;

public class SotckLiushuiActivity extends BaseActivity {

    private ActivitySotckLiushuiBinding mBinding;
    private StockLiushuiLock mLock;

    @Override
    protected void initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sotck_liushui);
        mLock=new StockLiushuiLock(this,mBinding);
        mBinding.setLock(mLock);

    }
}
