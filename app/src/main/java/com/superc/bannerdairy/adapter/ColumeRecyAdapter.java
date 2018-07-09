package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.model.ColumItem;

import java.util.List;

/**
 * 首页栏目的Adapter
 */
public class ColumeRecyAdapter extends RecyclerView.Adapter<ColumeRecyAdapter.ViewHOlder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ColumItem> mColumItems;
    private OnItemIClickListener mOnItemIClickListener;

    public ColumeRecyAdapter(Context context, List<ColumItem> columItems) {
        mContext = context;
        mColumItems = columItems;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setOnItemIClickListener(OnItemIClickListener onItemIClickListener) {
        mOnItemIClickListener = onItemIClickListener;
    }

    @Override
    public ViewHOlder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.item_colum_adapter, parent, false);
        ViewHOlder vh = new ViewHOlder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHOlder holder, final int position) {
        final ColumItem columItem = mColumItems.get(position);
        holder.tv_content.setText(columItem.getMenuName());
        Glide.with(mContext).load(ConnectUrl.REQUESTURL + columItem.getMenuIco()).into(holder.mimgv);

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemIClickListener!=null){
                    mOnItemIClickListener.OnItemIClickListenerColum(position);
                }
            }
        });

    }

    public interface OnItemIClickListener{
        void OnItemIClickListenerColum(int pos);

    }

    @Override
    public int getItemCount() {
        return mColumItems == null ? 0 : mColumItems.size();
    }

    class ViewHOlder extends RecyclerView.ViewHolder {
        TextView tv_content;
        ImageView mimgv;
        LinearLayout mLinearLayout;


        public ViewHOlder(View itemView) {
            super(itemView);
            mimgv = itemView.findViewById(R.id.imageView);
            tv_content = itemView.findViewById(R.id.tv_content);
            mLinearLayout = itemView.findViewById(R.id.colum_ll);
        }
    }
}
