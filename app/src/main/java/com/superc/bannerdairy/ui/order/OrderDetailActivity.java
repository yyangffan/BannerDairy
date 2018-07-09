package com.superc.bannerdairy.ui.order;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityOrderDetailBinding;
import com.superc.bannerdairy.databinding.ActivityPayOrderBinding;
import com.superc.bannerdairy.lock.OrderdetailLock;
import com.superc.bannerdairy.lock.PayOrderLock;
//我的订单详情页面
public class OrderDetailActivity extends BaseActivity{
    private ActivityOrderDetailBinding mBinding;
    private OrderdetailLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        Bundle bundle =getIntent().getExtras();
        mLock = new OrderdetailLock(this, mBinding,bundle);
        mBinding.setOrderdetailLock(mLock);

    }
}
