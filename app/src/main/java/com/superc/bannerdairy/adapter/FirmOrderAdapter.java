package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemFirmcarBinding;
import com.superc.bannerdairy.model.TreasureItem;
import com.superc.cframework.base.ui.CBaseViewHolder;

import java.util.List;

/**
 * 单个商品在进行购买时的确认订单进行显示的适配器
 */

public class FirmOrderAdapter extends RecyclerView.Adapter {
    protected List<TreasureItem> mDataList;// 数据列表
    protected Context mContext; // 上下文
    private int layoutId; // 单布局
    private int variableId; // DataBinding的BR
    private CarAdapter.OnItemClickListener mListener; // ItemClick监听
    private Boolean allIsCb = false;
    private int buyNum = 1; //购买数量
    private double price = 0.00;//z总价钱

    /**
     * 构造函数
     *
     * @param context    上下文
     * @param dataList   数据列表
     * @param layoutId   单布局
     * @param variableId DataBinding的BR
     */
    public FirmOrderAdapter(Context context, List<TreasureItem> dataList, int layoutId, int variableId) {
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        ItemFirmcarBinding itemBinding = (ItemFirmcarBinding) binding;
        binding.setVariable(variableId, mDataList.get(position));
        if (mDataList.get(position).getGoods_cover_image() != null) {
            Glide.with(mContext).load(ConnectUrl.REQUESTURL + mDataList.get(position).getGoods_cover_image()).into(itemBinding.image);
        }
        itemBinding.ivPrice.setText("￥" + MyApplication.getUser().getAllPirice() / MyApplication.getUser().getNum());
        itemBinding.firmNumber.setText(MyApplication.getUser().getNum() + "");
        String spec_name = MyApplication.getInstance().getUser().getSpec_name();
        itemBinding.tvStage.setText(spec_name);/*设置规格显示*/
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
    public void setOnItemClickListener(CarAdapter.OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    /**
     * Item点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(ViewDataBinding binding, int position);
    }


}
