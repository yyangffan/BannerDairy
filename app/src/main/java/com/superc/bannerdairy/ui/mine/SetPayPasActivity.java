package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivitySetPayPasBinding;
import com.superc.bannerdairy.lock.PayPasLock;


public class SetPayPasActivity extends BaseActivity {

    private ActivitySetPayPasBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_set_pay_pas);
        mBinding.setPayLock(new PayPasLock(this,mBinding,mBundle));
    }

    @Override
    protected void initBinding() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
