package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemCarBinding;
import com.superc.bannerdairy.model.CarGoodsItem;
import com.superc.cframework.base.ui.CBaseViewHolder;

import java.util.List;

/**
 * Created by Amorr on 2017/11/28.
 * 购物车
 */

public class CarAdapter extends RecyclerView.Adapter {
    protected List<CarGoodsItem> mDataList;// 数据列表
    protected Context mContext; // 上下文
    private int layoutId; // 单布局
    private int variableId; // DataBinding的BR
    private OnItemClickListener mListener; // ItemClick监听
    private Boolean allIsCb = false;
    private int buyNum = 1; //购买数量
    private double price = 0.00;//z总价钱
    public ViewClick mViewClick;

    /**
     * 构造函数
     *
     * @param context    上下文
     * @param dataList   数据列表
     * @param layoutId   单布局
     * @param variableId DataBinding的BR
     */
    public CarAdapter(Context context, List<CarGoodsItem> dataList, int layoutId, int variableId) {
        this.mContext = context;
        this.mDataList = dataList;
        this.layoutId = layoutId;
        this.variableId = variableId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, parent, false);
        return new CBaseViewHolder(mContext, mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        ItemCarBinding itemBinding = (ItemCarBinding) binding;
        binding.setVariable(variableId, mDataList.get(position));
        if (mDataList.get(position).getGoods_cover_image() != null) {
            Glide.with(mContext).load(ConnectUrl.REQUESTURL + mDataList.get(position).getGoods_cover_image()).into(itemBinding.image);
        }

        //减号
        ((ItemCarBinding) binding).less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String num = ((ItemCarBinding) binding).num.getText().toString();
//                if (Integer.parseInt(num) != 1) {
//                    ((ItemCarBinding) binding).num.setText(Integer.parseInt(num) - 1 + "");
//
//                }
                if (mViewClick != null) {
                    mViewClick.ItemClick(((ItemCarBinding) binding).less, position);

                }
            }
        });
        //加号
        ((ItemCarBinding) binding).more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String num = ((ItemCarBinding) binding).num.getText().toString();
//                ((ItemCarBinding) binding).num.setText(Integer.parseInt(num) + 1 + "");

                if (mViewClick != null) {
                    mViewClick.ItemClick(((ItemCarBinding) binding).more, position);

                }
            }
        });
        //cb
        ((ItemCarBinding) binding).cb.setChecked((mDataList.get(position).isCheck));
        //num
        //全局价钱
        if (((ItemCarBinding) binding).cb.isChecked()) {
            if (mDataList.get(position).goods_original_price != null) {
                price = price + Double.parseDouble(mDataList.get(position).goods_original_price);
            }

        }


        ((ItemCarBinding) binding).cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataList.get(position).getCheck()) {
                    mDataList.get(position).setCheck(false);
                } else {
                    mDataList.get(position).setCheck(true);

                }
                if (mViewClick != null) {
                    mViewClick.ItemClick(((ItemCarBinding) binding).cb, position);

                }
            }

        });
        subTask(itemBinding, position);

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


    //new
    public interface ViewClick {
        void ItemClick(View view, int position);
    }

    public void setViewClick(ViewClick viewClick) {
        this.mViewClick = viewClick;
    }
}
