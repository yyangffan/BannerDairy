package com.superc.bannerdairy.lock;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.GoodsColRcAdapter;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.FragmentGoodsCollectBinding;
import com.superc.bannerdairy.model.GoodsColItem;
import com.superc.cframework.utils.ToastUtil;
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
 * Created by user on 2017/12/18.
 */

public class GoodsColLock {
    Context mContext;
    FragmentGoodsCollectBinding mBinding;
    GoodsColRcAdapter adapter;
    private List<GoodsColItem.DataBean> mGoodsList;


    public GoodsColLock(Context context, FragmentGoodsCollectBinding binding) {
        mContext = context;
        mBinding = binding;
        init();
    }

    public void init() {
        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.rvCollect.setLayoutManager(ms);
        mBinding.rvCollect.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList();

        adapter = new GoodsColRcAdapter(mContext, mGoodsList, R.layout.item_goodscol, BR.goodsclLock);
        mBinding.rvCollect.setAdapter(adapter); // 这里或者在xml里设置adapter
        catchTheGoods();
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                catchTheGoods();
            }
        });


    }
    /*获取收藏商品列表*/
    private void catchTheGoods() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.MYCOLLECT, RequestMethod.GET);
        Gson gson = new Gson();
        request.add("act", "list");
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
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                GoodsColItem.DataBean item = new Gson().fromJson(jsonArray.get(i).toString(),  GoodsColItem.DataBean.class);
                                mGoodsList.add(item);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        if (show) {
                            ToastUtil.show(mContext,jsonObject.getString("msg"), Toast.LENGTH_SHORT);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                mBinding.refreshLayout.finishRefresh();
                ToastUtil.show(mContext,"网络异常", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFinish(int what) {
//                hideLoading();
                mBinding.refreshLayout.finishRefresh();
            }
        });

    }


}
