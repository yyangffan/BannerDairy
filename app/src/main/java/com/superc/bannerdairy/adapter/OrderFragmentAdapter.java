package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemTreasureOrderBinding;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amorr on 2017/12/5.
 * 代付款界面
 */

public class OrderFragmentAdapter extends RecyclerView.Adapter {
    protected List<OrderItem> mDataList;// 数据列表
    protected Context mContext; // 上下文
    private int layoutId; // 单布局
    private int variableId; // DataBinding的BR
    private OnItemClickListener mListener; // ItemClick监听
    private OnCheckbClickListener onCheckClick;
    private int a;

    private List<OrderItem.OrderGoodsBean> mGoodsList;
    public OrderListFragmentAdapter mAdapter;
    private OnCancelDingdan mOnCancelDingdan;

    /**
     * 构造函数
     *
     * @param context    上下文
     * @param dataList   数据列表
     * @param layoutId   单布局
     * @param variableId DataBinding的BR
     */
    public OrderFragmentAdapter(Context context, List<OrderItem> dataList, int layoutId, int variableId, int a) {
        this.mContext = context;
        this.mDataList = dataList;
        this.layoutId = layoutId;
        this.variableId = variableId;
        this.a = a;
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

    public void setOnCancelDingdan(OnCancelDingdan onCancelDingdan) {
        mOnCancelDingdan = onCancelDingdan;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        final ItemTreasureOrderBinding itemBinding = (ItemTreasureOrderBinding) binding;
        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        itemBinding.recycler.setLayoutManager(ms);
//        itemBinding.recycler.addItemDecoration(new RecycleViewDivider(
//                mContext, R.drawable.recycle_view_line));/*这个因为再刷新的时候会导致一直添加这个分割线所以去掉*/
        binding.setVariable(variableId, mDataList.get(position));
//        if (mDataList.get(position).getGoods_cover_image() != null) {
//            Glide.with(mContext).load(ConnectUrl.REQUESTURL +mDataList.get(position).getGoods_cover_image()).into(itemBinding.image);
//        }
        //商品列表
        mGoodsList = mDataList.get(position).getOrder_goods();
        mAdapter = new OrderListFragmentAdapter(mContext, mGoodsList, R.layout.item_treasure_one, BR.goodsBean);
        itemBinding.recycler.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        String order_type = mDataList.get(position).getOrder_type();
        if(mDataList.get(position).isCheck()){
            itemBinding.cbOrder.setChecked(true);
        }else {
            itemBinding.cbOrder.setChecked(false);
        }
        if (a == 1) {
            itemBinding.selectOrder.setVisibility(View.VISIBLE);
            itemBinding.exitOrder.setVisibility(View.VISIBLE);
            itemBinding.exitOrder.setText("删除订单");
            itemBinding.payOrder.setText("去支付");
            itemBinding.cbOrder.setVisibility(View.VISIBLE);
            itemBinding.status.setText("待付款");
            itemBinding.treasureOrderDanhao.setText(mDataList.get(position).getOrder_code());/*单号以及区分*/

        } else if (a == 2) {
            itemBinding.selectOrder.setVisibility(View.VISIBLE);
            itemBinding.exitOrder.setVisibility(View.GONE);
            itemBinding.payOrder.setText("提醒审核");
            itemBinding.payOrder.setVisibility(View.GONE);/*暂时隐藏掉*/
            itemBinding.cbOrder.setVisibility(View.GONE);
            itemBinding.status.setText("待审核");
            if (order_type.equals("1")) {
                itemBinding.treasureOrderDanhao.setText(mDataList.get(position).getOrder_code() + "(库存订单)");/*单号以及区分*/
            } else {
                itemBinding.treasureOrderDanhao.setText(mDataList.get(position).getOrder_code() + "(正常订单)");/*单号以及区分*/
            }
        } else if (a == 3) {
            itemBinding.selectOrder.setVisibility(View.VISIBLE);
            itemBinding.exitOrder.setVisibility(View.GONE);
            itemBinding.payOrder.setText("提醒发货");
            itemBinding.cbOrder.setVisibility(View.GONE);
            itemBinding.status.setText("待发货");
            if (order_type.equals("1")) {
                itemBinding.treasureOrderDanhao.setText(mDataList.get(position).getOrder_code() + "(库存订单)");/*单号以及区分*/
            } else {
                itemBinding.treasureOrderDanhao.setText(mDataList.get(position).getOrder_code() + "(正常订单)");/*单号以及区分*/
            }
        } else {
            itemBinding.selectOrder.setVisibility(View.GONE);
            itemBinding.cbOrder.setVisibility(View.GONE);
            itemBinding.status.setText("已发货");
            if (order_type.equals("1")) {
                itemBinding.treasureOrderDanhao.setText(mDataList.get(position).getOrder_code() + "(库存订单)");/*单号以及区分*/
            } else {
                itemBinding.treasureOrderDanhao.setText(mDataList.get(position).getOrder_code() + "(正常订单)");/*单号以及区分*/
            }
        }
        //总价
        itemBinding.price.setText("实付 ¥" + mDataList.get(position).getOrder_total());
        //时间
        itemBinding.time.setText(mDataList.get(position).getCreated());
        subTask(binding, position);
        //取消点击监听
        itemBinding.exitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消订单
                if(mOnCancelDingdan!=null){
                    mOnCancelDingdan.OnCancelDingDan(position);
                }
//                exitOrder();
            }
        });
        //支付点击监听
        itemBinding.payOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (a == 1) {
                    //去支付
                    Intent intent = new Intent(mContext, PayOrderActivity.class);
                    intent.putExtra("orderId", mDataList.get(position).getOrder_id());
                    Bundle bundle = new Bundle();
                    bundle.putString("diff", "2");
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                } else if (a == 2) {
                    //提醒审核
                    Toast.makeText(mContext, "pay" + position, Toast.LENGTH_LONG).show();

                } else if (a == 3) {
                    //提醒发货
                    Toast.makeText(mContext, "pay" + position, Toast.LENGTH_LONG).show();

                }
            }
        });
        //商品点击监听
        mAdapter.setOnItemClickListener(new OrderListFragmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViewDataBinding binding, int p) {
                Bundle bundle = new Bundle();
                bundle.putInt("flag", a);
                bundle.putParcelable("order", mDataList.get(position));
                bundle.putString("mobile", mDataList.get(position).getMobile());
                bundle.putParcelableArrayList("goods", (ArrayList<? extends Parcelable>) mDataList.get(position).getOrder_goods());
                ((BaseActivity) mContext).startActivity(OrderDetailActivity.class, bundle);

            }
        });
        itemBinding.treasureOrderDanhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemBinding.cbOrder.performClick();
            }
        });
        itemBinding.cbOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDataList.get(position).setCheck(itemBinding.cbOrder.isChecked());
                onCheckClick.onCheckClick(binding, position, itemBinding.cbOrder.isChecked());
            }
        });

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

    public interface OnCheckbClickListener {
        void onCheckClick(ViewDataBinding binding, int position, boolean isCheck);
    }

    public void setOnCheckClick(OnCheckbClickListener onCheckClick) {
        this.onCheckClick = onCheckClick;
    }

    public interface OnCancelDingdan{
        void OnCancelDingDan(int position);
    }

    //获取待付款订单列表
    private void exitOrder() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ORDERLIST, RequestMethod.GET);
        request.add("act", "list");

        if (MyApplication.getUser() != null) {

            request.add("creator", MyApplication.getUser().user_id);
        }
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
                            mAdapter.notifyDataSetChanged();
                        }
                        if (show) {
                            Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                Toast.makeText(mContext, "网络异常", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }
}
