package com.superc.bannerdairy.lock;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.FashionAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.FragmentFashionSellerBinding;
import com.superc.bannerdairy.model.FinshItem;
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
import java.util.HashMap;
import java.util.List;


/**
 * Created by Amorr on 2017/11/15.
 * 买家秀界面
 */

public class FashionLock extends BaseLock<FragmentFashionSellerBinding> {
    private List<FinshItem> mGoodsList;
    private FashionAdapter mAdapter;

    public FashionLock(Context context, FragmentFashionSellerBinding binding) {
        super(context, binding);
    }

    public FashionLock(Context context, FragmentFashionSellerBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.rvFinsionSeller.setLayoutManager(ms);
        mBinding.rvFinsionSeller.addItemDecoration(new RecycleViewDivider(
                mContext, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<>();
        httpKnow();
        mAdapter = new FashionAdapter(mContext, mGoodsList, R.layout.item_finshion, BR.finshItem);
        mBinding.rvFinsionSeller.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                httpKnow();
            }
        });
        mAdapter.setOnRefreshMsg(new FashionAdapter.OnRefreshMsg() {
            @Override
            public void OnRefreshMsg() {
                httpKnow();
            }
        });

    }

    //获取买家秀列表
    public void httpKnow() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LIST_ARTICLE, RequestMethod.GET);
        request.add("act", "applist");
        //article_category_type   2（买家秀）0（文章列表）
        request.add("article_category_type", 2);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        mRequestQueue.add(3, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mBinding.refreshLayout.finishRefresh();
                if (what == 3) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            mGoodsList.clear();
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                // TODO: 2017/12/20 数据结构发生了变化
                                FinshItem item = new Gson().fromJson(jsonArray.get(i).toString(), FinshItem.class);
                                mGoodsList.add(item);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        Log.e("FashionLock", "json解析失败了");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                mBinding.refreshLayout.finishRefresh();
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
                mBinding.refreshLayout.finishRefresh();
            }
        });
    }

}
