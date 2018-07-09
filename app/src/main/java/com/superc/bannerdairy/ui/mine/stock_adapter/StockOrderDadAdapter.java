package com.superc.bannerdairy.ui.mine.stock_adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.model.StockItem;

import java.util.List;

/**
 * Created by user on 2018/1/30.
 */

public class StockOrderDadAdapter extends RecyclerView.Adapter<StockOrderDadAdapter.ViewHolder> {
    public List<StockItem.DataBeanX> mStockOrderList;
    private Context mContext;
    private LayoutInflater mInflater;

    public StockOrderDadAdapter(List<StockItem.DataBeanX> stockOrderList, Context context) {
        mStockOrderList = stockOrderList;
        mContext = context;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=mInflater.inflate(R.layout.item_stock_order,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StockItem.DataBeanX dataBeanX = mStockOrderList.get(position);
        holder.mtv_title.setText(dataBeanX.getCategoryName());

        GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,1);
        holder.mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.recy_shopcar));
        holder.mRecyclerView.setLayoutManager(gridLayoutManager);
        StockOrderChildAdapter mAdapter=new StockOrderChildAdapter(dataBeanX.getData(),mContext);
        holder.mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return mStockOrderList==null?0:mStockOrderList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mtv_title;
        RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            mtv_title=itemView.findViewById(R.id.item_stock_title);
            mRecyclerView=itemView.findViewById(R.id.item_stock_rv);
        }
    }
}
