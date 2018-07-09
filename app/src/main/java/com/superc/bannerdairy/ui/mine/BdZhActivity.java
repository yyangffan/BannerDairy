package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityBdZhBinding;
import com.superc.bannerdairy.lock.BdzhLock;

/*绑定账号界面*/

public class BdZhActivity extends BaseActivity {
    private BdzhLock mBdzhLock;
    private ActivityBdZhBinding mBdZhBinding;


    @Override
    protected void initBinding() {
        mBdZhBinding = DataBindingUtil.setContentView(this, R.layout.activity_bd_zh);
        mBdzhLock=new BdzhLock(this,mBdZhBinding);
        mBdZhBinding.setLock(mBdzhLock);

    }
}
