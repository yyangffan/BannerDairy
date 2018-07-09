package com.superc.bannerdairy.ui.order;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityWlBinding;
import com.superc.bannerdairy.lock.WlLock;


public class WlActivity extends BaseActivity {
    private ActivityWlBinding mWlBinding;
    private WlLock mWlLock;


    @Override
    protected void initBinding() {
        mWlBinding=DataBindingUtil.setContentView(this,R.layout.activity_wl);
        mWlLock=new WlLock(this,mWlBinding,mBundle);
        mWlBinding.setLock(mWlLock);

    }
}
