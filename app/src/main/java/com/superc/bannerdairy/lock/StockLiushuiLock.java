package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.StockLiushuiAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivitySotckLiushuiBinding;
import com.superc.bannerdairy.model.StockLiushuiBean;
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
 * 库存流水使用的Lock
 */

public class StockLiushuiLock extends BaseLock<ActivitySotckLiushuiBinding> {
    private RequestQueue mRequestQueue;
    private List<StockLiushuiBean.DataBean> mDataBeen;
    private StockLiushuiAdapter mStockLiushuiAdapter;

    public StockLiushuiLock(Context context, ActivitySotckLiushuiBinding binding) {
        super(context, binding);
    }

    public StockLiushuiLock(Context context, ActivitySotckLiushuiBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.kucunliushui);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        mRequestQueue = NoHttp.newRequestQueue();
        mDataBeen=new ArrayList<>();
        mStockLiushuiAdapter=new StockLiushuiAdapter(mContext,mDataBeen);
        LinearLayoutManager manager=new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.stockLiushuiRecy.setLayoutManager(manager);
        mBinding.stockLiushuiRecy.setAdapter(mStockLiushuiAdapter);

        initData();

    }

    public void initData() {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.KUCUNLIUSHUI, RequestMethod.POST);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        mRequestQueue.add(12, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {


            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 12) {
                    Boolean success = false;
                    boolean isShow = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        isShow = jsonObject.getBoolean("show");
                        StockLiushuiBean bean=new Gson().fromJson(jsonObject.toString(),StockLiushuiBean.class);
                        if (success) {
                            mDataBeen.clear();
                            mDataBeen.addAll(bean.getData());
                            mStockLiushuiAdapter.notifyDataSetChanged();
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
