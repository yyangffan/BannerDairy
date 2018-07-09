package com.superc.bannerdairy.ui.manage;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.TeamPerAdapter;
import com.superc.bannerdairy.base.BaseFragment;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.FragmentTeamPerBinding;
import com.superc.bannerdairy.lock.TeamProFramentLock;
import com.superc.bannerdairy.model.TeamProItem;
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

//团队业绩fragment

@SuppressLint("ValidFragment")
public class TeamPerFragment extends BaseFragment {
    private FragmentTeamPerBinding mBinding;
    private TeamProFramentLock mLock;
    private List<TeamProItem> mGoodsList;
    private TeamPerAdapter mAdapter;
    private int a = 1;
    private RequestQueue mMRequestQueue;

    @SuppressLint("ValidFragment")
    public TeamPerFragment(int i) {
        super();
        this.a = i;
    }

    @Override
    protected View initBinding(LayoutInflater inflater, ViewGroup container) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_team_per, container, false);
        mLock = new TeamProFramentLock(mActivity, mBinding);
        mBinding.setTeamPerFramentLock(mLock);
        return mBinding.getRoot();
    }

    @Override
    protected void init() {
        GridLayoutManager ms = new GridLayoutManager(mActivity, 1);
        mBinding.rvTeampro.setLayoutManager(ms);
//        mBinding.rvTeampro.addItemDecoration(new RecycleViewDivider(mActivity, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<>();
        mMRequestQueue = NoHttp.newRequestQueue();
        httpData();
        getAllYj();
        mAdapter = new TeamPerAdapter(mActivity, mGoodsList, R.layout.item_team_pro, BR.teamProItem);
        mBinding.rvTeampro.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        mAdapter.setOnItemClickListener(new TeamPerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                //我的邀请

            }
        });
    }


    private void httpData() {

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.TEAMPERFORMANCE, RequestMethod.POST);
        final Gson gson = new Gson();
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().getUser_id());
        }
        request.add("type", a);
        request.add("act", "list");
        mMRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
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
                                    TeamProItem item = gson.fromJson(jsonArray.get(i).toString(), TeamProItem.class);
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

    public void getAllYj() {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.TEAMPERFORMANCE, RequestMethod.POST);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().getUser_id());
        }
        request.add("type", a);
        request.add("act", "total");
        mMRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
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
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                TeamProItem item = null;
                                if (jsonArray.length() != 0) {
                                    item = new Gson().fromJson(jsonArray.get(0).toString(), TeamProItem.class);
                                }
                                switch (a) {
                                    case 1:
                                        mBinding.tvYj.setText("今日总业绩:" + (item == null ? "0" : item.getGroup_buy_number())+"听");
                                        break;
                                    case 2:
                                        mBinding.tvYj.setText("本周总业绩:" + (item == null ? "0" : item.getGroup_buy_number())+"听");
                                        break;
                                    case 3:
                                        mBinding.tvYj.setText("本月总业绩:" + (item == null ? "0" : item.getGroup_buy_number())+"听");
                                        break;
                                    case 4:
                                        mBinding.tvYj.setText("上个月总业绩:" + (item == null ? "0" : item.getGroup_buy_number())+"听");
                                        break;
                                }
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
