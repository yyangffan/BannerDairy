package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.BaseCarAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityShoppingCartBinding;
import com.superc.bannerdairy.model.BaseCarGoodsItem;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.order.FirmOrderActivity;
import com.superc.cframework.utils.SPUtil;
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
 * Created by Amorr on 2017/11/20.
 * 购物车界面
 */

public class ShoppCartLock extends BaseLock<ActivityShoppingCartBinding> {
    //传到订单页新一个购物车列表
    public List<BaseCarGoodsItem.CarGoodsItem> mGoodsListTwo;
    public BaseCarAdapter mBaseCarAdapter;
    public List<BaseCarGoodsItem> mBaseCarGoodsItems;
    public String allprice = "0";
    public int mAllParentPrice = 0;

    public ShoppCartLock(Context context, ActivityShoppingCartBinding binding) {
        super(context, binding);
    }

    public ShoppCartLock(Context context, ActivityShoppingCartBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.shopping);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        mGoodsListTwo = new ArrayList<>();
        initNew();

    }

    public void initNew() {
        mBaseCarGoodsItems = new ArrayList<>();
        mBaseCarAdapter = new BaseCarAdapter(mContext, mBaseCarGoodsItems);

        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.rvCar.setLayoutManager(ms);
        mBinding.rvCar.setAdapter(mBaseCarAdapter);
        httpGoods();
        mBaseCarAdapter.setOnCbClickListener(new BaseCarAdapter.OnCbClickListener() {
            @Override
            public void onParenetCbClickListener(int postion, boolean isChecked, boolean isChildCheck) {
                mAllParentPrice = 0;
                boolean isc = true;
                for (BaseCarGoodsItem bcgi : mBaseCarGoodsItems) {
                    for (BaseCarGoodsItem.CarGoodsItem cgi : bcgi.getData()) {
                        if (cgi.isCheck) {
                            mAllParentPrice += Double.parseDouble(cgi.getAllprice());
                        } else {
                            isc = false;
                        }
                    }
                }
                mBinding.ckAll.setChecked(isc);
                mBinding.price.setText(mAllParentPrice + "");
            }
        });

        mBaseCarAdapter.setCarBaseClickChildListener(new BaseCarAdapter.CarBaseClickChildListener() {
            @Override
            public void OnCarChildClickListener(boolean isCheck, int pos, int position) {
                if (isCheck) {
                    mAllParentPrice = 0;
                    boolean isc = true;
                    for (BaseCarGoodsItem bcgi : mBaseCarGoodsItems) {
                        for (BaseCarGoodsItem.CarGoodsItem cgi : bcgi.getData()) {
                            if (cgi.isCheck) {
                                mAllParentPrice += Double.parseDouble(cgi.getAllprice());
                            } else {
                                isc = false;
                            }
                        }
                    }
                    mBinding.ckAll.setChecked(isc);
                    mBinding.price.setText(mAllParentPrice + "");
                } else {
                    mAllParentPrice = 0;
                    mBinding.ckAll.setChecked(false);
                    mBinding.price.setText("0.0");
                }
            }
        });
        mBinding.ckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAllParentPrice = 0;
                if (mBinding.ckAll.isChecked()) {
                    for (BaseCarGoodsItem bcgi : mBaseCarGoodsItems) {
                        bcgi.setChekc(true);
                        for (BaseCarGoodsItem.CarGoodsItem cgi : bcgi.getData()) {
                            cgi.setCheck(true);
                            mAllParentPrice += Double.parseDouble(cgi.getAllprice());
                        }
                    }
                } else {
                    for (BaseCarGoodsItem bcgi : mBaseCarGoodsItems) {
                        bcgi.setChekc(false);
                        for (BaseCarGoodsItem.CarGoodsItem cgi : bcgi.getData()) {
                            cgi.setCheck(false);
                        }
                    }
                }
                mBaseCarAdapter.setBaseCarGoodsItems(mBaseCarGoodsItems);
                mBinding.price.setText(mAllParentPrice + "");

            }
        });
    }


    //选择支付方式界面
    public void goPay() {
        mGoodsListTwo.clear();
        int number=0;
        for (BaseCarGoodsItem bcgi : mBaseCarGoodsItems) {
            bcgi.setChekc(true);
            for (BaseCarGoodsItem.CarGoodsItem cgi : bcgi.getData()) {
                if (cgi.isCheck) {
                    mGoodsListTwo.add(cgi);
                    number+=(Integer.parseInt(cgi.num));
                }
            }
        }
        if (mAllParentPrice == 0) {
            showToast("请选择商品");
        } else {
            isShowDialog(number+"");
        }
    }
    /*判断是否展示转库存弹窗 num所有数量总和*/
    public void isShowDialog(String num) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ISZKC, RequestMethod.POST);
        request.add("number",num);
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
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            String data = jsonObject.getString("data");
                            if(data.equals("0")){
                                toGoNext("1");
                            }else if(data.equals("1")){
                                ShowRemindDialog instance = ShowRemindDialog.getInstance();
                                instance.showDialog(mContext, R.drawable.chenggong1, "是否转库存", false, null);
                                instance.setBtNote("否", "是");
                                instance.setOnSomeThingClickListener(new OnSomeThingClickListener() {
                                    @Override
                                    public void OnDoSomeThingClickListener(int view_id) {
                                        toGoNext("1");
                                    }
                                });
                                instance.setOnCancleClickListener(new ShowRemindDialog.OnCancleClickListener() {
                                    @Override
                                    public void OnCancleClickListener() {
                                        toGoNext("2");
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

    public void toGoNext(String type) {
        // TODO: 2018/1/27 转库存的话需要调接口--判断是否能够转库存
        Bundle bundle = new Bundle();
        bundle.putString("flag", "list");
        bundle.putString("allPrice", mAllParentPrice + "");
       /*用来区分是否添加为库存 1普通  2转库存*/
        bundle.putString("type", type);
        bundle.putParcelableArrayList("orders", (ArrayList<? extends Parcelable>) mGoodsListTwo);
        startActivity(FirmOrderActivity.class, bundle);
        /*跳转之后数据初始化*/
        httpGoods();
        mAllParentPrice = 0;
        mBinding.price.setText("0.0");
        SPUtil.put(mContext, "carname", "");
    }

    //获取购物车列表
    public void httpGoods() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LISTCAR, RequestMethod.GET);
        request.add("act", "list");
        if (MyApplication.getUser() != null) {
            request.add("creator", MyApplication.getUser().user_id);
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
                            mBaseCarGoodsItems.clear();
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                BaseCarGoodsItem item = new Gson().fromJson(jsonArray1.get(i).toString(), BaseCarGoodsItem.class);
                                for (BaseCarGoodsItem.CarGoodsItem cgi : item.getData()) {
                                    cgi.setNum(cgi.getGoods_number());
                                    cgi.setCheck(false);
                                    cgi.setAllprice("" + Double.parseDouble(cgi.getGoods_original_price()) * Integer.parseInt(cgi.goods_number));
                                }
                                mBaseCarGoodsItems.add(item);
                            }
                            mBaseCarAdapter.notifyDataSetChanged();

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
