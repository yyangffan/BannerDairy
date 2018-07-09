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
import com.superc.bannerdairy.adapter.FashionAdapter;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.FragmentBuyShowBinding;
import com.superc.bannerdairy.model.FinshItem;
import com.superc.bannerdairy.ui.home.picture_show.MyWindowDialog;
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

import static cn.jpush.android.api.JPushInterface.a.i;

/**
 * Created by user on 2017/12/18.
 */

public class BuyShowLock {

    private List<FinshItem> mGoodsList;
    private FashionAdapter mAdapter;

    Context mContext;
    FragmentBuyShowBinding mBinding;

    public BuyShowLock(Context context, FragmentBuyShowBinding binding) {
        mContext = context;
        mBinding = binding;
//        init();
    }

    public void init() {
        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.rvCollect.setLayoutManager(ms);
        mBinding.rvCollect.addItemDecoration(new RecycleViewDivider(
                mContext, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<FinshItem>();
        mAdapter = new FashionAdapter(mContext, mGoodsList, R.layout.item_finshion, BR.finshItem);
        mBinding.rvCollect.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        catchCollect();
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                catchCollect();
            }
        });
    }


    /*获取买家秀收藏列表*/
    private void catchCollect() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.WENZHANGCOL, RequestMethod.GET);
        Gson gson = new Gson();
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
                    String resultCode = null;
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
                                FinshItem item = new Gson().fromJson(jsonArray.get(i).toString(), FinshItem.class);
                                mGoodsList.add(item);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        if (show) {
                            ToastUtil.show(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT);
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
                ToastUtil.show(mContext, "网络异常", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFinish(int what) {
//                hideLoading();
                mBinding.refreshLayout.finishRefresh();

            }
        });
    }


}
