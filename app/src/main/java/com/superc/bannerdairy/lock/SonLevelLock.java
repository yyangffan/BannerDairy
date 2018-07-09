package com.superc.bannerdairy.lock;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.FlatLevelAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.ActivitySonLevelBinding;
import com.superc.bannerdairy.model.LevelItem;
import com.superc.bannerdairy.ui.manage.OnePersonActivity;
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
 * Created by Amorr on 2017/11/23.
 * 子代理，i县级a代理，门店代理，特约代理，零售代理
 */

public class SonLevelLock extends BaseLock<ActivitySonLevelBinding> {
    private List<LevelItem> mGoodsList;
    private FlatLevelAdapter mAdapter;
    private int flag = 1;
    private String mName;
    private String mUser_id;
    private AppBarLock mAppBarLock;
    public boolean isName = false;

    public SonLevelLock(Context context, ActivitySonLevelBinding binding) {
        super(context, binding);
    }

    public SonLevelLock(Context context, ActivitySonLevelBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        //0:注册用户  1：特约代理，2：门店代理，3：区县代理，4：市级代理，5：省级代理',
        if (mBundle.getString("flag").equals("zero")) {
            flag = 0;
            mAppBarLock = new AppBarLock(mContext, R.string.zhu);
        } else if (mBundle.getString("flag").equals("one")) {
            flag = 1;
            mAppBarLock = new AppBarLock(mContext, R.string.te);
        } else if (mBundle.getString("flag").equals("two")) {
            flag = 2;
            mAppBarLock = new AppBarLock(mContext, R.string.men);
        } else if (mBundle.getString("flag").equals("three")) {
            flag = 3;
            mAppBarLock = new AppBarLock(mContext, R.string.xian);
        } else if (mBundle.getString("flag").equals("four")) {
            flag = 4;
            mAppBarLock = new AppBarLock(mContext, R.string.shi);
        } else if (mBundle.getString("flag").equals("five")) {
            flag = 5;
            mAppBarLock = new AppBarLock(mContext, R.string.sheng);
        } else if (mBundle.getString("flag").equals("name")) {
            mName = mBundle.getString("name");
            mUser_id = mBundle.getString("user_id");
            mAppBarLock = new AppBarLock(mContext, mName + "的团队");
            isName = true;
        }
        mAppBarLock.barData.isLeft = true;
        mAppBarLock.barData.titleLeft = "返回";
        mAppBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(mAppBarLock);
        httpPeroData();

        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.rvSonlevel.setLayoutManager(ms);
        mBinding.rvSonlevel.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<>();
        mAdapter = new FlatLevelAdapter(mContext, mGoodsList, R.layout.item_level_same, BR.levelItem);
//        mAdapter = new CBaseRecyclerViewAdapter(mContext, mGoodsList, R.layout.item_level_same, BR.flatFramentLock);
        mBinding.rvSonlevel.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        mAdapter.setOnItemClickListener(new FlatLevelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                // 县级代理
                Bundle bundle = new Bundle();
                bundle.putString("flag", mBundle.getString("flag"));
                bundle.putString("user_id", mGoodsList.get(position).getUser_id());
                bundle.putString("teamCount",mGoodsList.get(position).getTeamCount());
                startActivity(OnePersonActivity.class, bundle);
            }
        });
    }

    public void httpPeroData() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.GETLOWELLIST, RequestMethod.POST);
        final Gson gson = new Gson();
        if (isName) {
            request.add("user_id", mUser_id);
        } else {
            request.add("user_id", MyApplication.getUser().getUser_id());
            request.add("agency_level", flag);
        }
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
                            //填充数据
                            if (jsonObject.getJSONArray("data") != null) {
                                mGoodsList.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                if (jsonArray != null) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        LevelItem item = gson.fromJson(jsonArray.get(i).toString(), LevelItem.class);
                                        mGoodsList.add(item);
                                    }
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
