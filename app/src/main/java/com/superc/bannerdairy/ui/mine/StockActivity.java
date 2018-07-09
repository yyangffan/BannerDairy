package com.superc.bannerdairy.ui.mine;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.constant.MessageEvent;
import com.superc.bannerdairy.databinding.ActivityStockBinding;
import com.superc.bannerdairy.lock.StockLock;
import com.superc.bannerdairy.ui.order.OrderManageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 库存界面
 */
public class StockActivity extends BaseActivity {
    private StockLock mStockLock;
    private ActivityStockBinding mBinding;

    @Override
    protected void initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_stock);
        mStockLock = new StockLock(this, mBinding);
        mBinding.setStockLock(mStockLock);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mStockLock.initData();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleMessage(MessageEvent msg) {
        Bundle bundle = new Bundle();
        switch (msg.getMessage()) {
            case "finish":
                bundle.putString("what", "1");
                startActivity(OrderManageActivity.class, bundle);
                this.finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
