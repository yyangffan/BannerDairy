package com.superc.bannerdairy.lock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.google.gson.Gson;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.CarFirmAdapter;
import com.superc.bannerdairy.adapter.FirmOrderAdapter;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.constant.RunTime;
import com.superc.bannerdairy.databinding.ActivityFirmOrderBinding;
import com.superc.bannerdairy.model.BaseCarGoodsItem;
import com.superc.bannerdairy.model.MAddress;
import com.superc.bannerdairy.model.TreasureItem;
import com.superc.bannerdairy.ui.order.FirmOrderActivity;
import com.superc.bannerdairy.ui.order.PayOrderActivity;
import com.superc.bannerdairy.ui.order.SelectAddressActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amorr on 2017/11/16.
 * 确认订单，选择地址
 */

public class FirmOrderLock extends BaseLock<ActivityFirmOrderBinding> {
    public String user_address_id;
    public MAddress item;
    //立即下单adapter
    public FirmOrderAdapter mAdapter;
    public TreasureItem treasureItem;
    public List<TreasureItem> mTreasureList;
    //购物车adapter
    public CarFirmAdapter mcarAdapter;
    public List<BaseCarGoodsItem.CarGoodsItem> mGoodsList;
    //接受下单后的订单id
    private String orderId;
    public int pospos = -2;
    private String showPrice = "0";
    public String express_fee = "0";//总邮费
    public String express_fee_e = "0";//接口单个总
    public String express_fee_num = "0";//单听的邮费价格
    private int num = 0;/*数量用来计算邮费*/
    private String type = "1";

    private DecimalFormat mDecimalFormat;

    public FirmOrderLock(Context context, ActivityFirmOrderBinding binding) {
        super(context, binding);
    }

    public FirmOrderLock(Context context, ActivityFirmOrderBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }


    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.order_firm);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
        mDecimalFormat = new DecimalFormat("0.00");
        //订单列表
        mGoodsList = new ArrayList<>();
        mTreasureList = new ArrayList<>();
        num = 0;
        //从商品详情下单过来的
        if (mBundle.getString("flag").equals("one")) {
            treasureItem = mBundle.getParcelable("order");
//        treasureItem = (TreasureItem) RunTime.getRunTime(RunTime.FIRMGOODS);
            if (treasureItem != null) {
                mTreasureList.add(treasureItem);
                showPrice = MyApplication.getUser().getAllPirice() + "";
//                mBinding.price.setText("" + MyApplication.getUser().getAllPirice());
                mBinding.shangpinPrice.setText("" + MyApplication.getUser().getAllPirice());
            }
            GridLayoutManager ms = new GridLayoutManager(mContext, 1);
            mBinding.rvFirmorder.setLayoutManager(ms);
            mBinding.rvFirmorder.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.recy_shopcar));
            mAdapter = new FirmOrderAdapter(mContext, mTreasureList, R.layout.item_firmcar, BR.treasureItem);
            mBinding.rvFirmorder.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        } else if (mBundle.getString("flag").equals("list")) {
            //从购物车下单过来的
            mGoodsList = mBundle.getParcelableArrayList("orders");
            if (!mBundle.getString("allPrice").equals("0")) {
                showPrice = mBundle.getString("allPrice");
//                mBinding.price.setText(mBundle.getString("allPrice"));
                mBinding.shangpinPrice.setText(mBundle.getString("allPrice"));
            }
            GridLayoutManager ms = new GridLayoutManager(mContext, 1);
            mBinding.rvFirmorder.setLayoutManager(ms);
            mBinding.rvFirmorder.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.recy_shopcar));
            mcarAdapter = new CarFirmAdapter(mContext, mGoodsList, R.layout.item_car_firm, BR.carGoodsItem);
            mBinding.rvFirmorder.setAdapter(mcarAdapter); // 这里或者在xml里设置adapter

        } else {
            //从商品详情下单过来的,从全局拿(废弃了)
            treasureItem = (TreasureItem) RunTime.getRunTime(RunTime.FIRMGOODS);
            if (treasureItem != null) {
                mTreasureList.add(treasureItem);
                showPrice = MyApplication.getUser().getAllPirice() + "";
//                mBinding.price.setText("" + MyApplication.getUser().getAllPirice());
                mBinding.shangpinPrice.setText("" + MyApplication.getUser().getAllPirice());
            }
            GridLayoutManager ms = new GridLayoutManager(mContext, 1);
            mBinding.rvFirmorder.setLayoutManager(ms);
            mBinding.rvFirmorder.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.recycle_view_line));
            mAdapter = new FirmOrderAdapter(mContext, mTreasureList, R.layout.item_firmcar, BR.treasureItem);
            mBinding.rvFirmorder.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        }
        //获取默认地址
        httpAddress();/*写在这里是因为需要上一界面传过来的价格*/

    }

    //选择支付方式界面
    public void goPay() {
        if(user_address_id==null||user_address_id.equals("")) {
            showToast("请设置默认地址");
            return;
        }else {
            httpOrder();
        }
    }

    //选择收货地址
    public void goSelectAdd() {
        Intent intent = new Intent(mContext, SelectAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("pospos", pospos);
        intent.putExtras(bundle);
        ((FirmOrderActivity) mContext).startActivityForResult(intent, 110);
//        startActivity(SelectAddressActivity.class);


    }

    //获取默认地址
    public void httpAddress() {
        if (mGoodsList.size() != 0) {
            for (BaseCarGoodsItem.CarGoodsItem carGoodsItem : mGoodsList) {
                num += Integer.parseInt(carGoodsItem.getNum());
            }
        } else if (mTreasureList.size() != 0) {
            num = MyApplication.getUser().getNum();
        }
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.GETADDRESS, RequestMethod.GET);
        Gson gson = new Gson();
        request.add("act", "user");
        request.add("creator", MyApplication.getUser().user_id);
        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    String resultCode = null;
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
//                            mBinding.tvName.setText(item.creator);
                                user_address_id = item.user_address_id;
                                /*计算邮费*/
                                express_fee_num = item.getExpressFee().getExpressNumFee();
                                express_fee_e = item.getExpressFee().getExpressFee();
                                express_fee = (Float.parseFloat(express_fee_e) + ((num - 2) * (Float.parseFloat(express_fee_num)))) + "";
                                mBinding.youfei.setText(mDecimalFormat.format(Float.parseFloat(express_fee)));
                                float allparice = Float.parseFloat(mBinding.shangpinPrice.getText().toString()) + Float.parseFloat(express_fee);
                                mBinding.price.setText(mDecimalFormat.format(allparice));//计算邮费后的总价

                                showPriceAgin();
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

    //下单接口
    private void httpOrder() {
        type = mBundle.getString("type");
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.FIRMORDER, RequestMethod.POST);
        Gson gson = new Gson();
        String b = "";
        // now 立即下单  cart购物车下单
        if (mBundle.getString("flag").equals("one")) {
            //立即下单
            request.add("act", "now");
            request.add("goods_id", Integer.parseInt(treasureItem.getGoods_id()));
//            request.add("goods_id",39 );
            request.add("spec_id", MyApplication.getUser().getSpec_id());
            request.add("order_goods_price", MyApplication.getUser().getAllPirice() / MyApplication.getUser().getNum());
        } else {
            //购物车下单
            request.add("act", "cart");
            String a = "";
            for (BaseCarGoodsItem.CarGoodsItem in : mGoodsList) {
                a += in.cart_id + ",";
            }
            b = a.substring(0, a.length() - 1);
            request.add("cart_id", b);
        }
        //购买类型：1普通 2买库存
        request.add("type", Integer.parseInt(type));/*通过传过来的值进行设置*/
        request.add("order_goods_number", MyApplication.getUser().getNum());
        //地址id
        request.add("address", user_address_id);
        request.add("user_id", MyApplication.getUser().user_id);
        request.add("user_remarks", mBinding.orderEdtBeizhu.getText().toString());//填写的备注
        request.add("postage", express_fee);//邮费
//        Log.e("上传数据","?act=now&"+"goods_id="+Integer.parseInt(treasureItem.getGoods_id())+"&spec_id="+MyApplication.getUser().getSpec_id()+"&order_goods_price="+MyApplication.getUser().getAllPirice() / MyApplication.getUser().getNum()
//        +"&type="+Integer.parseInt(type)+"&order_goods_number="+MyApplication.getUser().getNum()+"&address="+user_address_id+"&user_id="+MyApplication.getUser().user_id+"&user_remarks="+mBinding.orderEdtBeizhu.getText().toString()+"&postage="+express_fee);

        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    String resultCode = null;
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
//                            item = new Gson().fromJson(jsonObject.getString("data").toString(), MAddress.class);
//                            showToast(jsonObject.getString("msg"));
                            orderId = jsonObject.getString("data");

                            Bundle bundle = new Bundle();
                            bundle.putString("diff", "2");
                            bundle.putString("orderId", orderId);
                            Intent intent = new Intent(mContext, PayOrderActivity.class);
                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
//                            startActivity(PayOrderActivity.class,bundle);
                            ((BaseActivity) mContext).finish();
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
        num=0;
        if (mGoodsList.size() != 0) {
            for (BaseCarGoodsItem.CarGoodsItem carGoodsItem : mGoodsList) {
                num += Integer.parseInt(carGoodsItem.getNum());
            }
        } else if (mTreasureList.size() != 0) {
            num = MyApplication.getUser().getNum();
        }
        express_fee =( Float.parseFloat(express_fee_e) + ((num - 2) * (Float.parseFloat(express_fee_num)))) + "";
        mBinding.youfei.setText(express_fee);
        float allparice = Float.parseFloat(mBinding.shangpinPrice.getText().toString()) + Float.parseFloat(express_fee);
        mBinding.price.setText(new DecimalFormat("0.00").format(allparice));//计算邮费后的总价
    }
}
