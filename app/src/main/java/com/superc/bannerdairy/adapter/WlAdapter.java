package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.model.MessageBean;

import java.util.List;

/**
 * Created by user on 2018/6/5.
 */
public class WlAdapter extends RecyclerView.Adapter<WlAdapter.ViewHolder> {
    private Context mContext;
    private List<MessageBean> mLists;
    private LayoutInflater mInflater;

    public WlAdapter(Context context, List<MessageBean> stringList) {
        mContext = context;
        mLists = stringList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_wl_recy, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        MessageBean bean = mLists.get(position);
        vh.mtv_content.setText(bean.getThree());
        vh.mtv_time.setText(bean.getOne());
        if (position == 0) {
            vh.mView.setBackground(mContext.getResources().getDrawable(R.drawable.circle_green));
            vh.mtv_content.setTextColor(mContext.getResources().getColor(R.color.color_grees));
            vh.mtv_time.setTextColor(mContext.getResources().getColor(R.color.color_grees));
        } else {
            vh.mView.setBackground(mContext.getResources().getDrawable(R.drawable.circle_grey));
            vh.mtv_content.setTextColor(mContext.getResources().getColor(R.color.gray_nnnn));
            vh.mtv_time.setTextColor(mContext.getResources().getColor(R.color.gray_nnnn));
        }

    }

    @Override
    public int getItemCount() {
        return mLists == null ? 0 : mLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mtv_content;
        private TextView mtv_time;
        private View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mtv_content = itemView.findViewById(R.id.item_wl_content);
            mtv_time = itemView.findViewById(R.id.item_wl_time);
            mView = itemView.findViewById(R.id.item_wl_circle);
        }
    }


}

