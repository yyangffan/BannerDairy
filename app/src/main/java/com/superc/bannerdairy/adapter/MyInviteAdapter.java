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
import com.superc.bannerdairy.databinding.ItemMyInviteBinding;
import com.superc.bannerdairy.model.MyInviteItem;
import com.superc.cframework.base.ui.CBaseViewHolder;

import java.util.List;

/**
 * Created by Amorr on 2017/12/12.
 */

public class MyInviteAdapter extends RecyclerView.Adapter {
    protected List<MyInviteItem> mDataList;// 数据列表
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
    public MyInviteAdapter(Context context, List<MyInviteItem> dataList, int layoutId, int variableId) {
        this.mContext = context;
        this.mDataList = dataList;
        this.layoutId = layoutId;
        this.variableId = variableId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, parent, false);
        return new CBaseViewHolder(mContext, mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        ItemMyInviteBinding itemBinding = (ItemMyInviteBinding) binding;

        binding.setVariable(variableId, mDataList.get(position));
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.head)
                .error(R.drawable.head)
                .priority(Priority.HIGH);
        if (mDataList.get(position).getUser_ico() != null) {
            Glide.with(mContext).load(ConnectUrl.REQUESTURL + mDataList.get(position).getUser_ico()).apply(options).into(itemBinding.imageView15);
        }
        itemBinding.textView11.setText(mDataList.get(position).getUsername());
        itemBinding.textView12.setText(mDataList.get(position).getCreated());
        itemBinding.tvPhone.setText("("+mDataList.get(position).getMobile()+")");
        ///0：注册用户，1：特约代理，2：门店代理，3：区县代理，4：市级代理，5：省级代理',
        if (mDataList.get(position).getAgency_level() != null) {
            switch (Integer.parseInt(mDataList.get(position).getAgency_level())) {
                case 0:
                    itemBinding.textView15.setText(R.string.zhu);
                    break;
                case 1:
                    itemBinding.textView15.setText(R.string.te);
                    break;
                case 2:
                    itemBinding.textView15.setText(R.string.men);
                    break;
                case 3:
                    itemBinding.textView15.setText(R.string.xian);
                    break;
                case 4:
                    itemBinding.textView15.setText(R.string.shi);
                    break;
                case 5:
                    itemBinding.textView15.setText(R.string.sheng);
                    break;
            }
        }
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


