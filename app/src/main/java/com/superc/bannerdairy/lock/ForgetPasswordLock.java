package com.superc.bannerdairy.lock;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityForgetPasswordBinding;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.login.ForgetPasswordActivity;
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
 * 创建日期：2017/11/6 on 17:41
 * 描述：
 *
 */

public class ForgetPasswordLock extends BaseLock<ActivityForgetPasswordBinding> {

    public ForgetPasswordLock(Context context, ActivityForgetPasswordBinding binding) {
        super(context, binding);
    }

    public ForgetPasswordLock(Context context, ActivityForgetPasswordBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {

    }

    //发送验证码
    public void sendCode() {
        if (TextUtils.isEmpty(mBinding.username.getText().toString())) {
            showToast("账号不能为空");
            return;
        }
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                mBinding.textView3.setEnabled(false);
                mBinding.textView3.setTextColor(Color.parseColor("#5c262626"));
                mBinding.textView3.setText("倒计时"
                        + (millisUntilFinished / 1000 + "秒") + "");
            }

            public void onFinish() {
                mBinding.textView3.setText("重新获取验证码");
                mBinding.textView3.setTextColor(Color.parseColor("#5c262626"));
                mBinding.textView3.setEnabled(true);
            }
        }.start();
        sendCodeHttp();
    }

    //提交
    public void send() {
        if (TextUtils.isEmpty(mBinding.username.getText().toString())) {
            showToast("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(mBinding.password1.getText().toString())) {
            showToast("密码不能为空");

            return;
        }
        if (TextUtils.isEmpty(mBinding.password2.getText().toString())) {
            showToast("密码不能为空");

            return;
        }
        if (!mBinding.password1.getText().toString().equals(mBinding.password2.getText().toString())) {
            showToast("两次密码不一致");

            return;
        }
        if (mBinding.password1.getText().toString().length() < 6) {
            showToast("密码至少6位");

            return;
        }
        if (TextUtils.isEmpty(mBinding.code.getText().toString())) {
            showToast("验证码不能为空");

            return;
        }
        sendHttp();
    }

    //发送验证码
    public void sendCodeHttp() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.SENDCODE, RequestMethod.POST);
        Gson gson = new Gson();
        request.add("mobile", mBinding.username.getText().toString());
        request.add("product", 1);
        //product   验证码类型1：注册，2：支付
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    String resultCode = null;
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {

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
//                hideLoading();
                showToast("网络异常");

            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }

    //提交
    public void sendHttp() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.FORGET_MIMA, RequestMethod.POST);
        Gson gson = new Gson();
        request.add("mobile", mBinding.username.getText().toString());
        request.add("password", MD5Utils.md5Password(mBinding.password1.getText().toString()));
        request.add("sjcode", mBinding.code.getText().toString());
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    String resultCode = null;
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            if(mContext!=null) {
                                ShowRemindDialog.getInstance().showDialog(mContext, R.drawable.chenggong1, "恭喜您，已修改成功！", false, new OnSomeThingClickListener() {
                                    @Override
                                    public void OnDoSomeThingClickListener(int view_id) {
                                        ((ForgetPasswordActivity) mContext).finish();
                                    }
                                }).setCanleGV(false, "去登录");
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
//                hideLoading();
                showToast("网络异常");

            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }
}
