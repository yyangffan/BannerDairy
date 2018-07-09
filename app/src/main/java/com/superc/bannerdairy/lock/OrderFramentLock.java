package com.superc.bannerdairy.lock;


import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.OrderFragmentAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.ActivityOrderFragmentBinding;
import com.superc.bannerdairy.model.OrderItem;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.order.OrderDetailActivity;
import com.superc.bannerdairy.ui.order.PayOrderActivity;
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
 * Created by Amorr on 2017/11/16.
 * 待付款fragment
 */

public class OrderFramentLock extends BaseLock<ActivityOrderFragmentBinding> {
    private List<OrderItem> mGoodsList;
    private OrderFragmentAdapter mAdapter;
    private int a = 1;
    private List<String> ordidList;
    private float price = 0;
    int page = 1;

    public OrderFramentLock(Context context, ActivityOrderFragmentBinding binding) {
        super(context, binding);
    }

    public OrderFramentLock(Context context, ActivityOrderFragmentBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.rvOrders.setLayoutManager(ms);
        mBinding.rvOrders.addItemDecoration(new RecycleViewDivider(
                mContext, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<OrderItem>();
        ordidList = new ArrayList<>();
        a = mBundle.getInt("flag");
        if (a == 1) {
            mBinding.bottomRl.setVisibility(View.VISIBLE);
        }
        mAdapter = new OrderFragmentAdapter(mContext, mGoodsList, R.layout.item_treasure_order, BR.orderFramentLock, a);
        mBinding.rvOrders.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        mAdapter.setOnItemClickListener(new OrderFragmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
//                RunTime.setData(RunTime.ALLGOODS, mGoodsList.get(position));
                //订单详情列表
                a = mBundle.getInt("flag");
                Bundle bundle = new Bundle();
                bundle.putInt("flag", a);
                bundle.putParcelable("order", mGoodsList.get(position));
                bundle.putString("mobile", mGoodsList.get(position).getMobile());
                String mobile = mGoodsList.get(position).getMobile();
                bundle.putParcelableArrayList("goods", (ArrayList<? extends Parcelable>) mGoodsList.get(position).getOrder_goods());
                startActivity(OrderDetailActivity.class, bundle);
            }
        });
        /*待付款中的CheckBox是否选择*/
        mAdapter.setOnCheckClick(new OrderFragmentAdapter.OnCheckbClickListener() {
            @Override
            public void onCheckClick(ViewDataBinding binding, int position, boolean isCheck) {
                String mOrder_id = mGoodsList.get(position).getOrder_id();
                if (isCheck) {
                    ordidList.add(mOrder_id);
                    price += Float.parseFloat(mGoodsList.get(position).getOrder_total());
                } else {
                    if (ordidList.contains(mOrder_id)) {
                        ordidList.remove(mOrder_id);
                        price -= Float.parseFloat(mGoodsList.get(position).getOrder_total());
                    }
                }
                mBinding.price.setText(price + "");
            }
        });
        /*取消订单监听*/
        mAdapter.setOnCancelDingdan(new OrderFragmentAdapter.OnCancelDingdan() {
            @Override
            public void OnCancelDingDan(final int position) {
                ShowRemindDialog instance = ShowRemindDialog.getInstance();
                instance.showDialog(mContext, R.mipmap.babycry, "确定取消订单", false, null);
                instance.setBtNote("确定", "取消");
                instance.setOnSomeThingClickListener(new OnSomeThingClickListener() {
                    @Override
                    public void OnDoSomeThingClickListener(int view_id) {
                        OrderItem orderItem = mGoodsList.get(position);
                        CancelDingdan(orderItem.getOrder_id(), position);
                    }
                });
                instance.setOnCancleClickListener(new ShowRemindDialog.OnCancleClickListener() {
                    @Override
                    public void OnCancleClickListener() {
                    }
                });
//                ShowRemindDialog.getInstance().showDialog(mContext, R.mipmap.babycry, "确定取消订单", false, new OnSomeThingClickListener() {
//                    @Override
//                    public void OnDoSomeThingClickListener(int view_id) {
//                        OrderItem orderItem = mGoodsList.get(position);
//                        CancelDingdan(orderItem.getOrder_id(), position);
//                    }
//                });
            }
        });


        httpOrder(a);
        //选择支付
        mBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //去支付(还没写完选择)
                String zhifu_str = "";
                Intent intent = new Intent(mContext, PayOrderActivity.class);
                if (ordidList.size() != 0) {
                    for (int i = 0; i < ordidList.size() - 1; i++) {
                        zhifu_str += ordidList.get(i) + ",";
                    }
                    zhifu_str += ordidList.get(ordidList.size() - 1);
                } else {
                    ToastUtil.show(mContext, "请选择要付款的商品", Toast.LENGTH_SHORT);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("diff", "2");
                intent.putExtras(bundle);
                intent.putExtra("orderId", zhifu_str);
//                intent.putExtra("orderId", mGoodsList.get(0).getOrder_id());
                mContext.startActivity(intent);
            }
        });
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                a = mBundle.getInt("flag");
                page = 1;
                httpOrder(a);
            }
        });
        mBinding.refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                a = mBundle.getInt("flag");
                ++page;
                httpOrder(a);
            }
        });
    }

    //获取待付款订单列表
    public void httpOrder(int pa) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ORDERLIST, RequestMethod.GET);
        request.add("act", "list");
        if (pa == 1) {  //待付款
            request.add("order_status", 0);            //订单状态 0：未结束   1：结束   2：完成
            request.add("payment_status", 0);          //支付状态 0：未支付   1：已支付
            request.add("delivery_status", 0);         //配送状态 0：未配货   1：已配货   2：已发货   3：库存
            request.add("return_status", 0);           //退换状态 0：正常   1：退货   2：换货
            request.add("order_type", 0);              //待付款(用来区分掉库存)
        } else if (pa == 2) {//待审核
            request.add("order_status", 0);
            request.add("payment_status", 1);
            request.add("delivery_status", 0);
            request.add("return_status", 0);
        } else if (pa == 3) {  //待发货
            request.add("order_status", 0);
            request.add("payment_status", 1);
            request.add("delivery_status", 1);
            request.add("return_status", 0);
        } else { //已发货
//            request.add("order_status", 0);
            request.add("payment_status", 1);
            request.add("delivery_status", 2);
            request.add("return_status", 0);
        }
        if (MyApplication.getUser() != null) {
            request.add("creator", MyApplication.getUser().user_id);
        }
        request.add("p", "10");
        request.add("page", page);
        mRequestQueue.add(3, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mBinding.refreshLayout.finishRefresh();
                mBinding.refreshLayout.finishLoadmore();
                if (what == 3) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            if (page == 1) {
                                mGoodsList.clear();
                                price = 0;
                                ordidList.clear();
                                mBinding.price.setText(price + "");
                            }
                            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                OrderItem item = new Gson().fromJson(jsonArray.get(i).toString(), OrderItem.class);
                                item.setCheck(false);
//                                  mGoodList.add(new TreasureItem(item.getId(),item.getGoods_name(),item.getGoods_type(),item.getGoods_original_price(),item.getCreated(),item.goods_cover_image));
                                mGoodsList.add(item);
                            }
                            mAdapter.notifyDataSetChanged();
//                            for (TreasureItem info : mGoodsList) {
//                                mGoodsList.add(new TreasureItem(info.id,info.goods_name,info.goods_type,info.goods_original_price,info.created,info.goods_cover_image));
//                            }
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

    public void CancelDingdan(String order_id, final int postion) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.RETURNORDER, RequestMethod.POST);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("order_id", order_id);
        mRequestQueue.add(3, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 3) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            mGoodsList.remove(postion);
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

    /*重设数据*/
    public void resetData() {
        page = 1;
        price = 0;
        ordidList.clear();
        a = mBundle.getInt("flag");
        httpOrder(a);
        mBinding.price.setText(price + "");
    }
}
