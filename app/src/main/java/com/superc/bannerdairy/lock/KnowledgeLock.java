package com.superc.bannerdairy.lock;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.KnowledgeLockAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.constant.RunTime;
import com.superc.bannerdairy.databinding.ActivitySpreadknowledgeBinding;
import com.superc.bannerdairy.model.KnowledgeItem;
import com.superc.bannerdairy.ui.manage.KnowledgeDetailActivity;
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
 * 传播专区界面
 */

public class KnowledgeLock extends BaseLock<ActivitySpreadknowledgeBinding> {
    private List<KnowledgeItem> mGoodsList;
    public KnowledgeLockAdapter mAdapter;
    private String mCategory_id;
    private String mTitle;
    private AppBarLock mAppBarLock;

    public KnowledgeLock(Context context, ActivitySpreadknowledgeBinding binding) {
        super(context, binding);
    }

    public KnowledgeLock(Context context, ActivitySpreadknowledgeBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
          /*home界面传过来的*/
        if(mBundle!=null){
            mCategory_id = mBundle.getString("category_id");
            mTitle = mBundle.getString("title");
        }
        if(mTitle==null){
            mAppBarLock = new AppBarLock(mContext, R.string.knowledge);
        }else {
            mAppBarLock = new AppBarLock(mContext,mTitle);
        }
        mAppBarLock.barData.imsLeft=mContext.getResources().getDrawable(R.drawable.fanhui);
        mAppBarLock.barData.isLeft=true;
        mAppBarLock.barData.titleLeft="返回";
        mBinding.titleBar.setAppBarLock(mAppBarLock);

        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.rvKnowledge.setLayoutManager(ms);
        mBinding.rvKnowledge.addItemDecoration(new RecycleViewDivider(
                mContext, R.drawable.recycle_view_line));
        mGoodsList = new ArrayList<KnowledgeItem>();

        mAdapter = new KnowledgeLockAdapter(mContext, mGoodsList, R.layout.item_knowledge, BR.knowledgeItem);
//        mBinding.rvKnowledge.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        mAdapter.setOnItemClickListener(new KnowledgeLockAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int position) {
                //区分从传播专区进去还是买家秀进去的
                RunTime.setData(RunTime.FINSHION, "1");
                RunTime.setData(RunTime.KNOWLEDGEID, mGoodsList.get(position));
                Bundle bundle=new Bundle();
                bundle.putString("title",mTitle);
                startActivity(KnowledgeDetailActivity.class,bundle);
            }
        });


        httpKnow();


    }
    //获取文章列表
    private void httpKnow() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LIST_ARTICLE, RequestMethod.GET);
        Gson gson = new Gson();
        request.add("act", "list");
        //article_category_type   2（买家秀）0（文章列表）
        request.add("article_category_type", 0);
        request.add("category_id", mCategory_id);
        if(MyApplication.getUser()!=null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {


            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    String resultCode = null;
                    Boolean success=false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
//                        resultCode = jsonObject.getString("code");
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");

//                        if (resultCode.equals("200")) {
                        if (success) {
                            JSONArray jsonArray= jsonObject.getJSONObject("data").getJSONArray("data");
                            for(int i=0;i<jsonArray.length();i++){
                                KnowledgeItem  item = new Gson().fromJson(jsonArray.get(i).toString(), KnowledgeItem.class);
//                                  mGoodList.add(new TreasureItem(item.getId(),item.getGoods_name(),item.getGoods_type(),item.getGoods_original_price(),item.getCreated(),item.goods_cover_image));
                                mGoodsList.add(item);
                            }
                            mAdapter.notifyDataSetChanged();

//                            for (TreasureItem info : mGoodsList) {
//                                mGoodsList.add(new TreasureItem(info.id,info.goods_name,info.goods_type,info.goods_original_price,info.created,info.goods_cover_image));
//
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
//                hideLoading();
                showToast("网络异常");

            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }

}
