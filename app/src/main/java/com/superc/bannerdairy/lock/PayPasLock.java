package com.superc.bannerdairy.lock;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivitySetPayPasBinding;
import com.superc.bannerdairy.model.User;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.mine.SetPayPasActivity;
import com.superc.bannerdairy.widget.MD5Utils;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2017/12/20.
 */

public class PayPasLock extends BaseLock<ActivitySetPayPasBinding> {
    public PayPasLock(Context context, ActivitySetPayPasBinding binding) {
        super(context, binding);
    }

    public PayPasLock(Context context, ActivitySetPayPasBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.pay_password);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
        if(MyApplication.getUser().getMobile()!=null){
            mBinding.userphone.setText(MyApplication.getUser().getMobile());
        }else {

        }
    }

    public void sendCode() {
        if (TextUtils.isEmpty(mBinding.userphone.getText().toString())) {
            showToast("请登录");
            return;
        }
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                mBinding.tvCode.setEnabled(false);
                mBinding.tvCode.setTextColor(Color.parseColor("#5c262626"));
                mBinding.tvCode.setText("倒计时"
                        + (millisUntilFinished / 1000 + "秒") + "");
            }

            public void onFinish() {
                mBinding.tvCode.setText("重新获取验证码");
                mBinding.tvCode.setTextColor(Color.parseColor("#5c262626"));
                mBinding.tvCode.setEnabled(true);
            }
        }.start();
        sendCodeHttp();
    }

    public void sendCodeHttp() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.SENDCODE, RequestMethod.POST);
        request.add("mobile", mBinding.userphone.getText().toString());
        request.add("product", 2);
        //product   验证码类型1：注册，2：支付
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
//                            showToast(jsonObject.getString("msg"));
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

    /*设置支付密码*/
    public void send() {
        String pas1 = mBinding.password1.getText().toString();
        String pas2 = mBinding.password2.getText().toString();
           /*验证密码*/
        if (pas1.length() != 6) {
            showToast("请输入6位数字密码");
            return;
        } else if (!pas1.equals(pas2)) {
            showToast("两次密码不一致");
            return;
        }
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.SETPAYWORD, RequestMethod.POST);
        if (MyApplication.getUser().getUser_id() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("pay_password", MD5Utils.md5Password(mBinding.password1.getText().toString()));
        request.add("sjcode",mBinding.code.getText().toString());//输入验证码

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
                            User user = MyApplication.getUser();
                            // TODO: 2017/12/23 获取
                            user.setIs_pay_pwd("1");
                            MyApplication.setUser(user);
                            ((SetPayPasActivity)mContext).finish();
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
