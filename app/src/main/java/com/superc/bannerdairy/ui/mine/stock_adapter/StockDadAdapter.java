package com.superc.bannerdairy.ui.mine.stock_adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.model.StockItem;

import java.util.List;

/**
 * 我的库存界面外层的Adapter
 */

public class StockDadAdapter extends RecyclerView.Adapter<StockDadAdapter.ViewHolder> {
    private List<StockItem.DataBeanX> mBaseCarGoodsItems;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnDadCbClickListener mOnDadCbClickListener;

    public StockDadAdapter(List<StockItem.DataBeanX> baseCarGoodsItems, Context context) {
        mBaseCarGoodsItems = baseCarGoodsItems;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_basecar_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void setOnDadCbClickListener(OnDadCbClickListener onDadCbClickListener) {
        mOnDadCbClickListener = onDadCbClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mtv_title.setText(mBaseCarGoodsItems.get(position).getCategoryName());
        final StockChildAdapter mAdapter = new StockChildAdapter(mContext, mBaseCarGoodsItems.get(position).getData(), R.layout.item_stockchild, BR.stockGoodsItem);
        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        holder.mRecyclerView.setLayoutManager(ms);
        holder.mRecyclerView.setAdapter(mAdapter);
        StockItem.DataBeanX dataBeanX = mBaseCarGoodsItems.get(position);
        if(dataBeanX.isCheck()){
            holder.mCheckBox.setChecked(true);
        }else {
            holder.mCheckBox.setChecked(false);
        }
        mAdapter.setOnCbClickListener(new StockChildAdapter.OnCbClickListener() {
            @Override
            public void OnCbClickListener() {
                boolean isC = true;
                for (StockItem.DataBeanX.DataBean dataBean : mBaseCarGoodsItems.get(position).getData()) {
                    if (!dataBean.isCheck()) {//只要有一个为false即为false
                        isC = false;
                    }
                }
                holder.mCheckBox.setChecked(isC);
                mBaseCarGoodsItems.get(position).setCheck(isC);
                if (mOnDadCbClickListener != null) {
                    mOnDadCbClickListener.OnDadCbClickListener();
                }
            }
        });
        mAdapter.setOnJiaJianClickListener(new StockChildAdapter.OnJiaJianClickListener() {
            @Override
            public void OnJiaJianClickListener() {
                if (mOnDadCbClickListener != null) {
                    mOnDadCbClickListener.OnDadCbClickListener();
                }
            }
        });
        holder.mtv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mCheckBox.performClick();
            }
        });
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = holder.mCheckBox.isChecked();
                mBaseCarGoodsItems.get(position).setCheck(checked);
                if(checked) {
                    for (StockItem.DataBeanX.DataBean dataBean : mBaseCarGoodsItems.get(position).getData()) {
                        dataBean.setCheck(true);
                    }
                }else {
                    for (StockItem.DataBeanX.DataBean dataBean : mBaseCarGoodsItems.get(position).getData()) {
                        dataBean.setCheck(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
                if (mOnDadCbClickListener != null) {
                    mOnDadCbClickListener.OnDadCbClickListener();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mBaseCarGoodsItems == null ? 0 : mBaseCarGoodsItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        TextView mtv_title;
        CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.rv_car);
            mtv_title = itemView.findViewById(R.id.car_title);
            mCheckBox = itemView.findViewById(R.id.car_cb);
        }
    }

    public interface OnDadCbClickListener {
        void OnDadCbClickListener();
    }


}
