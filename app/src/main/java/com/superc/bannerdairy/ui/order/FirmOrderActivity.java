package com.superc.bannerdairy.ui.order;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityFirmOrderBinding;
import com.superc.bannerdairy.lock.FirmOrderLock;
//确认订单界面，选择地址

public class FirmOrderActivity extends BaseActivity {
    private ActivityFirmOrderBinding mBinding;
    private FirmOrderLock mLock;


    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_firm_order);
        Bundle bundle =getIntent().getExtras();
        mLock = new FirmOrderLock(this, mBinding,bundle);
        mBinding.setFirmOrderLock(mLock);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLock.httpAddress();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==110){
                if(data!=null) {
                    mBinding.tvName.setText(data.getStringExtra("name_mob"));
                    mBinding.tvAddress.setText(data.getStringExtra("position"));
                    mLock.user_address_id=data.getStringExtra("posid");
                    mLock.express_fee_e=data.getStringExtra("express_fee_e");
                    mLock.express_fee_num=data.getStringExtra("expree_fee");
                    mLock.pospos=data.getIntExtra("pospos",-1);
                    mLock.showPriceAgin();
                }
            }
        }

    }
}
