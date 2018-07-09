package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityStockBinding;
import com.superc.bannerdairy.model.StockItem;
import com.superc.bannerdairy.ui.mine.StockOrderActivity;
import com.superc.bannerdairy.ui.mine.stock_adapter.StockDadAdapter;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static cn.jpush.android.api.JPushInterface.a.e;
import static cn.jpush.android.api.JPushInterface.a.i;
import static cn.jpush.android.api.JPushInterface.a.j;
import static com.superc.bannerdairy.lock.MainLock.KUCUNLIUSHUI;

/**
 * Created by user on 2018/1/27.
 */

public class StockLock extends BaseLock<ActivityStockBinding> {
    public List<StockItem.DataBeanX> mStockDadList;
    public StockDadAdapter mStockDadAdapter;
    public List<StockItem.DataBeanX> mStockOrderList;
    public boolean fahuo=true;
    public boolean fahuoN=false;


    public StockLock(Context context, ActivityStockBinding binding) {
        super(context, binding);
    }

    public StockLock(Context context, ActivityStockBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.mine_stock);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.setRight(KUCUNLIUSHUI);
        appBarLock.barData.isRight = true;
        appBarLock.barData.titleRight = "库存流水";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        initData();
    }


    /*申请发货*/
    public void startFahuo() {
        fahuo=true;
        fahuoN=false;
        mStockOrderList = new ArrayList<>();
        mStockOrderList.addAll(mStockDadList);
        for (int i=0;i<mStockDadList.size();i++) {/*如下为了判断是否有发货商品(通过fahuo)*/
            StockItem.DataBeanX dataBeanX = mStockOrderList.get(i);
            List<StockItem.DataBeanX.DataBean> data = dataBeanX.getData();
            for (int j=0;j<data.size();j++) {
                StockItem.DataBeanX.DataBean dataBean = data.get(j);
                if (dataBean.isCheck()) {
                    fahuoN=true;
                    if(dataBean.getNum().equals("0")){
                        fahuo=false;
                    }
                }
            }
        }
        if(!fahuoN){
            ToastUtil.show(mContext,"请选择发货商品", Toast.LENGTH_SHORT);
            return;
        }
        if(!fahuo){
            ToastUtil.show(mContext,"请选择发货数量", Toast.LENGTH_SHORT);
            return;
        }
        Bundle bundle=new Bundle();
        bundle.putSerializable("data", (Serializable) mStockOrderList);
        startActivity(StockOrderActivity.class,bundle);

    }


    /*所有内容的初始化操作*/
    public void initData() {
        mStockDadList = new ArrayList<>();
        mStockDadAdapter = new StockDadAdapter(mStockDadList, mContext);
        mBinding.stockNum.setText("0听");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        mBinding.stockRv.setLayoutManager(gridLayoutManager);
        mBinding.stockRv.setAdapter(mStockDadAdapter);

        mStockDadAdapter.setOnDadCbClickListener(new StockDadAdapter.OnDadCbClickListener() {
            @Override
            public void OnDadCbClickListener() {/*点击复选框或者加减都会触发该方法*/
                int num = 0;
                for (StockItem.DataBeanX dataBean : mStockDadList) {
                    for (StockItem.DataBeanX.DataBean dataB : dataBean.getData()) {
                        if (dataB.isCheck()) {
                            num += Integer.parseInt(dataB.getNum());
                        }
                    }
                }
                mBinding.stockNum.setText(num + "听");
            }
        });
        httpGoods();
    }

    //获取-我的库存数据
    private void httpGoods() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.STOCKLIST, RequestMethod.GET);
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
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            JSONArray jsonArray1 = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                StockItem.DataBeanX item = new Gson().fromJson(jsonArray1.get(i).toString(), StockItem.DataBeanX.class);
                                for (StockItem.DataBeanX.DataBean cgi : item.getData()) {
                                    cgi.setCheck(false);
                                    cgi.setNum("0");
                                    cgi.setStockNum((Integer.parseInt(cgi.getOrderGoodsStockNumber())) + "");
                                }
                                item.setCheck(false);
                                mStockDadList.add(item);
                            }
                            mStockDadAdapter.notifyDataSetChanged();
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
