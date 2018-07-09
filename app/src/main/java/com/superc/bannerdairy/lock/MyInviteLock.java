package com.superc.bannerdairy.lock;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.MyInviteAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.ActivityMyInviteBinding;
import com.superc.bannerdairy.model.MyInviteItem;
import com.superc.bannerdairy.ui.order.MyFragmentPagerAdapter;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amorr on 2017/11/22.
 * 我的邀请界面
 */

public class MyInviteLock extends BaseLock<ActivityMyInviteBinding> {

    private String[] titles = {"激活", "未激活"};
    private List<Fragment> fragments;
    private int page = 0;
    private MyFragmentPagerAdapter adapter;
    private List<MyInviteItem> mGoodsList;
    private MyInviteAdapter mAdapter;

    public MyInviteLock(Context context, ActivityMyInviteBinding binding) {
        super(context, binding);
    }

    public MyInviteLock(Context context, ActivityMyInviteBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.my_invitation);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.inviteRv.setLayoutManager(ms);
        mBinding.inviteRv.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<>();
        mAdapter = new MyInviteAdapter(mContext, mGoodsList, R.layout.item_my_invite, BR.myInviteLock);
        mBinding.inviteRv.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        mAdapter.setOnItemClickListener(new MyInviteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                //我的邀请
            }
        });
        httpData();

    }


    private void httpData() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.INVITE, RequestMethod.POST);
        final Gson gson = new Gson();
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().getUser_id());
        }

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
                            //填充数据
                            if (jsonObject.getJSONArray("data") != null) {
                                mGoodsList.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    MyInviteItem item = gson.fromJson(jsonArray.get(i).toString(), MyInviteItem.class);
                                    mGoodsList.add(item);
                                }
                                mAdapter.notifyDataSetChanged();
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
