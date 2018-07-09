package com.superc.bannerdairy.ui.goods;

import android.databinding.DataBindingUtil;

import com.mob.tools.utils.SharePrefrenceHelper;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityShoppingCartBinding;
import com.superc.bannerdairy.lock.ShoppCartLock;
import com.superc.cframework.utils.SPUtil;
//购物车界面

public class ShoppingCartActivity extends BaseActivity {

    private ActivityShoppingCartBinding mBinding;
    private ShoppCartLock mLock;


    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_shopping_cart);
        mLock = new ShoppCartLock(this, mBinding);
        mBinding.setShoppCartLock(mLock);
        /*这个参数是用来区分购物车的复选框--因为不同种类的奶粉是不可以同时下单的*/
        SPUtil.put(this,"carname","");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLock.httpGoods();
        mLock.mAllParentPrice=0;
        mBinding.price.setText("0");
        SPUtil.put(this,"carname","");
    }
}
