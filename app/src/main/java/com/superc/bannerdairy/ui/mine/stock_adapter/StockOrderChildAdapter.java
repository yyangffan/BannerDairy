package com.superc.bannerdairy.ui.mine.stock_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.model.StockItem;

import java.util.List;

/**
 * Created by user on 2018/1/30.
 */

public class StockOrderChildAdapter extends RecyclerView.Adapter<StockOrderChildAdapter.ViewHolder>{
    private List<StockItem.DataBeanX.DataBean> mDataBeen;
    private Context mContext;
    private LayoutInflater mInflater;

    public StockOrderChildAdapter(List<StockItem.DataBeanX.DataBean> dataBeen, Context context) {
        mDataBeen = dataBeen;
        mContext = context;
        mInflater= LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_stock_order_one,parent,false);
        ViewHolder vh=new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StockItem.DataBeanX.DataBean dataBean = mDataBeen.get(position);
        if(dataBean.getGoodsCoverImage()!=null){
            Glide.with(mContext).load(ConnectUrl.REQUESTURL+dataBean.getGoodsCoverImage()).into(holder.mImageView);
        }
        holder.mtv_title.setText(dataBean.getGoodsName());
//        holder.mtv_price.setText(dataBean.get);
//        holder.mtv_num.setText("x"+dataBean.getNum());
        holder.mtv_price.setText("x"+dataBean.getNum());/*暂时将价格显示成数量*/
        holder.mtv_stage.setText(dataBean.getSpecName());
    }

    @Override
    public int getItemCount() {
        return mDataBeen==null?0:mDataBeen.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mtv_title;
        private TextView mtv_price;
        private TextView mtv_num;
        private TextView mtv_stage;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.image);
            mtv_title=itemView.findViewById(R.id.tv_name);
            mtv_price=itemView.findViewById(R.id.iv_price);
            mtv_num=itemView.findViewById(R.id.iv_buy_num);
            mtv_stage=itemView.findViewById(R.id.tv_stage);
        }
    }
}
