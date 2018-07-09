package com.superc.bannerdairy.lock;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.OrderDetailAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.ActivityOrderDetailBinding;
import com.superc.bannerdairy.model.MessageBean;
import com.superc.bannerdairy.model.OrderItem;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.order.ContactServiceActivity;
import com.superc.bannerdairy.ui.order.OrderDetailActivity;
import com.superc.bannerdairy.ui.order.PayOrderActivity;
import com.superc.bannerdairy.ui.order.WlActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Amorr on 2017/12/6.
 * 我的订单详情界面
 */

public class OrderdetailLock extends BaseLock<ActivityOrderDetailBinding> {
    private int a;
    private OrderItem mData;
    private OrderDetailAdapter mAdapter;
    private ArrayList<OrderItem.OrderGoodsBean> mGoodsList;
    private List<View> mViews;
    private String mMobile = "";
    private List<MessageBean> mMessageBeenlist;//用来储存物流信息的集合
    private String mExpress_name = "";//物流名称
    private String mDanhao = "";


    public OrderdetailLock(Context context, ActivityOrderDetailBinding binding) {
        super(context, binding);
    }

    public OrderdetailLock(Context context, ActivityOrderDetailBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        a = mBundle.getInt("flag");
        mMessageBeenlist = new ArrayList<>();
        mGoodsList = new ArrayList<>();
        mViews = new ArrayList<>();
        //从我的订单列表拿数据
        if (mBundle.getParcelable("order") != null) {
            mData = mBundle.getParcelable("order");
            mMobile = mBundle.getString("mobile");
        }
        if (mBundle.getParcelableArrayList("goods") != null) {
            mGoodsList = mBundle.getParcelableArrayList("goods");
        }

        if (a == 1) {
            //待付款
            AppBarLock appBarLock = new AppBarLock(mContext, R.string.order_one);
            appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
            appBarLock.barData.isLeft = true;
            appBarLock.barData.titleLeft = "返回";
            mBinding.titleBar.setAppBarLock(appBarLock);
            mBinding.status.setText("待付款");
            mBinding.payStatus.setText("等待买家付款");


        } else if (a == 2) {
            //待审核
            AppBarLock appBarLock = new AppBarLock(mContext, R.string.order_two);
            appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
            appBarLock.barData.isLeft = true;
            appBarLock.barData.titleLeft = "返回";
            mBinding.titleBar.setAppBarLock(appBarLock);
            mBinding.status.setText("待审核");
            mBinding.payStatus.setText("等待商家审核");
            mBinding.bottomRl.setVisibility(View.GONE);

        } else if (a == 3) {
            //待发货
            AppBarLock appBarLock = new AppBarLock(mContext, R.string.order_three);
            appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
            appBarLock.barData.isLeft = true;
            appBarLock.barData.titleLeft = "返回";
            mBinding.titleBar.setAppBarLock(appBarLock);
            mBinding.status.setText("待发货");
            mBinding.payStatus.setText("等待商家发货");
            mBinding.bottomRl.setVisibility(View.GONE);

        } else {
            //订单详情
            AppBarLock appBarLock = new AppBarLock(mContext, R.string.order_four);
            appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
            appBarLock.barData.isLeft = true;
            appBarLock.barData.titleLeft = "返回";
            mBinding.titleBar.setAppBarLock(appBarLock);
            mBinding.status.setText("已完成");
            mBinding.payStatus.setText("卖家已发货");
            mBinding.bottomRl.setVisibility(View.GONE);
            mBinding.wlRel.setVisibility(View.VISIBLE);
            httpOrder(mData.getOrder_id());
        }
        //设置地址和订单号数据
        mBinding.orderName.setText(mData.getContact() + "");
        mBinding.orderPhone.setText(mMobile);

        mBinding.orderAddress.setText("收货地址:" + mData.address);
        mBinding.title.setText("旗帜奶粉");
        mBinding.price.setText("实付 ¥" + mData.getOrder_total());

        mBinding.orderCode.setText("订单编号：" + mData.getOrder_code());
        mBinding.orderJiaoyicode.setText("交易号：" + mData.getTransaction_code());
        mBinding.orderCreattime.setText("创建时间：" + mData.getCreated());
        mBinding.orderPaytime.setText("付款时间：" + mData.getPayment_time());
        mBinding.orderSendtime.setText("交易时间:" + mData.getDeliver_goods());
        mBinding.postage.setText("邮费 ¥" + mData.getPostage());
        showOgone(mBinding.orderCode, mData.getOrder_code());
        showOgone(mBinding.orderJiaoyicode, mData.getTransaction_code());
        showOgone(mBinding.orderCreattime, mData.getCreated());
        showOgone(mBinding.orderPaytime, mData.getPayment_time());
        showOgone(mBinding.orderSendtime, mData.getDeliver_goods());
        //填充商品信息
        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.orderDetaitRv.setLayoutManager(ms);
        mBinding.orderDetaitRv.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.recycle_view_line));
        if (mGoodsList != null) {
            mAdapter = new OrderDetailAdapter(mContext, mGoodsList, R.layout.item_treasure_two, BR.goodBean, a);
            mBinding.orderDetaitRv.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        }
//        mAdapter.setOnItemClickListener(new OrderFragmentAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(ViewDataBinding binding, int position) {
////                RunTime.setData(RunTime.ALLGOODS, mGoodsList.get(position));
//                //订单详情列表
//                Bundle bundle=new Bundle();
//                bundle.putInt("flag",a);
//                bundle.putParcelable("order",  mGoodsList.get(position));
//                startActivity(OrderDetailActivity.class,bundle);
//
//            }
//        });

        //取消订单
        mBinding.exitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowRemindDialog instance = ShowRemindDialog.getInstance();
                instance.showDialog(mContext, R.mipmap.babycry, "确定取消订单", false, null);
                instance.setBtNote("确定", "取消");
                instance.setOnSomeThingClickListener(new OnSomeThingClickListener() {
                    @Override
                    public void OnDoSomeThingClickListener(int view_id) {
                        CancelDingdan(mData.getOrder_id());
                    }
                });
                instance.setOnCancleClickListener(new ShowRemindDialog.OnCancleClickListener() {
                    @Override
                    public void OnCancleClickListener() {
                    }
                });
            }
        });
        //去支付
        mBinding.payOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a == 1) {
                    //去支付
                    Intent intent = new Intent(mContext, PayOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("diff", "2");
                    intent.putExtras(bundle);
                    intent.putExtra("orderId", mData.getOrder_id());
                    mContext.startActivity(intent);
                } else if (a == 2) {
                    //提醒审核
                    Toast.makeText(mContext, "pay" + 2, Toast.LENGTH_LONG).show();

                } else if (a == 3) {
                    //提醒发货
                    Toast.makeText(mContext, "pay" + 3, Toast.LENGTH_LONG).show();

                }
            }
        });
        //复制
        mBinding.orderCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(mData.getOrder_code());
                Toast.makeText(mContext, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showOgone(View v, String str) {
        if (str == null || str.equals("")) {
            v.setVisibility(View.GONE);
        }
    }

    /*取消订单操作*/
    public void CancelDingdan(String order_id) {
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
                            ((OrderDetailActivity) mContext).finish();
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

    /*跳转到联系客服界面*/
    public void goContactService() {
        startActivity(ContactServiceActivity.class);
    }

    /*拨打电话*/
    public void toCallPhone() {
        // TODO: 2018/1/27 暂时去掉
//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "15611397748"));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
    }


    //订单详情时调用接口获取物流信息
    public void httpOrder(String order_id) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ORDERLIST, RequestMethod.GET);
        request.add("order_id", order_id);
        request.add("act", "one");
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
                            JSONObject data = jsonObject.getJSONObject("data");
                            mExpress_name = data.getString("express_name");
                            JSONObject express = data.getJSONObject("express");
                            mDanhao = express.getString("nu");
                            JSONArray express_data = express.getJSONArray("data");
                            if (express_data != null && express_data.length() != 0) {
                                mBinding.wlAddress.setText("物流信息："+mExpress_name);
                                mBinding.wlTime.setText("快递单号："+mDanhao);
                                for (int i = 0; i < express_data.length(); i++) {
                                    JSONObject express_json = express_data.getJSONObject(0);
                                    MessageBean messageBean = new MessageBean(express_json.getString("time"), express_json.getString("ftime"), express_json.getString("context"));
                                    mMessageBeenlist.add(messageBean);
                                }
                            }

                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        Log.e("详情中物流信息解析失败", "数据为空或没拿到");
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

    public void togoWlDetail() {
        if (!mBinding.wlTime.getText().toString().equals("- -")) {
            Bundle bundle = new Bundle();
            bundle.putString("mExpress_name", mExpress_name);
            bundle.putString("danhao", mDanhao);
            bundle.putSerializable("wl_list", (Serializable) mMessageBeenlist);
            bundle.putParcelableArrayList("goods", mGoodsList);
            startActivity(WlActivity.class, bundle);
        }
    }


}
