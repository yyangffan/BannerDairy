package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemGoodscolBinding;
import com.superc.bannerdairy.model.GoodsColItem;
import com.superc.cframework.base.ui.CBaseViewHolder;
import com.superc.cframework.utils.ToastUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 2017/12/18.
 */

public class GoodsColRcAdapter extends RecyclerView.Adapter {
    List<GoodsColItem.DataBean> datalist;
    LayoutInflater mInflater;
    int layout_id;
    int variable_id;
    Context mContext;
    String specId="";

    public GoodsColRcAdapter(Context context, List datalist, int layout_id, int variable_id) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.datalist = datalist;
        this.layout_id = layout_id;
        this.variable_id = variable_id;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding mBinding = DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                layout_id,
                parent,
                false);
        return new CBaseViewHolder(mContext, mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        final ItemGoodscolBinding itemBinding = (ItemGoodscolBinding) binding;

        binding.setVariable(variable_id, datalist.get(position));
        if (datalist.get(position).getGoodsCoverImage() != null) {
            Glide.with(mContext).load(ConnectUrl.REQUESTURL + datalist.get(position).getGoodsCoverImage()).into(itemBinding.goodsColImg);
        }
        //设置默认规格
        if (datalist.get(position).getSpec() != null && datalist.get(position).getSpec().size() != 0) {
            //规格id
            specId = datalist.get(position).getSpec().get(0).spec_id;
        }
        //取消收藏
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View dialogV = LayoutInflater.from(mContext).inflate(R.layout.dialog_collect, null);
        TextView tv_cancle = dialogV.findViewById(R.id.dialog_tv_cancle);
        TextView tv_sure = dialogV.findViewById(R.id.dialog_tv_sure);
        dialog.setView(dialogV);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delectCollect(datalist.get(position).getGoodsId(),specId,position);
                dialog.dismiss();
            }
        });
        itemBinding.goodsColDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist == null ? 0 : datalist.size();
    }

    public void delectCollect(Object goods_id, Object spec_id, final int pos) {

        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.GOODSCANCLECOLLECT, RequestMethod.GET);
        Gson gson = new Gson();
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("goods_id", goods_id+"");
        //article_category_type   1（点赞）0（收藏）
        request.add("spec_id", spec_id+"");
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            datalist.remove(pos);
                            GoodsColRcAdapter.this.notifyDataSetChanged();
                        }
                        if (show) {
                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                ToastUtil.showShort(mContext, "网络异常");
            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }

}
