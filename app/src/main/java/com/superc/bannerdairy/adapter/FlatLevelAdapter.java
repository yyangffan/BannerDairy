package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemLevelSameBinding;
import com.superc.bannerdairy.model.LevelItem;
import com.superc.cframework.base.ui.CBaseViewHolder;

import java.util.List;

/**
 * Created by Amorr on 2017/12/12.
 */

public class FlatLevelAdapter extends RecyclerView.Adapter {
    protected List<LevelItem> mDataList;// 数据列表
    protected Context mContext; // 上下文
    private int layoutId; // 单布局
    private int variableId; // DataBinding的BR
    private OnItemClickListener mListener; // ItemClick监听


    /**
     * 构造函数
     *
     * @param context    上下文
     * @param dataList   数据列表
     * @param layoutId   单布局
     * @param variableId DataBinding的BR
     */
    public FlatLevelAdapter(Context context, List<LevelItem> dataList, int layoutId, int variableId) {
        this.mContext = context;
        this.mDataList = dataList;
        this.layoutId = layoutId;
        this.variableId = variableId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                layoutId,
                parent,
                false);
        return new CBaseViewHolder(mContext, mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        ItemLevelSameBinding itemBinding = (ItemLevelSameBinding) binding;

        binding.setVariable(variableId, mDataList.get(position));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.head)
                .error(R.drawable.head)
                .priority(Priority.HIGH);
        if (mDataList.get(position).getUser_ico() != null) {
            Glide.with(mContext).load(ConnectUrl.REQUESTURL +mDataList.get(position).getUser_ico()).apply(options).into(itemBinding.imageView15);
        }
        itemBinding.textView11.setText(mDataList.get(position).getUsername());
        itemBinding.textView12.setText(mDataList.get(position).getMobile());
        itemBinding.textView15.setText("直属人数（"+mDataList.get(position).getSuborCount()+"）");
        itemBinding.textView16.setText("团队数量（"+mDataList.get(position).getTeamCount()+"）");

        subTask(binding, position);
    }

    /**
     * 其它操作
     *
     * @param binding  绑定
     * @param position 列表位置
     */
    private void subTask(final ViewDataBinding binding, final int position) {
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(binding, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    /**
     * 设置监听Item点击事件
     *
     * @param onItemClickListener 监听
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    /**
     * Item点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(ViewDataBinding binding, int position);
    }
}

