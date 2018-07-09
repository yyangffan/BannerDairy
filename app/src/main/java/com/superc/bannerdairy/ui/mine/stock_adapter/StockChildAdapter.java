package com.superc.bannerdairy.ui.mine.stock_adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemBasecargoodsBinding;
import com.superc.bannerdairy.databinding.ItemStockchildBinding;
import com.superc.bannerdairy.model.BaseCarGoodsItem;
import com.superc.bannerdairy.model.StockItem;
import com.superc.cframework.base.ui.CBaseViewHolder;

import java.util.List;

import static android.R.attr.data;

/**
 * 我的库存界面内部RecyclerView的Adapter
 */

public class StockChildAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private int layoutid;
    private int variabledid;
    private List<StockItem.DataBeanX.DataBean> mCarGoodsItems;
    private OnJiaJianClickListener mOnJiaJianClickListener;
    private OnCbClickListener mOnCbClickListener;

    public StockChildAdapter(Context context, List<StockItem.DataBeanX.DataBean> dataList, int layoutId, int variableId) {
        this.mContext = context;
        this.mCarGoodsItems = dataList;
        this.layoutid = layoutId;
        this.variabledid = variableId;
    }

    public void setOnJiaJianClickListener(OnJiaJianClickListener onJiaJianClickListener) {
        mOnJiaJianClickListener = onJiaJianClickListener;
    }

    public void setOnCbClickListener(OnCbClickListener onCbClickListener) {
        mOnCbClickListener = onCbClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutid, parent, false);
        return new CBaseViewHolder(mContext, mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ItemStockchildBinding itemBinding = DataBindingUtil.findBinding(holder.itemView);

        itemBinding.setVariable(variabledid, mCarGoodsItems.get(position));
        if (mCarGoodsItems.get(position).getGoodsCoverImage() != null) {
            Glide.with(mContext).load(ConnectUrl.REQUESTURL + mCarGoodsItems.get(position).getGoodsCoverImage()).into(itemBinding.image);
        }
        final StockItem.DataBeanX.DataBean carGoodsItem = mCarGoodsItems.get(position);
        /*对库存界面的布局进行设置*/
        itemBinding.basecarDeleteRv.setVisibility(View.INVISIBLE);
        itemBinding.tvPrice.setVisibility(View.GONE);
        itemBinding.tvStage.setVisibility(View.GONE);
        itemBinding.tvStockStage.setVisibility(View.VISIBLE);
        itemBinding.tvStockStage.setText("库存:" + carGoodsItem.getStockNum());
        if(carGoodsItem.isCheck()){
            itemBinding.cb.setChecked(true);
        }else {
            itemBinding.cb.setChecked(false);
        }
        /*这个界面的binding事件会进行修改因为逻辑上不对*/
        /*进行加减时候的监听事件*/
        final StockItem.DataBeanX.DataBean dataBean = mCarGoodsItems.get(position);
        itemBinding.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num =dataBean.getNum();
                if (!num.equals("0")) {
                    dataBean.setNum((Integer.parseInt(num) - 2) + "");
                    itemBinding.num.setText(dataBean.getNum());
                    dataBean.setStockNum((Integer.parseInt(dataBean.getStockNum())+2)+"");
                    itemBinding.tvStockStage.setText("库存:" + (Integer.parseInt(carGoodsItem.getStockNum())));
                }
                if (mOnJiaJianClickListener != null) {
                    mOnJiaJianClickListener.OnJiaJianClickListener();
                }
            }
        });
        itemBinding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = dataBean.getNum();
                if(!num.equals(carGoodsItem.getOrderGoodsStockNumber())){
                    dataBean.setNum((Integer.parseInt(num) + 2) + "");
                    itemBinding.num.setText(dataBean.getNum());
                    dataBean.setStockNum((Integer.parseInt(dataBean.getStockNum())-2)+"");
                    itemBinding.tvStockStage.setText("库存:" + dataBean.getStockNum());
                }else {
                    dataBean.setStockNum("0");
                    dataBean.setNum(dataBean.getOrderGoodsStockNumber());
                    itemBinding.tvStockStage.setText("库存:0");
                }
                if (mOnJiaJianClickListener != null) {
                    mOnJiaJianClickListener.OnJiaJianClickListener();
                }
            }
        });
        itemBinding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemBinding.cb.performClick();
            }
        });
        itemBinding.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBean.setCheck(itemBinding.cb.isChecked());
                if(mOnCbClickListener!=null){
                    mOnCbClickListener.OnCbClickListener();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mCarGoodsItems == null ? 0 : mCarGoodsItems.size();
    }

    public interface OnJiaJianClickListener {
        void OnJiaJianClickListener();
    }

    public interface OnCbClickListener{
        void OnCbClickListener();
    }
}



