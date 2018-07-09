package com.superc.bannerdairy.ui.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.constant.MessageEvent;
import com.superc.bannerdairy.databinding.ActivityAllGoodsBinding;
import com.superc.bannerdairy.lock.AllGoodsLock;
import com.superc.bannerdairy.ui.order.OrderManageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 创建日期：2017/11/9 on 18:05
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class AllGoodsActivity extends BaseActivity {

    private ActivityAllGoodsBinding mBinding;
    private AllGoodsLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_all_goods);
        mLock = new AllGoodsLock(this, mBinding, mBundle);
        mBinding.setAllGoodsLock(mLock);
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleMessage(MessageEvent msg) {
        Bundle bundle = new Bundle();
        switch (msg.getMessage()) {
            case "finish":
                bundle.putString("what", "1");
                startActivity(OrderManageActivity.class, bundle);
//                Intent intent = new Intent(this, OrderManageActivity.class);
//                startActivity(intent);
                this.finish();
                break;
            case "cancel":
                bundle.putString("what", "0");
                startActivity(OrderManageActivity.class, bundle);
                break;
        }
    }

}
