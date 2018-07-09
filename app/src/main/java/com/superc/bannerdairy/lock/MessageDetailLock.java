package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityMessageDetailBinding;
import com.superc.bannerdairy.ui.mine.MessageDetailActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2017/12/16.
 */

public class MessageDetailLock extends BaseLock<ActivityMessageDetailBinding> {


    private String mNotice_id;
    private String mNotice_title;
    private String mCreator;

    public MessageDetailLock(Context context, ActivityMessageDetailBinding binding) {
        super(context, binding);
    }

    public MessageDetailLock(Context context, ActivityMessageDetailBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.message_title);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        if (mBundle != null) {
            mNotice_id = mBundle.getString(MessageDetailActivity.NOTICEID);
            mNotice_title = mBundle.getString(MessageDetailActivity.NOTICETITLE);
            mCreator = mBundle.getString(MessageDetailActivity.CREATOR);
            mBinding.tvTitle.setText(mNotice_title);
            mBinding.tvTime.setText(mCreator);
            /*获取具体消息的内容*/
            RequestQueue mRequestQueue = NoHttp.newRequestQueue();
            Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.NOTICELIST, RequestMethod.GET);
            if (MyApplication.getUser().getUser_id() != null) {
                request.add("user_id", MyApplication.getUser().getUser_id());
            }
            request.add("notice_id", mNotice_id);
            request.add("act", "one");

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
                                String notice_content = data.getString("notice_content");
                                String created = data.getString("created");
                                mBinding.tvContent.setText(notice_content);
                                mBinding.tvTime.setText(created);
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
}
