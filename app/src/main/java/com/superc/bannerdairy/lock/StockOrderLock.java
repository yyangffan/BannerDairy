package com.superc.bannerdairy.lock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityStockOrderBinding;
import com.superc.bannerdairy.model.MAddress;
import com.superc.bannerdairy.model.StockItem;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.mine.StockOrderActivity;
import com.superc.bannerdairy.ui.mine.StockPayActivity;
import com.superc.bannerdairy.ui.mine.stock_adapter.StockOrderDadAdapter;
import com.superc.bannerdairy.ui.order.SelectAddressActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.superc.bannerdairy.R.string.te;

/**
 * 库存的确认订单界面
 */

public class StockOrderLock extends BaseLock<ActivityStockOrderBinding> {
    public String user_address_id;
    public MAddress item;
    public int pospos = -2;
    public List<StockItem.DataBeanX> mStockOrderList;
    public StockOrderDadAdapter mStockOrderDadAdapter;
    public String express_fee = "0";//总邮费
    public String express_fee_e = "0";//接口单个总
    public String express_fee_num = "0";//单听的邮费价格
    private int num = 0;/*数量用来计算邮费*/
    private String mOrder_goods_id;
    private String mOrder_stock_num;


    public StockOrderLock(Context context, ActivityStockOrderBinding binding) {
        super(context, binding);
    }

    public StockOrderLock(Context context, ActivityStockOrderBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.order_firm);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        httpAddress();/*获取默认地址*/
        /*下面是将传过来的数据进行显示*/
        if (mBundle != null) {
            mStockOrderList = (List<StockItem.DataBeanX>) mBundle.getSerializable("data");
        }
        if (mStockOrderList != null) {
            for (int i = mStockOrderList.size() - 1; i >= 0; i--) {
                StockItem.DataBeanX dataBeanX = mStockOrderList.get(i);
                List<StockItem.DataBeanX.DataBean> data = dataBeanX.getData();
                for (int j = data.size() - 1; j >= 0; j--) {
                    StockItem.DataBeanX.DataBean dataBean = data.get(j);
                    if (!dataBean.isCheck()) {
                        data.remove(dataBean);
                    } else {
                        Log.e("传过来的数据:", dataBean.toString() + "\n");
                    }
                }
                if (data.size() == 0) {/*如下是为了移除掉显示时为数组零的数据*/
                    mStockOrderList.remove(dataBeanX);
                }
            }
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        mBinding.stockOrderRv.setLayoutManager(gridLayoutManager);
        mStockOrderDadAdapter = new StockOrderDadAdapter(mStockOrderList, mContext);
        mBinding.stockOrderRv.setAdapter(mStockOrderDadAdapter);
    }

    /*跳转到选择地址*/
    public void goSelectAdd() {
        Intent intent = new Intent(mContext, SelectAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pospos", pospos);
        intent.putExtras(bundle);
        ((StockOrderActivity) mContext).startActivityForResult(intent, 110);
    }

    /*去支付*/
    public void goPay() {
        if (user_address_id == null||user_address_id.equals("")) {
            showToast("请设置默认地址");
            return;
        }
        getMsg();/*获取上传字段的数据*/
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ORDERSTOCK, RequestMethod.POST);
        request.add("user_id", MyApplication.getUser().getUser_id());
        request.add("order_goods_stock_id", mOrder_goods_id);/*库存id多个逗号分隔*/
        request.add("order_stock_num", mOrder_stock_num);/*申请发货的数量，多个逗号分隔*/
        request.add("address", user_address_id);/*地址id*/
        request.add("postage", express_fee);/*邮费*/
        request.add("user_remarks", mBinding.orderEdtBeizhu.getText().toString());/*备注*/
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
                            JSONObject jsonO = jsonObject.getJSONObject("data");
                            final String data = jsonO.getString("order_id");
                            String is_pay = jsonO.getString("is_pay");
                            if (is_pay.equals("1")) {/*需要支付时*/
                                Bundle bundle = new Bundle();
                                bundle.putString("orderId", data);
                                bundle.putString("expree", express_fee);
                                Intent intent = new Intent(mContext, StockPayActivity.class);
                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                                ((BaseActivity) mContext).finish();
                            } else if (is_pay.equals("0")) {/*无需支付时*/
                                ShowRemindDialog.getInstance().showDialog(mContext, R.drawable.chenggong1, "无需支付", false, new OnSomeThingClickListener() {
                                    public void OnDoSomeThingClickListener(int view_id) {
                                        startPay(data, "4");
                                    }
                                });
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

    /*库存无邮费时调用该接口type传4*/
    public void startPay(String orderId, String type) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ORDERPAY, RequestMethod.POST);
        request.add("user_id", MyApplication.getUser().getUser_id());
        request.add("order_id", orderId);
        request.add("pay_type", type);

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
                            ((StockOrderActivity) mContext).finish();
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


    public void getMsg() {
        mOrder_goods_id = "";
        mOrder_stock_num = "";
        for (StockItem.DataBeanX dataBeanX : mStockOrderList) {
            for (int i = 0; i < dataBeanX.getData().size(); i++) {
                StockItem.DataBeanX.DataBean dataBean = dataBeanX.getData().get(i);
                mOrder_goods_id += dataBean.getOrderGoodsStockId() + ",";
                mOrder_stock_num += dataBean.getNum() + ",";
            }
        }
        mOrder_goods_id = mOrder_goods_id.substring(0, mOrder_goods_id.length() - 1);
        mOrder_stock_num = mOrder_stock_num.substring(0, mOrder_stock_num.length() - 1);


    }


    //获取默认地址
    public void httpAddress() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.GETADDRESS, RequestMethod.GET);
        request.add("act", "user");
        request.add("creator", MyApplication.getUser().user_id);
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
                            if (jsonObject.getBoolean("show")) {
                                mBinding.tvName.setText("点击添加收货地址");
                            } else {
                                item = new Gson().fromJson(jsonObject.getString("data").toString(), MAddress.class);
                                mBinding.tvName.setText(item.getContact() + "     " + item.mobile);
                                mBinding.tvAddress.setText("收货地址:" + item.province + "" + item.city + item.area + item.address);
                                user_address_id = item.user_address_id;
                                /*计算邮费==返回的express_fee+返回的express_num_fee*（商品数量-2）*/
                                express_fee_num = item.getExpressFee().getExpressNumFee();
                                express_fee_e = item.getExpressFee().getExpressFee();
                                showPriceAgin();
//                                float allparice = Float.parseFloat(mBinding.shangpinPrice.getText().toString()) + Float.parseFloat(express_fee);
//                                mBinding.price.setText(mDecimalFormat.format(allparice));//计算邮费后的总价
                            }

                        } else if (!success) {
                            mBinding.tvName.setText("点击添加收货地址");
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

    /*计算邮费==返回的express_fee+返回的express_num_fee*（商品数量-2）*/
    public void showPriceAgin() {
        num = 0;
        for (StockItem.DataBeanX dataBeanX : mStockOrderList) {
            for (StockItem.DataBeanX.DataBean dataBean : dataBeanX.getData()) {
                num += Integer.parseInt(dataBean.getNum());
            }
        }
        express_fee = (Float.parseFloat(express_fee_e) + ((num - 2) * (Float.parseFloat(express_fee_num)))) + "";
        mBinding.price.setText(express_fee);
    }

}
