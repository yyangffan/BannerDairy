package com.superc.bannerdairy.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemBasecargoodsBinding;
import com.superc.bannerdairy.model.BaseCarGoodsItem;
import com.superc.cframework.base.ui.CBaseViewHolder;
import com.superc.cframework.utils.SPUtil;
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
 * 购物车里面RecyclerView的Adapter
 */

public class CarBaseGoodsItemAdapter extends RecyclerView.Adapter {
    private List<BaseCarGoodsItem.CarGoodsItem> mCarGoodsItems;
    private Context mContext;
    private LayoutInflater mInflater;
    public int theAllnum = 1;
    public CarbaseClickListener mCarbaseClickListener;
    public OnCarChildDeletListener mOnCarChildDeletListener;

    private int layoutid;
    private int variabledid;


    public void setCarbaseClickListener(CarbaseClickListener carbaseClickListener) {
        mCarbaseClickListener = carbaseClickListener;
    }

    public void setOnCarChildDeletListener(OnCarChildDeletListener onCarChildDeletListener) {
        mOnCarChildDeletListener = onCarChildDeletListener;
    }

    public CarBaseGoodsItemAdapter(Context context, List<BaseCarGoodsItem.CarGoodsItem> dataList, int layoutId, int variableId) {
        this.mContext = context;
        this.mCarGoodsItems = dataList;
        this.layoutid = layoutId;
        this.variabledid = variableId;
    }

    public CarBaseGoodsItemAdapter(List<BaseCarGoodsItem.CarGoodsItem> carGoodsItems, Context context) {
        mCarGoodsItems = carGoodsItems;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutid, parent, false);
        return new CBaseViewHolder(mContext, mBinding.getRoot(), mBinding);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final ViewDataBinding binding = DataBindingUtil.findBinding(holder.itemView);
        final ItemBasecargoodsBinding itemBinding = (ItemBasecargoodsBinding) binding;

        binding.setVariable(variabledid, mCarGoodsItems.get(position));
        if (mCarGoodsItems.get(position).getGoods_cover_image() != null) {
            Glide.with(mContext).load(ConnectUrl.REQUESTURL + mCarGoodsItems.get(position).getGoods_cover_image()).into(itemBinding.image);
        }
        itemBinding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemBinding.cb.performClick();
            }
        });
        itemBinding.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2018/1/26 添加的复选框限制条件(只能选择一种奶粉进行支付)
                if (!mCarGoodsItems.get(position).isCheck) {
                    String carname = SPUtil.get(mContext, "carname", "");
                    if (carname == null || carname.equals("") || mCarGoodsItems.get(position).getCategory_name().equals(carname)) {
                        itemBinding.cb.setChecked(true);
                    } else {
                        itemBinding.cb.setChecked(false);
                        ToastUtil.show(mContext, "只能选择同种进行支付", Toast.LENGTH_SHORT);
                    }
                } else {
                    itemBinding.cb.setChecked(false);
                }
                /*-------------------------------------------------------*/

                mCarGoodsItems.get(position).setCheck(itemBinding.cb.isChecked());
                if (mCarbaseClickListener != null) {
                    mCarbaseClickListener.SomeThingClick(itemBinding.cb.isChecked(), position);
                }
            }
        });
        if (mCarGoodsItems.get(position).isCheck) {
            itemBinding.cb.setChecked(true);
        } else {
            itemBinding.cb.setChecked(false);
        }
        itemBinding.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(mCarGoodsItems.get(position).getNum()) != 2) {
                    setCarNum(mCarGoodsItems.get(position).getCart_id(), (Integer.parseInt(mCarGoodsItems.get(position).getNum()) - 2) + "", true, position, itemBinding.cb.isChecked());
                }
            }
        });
        itemBinding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCarNum(mCarGoodsItems.get(position).getCart_id(), (Integer.parseInt(mCarGoodsItems.get(position).getNum()) + 2) + "", false, position, itemBinding.cb.isChecked());
            }
        });

        itemBinding.imgvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasDialog(position);
            }
        });
        itemBinding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPasDialog(position);
            }
        });

    }

    public interface CarbaseClickListener {
        void SomeThingClick(boolean ischeck, int position);

    }

    public interface OnCarChildDeletListener {
        void OnCarChildDeleteListener(int position);

    }

    public void showPasDialog(final int position) {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View dialogV = LayoutInflater.from(mContext).inflate(R.layout.dialog_collect, null);
        TextView tv_cancle = dialogV.findViewById(R.id.dialog_tv_cancle);
        TextView tv_sure = dialogV.findViewById(R.id.dialog_tv_sure);
        TextView tv_content = dialogV.findViewById(R.id.dialog_content);
        ImageView imgv = dialogV.findViewById(R.id.dialog_imgv);
        imgv.setImageResource(R.mipmap.babycry);
        tv_content.setText("删除购物车");
        dialog.setView(dialogV);
        dialog.show();
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carDelete(mCarGoodsItems.get(position).cart_id, position);
                dialog.dismiss();
            }
        });
    }


    public void carDelete(String cartId, final int pos) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.CARTDELETE, RequestMethod.GET);
        request.add("cart_id", cartId);

        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            mCarGoodsItems.remove(pos);
                            CarBaseGoodsItemAdapter.this.notifyDataSetChanged();
                            if (mOnCarChildDeletListener != null) {
                                mOnCarChildDeletListener.OnCarChildDeleteListener(pos);
                            }
                            if (mCarbaseClickListener != null) {
//                                if (mCarGoodsItems.size() > 0) {
                                    mCarbaseClickListener.SomeThingClick(true, pos);
//                                }
                            }
                        }
                        if (show) {
                            ToastUtil.show(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                ToastUtil.show(mContext, "网络异常", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }

    public void setCarNum(String cartId, String num, final boolean isMore, final int position, final boolean cb_check) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.SHOPADDORCAN, RequestMethod.POST);
        request.add("cart_id", cartId);
        request.add("goods_number", num);

        mRequestQueue.add(2, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 2) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            if (isMore) {
                                mCarGoodsItems.get(position).setNum((Integer.parseInt(mCarGoodsItems.get(position).getNum()) - 2) + "");
                            } else {
                                mCarGoodsItems.get(position).setNum((Integer.parseInt(mCarGoodsItems.get(position).getNum()) + 2) + "");
                            }
                            if (mCarbaseClickListener != null) {
                                mCarbaseClickListener.SomeThingClick(cb_check, position);
                            }
                        }
                        if (show) {
                            ToastUtil.show(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                ToastUtil.show(mContext, "网络异常", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }


    @Override
    public int getItemCount() {
        return mCarGoodsItems == null ? 0 : mCarGoodsItems.size();
    }


}
