package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;

import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityAboutBinding;
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
 */

public class AboutLock extends BaseLock<ActivityAboutBinding> {
    public AboutLock(Context context, ActivityAboutBinding binding) {
        super(context, binding);
    }

    public AboutLock(Context context, ActivityAboutBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock=new AppBarLock(mContext, R.string.my_about);
        appBarLock.barData.isLeft=true;
        appBarLock.barData.titleLeft="返回";
        appBarLock.barData.imsLeft=mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
        httpKnow();
    }
    //获取文章详情第一条---即为关于我们的内容
    private void httpKnow() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LIST_ARTICLE, RequestMethod.GET);
        request.add("act", "one");
        //article_category_type   2（买家秀）0（文章列表）
        request.add("article_category_type", 0);
        request.add("article_id", 1);
        request.add("category_id", "4");
        if(MyApplication.getUser()!=null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    Boolean success=false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            JSONObject jsonOb= jsonObject.getJSONObject("data");
                            String article_content = jsonOb.getString("article_content");
                            String contentt = article_content.replace("<img", "<img height=\"auto\"; width=\"100%\"");
                            mBinding.aboutWb.loadDataWithBaseURL(null, contentt, "text/html", "UTF-8", null); // 加载定义的代码，并设定编码格式和字符集。
                            mBinding.aboutTitle.setText(jsonOb.getString("article_title"));
                            mBinding.aboutTime.setText(jsonOb.getString("created"));
                            mBinding.aboutNum.setText(jsonOb.getString("pageview"));
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
