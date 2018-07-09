package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.superc.bannerdairy.BR;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.OrderDetailAdapter;
import com.superc.bannerdairy.adapter.WlAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.RecycleViewDivider;
import com.superc.bannerdairy.databinding.ActivityWlBinding;
import com.superc.bannerdairy.model.MessageBean;
import com.superc.bannerdairy.model.OrderItem;

import java.util.ArrayList;
import java.util.List;

/**
 *物流详情
 */

public class WlLock extends BaseLock<ActivityWlBinding> {
    private OrderDetailAdapter mAdapter;
    private ArrayList<OrderItem.OrderGoodsBean> mGoodsList;
    private String mExpress_name = "";
    private String mDanhao;
    private WlAdapter mWlAdapter;
    private List<MessageBean> mMessageBeenList;

    public WlLock(Context context, ActivityWlBinding binding) {
        super(context, binding);
    }

    public WlLock(Context context, ActivityWlBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.wl_xiangqing);
        appBarLock.barData.isLeft = true;
        appBarLock.setLeft(AppBarLock.BACK);
        appBarLock.barData.isRight = false;
        appBarLock.barData.imsLeft=mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

        mGoodsList = new ArrayList<>();
        if (mBundle.getParcelableArrayList("goods") != null) {
            mGoodsList = mBundle.getParcelableArrayList("goods");
        }
        mMessageBeenList= (List<MessageBean>) mBundle.getSerializable("wl_list");
        mExpress_name = mBundle.getString("mExpress_name");
        mDanhao = mBundle.getString("danhao");
        initData();

    }
    /*初始化数据*/
    public void initData() {
        //填充商品信息
        GridLayoutManager ms = new GridLayoutManager(mContext, 1);
        mBinding.wlRecyGood.setLayoutManager(ms);
        mBinding.wlRecyGood.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.recycle_view_line));
        if (mGoodsList != null) {
            mAdapter = new OrderDetailAdapter(mContext, mGoodsList, R.layout.item_treasure_two, BR.goodBean, 4);
            mBinding.wlRecyGood.setAdapter(mAdapter); // 这里或者在xml里设置adapter
        }
        if(mMessageBeenList!=null){
            mWlAdapter=new WlAdapter(mContext,mMessageBeenList);
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(mContext);
            mBinding.wlRecyWl.setLayoutManager(linearLayoutManager);
            mBinding.wlRecyWl.setAdapter(mWlAdapter);
        }
        mBinding.wlTvMsg.setText("物流信息：" + mExpress_name);
        mBinding.wlTvDanhao.setText("快递单号：" + mDanhao);

    }

}
