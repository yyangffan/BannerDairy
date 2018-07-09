package com.superc.bannerdairy.lock;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.BuyRecordAdapter;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityBuyRecordBinding;
import com.superc.bannerdairy.model.BuyRecordBean;
import com.superc.cframework.utils.ToastUtil;
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
 * Created by user on 2018/1/15.
 */

public class BuyRecordLock {
    private ActivityBuyRecordBinding mBinding;
    private Context mContext;
    private BuyRecordAdapter mAdapter;
    private List<BuyRecordBean.DataBeanX.DataBean> mList;
    private int page = 1;


    public BuyRecordLock(ActivityBuyRecordBinding activityBuyRecordBinding, Context context) {
        mBinding = activityBuyRecordBinding;
        mContext = context;
        initViews();
    }

    public void initViews() {
        AppBarLock appBarLock = new AppBarLock(mContext, "交易记录");
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        mList = new ArrayList<>();
        mAdapter = new BuyRecordAdapter(mContext, mList);
        mBinding.buyrecordLv.setAdapter(mAdapter);
        initData();
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mBinding.refreshLayout.setEnableLoadmore(true);
                page = 1;
                initData();
            }
        });

        mBinding.refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                initData();
            }
        });

    }

    /*交易记录*/
    private void initData() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.USERMONEYRECORD, RequestMethod.GET);
        final Gson gson = new Gson();
        request.add("act", "applist");
        request.add("user_id", MyApplication.getUser().getUser_id());
        request.add("page", page);
        request.add("p", "15");

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
                        BuyRecordBean buyRecordBean = gson.fromJson(response.get().toString(), BuyRecordBean.class);
                        if (success) {
                            BuyRecordBean.DataBeanX data = buyRecordBean.getData();
                            if (data != null) {
                                List<BuyRecordBean.DataBeanX.DataBean> databea_list = data.getData();
                                if (databea_list != null) {
                                    if (page == 1) {
                                        mList.clear();
                                        mList.addAll(buyRecordBean.getData().getData());
                                        if (databea_list.size() < 15) {
                                            mBinding.refreshLayout.setEnableLoadmore(false);
                                        }
                                    } else {
                                        for (BuyRecordBean.DataBeanX.DataBean dataBean : databea_list) {
                                            mList.add(dataBean);
                                        }
                                        if (databea_list.size() < 15) {
                                            mBinding.refreshLayout.setEnableLoadmore(false);
                                        }
                                    }
                                    page++;
                                }
                            }
                        } else {
                            mBinding.refreshLayout.setEnableLoadmore(true);
                            page = 1;
                            mList.clear();
                        }
                        mAdapter.notifyDataSetChanged();
                        if (show) {
                            ToastUtil.show(mContext, (jsonObject.getString("msg")), Toast.LENGTH_SHORT);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                mBinding.refreshLayout.finishLoadmore();
                mBinding.refreshLayout.finishRefresh();
                ToastUtil.show(mContext, "网络异常", Toast.LENGTH_SHORT);

            }

            @Override
            public void onFinish(int what) {
                mBinding.refreshLayout.finishLoadmore();
                mBinding.refreshLayout.finishRefresh();
            }
        });

    }


}
