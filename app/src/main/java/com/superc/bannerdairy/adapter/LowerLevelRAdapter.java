package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.model.LevelCountList;

import java.util.List;

/**
 * Created by user on 2018/2/2.
 */

public class LowerLevelRAdapter extends RecyclerView.Adapter<LowerLevelRAdapter.ViewHolder> {
    private List<LevelCountList> mLevelCountListList;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public LowerLevelRAdapter(List<LevelCountList> levelCountListList, Context context) {
        mLevelCountListList = levelCountListList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_lower_level, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        LevelCountList levelCountList = mLevelCountListList.get(position);
        String level = levelCountList.getAgency_level();
        String name="";
        switch (level) {
            case "0":name=mContext.getResources().getString(R.string.zhu);break;
            case "1":name=mContext.getResources().getString(R.string.te);break;
            case "2":name=mContext.getResources().getString(R.string.men);break;
            case "3":name=mContext.getResources().getString(R.string.xian);break;
            case "4":name=mContext.getResources().getString(R.string.shi);break;
            case "5":name=mContext.getResources().getString(R.string.sheng);break;
        }

        holder.mtv_title.setText(name);
        holder.mtv_num.setText("(" + levelCountList.getLevel_count() + ")");
        holder.mrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.OnItemClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLevelCountListList == null ? 0 : mLevelCountListList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtv_title;
        TextView mtv_num;
        RelativeLayout mrv;

        public ViewHolder(View itemView) {
            super(itemView);
            mtv_title = itemView.findViewById(R.id.item_lower_title);
            mtv_num = itemView.findViewById(R.id.item_lower_num);
            mrv = itemView.findViewById(R.id.item_rv);
        }
    }

    public interface OnItemClickListener {
        void OnItemClickListener(int position);
    }
}
