package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityWalletBinding;
import com.superc.bannerdairy.lock.WalletLock;
//我的钱包界面
public class WalletActivity extends BaseActivity {

    private ActivityWalletBinding mBinding;
    private WalletLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        mLock = new WalletLock(this, mBinding,mBundle);
        mBinding.setWalletLock(mLock);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLock.getUserMsg();
    }
}
