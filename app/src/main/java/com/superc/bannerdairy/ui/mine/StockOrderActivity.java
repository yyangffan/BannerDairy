package com.superc.bannerdairy.ui.mine;

import android.content.Intent;
import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityStockOrderBinding;
import com.superc.bannerdairy.lock.StockOrderLock;

/**
 * 库存的确认订单界面
 */
public class StockOrderActivity extends BaseActivity {
    private ActivityStockOrderBinding mBinding;
    private StockOrderLock mStockOrderLock;

    @Override
    protected void initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_stock_order);
        mStockOrderLock = new StockOrderLock(this,mBinding,mBundle);
        mBinding.setStockOrderLock(mStockOrderLock);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mStockOrderLock.httpAddress();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==110){
                if(data!=null) {
                    mBinding.tvName.setText(data.getStringExtra("name_mob"));
                    mBinding.tvAddress.setText(data.getStringExtra("position"));
                    mStockOrderLock.user_address_id=data.getStringExtra("posid");
                    mStockOrderLock.express_fee_e=data.getStringExtra("express_fee_e");
                    mStockOrderLock.express_fee_num=data.getStringExtra("expree_fee");
                    mStockOrderLock.pospos=data.getIntExtra("pospos",-1);
                    mStockOrderLock.showPriceAgin();
                }
            }
        }
    }


}
