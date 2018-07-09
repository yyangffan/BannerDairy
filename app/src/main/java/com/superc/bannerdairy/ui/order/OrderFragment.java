package com.superc.bannerdairy.ui.order;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseFragment;
import com.superc.bannerdairy.constant.MessageEvent;
import com.superc.bannerdairy.databinding.ActivityOrderFragmentBinding;
import com.superc.bannerdairy.lock.OrderFramentLock;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@SuppressLint("ValidFragment")
public class OrderFragment extends BaseFragment {
    private ActivityOrderFragmentBinding mBinding;
    private OrderFramentLock mLock;
    private int a = 1;

    public OrderFragment() {
    }

    public OrderFragment(int i) {
        super();
        this.a = i;

    }

    @Override
    protected View initBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_order_fragment, container, false);
        Bundle bundle = new Bundle();
        bundle.putInt("flag", a);
        mLock = new OrderFramentLock(mActivity, mBinding, bundle);
        mBinding.setOrderFramentLock(mLock);
        EventBus.getDefault().register(this);
        return mBinding.getRoot();
    }

    @Override
    protected void init() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleMsg(MessageEvent msg) {
        switch (msg.getMessage()) {
            case "finish":
//                mLock.httpOrder();
//                mLock.resetData();

                break;
        }
    }
/*重新回到该界面时重设数据*/
    public void getDataAgin(int pa) {
        mLock.httpOrder(pa);
        mLock.resetData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
