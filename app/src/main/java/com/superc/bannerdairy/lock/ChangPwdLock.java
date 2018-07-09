package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;

import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityChangePwdBinding;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2018/5/4.
 */

public class ChangPwdLock extends BaseLock<ActivityChangePwdBinding> {
    public ChangPwdLock(Context context, ActivityChangePwdBinding binding) {
        super(context, binding);
    }

    public ChangPwdLock(Context context, ActivityChangePwdBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {


    }

    public void toChange() {
        String oldPwd = mBinding.oldPwd.getText().toString();
        String newPwdOne = mBinding.newPwdOne.getText().toString();
        String newPwdTwo = mBinding.newPwdTwo.getText().toString();
        if (oldPwd.equals("")) {
            showToast("请输入原密码");
            return;
        }
        if (newPwdOne.equals("")) {
            showToast("请输入新密码");
            return;
        }
        if (newPwdTwo.equals("")) {
            showToast("请再次输入新密码");
            return;
        }

        if(newPwdOne.length()<6){
            showToast("密码至少6位");
            return;
        }
        if (!newPwdOne.equals(newPwdTwo)) {
            showToast("两次密码不一致,请重新输入");
            return;
        }

        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.CHANGEPWD, RequestMethod.POST);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("password", oldPwd);
        request.add("Password2", newPwdOne);
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
