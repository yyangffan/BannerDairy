package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemTreasureOneBinding;
import com.superc.bannerdairy.model.OrderItem;
import com.superc.cframework.base.ui.CBaseViewHolder;

import java.util.List;

/**
 * Created by Amorr on 2017/12/5.
 */

public class OrderListFragmentAdapter extends RecyclerView.Adapter {
    protected List<OrderItem.OrderGoodsBean> mDataList;// 数据列表
    protected Context mContext; // 上下文
    private int layoutId; // 单布局
    private int variableId; // DataBinding的BR
    private OnItemClickListener mListener; // ItemClick监听


    /**
     * 构造函数
     *
     * @param context    上下文
     * @param dataList   数据列表
     * @param layoutId   单布局
     * @param variableId DataBinding的BR
     */
    public OrderListFragmentAdapter(Context context, List<OrderItem.OrderGoodsBean> dataList, int layoutId, int variableId) {
        this.mContext = context;
        this.mDataList = dataList;
        this.layoutId = layoutId;
        this.variableId = variableId;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        ItemTreasureOneBinding itemBinding = (ItemTreasureOneBinding) binding;

        binding.setVariable(variableId, mDataList.get(position));
        if (mDataList.get(position).getGoods_cover_image() != null) {
            Glide.with(mContext).load(ConnectUrl.REQUESTURL +mDataList.get(position).getGoods_cover_image()).into(itemBinding.image);
        }
        itemBinding.tvName.setText(mDataList.get(position).getGoods_name());
        itemBinding.ivBuyNum.setText("X"+mDataList.get(position).getOrder_goods_number());
        itemBinding.ivPrice.setText("¥"+mDataList.get(position).getOrder_goods_price());
        subTask(binding, position);
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
}
