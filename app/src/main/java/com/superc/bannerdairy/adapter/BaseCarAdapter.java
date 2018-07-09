package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.keepclass.GoodsPrice;
import com.superc.bannerdairy.keepclass.KeepGoodsPrice;
import com.superc.bannerdairy.model.BaseCarGoodsItem;
import com.superc.cframework.utils.SPUtil;
import com.superc.cframework.utils.ToastUtil;

import java.util.List;


/**
 * 购物车外部的Adapter
 */

public class BaseCarAdapter extends RecyclerView.Adapter<BaseCarAdapter.ViewHolder> {
    Context mContext;
    LayoutInflater mInflater;
    List<BaseCarGoodsItem> mBaseCarGoodsItems;
    OnCbClickListener mOnCbClickListener;
    public CarBaseClickChildListener mCarBaseClickChildListener;


    public BaseCarAdapter(Context context, List<BaseCarGoodsItem> mBaseGoodsList) {
        mContext = context;
        this.mBaseCarGoodsItems = mBaseGoodsList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnCbClickListener(OnCbClickListener onCbClickListener) {
        mOnCbClickListener = onCbClickListener;
    }

    public void setCarBaseClickChildListener(CarBaseClickChildListener carBaseClickChildListener) {
        mCarBaseClickChildListener = carBaseClickChildListener;
    }

    public void setBaseCarGoodsItems(List<BaseCarGoodsItem> baseCarGoodsItems) {
        mBaseCarGoodsItems = baseCarGoodsItems;
        this.notifyDataSetChanged();
    }

    @Override
    public BaseCarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_basecar_adapter, null);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final BaseCarAdapter.ViewHolder holder, final int position) {
        holder.mtv_title.setText(mBaseCarGoodsItems.get(position).getCategoryName());
        final CarBaseGoodsItemAdapter mAdapter = new CarBaseGoodsItemAdapter(mContext, mBaseCarGoodsItems.get(position).getData(), R.layout.item_basecargoods, BR.carGoodsItem);

        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        holder.mRecyclerView.setLayoutManager(ms);
        holder.mRecyclerView.setAdapter(mAdapter);
        holder.mtv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mCheckBox.performClick();
            }
        });
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/1/26 添加的复选框限制条件(只能选择一种奶粉进行支付)
                if (!mBaseCarGoodsItems.get(position).isChekc()) {
                    String carname = SPUtil.get(mContext, "carname", "");
                    if (carname == null || carname.equals("") || mBaseCarGoodsItems.get(position).getCategoryName().equals(carname)) {
                        holder.mCheckBox.setChecked(true);
                        SPUtil.put(mContext, "carname", mBaseCarGoodsItems.get(position).getCategoryName());
                    } else {
                        holder.mCheckBox.setChecked(false);
                        ToastUtil.show(mContext,"只能选择同种进行支付", Toast.LENGTH_SHORT);
                    }
                } else {
                    holder.mCheckBox.setChecked(false);
                    SPUtil.put(mContext, "carname", "");
                }
                /*------------------------------------------------------------------*/
                mBaseCarGoodsItems.get(position).setChekc(holder.mCheckBox.isChecked());
                List<GoodsPrice> goodsPrices = KeepGoodsPrice.getInstance().getGoodsPrices();
                for (BaseCarGoodsItem.CarGoodsItem cgi : mBaseCarGoodsItems.get(position).getData()) {
                    GoodsPrice goodsPrice = new GoodsPrice(cgi.getGoods_number(), cgi.getGoods_sale_price(), cgi.gds_id);
                    goodsPrices.add(goodsPrice);
                }
                if (holder.mCheckBox.isChecked()) {
                    for (BaseCarGoodsItem.CarGoodsItem cgi : mBaseCarGoodsItems.get(position).getData()) {
                        cgi.setCheck(true);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    for (BaseCarGoodsItem.CarGoodsItem cgi : mBaseCarGoodsItems.get(position).getData()) {
                        cgi.setCheck(false);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                if (mOnCbClickListener != null) {
                    mOnCbClickListener.onParenetCbClickListener(position, holder.mCheckBox.isChecked(), false);
                }
            }
        });


        mAdapter.setCarbaseClickListener(new CarBaseGoodsItemAdapter.CarbaseClickListener() {
            @Override
            public void SomeThingClick(boolean ischeck, int pos) {
                boolean isc = true;
                boolean isAl = false; // TODO: 2018/1/26 添加的复选框限制条件(只能选择一种奶粉进行支付)
                if (mBaseCarGoodsItems.size() > 0) {
                    for (BaseCarGoodsItem.CarGoodsItem cgi : mBaseCarGoodsItems.get(position).getData()) {
                        if (!cgi.isCheck) {
                            isc = false;
                        }
                        // TODO: 2018/1/26 添加的复选框限制条件(只能选择一种奶粉进行支付)
                        if (cgi.isCheck) {
                            isAl = true;
                        }
                    }
                    // TODO: 2018/1/26 添加的复选框限制条件(只能选择一种奶粉进行支付)
                    String carname = SPUtil.get(mContext, "carname", "");
                    if (carname == null || carname.equals("") || mBaseCarGoodsItems.get(position).getCategoryName().equals(carname)) {
                        if (isAl) {
                            SPUtil.put(mContext, "carname", mBaseCarGoodsItems.get(position).getCategoryName());
                        } else {
                            SPUtil.put(mContext, "carname", "");
                        }
                    }
                    /*---------------------------------------------------------------*/
                    holder.mCheckBox.setChecked(isc);
                    if (isc) {
                        mBaseCarGoodsItems.get(position).setChekc(true);
                    } else {
                        mBaseCarGoodsItems.get(position).setChekc(false);
                    }
                    if (mCarBaseClickChildListener != null) {
                        mCarBaseClickChildListener.OnCarChildClickListener(true, pos, position);
                    }
                } else {
                    if (mCarBaseClickChildListener != null) {
                        mCarBaseClickChildListener.OnCarChildClickListener(false, pos, position);
                    }
                }

            }
        });
        if (mBaseCarGoodsItems.get(position).isChekc()) {
            holder.mCheckBox.setChecked(true);
        } else {
            holder.mCheckBox.setChecked(false);
        }
        mAdapter.setOnCarChildDeletListener(new CarBaseGoodsItemAdapter.OnCarChildDeletListener() {
            @Override
            public void OnCarChildDeleteListener(int pos) {
                if (mBaseCarGoodsItems.get(position).getData().size() == 0) {
                    mBaseCarGoodsItems.remove(position);
                }
                BaseCarAdapter.this.notifyDataSetChanged();
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

    public interface OnCbClickListener {
        void onParenetCbClickListener(int postion, boolean isChecked, boolean childCheck);
    }

    public interface CarBaseClickChildListener {
        void OnCarChildClickListener(boolean isCheck, int pos, int position);
    }


}
