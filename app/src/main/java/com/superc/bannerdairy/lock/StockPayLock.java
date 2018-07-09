package com.superc.bannerdairy.lock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.constant.MessageEvent;
import com.superc.bannerdairy.databinding.ActivityStockPayBinding;
import com.superc.bannerdairy.service.BlackEditText;
import com.superc.bannerdairy.ui.mine.SetPayPasActivity;
import com.superc.bannerdairy.ui.mine.StockPayActivity;
import com.superc.bannerdairy.ui.order.PayOrderActivity;
import com.superc.bannerdairy.ui.order.PayResult;
import com.superc.bannerdairy.widget.MD5Utils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 库存支付界面
 */

public class StockPayLock extends BaseLock<ActivityStockPayBinding> {
    private String orderId;
    private String price = "0";
    private String password = "";
    private String mMoney;
    private String mYue;
    public static final int SDK_PAY_FLAG = 110;
    private int isWhat=0;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new MessageEvent("finish"));
                    } else if (TextUtils.equals(resultStatus, "6001")) {
                        Toast.makeText(mContext, "用户取消", Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new MessageEvent("cancel"));
                    } else {
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                        Log.e("pay", "支付结果失败--->" + resultInfo);
                    }
                    break;
                }
            }
        }

        ;
    };

    public StockPayLock(Context context, ActivityStockPayBinding binding) {
        super(context, binding);
    }

    public StockPayLock(Context context, ActivityStockPayBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock mAppBarLock = new AppBarLock(mContext, R.string.order_pay);
        mAppBarLock.barData.isLeft = true;
        mAppBarLock.barData.titleLeft = "返回";
        mAppBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(mAppBarLock);
        getUserMsg();/*主要用来获取余额*/
//        mBinding.weChatPay.setChecked(true);
        if (mBundle.getString("expree") != null) {//带过来的邮费
            mMoney = mBundle.getString("expree");
        }
        if (mBundle.getString("orderId") != null) {//带过来的订单id
            orderId = mBundle.getString("orderId");
        }
        mBinding.payPrice.setText(mMoney == null || mMoney.equals("") ? "0.00" : mMoney);

        mBinding.llWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWhat=0;

                mBinding.payWeixin.setVisibility(View.VISIBLE);
                mBinding.payZfb.setVisibility(View.INVISIBLE);
                mBinding.payYe.setVisibility(View.INVISIBLE);
            }
        });
        mBinding.llZfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWhat=1;

                mBinding.payWeixin.setVisibility(View.INVISIBLE);
                mBinding.payZfb.setVisibility(View.VISIBLE);
                mBinding.payYe.setVisibility(View.INVISIBLE);
            }
        });
        mBinding.llYue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWhat=2;

                mBinding.payWeixin.setVisibility(View.INVISIBLE);
                mBinding.payZfb.setVisibility(View.INVISIBLE);
                mBinding.payYe.setVisibility(View.VISIBLE);
            }
        });


    }

    /*支付*/
    public void topay() {
        if (isWhat==0) {/*微信支付*/
            startPay("2", null);
        } else if (isWhat==1) {/*支付宝支付*/
            startPay("3", null);
        } else if (isWhat==2) {/*余额支付*/
                  /*获取支付密码*/
            if (MyApplication.getUser().getIs_pay_pwd().equals("0")) {
                showPasDialog();
                return;
            }
            if (Double.parseDouble(price) < Double.parseDouble(mMoney)) {
                showBuzuDialog();
                return;
            }
            showPayDialog();
        }

    }

    /*展示支付dialog*/
    public void showPayDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View dialogV = LayoutInflater.from(mContext).inflate(R.layout.dialog_pay, null);
        ImageView img_delet = dialogV.findViewById(R.id.dialog_delete);
        TextView tv_money = dialogV.findViewById(R.id.dialog_money);
        TextView tv_yue = dialogV.findViewById(R.id.dialog_tv_yue);
        final BlackEditText etPwd = dialogV.findViewById(R.id.et_pwd);
        tv_money.setText(mMoney);
        tv_yue.setText("         余额支付(" + price + ")");
        dialog.setView(dialogV);
        dialog.show();
        img_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        etPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPwd.setSelection(etPwd.getText().toString().length());
            }
        });
        /*密码输入完成后即进行付款*/
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 6) {
                    startPay("1", charSequence + "");
                    dialog.dismiss();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    /*展示余额不足以支付dialog*/
    public void showBuzuDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View dialogV = LayoutInflater.from(mContext).inflate(R.layout.dialog_collect, null);
        TextView tv_cancle = dialogV.findViewById(R.id.dialog_tv_cancle);
        TextView tv_sure = dialogV.findViewById(R.id.dialog_tv_sure);
        TextView tv_content = dialogV.findViewById(R.id.dialog_content);
        ImageView imgv = dialogV.findViewById(R.id.dialog_imgv);
        imgv.setImageResource(R.mipmap.babycry);
        tv_content.setText("当前余额不足以支付请充值");
        dialog.setView(dialogV);
        dialog.show();
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("diff", "0");
                Intent intent = new Intent(mContext, PayOrderActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    /*展示未设置密码的dialog*/
    public void showPasDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View dialogV = LayoutInflater.from(mContext).inflate(R.layout.dialog_collect, null);
        TextView tv_cancle = dialogV.findViewById(R.id.dialog_tv_cancle);
        TextView tv_sure = dialogV.findViewById(R.id.dialog_tv_sure);
        TextView tv_content = dialogV.findViewById(R.id.dialog_content);
        ImageView imgv = dialogV.findViewById(R.id.dialog_imgv);
        imgv.setImageResource(R.mipmap.babycry);
        tv_content.setText("您当前没有设置账户支付密码！");
        dialog.setView(dialogV);
        dialog.show();
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(SetPayPasActivity.class);
                dialog.dismiss();
            }
        });
    }


    //支付成功的dialog
    private Dialog initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.car_dialog, null);
        TextView success = (TextView) v.findViewById(R.id.success);
        TextView count = (TextView) v.findViewById(R.id.count);
        ImageView img = (ImageView) v.findViewById(R.id.car_img);
        img.setImageResource(R.drawable.chenggong1);
        count.setText("您已支付成功");
        builder.setView(v);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        //设置背景为透明色
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        HashMap hashMap = new HashMap();
        dialog.show();
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ((BaseActivity) mContext).finish();
                EventBus.getDefault().post(new MessageEvent("finish"));
            }
        });
        return dialog;
    }


    /*库存支付邮费*/
    public void startPay(final String type, String password) {
        if (type.equals("2") || type.equals("3")) {
            ((StockPayActivity) mContext).showLoadPop();
        }
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ORDERPAY, RequestMethod.POST);
        request.add("user_id", MyApplication.getUser().getUser_id());
        request.add("order_id", orderId);
        if (password != null && !password.equals("")) {
            request.add("pay_password", MD5Utils.md5Password(password));
        }
        request.add("pay_type", type);

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
                        if (type.equals("3")) {
                            String code = jsonObject.getString("code");
                            if (code.equals("200")) {
                                final String orderInfo = jsonObject.getString("data");
                                Runnable payRunnable =
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                PayTask alipay = new PayTask((Activity) mContext);
                                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                                Message msg = new Message();
                                                msg.what = SDK_PAY_FLAG;
                                                msg.obj = result;
                                                mHandler.sendMessage(msg);
                                            }
                                        };
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            }
                        } else {
                            success = jsonObject.getBoolean("success");
                            show = jsonObject.getBoolean("show");
                            if (success) {
                                switch (type) {
                                    case "1":
                                        initDialog();
                                        break;
                                    case "2":
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        PayReq req = new PayReq();
                                        req.appId = data.getString("appid");
                                        req.partnerId = data.getString("partnerid");
                                        req.prepayId = data.getString("prepayid");
                                        req.nonceStr = data.getString("noncestr");
                                        req.timeStamp = data.getString("timestamp");
                                        req.packageValue = data.getString("package");
                                        req.sign = data.getString("sign");
                                        MyApplication.getInstance().getApi().sendReq(req);
                                        break;
                                }

                            }
                            if (show) {
                                showToast(jsonObject.getString("msg"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                showToast("网络异常");
                ((StockPayActivity) mContext).hideLoadPop();
            }

            @Override
            public void onFinish(int what) {
                ((StockPayActivity) mContext).hideLoadPop();
            }
        });

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
                            mYue = data.getString("money");
                            price = data.getString("money");
                            mBinding.tvYue.setText("         余额支付(" + mYue + ")");
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
