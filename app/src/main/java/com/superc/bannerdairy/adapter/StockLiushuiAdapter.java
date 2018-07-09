package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.model.StockLiushuiBean;

import java.util.List;

/**
 * Created by user on 2018/3/29.
 */

public class StockLiushuiAdapter extends RecyclerView.Adapter<StockLiushuiAdapter.ViewHolder>{
    private Context mContext;
    private List<StockLiushuiBean.DataBean> mDataBeen;
    private LayoutInflater mInflater;

    public StockLiushuiAdapter(Context context, List<StockLiushuiBean.DataBean> dataBeen) {
        mContext = context;
        mDataBeen = dataBeen;
        mInflater=LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=mInflater.inflate(R.layout.item_stock_layout,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        StockLiushuiBean.DataBean bean=mDataBeen.get(position);
        vh.mtv_title.setText(bean.getGoodsName());
        vh.mtv_time.setText(bean.getCreated());
        vh.mtv_money.setText(bean.getOrderGoodsNumber());
    }

    @Override
    public int getItemCount() {
        return mDataBeen==null?0:mDataBeen.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtv_title;
        private TextView mtv_time;
        private TextView mtv_money;

        public ViewHolder(View itemView) {
            super(itemView);
            mtv_title=itemView.findViewById(R.id.buyrecord_title);
            mtv_time=itemView.findViewById(R.id.buyrecord_time);
            mtv_money=itemView.findViewById(R.id.buyrecord_money);
        }
    }
}
