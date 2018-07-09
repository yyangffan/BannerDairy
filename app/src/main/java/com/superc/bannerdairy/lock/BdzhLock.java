package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityBdZhBinding;
import com.superc.bannerdairy.model.User;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.mine.BdZhActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static com.alipay.sdk.app.statistic.c.B;

/**
 * Created by user on 2018/3/6.
 */

public class BdzhLock extends BaseLock<ActivityBdZhBinding> {
    public BdzhLock(Context context, ActivityBdZhBinding binding) {
        super(context, binding);
    }

    public BdzhLock(Context context, ActivityBdZhBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.my_info);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
        httpUser();
    }

    /*微信绑定*/
    public void bdWx() {
        String ex_id = mBinding.bdWx.getText().toString();
        if (ex_id == null || ex_id.equals("")) {
            showToast("请输入微信账号");
            return;
        }
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.WXBINDING, RequestMethod.POST);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        //openid是什么
        request.add("openid", ex_id);

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
                            ShowRemindDialog.getInstance().showDialog(mContext, R.drawable.chenggong1, "恭喜您!绑定成功", false, new OnSomeThingClickListener() {
                                @Override
                                public void OnDoSomeThingClickListener(int iew_id) {
                                    ((BdZhActivity) mContext).finish();
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
            }
        });
    }

    /*支付宝绑定*/
    public void bdZfb() {
        final String zfb_id = mBinding.bdZfb.getText().toString();
        if (zfb_id == null || zfb_id.equals("")) {
            showToast("请输入支付宝账号");
            return;
        }
        ShowRemindDialog instance = ShowRemindDialog.getInstance();
        instance.showDialog(mContext, R.drawable.chenggong1, "请确认绑定账号:" + zfb_id, false, new OnSomeThingClickListener() {
            @Override
            public void OnDoSomeThingClickListener(int view_id) {
                gotoBdZfb(zfb_id);
            }
        });
    }

    public void gotoBdZfb(String zfb_id) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ZFBBINDING, RequestMethod.POST);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("buyer_logon_id", zfb_id);

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
                            if (mContext != null) {
                                ShowRemindDialog.getInstance().showDialog(mContext, R.drawable.chenggong1, "恭喜您!绑定成功", false, new OnSomeThingClickListener() {
                                    @Override
                                    public void OnDoSomeThingClickListener(int iew_id) {
                                        ((BdZhActivity) mContext).finish();
                                    }
                                }).setCanleGV(false, "确定");
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

    //获取个人资料接口
    public void httpUser() {
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
                            User user = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), User.class);
                            mBinding.bdZfb.setText(user.getBuyer_logon_id());
                            mBinding.bdZfb.setSelection(user.getBuyer_logon_id() == null || user.getBuyer_logon_id().equals("") ? 0 : user.getBuyer_logon_id().length());
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
