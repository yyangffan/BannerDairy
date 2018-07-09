package com.superc.bannerdairy.ui.order;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.constant.MessageEvent;
import com.superc.bannerdairy.databinding.ActivityOrderManageBinding;
import com.superc.bannerdairy.lock.OrderManageLock;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

// 订单管理
public class OrderManageActivity extends BaseActivity {
    private ActivityOrderManageBinding mBinding;
    private OrderManageLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_manage);
        mLock = new OrderManageLock(this, mBinding,mBundle);
        mBinding.setOrderManageLock(mLock);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLock.oneGetMsgAgin();
//        mLock.getMsgAgin();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleMsg(MessageEvent msg) {
        switch (msg.getMessage()) {
            case "finish":
                mLock.oneGetMsgAgin();
                break;
        }
    }


}
