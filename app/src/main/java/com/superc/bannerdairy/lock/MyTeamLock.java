package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityMyTeamBinding;
import com.superc.bannerdairy.ui.manage.FlatLevelFragment;
import com.superc.bannerdairy.ui.manage.LowerLevelFragment;
import com.superc.bannerdairy.ui.manage.MyTeamActivity;
import com.superc.bannerdairy.ui.order.MyFragmentPagerAdapter;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期：2017/11/10 on 16:52
 * 描述：
 * 作者：
 * QQ：
 * 我的团队页面
 */

public class MyTeamLock extends BaseLock<ActivityMyTeamBinding> {
    private String[] titles = {"下级", "平级"};
    private List<Fragment> fragments;
    private int page = 0;
    private MyFragmentPagerAdapter adapter;
    private RequestQueue mRequestQueue = NoHttp.newRequestQueue();

    public MyTeamLock(Context context, ActivityMyTeamBinding binding) {
        super(context, binding);
    }

    public MyTeamLock(Context context, ActivityMyTeamBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        getTitle();
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.team_title);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        fragments = new ArrayList<>();
        fragments.add(new LowerLevelFragment());
        fragments.add(new FlatLevelFragment());
    }

    public void setTitles(String[] ti) {
        if(ti!=null&&ti.length!=0){

        }else {
            ti = new String[]{"下级", "平级"};
        }

        adapter = new MyFragmentPagerAdapter(((MyTeamActivity) mContext).getSupportFragmentManager(), fragments, ti);
        mBinding.viewpager.setAdapter(adapter);
        mBinding.viewpager.setCurrentItem(page);
        mBinding.viewpager.setOffscreenPageLimit(4);
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
        //防止点击的时候出现中间的条目
//        mBinding.orderTabLayout.setOnTabSelectedListener(new MyOnTabSelectedListener(mBinding.orderViewPager));
        mBinding.tabs.setTabMode(TabLayout.MODE_FIXED);

    }


    public void getTitle() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.SUBORINATE, RequestMethod.POST);
        Gson gson = new Gson();
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
                    String resultCode = null;
                    Boolean success = false;
                    boolean isShow = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        isShow = jsonObject.getBoolean("show");
                        if (success) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String subordinate = data.getString("subordinate");
                            String same = data.getString("same");
                            titles = new String[]{subordinate, same};
                            setTitles(titles);
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
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }


}
