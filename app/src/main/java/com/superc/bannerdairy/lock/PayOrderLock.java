package com.superc.bannerdairy.lock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.constant.MessageEvent;
import com.superc.bannerdairy.databinding.ActivityPayOrderBinding;
import com.superc.bannerdairy.service.BlackEditText;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.mine.BdZhActivity;
import com.superc.bannerdairy.ui.mine.SetPayPasActivity;
import com.superc.bannerdairy.ui.order.PayOrderActivity;
import com.superc.bannerdairy.ui.order.PayResult;
import com.superc.bannerdairy.widget.MD5Utils;
import com.superc.bannerdairy.widget.PasswordInputView;
import com.superc.cframework.utils.ToastUtil;
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

import java.util.Map;

/**
 * Created by Amorr on 2017/11/16.
 */

public class PayOrderLock extends BaseLock<ActivityPayOrderBinding> {
    private String orderId;
    private String price = "0";
    private String yue = "0";
    public String different = "-1";
    private AppBarLock mAppBarLock;
    private String mMoney;
    public static final int SDK_PAY_FLAG = 110;
    private int isWhat = 0;
    private int appVersionCode;


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
//                        EventBus.getDefault().post(new MessageEvent("finish"));
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

    public PayOrderLock(Context context, ActivityPayOrderBinding binding) {
        super(context, binding);
    }

    public PayOrderLock(Context context, ActivityPayOrderBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }


    @Override
    protected void init() {
        getVersionCode();
        if (mBundle.getString("money") != null) {//在线充值时携带过来的余额
            mMoney = mBundle.getString("money");
        }
        mBinding.payPrice.setText(mMoney == null || mMoney.equals("") ? "0.00" : mMoney);

        if (mBundle.getString("orderId") != null) {//购物车那边的支付（商品支付）
            orderId = mBundle.getString("orderId");
            httpPrice();
        }
        different = mBundle.getString("diff");
        if (different != null) {
            if (different.equals("0")) {//在线充值
                mBinding.payMoney.setVisibility(View.VISIBLE);
                mBinding.payMoney.setHint("请输入充值金额");
                mAppBarLock = new AppBarLock(mContext, R.string.pay_online);
                mBinding.payorderTitle.setText("账户金额");
                mBinding.payYe.setVisibility(View.GONE);
                mBinding.llYue.setVisibility(View.GONE);

            } else if (different.equals("1")) {//提现
                mBinding.tixianNote.setVisibility(View.VISIBLE);
                mBinding.payMoney.setVisibility(View.VISIBLE);
                // TODO: 2018/4/17 先把微信和余额的隐藏掉
                mBinding.payZfb.setVisibility(View.VISIBLE);
                mBinding.llWeixin.setVisibility(View.GONE);
                mBinding.llYue.setVisibility(View.GONE);
                isWhat = 1;

                mBinding.payMoney.setHint("请输入提现金额");
                mAppBarLock = new AppBarLock(mContext, R.string.totixian);
                mBinding.payorderTitle.setText("账户金额");

            } else if (different.equals("2")) {//购物车支付
                mAppBarLock = new AppBarLock(mContext, R.string.order_pay);
                mBinding.payorderTitle.setText("支付金额");
            }
        } else {
            mAppBarLock = new AppBarLock(mContext, R.string.order_pay);
            mBinding.payorderTitle.setText("支付金额");
        }
        mAppBarLock.barData.isLeft = true;
        mAppBarLock.barData.titleLeft = "返回";
        mAppBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(mAppBarLock);

        mBinding.llWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                different = mBundle.getString("diff");
                isWhat = 0;
                mBinding.payWeixin.setVisibility(View.VISIBLE);
                mBinding.payZfb.setVisibility(View.INVISIBLE);
                if (different != null) {
                    if (different.equals("0")) {/*在线充值*/
                        mBinding.payYe.setVisibility(View.GONE);
                        mBinding.llYue.setVisibility(View.GONE);
                    } else if (different.equals("1")) {/*提现*/
                        mBinding.payYe.setVisibility(View.INVISIBLE);
                    } else if (different.equals("2")) {/*购物车支付*/
                        mBinding.payYe.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        mBinding.llZfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                different = mBundle.getString("diff");
                isWhat = 1;

                mBinding.payWeixin.setVisibility(View.INVISIBLE);
                mBinding.payZfb.setVisibility(View.VISIBLE);
                mBinding.payYe.setVisibility(View.INVISIBLE);
                if (different != null) {
                    if (different.equals("0")) {/*在线充值*/
                        mBinding.payYe.setVisibility(View.GONE);
                        mBinding.llYue.setVisibility(View.GONE);
                    } else if (different.equals("1")) {/*提现*/
                        mBinding.payYe.setVisibility(View.INVISIBLE);
                    } else if (different.equals("2")) {/*购物车支付*/
                        mBinding.payYe.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        mBinding.llYue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isWhat = 2;
                mBinding.payWeixin.setVisibility(View.INVISIBLE);
                mBinding.payZfb.setVisibility(View.INVISIBLE);
                mBinding.payYe.setVisibility(View.VISIBLE);
            }
        });

    }

    /*支付*/
    public void topay() {
        different = mBundle.getString("diff");
        if (different.equals("1")) {
            isWhat = 1;
        }
        if (isWhat == 0) {//微信支付
            if (different != null) {
                if (different.equals("0")) {/*在线充值*/
                    payWeChatOnline();
                } else if (different.equals("1")) {/*提现*/
                    getOutMoneyWechat();
                } else if (different.equals("2") || different.equals("-1")) {/*购物车支付*/
                    payWeChat();
                }
            }
        } else if (isWhat == 1) {//支付宝支付
            if (different != null) {
                if (different.equals("0")) {/*在线充值*/
                    payAliOnline();
                } else if (different.equals("1")) {/*提现*/
                    yzBinding();
                } else if (different.equals("2") || different.equals("-1")) {/*购物车支付*/
                    payZhiFuBao();
                }
            }
        } else if (isWhat == 2) {//余额支付
            if (different != null) {
                if (different.equals("0")) {/*在线充值*/

                } else if (different.equals("1")) {/*提现*/

                } else if (different.equals("2") || different.equals("-1")) {/*购物车支付*/
                  /*获取支付密码*/
                    if (MyApplication.getUser().getIs_pay_pwd().equals("0")) {
                        showPasDialog();
                        return;
                    }
                    if (Double.parseDouble(yue) < Double.parseDouble(price)) {
                        showBuzuDialog();
                        return;
                    }
                    showPayDialog();
                }
            }
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
        tv_money.setText(price);
        tv_yue.setText("         余额支付(" + yue + ")");
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
                    postPrice(charSequence + "");
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

    //获取支付价格
    private void httpPrice() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ORDERPRICE, RequestMethod.GET);
        request.add("order_id", orderId);
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
                            price = jsonObject.getJSONObject("data").getString("total_price");
                            yue = jsonObject.getJSONObject("data").getString("balance");
                            mBinding.payPrice.setText(price);
                            mBinding.tvYue.setText("         余额支付(" + yue + ")");
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

    //余额支付
    private void postPrice(String pass) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.BALANCE, RequestMethod.POST);
        request.add("order_id", orderId);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("price", price);
        request.add("pay_password", MD5Utils.md5Password(pass));
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
                            initDialog();
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

    //支付成功dialog
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

    public void onResume() {
        if (mBundle.getString("orderId") != null) {
            orderId = mBundle.getString("orderId");
            httpPrice();
        }
    }

    /*购物车支付--微信*/
    public void payWeChat() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.WXAPPAY, RequestMethod.POST);
        request.add("order_id", orderId);
        request.add("price", price);
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
                            PayReq req = new PayReq();
                            req.appId = data.getString("appid");
                            req.partnerId = data.getString("partnerid");
                            req.prepayId = data.getString("prepayid");
                            req.nonceStr = data.getString("noncestr");
                            req.timeStamp = data.getString("timestamp");
                            req.packageValue = data.getString("package");
                            req.sign = data.getString("sign");
                            MyApplication.getInstance().getApi().sendReq(req);
                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.show(mContext, "微信支付错误", Toast.LENGTH_SHORT);
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

    /*购物车支付--支付宝*/
    public void payZhiFuBao() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ALIPAY, RequestMethod.POST);
        request.add("order_id", orderId);
        request.add("price", price);
        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    String code = "";
                    try {
                        JSONObject jsonObject = response.get();
                        code = jsonObject.getString("code");
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.show(mContext, "支付宝支付错误", Toast.LENGTH_SHORT);
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

    /*微信提现*/
    public void getOutMoneyWechat() {

    }

    /*验证是否绑定了支付宝(否-跳转到绑定)*/
    public void yzBinding() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ISBANGDING, RequestMethod.POST);
        request.add("user_id", MyApplication.getUser().getUser_id());

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
//                            getOutMoneyZhifubao();
                            /*获取支付密码*/
                            if (MyApplication.getUser().getIs_pay_pwd().equals("0")) {
                                showPasDialog();
                            } else {
                                showPassDialog();//首先输入支付密码
                            }
                        } else {
                            ShowRemindDialog.getInstance().showDialog(mContext, R.mipmap.babycry, "还未绑定,前去绑定？", false, new OnSomeThingClickListener() {
                                @Override
                                public void OnDoSomeThingClickListener(int view_id) {
                                    startActivity(BdZhActivity.class);
                                }
                            }).setBtNote("是", "否");
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

    /*显示输入支付密码的弹窗*/
    public void showPassDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_pass, null);
        final PasswordInputView pass = v.findViewById(R.id.again_paypswd_pet);
        alertDialog.setView(v);
        alertDialog.show();
        pass.setFocusable(true);
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


//        alertDialog.setContentView(v);
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    getOutMoneyZhifubao(pass.getText().toString());
                    alertDialog.dismiss();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /*支付宝提现==肯定需要先绑定才能提现*/
    public void getOutMoneyZhifubao(String password) {
        mBinding.submitPay.setEnabled(false);
        String money = mBinding.payMoney.getText().toString();
        if (money == null || money.equals("")) {
            ToastUtil.show(mContext, "请输入提现金额", Toast.LENGTH_SHORT);
            return;
        }
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ZHIFUBAOTIXIAN, RequestMethod.POST);
        request.add("user_id", MyApplication.getUser().getUser_id());
        request.add("amount", money);
        request.add("password", password);
        request.add("version_num", appVersionCode);
        Log.i("当前版本号",appVersionCode+"");
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
                            ShowRemindDialog.getInstance().showDialog(mContext, R.drawable.chenggong1, "提现成功", false, new OnSomeThingClickListener() {
                                @Override
                                public void OnDoSomeThingClickListener(int view_id) {
                                    ((PayOrderActivity) mContext).finish();
                                }
                            }).setCanleGV(false, "确定");
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
                mBinding.submitPay.setEnabled(true);

            }
        });
    }

    /*获取当前版本信息*/
    public void getVersionCode() {
        try {
            PackageInfo packageInfo = mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            appVersionCode = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*微信在线充值*/
    public void payWeChatOnline() {
        String money = mBinding.payMoney.getText().toString();
        if (money == null || money.equals("")) {
            ToastUtil.show(mContext, "请输入充值金额", Toast.LENGTH_SHORT);
            return;
        }
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.WXRECHARGE, RequestMethod.POST);
        request.add("user_id", MyApplication.getUser().getUser_id());
        request.add("price", money);

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
                            PayReq req = new PayReq();
                            req.appId = data.getString("appid");
                            req.partnerId = data.getString("partnerid");
                            req.prepayId = data.getString("prepayid");
                            req.nonceStr = data.getString("noncestr");
                            req.timeStamp = data.getString("timestamp");
                            req.packageValue = data.getString("package");
                            req.sign = data.getString("sign");
                            MyApplication.getInstance().getApi().sendReq(req);
                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.show(mContext, "微信支付错误", Toast.LENGTH_SHORT);
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

    /*支付宝在线充值*/
    public void payAliOnline() {
        String money = mBinding.payMoney.getText().toString();
        if (money == null || money.equals("")) {
            ToastUtil.show(mContext, "请输入充值金额", Toast.LENGTH_SHORT);
            return;
        }
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ALIRECHARGE, RequestMethod.POST);
        request.add("user_id", MyApplication.getUser().getUser_id());
        request.add("price", money);

        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    String code = "";
                    try {
                        JSONObject jsonObject = response.get();
                        code = jsonObject.getString("code");
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ToastUtil.show(mContext, "支付宝支付错误", Toast.LENGTH_SHORT);
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
                            mMoney = data.getString("money");
                            if (mMoney == null || mMoney.equals("")) {
                                mBinding.payPrice.setText("0.00");
                            } else {
                                mBinding.payPrice.setText(mMoney);
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
