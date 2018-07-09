package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;
import android.support.annotation.MainThread;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.constant.MessageEvent;
import com.superc.bannerdairy.databinding.ActivityStockPayBinding;
import com.superc.bannerdairy.lock.StockPayLock;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 库存支付界面
 */
public class StockPayActivity extends BaseActivity {

    private ActivityStockPayBinding mBinding;
    private StockPayLock mLock;

    @Override
    protected void initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_stock_pay);
        mLock = new StockPayLock(this, mBinding, mBundle);
        mBinding.setStockPayLock(mLock);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleMessage(MessageEvent msg) {
        switch (msg.getMessage()) {
            case "finish":
                this.finish();
                break;
        }

    }

}
