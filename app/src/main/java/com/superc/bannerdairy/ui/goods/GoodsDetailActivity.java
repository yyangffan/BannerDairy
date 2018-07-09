package com.superc.bannerdairy.ui.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.constant.MessageEvent;
import com.superc.bannerdairy.databinding.ActivityGoodsDetailBinding;
import com.superc.bannerdairy.lock.GoodsDetailLock;
import com.superc.bannerdairy.ui.order.OrderManageActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 创建日期：2017/11/10 on 15:06
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 * 商品详情的界面
 */

public class GoodsDetailActivity extends BaseActivity {

    private ActivityGoodsDetailBinding mBinding;
    private GoodsDetailLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_goods_detail);
        mLock = new GoodsDetailLock(this, mBinding);
        mBinding.setGoodsDetailLock(mLock);
        EventBus.getDefault().register(this);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLock.httpGoods();
    }
}
