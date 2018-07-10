package com.superc.bannerdairy.lock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.ColumeRecyAdapter;
import com.superc.bannerdairy.adapter.HomeLockAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.constant.NetworkImageHolderView;
import com.superc.bannerdairy.constant.RunTime;
import com.superc.bannerdairy.databinding.FragmentHomeBinding;
import com.superc.bannerdairy.model.Banner;
import com.superc.bannerdairy.model.ColumItem;
import com.superc.bannerdairy.model.TreasureItem;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.goods.AllGoodsActivity;
import com.superc.bannerdairy.ui.goods.GoodsDetailActivity;
import com.superc.bannerdairy.ui.home.MainActivity;
import com.superc.bannerdairy.ui.manage.PersonManageActivity;
import com.superc.bannerdairy.ui.manage.SpreadknowledgeActivity;
import com.superc.bannerdairy.ui.order.OrderManageActivity;
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
 * 创建日期：2017/11/7 on 15:57
 * 描述：
 */

public class HomeLock extends BaseLock<FragmentHomeBinding> {
    private List<TreasureItem> mGoodsList;
    private List<String> bannerList;
    public HomeLockAdapter mAdapter;
    //判断公告
    public static Boolean isfirst = true;
    private MainActivity mMainActivity;


    private List<ColumItem> mColumItems;
    private ColumeRecyAdapter mRyAdapter;


    public HomeLock(Context context, FragmentHomeBinding binding) {
        super(context, binding);
    }

    public HomeLock(Context context, FragmentHomeBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        mMainActivity = (MainActivity) mContext;

        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.rvHome.setLayoutManager(ms);
        mBinding.rvHome.addItemDecoration(new RecycleViewDivider(
                mContext, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<TreasureItem>();
        bannerList = new ArrayList<>();
        mColumItems = new ArrayList<>();
        httpColumn();
        httpGoods();
        httpBanner();
        if (isfirst) {
            httpSystem();

        }
        mAdapter = new HomeLockAdapter(mContext, mGoodsList, R.layout.item_treasure, BR.treasureItem);
//        mBinding.rvHome.setAdapter(mAdapter); // 这里或者在xml里设置adapter

        mAdapter.setOnItemClickListener(new HomeLockAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                RunTime.setData(RunTime.ALLGOODS, mGoodsList.get(position));
                if (MyApplication.getUser().user_id == null) {
                    ShowRemindDialog.getInstance().gotoLogin(mContext);
                } else {
                    startActivity(GoodsDetailActivity.class);
                }
            }
        });
        GridLayoutManager gm = new GridLayoutManager(mContext, 5);
        mRyAdapter = new ColumeRecyAdapter(mContext, mColumItems);
        mBinding.homeColumn.setLayoutManager(gm);
        mBinding.homeColumn.setAdapter(mRyAdapter);

        mRyAdapter.setOnItemIClickListener(new ColumeRecyAdapter.OnItemIClickListener() {
            @Override
            public void OnItemIClickListenerColum(int pos) {
                ColumItem columItem = mColumItems.get(pos);
                Intent intent = null;
                Bundle bundle = new Bundle();
                bundle.putString("category_id", columItem.getCategoryId());
                String titleName = columItem.getMenuName();
                switch (columItem.getMenuType()) {
                    case "order":/*订单管理*/
                        intent = new Intent(mContext, OrderManageActivity.class);
                        intent.putExtras(bundle);
                        if (MyApplication.getUser().user_id == null) {
                            ShowRemindDialog.getInstance().gotoLogin(mContext);
                        } else {
                            mContext.startActivity(intent);
                        }
                        break;
                    case "user":/*人员管理*/
                        intent = new Intent(mContext, PersonManageActivity.class);
                        intent.putExtras(bundle);
                        if (MyApplication.getUser().user_id == null) {
                            ShowRemindDialog.getInstance().gotoLogin(mContext);
                        } else {
                            mContext.startActivity(intent);
                        }
                        break;
                    case "goods":/*全部商品*/
                        intent = new Intent(mContext, AllGoodsActivity.class);
                        intent.putExtras(bundle);
                        if (MyApplication.getUser().user_id == null) {
                            ShowRemindDialog.getInstance().gotoLogin(mContext);
                        } else {
                            mContext.startActivity(intent);
                        }
                        break;
                    case "article":/*传播知识*/
                        intent = new Intent(mContext, SpreadknowledgeActivity.class);
                        bundle.putString("title", titleName);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                        break;
                    case "show":/*买家秀 */
                        mMainActivity.mLock.showPageMsg(columItem.getCategoryId());
                        break;
                    default:
                        ToastUtil.show(mContext, "未找到该栏目", Toast.LENGTH_SHORT);
                        break;
                }

            }
        });
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                httpGoods();
            }
        });
    }

    /*获取栏目名称*/
    public void httpColumn() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.COLUMNLIST, RequestMethod.GET);
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
                        if (success) {
                            mColumItems.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                ColumItem columItem = new Gson().fromJson(jsonArray.get(i).toString(), ColumItem.class);
                                mColumItems.add(columItem);
                            }
                            mRyAdapter.notifyDataSetChanged();
                        }
                        show = jsonObject.getBoolean("show");
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
                if (MyApplication.getUser().user_id != null) {
                    showToast("网络异常");
                }
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }
    //全部商品
    public void home5() {
        if (MyApplication.getUser().user_id == null) {
            ShowRemindDialog.getInstance().gotoLogin(mContext);
        } else {
            startActivity(AllGoodsActivity.class);
        }
    }

    //获取宝贝推荐列表
    public void httpGoods() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LIST_GOODS, RequestMethod.GET);
        request.add("act", "list");
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    mBinding.refreshLayout.finishRefresh();
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        if (success) {
                            mGoodsList.clear();
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                TreasureItem item = new Gson().fromJson(jsonArray.get(i).toString(), TreasureItem.class);
                                mGoodsList.add(item);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        show = jsonObject.getBoolean("show");
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
                if (MyApplication.getUser().user_id != null) {
                    showToast("网络异常");
                }
            }

            @Override
            public void onFinish(int what) {
                mBinding.refreshLayout.finishRefresh();
            }
        });
    }
/*系统公告*/
    private void httpSystem() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.SYSTEM, RequestMethod.GET);
        request.add("act", "one");
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
                        if (success) {
                            JSONObject object = jsonObject.getJSONObject("data");
                            String count = object.getString("announcement");
                            initDialog(count);
                        }
                        show = jsonObject.getBoolean("show");
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
                if (MyApplication.getUser().user_id != null) {
                    showToast("网络异常");
                }
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }

    //首页公告dialog
    private Dialog initDialog(String count) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.redbao_dialog, null);
        TextView success = (TextView) v.findViewById(R.id.success);
        TextView coun = (TextView) v.findViewById(R.id.count);
        ScrollView scrollView=v.findViewById(R.id.redbao_dialog_scroll);
        ViewGroup.LayoutParams layoutParams = scrollView.getLayoutParams();
        if(count.length()>150){
            layoutParams.height=dp2px(mContext,280);
        }
        scrollView.setLayoutParams(layoutParams);

        coun.setText(count);
        builder.setView(v);
        final AlertDialog dialog = builder.create();
        //设置背景为透明色
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isfirst = false;
                dialog.dismiss();
            }
        });
        return dialog;
    }
    /**
     * dp转换成px
     */
    private int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }

    private void httpBanner() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.BANNER, RequestMethod.POST);
        final Gson gson = new Gson();
        request.add("act", "list");
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
                            if (jsonObject.getJSONArray("data") != null) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Banner banner = gson.fromJson(jsonArray.get(i).toString(), Banner.class);
                                    bannerList.add(banner.getBanner_image());
                                }
                                initBanner();
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
                if (MyApplication.getUser().user_id != null) {
                    showToast("网络异常");
                }
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }

    //设置bannner图片
    private void initBanner() {
        List list = new ArrayList();
        for (int a = 0; a < bannerList.size(); a++) {
            mBinding.banner.setBackground(null);
            list.add(ConnectUrl.REQUESTURL + bannerList.get(a));
//            }
            if (bannerList.size() > 1) {
                mBinding.banner.startTurning(4000);
            }
            mBinding.banner.setPages(new CBViewHolderCreator() {

                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, list)
                    .setPageIndicator(new int[]{R.drawable.circle_grey, R.drawable.circle_blue})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
//                    .setOnPageChangeListener((ViewPager.OnPageChangeListener) this).setOnItemClickListener((OnItemClickListener) this);

        }
    }
}
