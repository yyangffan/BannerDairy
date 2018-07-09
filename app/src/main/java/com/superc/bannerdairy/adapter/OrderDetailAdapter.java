package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.ItemTreasureOneBinding;
import com.superc.bannerdairy.databinding.ItemTreasureOrderBinding;
import com.superc.bannerdairy.databinding.ItemTreasureTwoBinding;
import com.superc.bannerdairy.model.OrderItem;
import com.superc.bannerdairy.ui.order.OrderDetailActivity;
import com.superc.bannerdairy.ui.order.PayOrderActivity;
import com.superc.cframework.base.ui.CBaseViewHolder;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Amorr on 2017/12/11.
 * 我的订单列表里面的详情商品数据
 */

public class OrderDetailAdapter extends RecyclerView.Adapter {
    protected List<OrderItem.OrderGoodsBean> mDataList;// 数据列表
    protected Context mContext; // 上下文
    private int layoutId; // 单布局
    private int variableId; // DataBinding的BR
    private OnItemClickListener mListener; // ItemClick监听
    private int a;

    private List<OrderItem.OrderGoodsBean> mGoodsList;
    public OrderListFragmentAdapter mAdapter;
    /**
     * 构造函数
     *
     * @param context    上下文
     * @param dataList   数据列表
     * @param layoutId   单布局
     * @param variableId DataBinding的BR
     */
    public OrderDetailAdapter(Context context, List<OrderItem.OrderGoodsBean> dataList, int layoutId, int variableId,int a) {
        this.mContext = context;
        this.mDataList = dataList;
        this.layoutId = layoutId;
        this.variableId = variableId;
        this.a=a;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                layoutId,
                parent,
                false);
        return new CBaseViewHolder(mContext, mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        ItemTreasureTwoBinding itemBinding = (ItemTreasureTwoBinding) binding;

        binding.setVariable(variableId, mDataList.get(position));
        if (mDataList.get(position).getGoods_cover_image() != null) {
            Glide.with(mContext).load(ConnectUrl.REQUESTURL +mDataList.get(position).getGoods_cover_image()).into(itemBinding.image);
        }
        itemBinding.tvName.setText(mDataList.get(position).getGoods_name());
        itemBinding.ivBuyNum.setText("X"+mDataList.get(position).getOrder_goods_number());
        itemBinding.ivPrice.setText("¥"+mDataList.get(position).getOrder_goods_price());
        subTask(binding, position);

        //商品点击监听
//        mAdapter.setOnItemClickListener(new OrderListFragmentAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(ViewDataBinding binding, int position) {
//                Bundle bundle=new Bundle();
//                bundle.putInt("flag",a);
//                bundle.putParcelable("order",  mDataList.get(position));
////                bundle.putParcelableArrayList("order", (ArrayList<? extends Parcelable>) mDataList);
//                ((BaseActivity)mContext).startActivity(OrderDetailActivity.class,bundle);
//
//            }
//        });
    }

    /**
     * 其它操作
     *
     * @param binding  绑定
     * @param position 列表位置
     */
    private void subTask(final ViewDataBinding binding, final int position) {
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(binding, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    /**
     * 设置监听Item点击事件
     *
     * @param onItemClickListener 监听
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    /**
     * Item点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(ViewDataBinding binding, int position);
    }


    //获取待付款订单列表
    private void exitOrder() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ORDERLIST, RequestMethod.GET);
        Gson gson = new Gson();
        request.add("act", "list");

        if(MyApplication.getUser()!=null) {

            request.add("creator", MyApplication.getUser().user_id);
        }
        mRequestQueue.add(3, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {


            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 3) {
                    String resultCode = null;
                    Boolean success = false;
                    try {
                        JSONObject jsonObject = response.get();
//                        resultCode = jsonObject.getString("code");
                        success = jsonObject.getBoolean("success");

//                        if (resultCode.equals("200")) {
                        if (success) {

                            mAdapter.notifyDataSetChanged();

                        } else if (!success) {
                            Toast.makeText(mContext,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(mContext,"网络异常",Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                Toast.makeText(mContext,"网络异常",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }
}
