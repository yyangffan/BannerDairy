package com.superc.bannerdairy.ui.manage;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.FlatLevelAdapter;
import com.superc.bannerdairy.base.BaseFragment;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.FragmentFlatLevelBinding;
import com.superc.bannerdairy.lock.FlatFramentLock;
import com.superc.bannerdairy.model.LevelItem;
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

import static android.media.CamcorderProfile.get;

//平级代理
public class FlatLevelFragment extends BaseFragment {
    private FragmentFlatLevelBinding mBinding;
    private FlatFramentLock mLock;
    private List<LevelItem> mGoodsList;
    private FlatLevelAdapter mAdapter;
    private int flag = 0;

    @Override
    protected View initBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_flat_level, container, false);
        mLock = new FlatFramentLock(mActivity, mBinding);
        mBinding.setFlatFramentLock(mLock);

        return mBinding.getRoot();
    }

    @Override
    protected void init() {
        GridLayoutManager ms = new GridLayoutManager(mActivity, 1);
        mBinding.rvOrders.setLayoutManager(ms);
        mBinding.rvOrders.addItemDecoration(new RecycleViewDivider(
                mActivity, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<LevelItem>();

        mAdapter = new FlatLevelAdapter(mActivity, mGoodsList, R.layout.item_level_same, BR.levelItem);
        mBinding.rvOrders.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        mAdapter.setOnItemClickListener(new FlatLevelAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                //平级推广人员
                Bundle bundle = new Bundle();
                String level=mGoodsList.get(position).getAgency_level();
                String teamCount = mGoodsList.get(position).getTeamCount();
                String data="";
                if (level!= null) {
                    switch (level){
                        case "0":data="zero";break;
                        case "1":data="one";break;
                        case "2":data="two";break;
                        case "3":data="three";break;
                        case "4":data="four";break;
                        case "5":data="five";break;
                    }
                    bundle.putString("flag", data);
                    bundle.putString("teamCount",teamCount);
                    bundle.putString("user_id", mGoodsList.get(position).getUser_id());
                }
                startActivity(OnePersonActivity.class, bundle);
            }
        });
        //获取平级列表
        httpData();

    }

    //获取平级列表
    private void httpData() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.GETFLAT, RequestMethod.POST);
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
                    String resultCode = null;
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            //填充数据
                            if (jsonObject.getJSONObject("data") != null) {
                                mGoodsList.clear();
                                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("list");
                                if (jsonArray != null) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        LevelItem item = gson.fromJson(jsonArray.get(i).toString(), LevelItem.class);
                                        mGoodsList.add(item);
                                    }
                                }
                                String todayUserCount = jsonObject.getJSONObject("data").getString("todayUserCount");
                                String tomonthUserCount = jsonObject.getJSONObject("data").getString("tomonthUserCount");
                                mBinding.textView14.setText("本日新增(" + todayUserCount + ")");
                                mBinding.textView13.setText("本月新增(" + tomonthUserCount + ")");
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