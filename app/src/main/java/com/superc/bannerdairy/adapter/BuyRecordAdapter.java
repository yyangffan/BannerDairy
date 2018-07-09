package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.model.BuyRecordBean;

import java.util.List;

/**
 * Created by user on 2018/1/15.
 */

public class BuyRecordAdapter extends BaseAdapter {
    private Context mContext;
    private List<BuyRecordBean.DataBeanX.DataBean> mBuyRecordBeanList;
    private LayoutInflater mInflater;

    public BuyRecordAdapter(Context context, List<BuyRecordBean.DataBeanX.DataBean> buyRecordBeanList) {
        mContext = context;
        mBuyRecordBeanList = buyRecordBeanList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mBuyRecordBeanList == null ? 0 : mBuyRecordBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBuyRecordBeanList == null ? null : mBuyRecordBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mBuyRecordBeanList == null ? 0 : i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BuyViewHolder vh = null;
        if (view == null) {
            vh = new BuyViewHolder();
            view = mInflater.inflate(R.layout.item_buyrecord_layout, viewGroup, false);
            vh.mtv_title = view.findViewById(R.id.buyrecord_title);
            vh.mtv_money = view.findViewById(R.id.buyrecord_money);
            vh.mtv_time = view.findViewById(R.id.buyrecord_time);
            view.setTag(vh);
        } else {
            vh = (BuyViewHolder) view.getTag();
        }
        BuyRecordBean.DataBeanX.DataBean dataBean = mBuyRecordBeanList.get(i);
        if (dataBean.getSource().equals("1")) {
            vh.mtv_title.setText("充值");
        } else if (dataBean.getSource().equals("2")) {
            vh.mtv_title.setText("提现");
        } else if (dataBean.getSource().equals("3")) {
            vh.mtv_title.setText("返利");
        } else if (dataBean.getSource().equals("4")) {
            vh.mtv_title.setText("伯乐奖");
        } else if (dataBean.getSource().equals("5")) {
            vh.mtv_title.setText("高手将");
        } else if (dataBean.getSource().equals("6")) {
            vh.mtv_title.setText("购买商品");
        }else if(dataBean.getSource().equals("7")){
            vh.mtv_title.setText("差价收益("+dataBean.getUsername()+")");
        }else if(dataBean.getSource().equals("8")){
            vh.mtv_title.setText("购买库存");
        }

//        vh.mtv_money.setText(dataBean.getMoney().contains("-")?dataBean.getMoney():"+"+dataBean.getMoney());
//        vh.mtv_money.setTextColor(dataBean.getMoney().contains("-")?mContext.getResources().getColor(R.color.gray_nnnn):mContext.getResources().getColor(R.color.red));
        vh.mtv_money.setText(dataBean.getMoney());
        vh.mtv_time.setText(dataBean.getCreated());
        return view;
    }

    class BuyViewHolder {
        private TextView mtv_title;
        private TextView mtv_money;
        private TextView mtv_time;
    }


}
