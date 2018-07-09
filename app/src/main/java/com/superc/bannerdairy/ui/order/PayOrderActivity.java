package com.superc.bannerdairy.ui.order;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.constant.MessageEvent;
import com.superc.bannerdairy.databinding.ActivityPayOrderBinding;
import com.superc.bannerdairy.lock.PayOrderLock;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PayOrderActivity extends BaseActivity {
//确认订单选择支付方式

    private ActivityPayOrderBinding mBinding;
    private PayOrderLock mLock;
    private String different;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pay_order);
        mLock = new PayOrderLock(this, mBinding, mBundle);
        mBinding.setPayOrderLock(mLock);
        EventBus.getDefault().register(this);
        different = mBundle.getString("diff");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBundle.getString("orderId") != null) {
            mLock.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHandleMessage(MessageEvent msg) {
        switch (msg.getMessage()){
            case "finish":
                if (different != null) {
                    if (different.equals("0")) {/*在线充值*/
                        mLock.getUserMsg();
                        mBinding.payMoney.setText("");
                        ShowRemindDialog.getInstance().showDialog(this,R.drawable.chenggong1,"充值成功",false,new OnSomeThingClickListener(){
                            @Override
                            public void OnDoSomeThingClickListener(int view_id) {/*点击确定时需要做的事情*/

                            }
                        }).setCanleGV(false);

                    } else if (different.equals("1")) {/*提现*/

                    } else if (different.equals("2")) {/*购物车支付*/
                        this.finish();
                    }
                }
                break;
            case "cancel":
                PayOrderActivity.this.finish();
                break;

        }

    }

}
