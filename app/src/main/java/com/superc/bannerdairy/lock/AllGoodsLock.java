package com.superc.bannerdairy.lock;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.AllGoodsLockAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.constant.Connection;
import com.superc.bannerdairy.constant.RunTime;
import com.superc.bannerdairy.databinding.ActivityAllGoodsBinding;
import com.superc.bannerdairy.model.TreasureItem;
import com.superc.bannerdairy.ui.goods.AllGoodsActivity;
import com.superc.bannerdairy.ui.goods.GoodsDetailActivity;
import com.superc.bannerdairy.ui.goods.ShoppingCartActivity;
import com.superc.cframework.network.HttpResponse;
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
import java.util.HashMap;
import java.util.List;

/**
 * 创建日期：2017/11/9 on 18:06
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 * 首页全部商品
 */

public class AllGoodsLock extends BaseLock<ActivityAllGoodsBinding> {
    public static final int BIND = 1001;

    private List<TreasureItem> mGoodsList;
    private AllGoodsLockAdapter mAdapter;
    private String mCategory_id="";

    public AllGoodsLock(Context context, ActivityAllGoodsBinding binding) {
        super(context, binding);
    }

    public AllGoodsLock(Context context, ActivityAllGoodsBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock=new AppBarLock(mContext, R.string.all_goods);
        appBarLock.barData.isLeft=true;
        appBarLock.barData.titleLeft="返回";
        appBarLock.setRight(BIND);
        appBarLock.barData.isRight=false;
        appBarLock.barData.imsRight=mContext.getResources().getDrawable(R.drawable.xiaoxi);
        appBarLock.barData.imsLeft=mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        GridLayoutManager ms = new GridLayoutManager(mContext, 2);
        mBinding.rvGoods.setLayoutManager(ms);
        mBinding.rvGoods.addItemDecoration(new RecycleViewDivider(
                mContext, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<TreasureItem>();
        httpGoods();

//        mAdapter = new CBaseRecyclerViewAdapter(mContext, mGoodsList, R.layout.item_goods, BR.goodsItem);
        mAdapter = new AllGoodsLockAdapter(mContext, mGoodsList, R.layout.item_goods, BR.goodsItem);

        mBinding.rvGoods.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        mAdapter.setOnItemClickListener(new AllGoodsLockAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                RunTime.setData(RunTime.ALLGOODS, mGoodsList.get(position));
                startActivity(GoodsDetailActivity.class);
            }
        });
/*home界面传过来的*/
        if(mBundle!=null){
            mCategory_id = mBundle.getString("category_id");
        }
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                httpGoods();
            }
        });


    }

    public void shoppingCart() {
        startActivity(ShoppingCartActivity.class);
    }

    //okHttp网络请求，不用
    private void httpgetGoods(){
        HashMap hashMap = new HashMap();
        hashMap.put("act", "list");
        Connection.getInstance().post(AllGoodsActivity.class, ConnectUrl.LIST_GOODS, hashMap, new Connection.ResponseListener() {
            @Override
            public void tryReturn(int id, Object response) {
                switch (id) {
                    case 200:
                        break;
                    case 100:
                        showToast("用户不存在");
                        break;
                    default:
                        showToast(((HttpResponse)response).getMsg());
                        break;
                }
            }
        });
    }



    //noHtpp网络请求，获取宝贝推荐列表
    private void httpGoods() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LIST_GOODS, RequestMethod.GET);
        Gson gson = new Gson();
        request.add("act", "list");
        request.add("category_id", mCategory_id);
        if(MyApplication.getUser()!=null){
            request.add("user_id", MyApplication.getUser().user_id);

        }

        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                mBinding.refreshLayout.finishRefresh();
                if (what == 1) {
                    Boolean success=false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            mGoodsList.clear();
                            JSONArray jsonArray= jsonObject.getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){
                                TreasureItem  item = new Gson().fromJson(jsonArray.get(i).toString(), TreasureItem.class);
//                                  mGoodList.add(new TreasureItem(item.getId(),item.getGoods_name(),item.getGoods_type(),item.getGoods_original_price(),item.getCreated(),item.goods_cover_image));
                                mGoodsList.add(item);

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
                mBinding.refreshLayout.finishRefresh();
            }
            @Override
            public void onFinish(int what) {
                mBinding.refreshLayout.finishRefresh();
            }
        });
    }

}
