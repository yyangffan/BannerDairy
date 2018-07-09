package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.SPUtils;
import com.superc.bannerdairy.databinding.ActivityLoginBinding;
import com.superc.bannerdairy.jiguang.SetJPushAlias;
import com.superc.bannerdairy.model.User;
import com.superc.bannerdairy.ui.home.MainActivity;
import com.superc.bannerdairy.ui.login.ForgetPasswordActivity;
import com.superc.bannerdairy.ui.login.SignActivity;
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
 * 创建日期：2017/11/6 on 15:02
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class LoginLock extends BaseLock<ActivityLoginBinding> {
    public LoginData loginData;
    private RequestQueue mRequestQueue = NoHttp.newRequestQueue();
    private final static int REQUEST = 0;

    public LoginLock(Context context, ActivityLoginBinding binding) {
        super(context, binding);
        loginData = new LoginData();

    }

    public LoginLock(Context context, ActivityLoginBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    public class LoginData {
        public String loginName = "";
        public String loginPwd = "";
    }

    @Override
    protected void init() {

    }

    public void toLogin() {
        judgeData();

    }

    public void toSign() {
        startActivity(SignActivity.class);
    }

    public void toForgetPassword() {
        startActivity(ForgetPasswordActivity.class);
    }

    /**
     * 数据判断
     */
    private void judgeData() {

        if (TextUtils.isEmpty(loginData.loginName)) {
            showToast("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(loginData.loginPwd)) {
            showToast("密码不能为空");

            return;
        }
        if (loginData.loginPwd.length() < 6) {
            showToast("密码大6位");

            return;
        }
        httpLogin();
    }


    private void httpLogin() {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LOGIN_MIMA, RequestMethod.POST);
        request.add("mobile", loginData.loginName);
//        request.add("password", loginData.loginPwd);
        request.add("password", MD5Utils.md5Password(loginData.loginPwd));

        mRequestQueue.add(REQUEST, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {


            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == REQUEST) {
                    Boolean success = false;
                    boolean isShow = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        isShow = jsonObject.getBoolean("show");
                        if (success) {

                            User user = new User();
                            user = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), User.class);
                            SPUtils.put(mContext, "userInfo", user);
                            SPUtils.put(mContext, "username", loginData.loginName);
                            SPUtils.put(mContext, "password", loginData.loginPwd);
                            MyApplication.setUser(user);
                            Log.e("user_id",user.getUser_id());
                            startActivity(MainActivity.class);
                            ((BaseActivity) mContext).finish();
                            new SetJPushAlias(user.user_id,mContext).setAlias();

                        }
                        if (isShow) {
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
