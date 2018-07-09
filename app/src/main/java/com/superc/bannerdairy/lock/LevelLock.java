package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.adapter.LowerLevelRAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.FragmentLevelLowerBinding;
import com.superc.bannerdairy.model.LevelCountList;
import com.superc.bannerdairy.ui.manage.SonLevelActivity;
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

import static android.R.attr.level;

/**
 * Created by Amorr on 2017/11/18.
 * 下级推广
 */

public class LevelLock extends BaseLock<FragmentLevelLowerBinding> {
    private List<LevelCountList>  mLevelCountLists;
    private LowerLevelRAdapter mAdapter;

    public LevelLock(Context context, FragmentLevelLowerBinding binding) {
        super(context, binding);
    }

    public LevelLock(Context context, FragmentLevelLowerBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        mLevelCountLists=new ArrayList<>();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,1);
        mBinding.levelLowerRv.setLayoutManager(gridLayoutManager);
        mAdapter = new LowerLevelRAdapter(mLevelCountLists,mContext);
        mBinding.levelLowerRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new LowerLevelRAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(int position) {
                LevelCountList countList = mLevelCountLists.get(position);
                String level=countList.getAgency_level();
                Bundle bundle = new Bundle();
                bundle.putString("flag", "one");
                switch (level) {
                    case "0":bundle.putString("flag", "zero");break;
                    case "1":bundle.putString("flag", "one");break;
                    case "2":bundle.putString("flag", "two");break;
                    case "3":bundle.putString("flag", "three");break;
                    case "4":bundle.putString("flag", "four");break;
                    case "5":bundle.putString("flag", "five");;break;
                }
                startActivity(SonLevelActivity.class, bundle);
            }
        });
        //获取下级列表
        httpData();
    }

    //获取下级列表
    private void httpData() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.GETLOWEL, RequestMethod.POST);
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
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            //本日和本月新增会员
                            String todayUserCount = jsonObject1.getString("todayUserCount");
                            String tomonthUserCount = jsonObject1.getString("tomonthUserCount");
                            mBinding.textView14.setText("本日新增(" + todayUserCount + ")");
                            mBinding.textView13.setText("本月新增(" + tomonthUserCount + ")");
                            JSONArray jsonArray = jsonObject1.getJSONArray("levelCountList");
                            if(jsonArray!=null){
                              for(int i=0;i<jsonArray.length();i++){
                                  LevelCountList countList=gson.fromJson(jsonArray.get(i).toString(),LevelCountList.class);
                                  mLevelCountLists.add(countList);
                              }
                            }
                            mAdapter.notifyDataSetChanged();
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
