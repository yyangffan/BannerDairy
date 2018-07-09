package com.superc.bannerdairy.lock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityWalletBinding;
import com.superc.bannerdairy.ui.mine.SetPayPasActivity;
import com.superc.bannerdairy.ui.order.BuyRecordActivity;
import com.superc.bannerdairy.ui.order.PayOrderActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Amorr on 2017/11/18.
 * 我的钱包界面
 */

public class WalletLock extends BaseLock<ActivityWalletBinding> {

    private String mMoney;
    private String mTx_money;

    public WalletLock(Context context, ActivityWalletBinding binding) {
        super(context, binding);
    }

    public WalletLock(Context context, ActivityWalletBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.my_wallet);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
        if(mBundle!=null){
            mMoney = mBundle.getString("mMoney");
            if(mMoney ==null|| mMoney.equals("")) {
                mBinding.walletMoney.setText("账户余额\n0.00");
            }else {
                mBinding.walletMoney.setText("账户余额\n" + mMoney);
            }
            String txMoney = mBundle.getString("txMoney");
            if(txMoney==null||txMoney.equals("")) {
                mBinding.walletTxMoney.setText("提现金额\n0.00");
            }else {
                mBinding.walletTxMoney.setText("提现金额\n" + txMoney);
            }
        }else {
            mBinding.walletTxMoney.setText("提现金额\n0.00");
            mBinding.walletMoney.setText("账户余额\n0.00");
        }
    }
/*交易记录*/
    public void pay() {
        // TODO: 2017/12/15  需要修改Bundle
        Intent intent=new Intent(mContext,BuyRecordActivity.class);
        mContext.startActivity(intent);
    }
/*在线充值*/
    public void payOnline() {
        Bundle bundle=new Bundle();
        bundle.putString("diff","0");
        bundle.putString("money",mMoney);
        Intent intent=new Intent(mContext,PayOrderActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
/*提现*/
    public void payTixian() {
        Bundle bundle=new Bundle();
        bundle.putString("diff","1");
        bundle.putString("money",mMoney);
        Intent intent=new Intent(mContext,PayOrderActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }
/*支付密码*/
    public void setPass() {
        startActivity(SetPayPasActivity.class, new Bundle());
    }

    /*获取用户数据*/
    public void getUserMsg() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LOOKUSERINFO, RequestMethod.GET);
        request.add("act", "user");
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }

        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            mTx_money = data.getString("tx_money");
                            mMoney = data.getString("money");
                            if(mMoney ==null|| mMoney.equals("")) {
                                mBinding.walletMoney.setText("账户余额\n0.00");
                            }else {
                                mBinding.walletMoney.setText("账户余额\n" + mMoney);
                            }
                            if(mTx_money==null||mTx_money.equals("")) {
                                mBinding.walletTxMoney.setText("提现金额\n0.00");
                            }else {
                                mBinding.walletTxMoney.setText("提现金额\n" + mTx_money);
                            }
                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });


    }

}
