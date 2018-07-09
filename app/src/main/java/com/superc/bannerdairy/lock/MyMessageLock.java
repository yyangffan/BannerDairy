package com.superc.bannerdairy.lock;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityMyMessageBinding;
import com.superc.bannerdairy.model.MyMessageItem;
import com.superc.bannerdairy.ui.mine.MessageDetailActivity;
import com.superc.bannerdairy.ui.mine.MessageRvAdapter;
import com.superc.bannerdairy.ui.mine.MyMessageActivity;
import com.superc.cframework.base.ui.CBaseRecyclerViewAdapter;
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
 * 创建日期：2017/11/9 on 16:17
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class MyMessageLock extends BaseLock<ActivityMyMessageBinding> {

    private MessageRvAdapter mAdapter;
    private List<MyMessageItem> mItemList;
    public int page=1;

    public MyMessageLock(Context context, ActivityMyMessageBinding binding) {
        super(context, binding);
    }

    public MyMessageLock(Context context, ActivityMyMessageBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        mItemList = new ArrayList<MyMessageItem>();
        LinearLayoutManager ms = new LinearLayoutManager(mContext);
        mBinding.rvMessage.setLayoutManager(ms);
        mAdapter = new MessageRvAdapter(mContext, mItemList, R.layout.item_my_message, BR.myMessageItem);
        mBinding.rvMessage.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        mAdapter.setOnItemClickListener(new CBaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                MyMessageItem item = mItemList.get(position);
                String noticeId = item.getNoticeId();
                String noticeTitle = item.getNoticeTitle();
                String created = item.getCreated();
                mContext.startActivity(MessageDetailActivity.getCallIntent(mContext, noticeId, noticeTitle, created));

            }
        });
        getMessage();//获取消息
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=1;
                mBinding.refreshLayout.setEnableLoadmore(true);
                getMessage();//获取消息
            }
        });
        mBinding.refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getMessage();//获取消息
            }
        });
    }

    public void onBackClick() {
        ((MyMessageActivity) mContext).finish();
    }

    public void onMenuClick() {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.message_content, null);
        final PopupWindow popW = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tv_read = contentView.findViewById(R.id.menu_allRead);
        TextView tv_delete = contentView.findViewById(R.id.menu_allDelete);
        if (popW.isShowing()) {
            popW.dismiss();
        } else {
            popW.showAsDropDown(mBinding.titleMenuTv, Gravity.BOTTOM, 0, mBinding.titleMenuTv.getLayoutParams().width);
        }
        tv_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allControl(true);
                popW.dismiss();
            }
        });
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allControl(false);
                popW.dismiss();
            }
        });



    }

    /*获取消息*/
    public void getMessage() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        final Gson gson = new Gson();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.NOTICELIST, RequestMethod.GET);
        if (MyApplication.getUser().getUser_id() != null) {
            request.add("user_id", MyApplication.getUser().getUser_id());
        }
        request.add("act", "list");
        request.add("page",page);
        request.add("p","10");

        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mBinding.refreshLayout.finishRefresh();
                if (what == 2) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray dataa = data.getJSONArray("data");
                            if(page==1) {
                                mItemList.clear();
                            }
                            for (int i = 0; i < dataa.length(); i++) {
                                MyMessageItem item = gson.fromJson(dataa.get(i).toString(), MyMessageItem.class);
                                mItemList.add(item);
                            }
                            if(dataa.length()<10){
                                mBinding.refreshLayout.setEnableLoadmore(false);
                            }
                            page++;
                            mAdapter.notifyDataSetChanged();
                            Log.e("消息数据", response.toString());
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
                mBinding.refreshLayout.finishRefresh();
                mBinding.refreshLayout.finishLoadmore();
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
                mBinding.refreshLayout.finishRefresh();
                mBinding.refreshLayout.finishLoadmore();
            }
        });
    }


    /*已读还是删除*/
    public void allControl(final boolean isRead) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = null;
        final Gson gson = new Gson();
        if (isRead) {/*已读*/
            request = NoHttp.createJsonObjectRequest(ConnectUrl.READALLNOTICE, RequestMethod.GET);
        } else {/*删除*/
            request = NoHttp.createJsonObjectRequest(ConnectUrl.DELETEALLNOTICE, RequestMethod.GET);
        }
        if (MyApplication.getUser().getUser_id() != null) {
            request.add("user_id", MyApplication.getUser().getUser_id());
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
                            if(!isRead) {
                                mItemList.clear();
                                mAdapter.notifyDataSetChanged();
                            }else {
                                getMessage();//获取消息
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
                Log.e("错误日志", response.toString());
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }


}
