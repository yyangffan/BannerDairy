package com.superc.bannerdairy.lock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.constant.NetworkImageHolderView;
import com.superc.bannerdairy.constant.RunTime;
import com.superc.bannerdairy.databinding.ActivityGoodsDetailBinding;
import com.superc.bannerdairy.model.TreasureItem;
import com.superc.bannerdairy.model.User;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.goods.ShoppingCartActivity;
import com.superc.bannerdairy.ui.mine.MyMessageActivity;
import com.superc.bannerdairy.ui.order.FirmOrderActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.superc.bannerdairy.base.ConnectUrl.REQUESTURL;

/**
 * 创建日期：2017/11/10 on 15:05
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 * 商品展示详情页面
 */

public class GoodsDetailLock extends BaseLock<ActivityGoodsDetailBinding> implements OnItemClickListener, ViewPager.OnPageChangeListener {
    public TreasureItem treasureItem;
    private int buyNum = 2; //购买数量
    private double pice = 0.00;
    private String specId;
    private boolean flag = true;//判断立即购买还是添加购物车，true=立即购买
    private boolean isCollect = false;
    private String mSpec_name;

    public GoodsDetailLock(Context context, ActivityGoodsDetailBinding binding) {
        super(context, binding);
    }

    public GoodsDetailLock(Context context, ActivityGoodsDetailBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {

        treasureItem = (TreasureItem) RunTime.getRunTime(RunTime.ALLGOODS);
        if (treasureItem != null) {
            httpGoods();
        }
    }

    //设置bannner图片
    private void initBanner() {
        List list = new ArrayList();
        for (int a = 0; a < treasureItem.images.size(); a++) {
            mBinding.banner.setBackground(null);
            list.add(REQUESTURL + treasureItem.getImages().get(a).getGoods_image());
//            }
            if (treasureItem.getImages().size() > 1) {
                mBinding.banner.startTurning(4000);
            }
            mBinding.banner.setPages(new CBViewHolderCreator() {

                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, list)
                    .setPageIndicator(new int[]{R.drawable.circle_grey, R.drawable.circle_blue})
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setOnPageChangeListener((ViewPager.OnPageChangeListener) this).setOnItemClickListener((OnItemClickListener) this);

        }
    }

    //获取宝贝推荐列表
    public void httpGoods() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LIST_GOODS, RequestMethod.GET);
        request.add("act", "one");
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("goods_id", treasureItem.getGoods_id());
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
                            JSONObject object = jsonObject.getJSONObject("data");
                            String goodsSaleNum = object.getString("goods_sales_volume");/*商品销售量--没有写到实体类中*/
                            mBinding.goodsSaleNum.setText("销量:" + (goodsSaleNum == null || goodsSaleNum.equals("") ? "0" : goodsSaleNum));/*月销量的赋值*/
                            TreasureItem item = new Gson().fromJson(object.toString(), TreasureItem.class);
                            treasureItem = item;
                            mBinding.web.getSettings().setJavaScriptEnabled(true);
//                            mBinding.web.setText(Html.fromHtml(treasureItem.attribute, null, null));
//                            mBinding.webb.setText(Html.fromHtml(treasureItem.getGoods_details(), null, null));
                            String content = treasureItem.attribute.replace("<img", "<img height=\"auto\"; width=\"100%\"");
                            mBinding.web.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//                            mBinding.web.loadDataWithBaseURL(null, treasureItem.attribute, "text/html", "UTF-8", null); // 加载定义的代码，并设定编码格式和字符集。
                            String contentt = treasureItem.getGoods_details().replace("<img", "<img height=\"auto\"; width=\"100%\"");
                            mBinding.webb.loadDataWithBaseURL(null, contentt, "text/html", "UTF-8", null); // 加载定义的代码，并设定编码格式和字符集。

                            mBinding.title.setText(treasureItem.getGoods_name());
                            mBinding.titleCar.setText(treasureItem.getGoods_name());
                            if(mContext!=null) {
                                Glide.with(mContext).load(REQUESTURL + treasureItem.getGoods_cover_image()).into(mBinding.logo);
                            }
                            //现价
                            mBinding.pice.setText(treasureItem.getGoods_sale_price());
                            //原价
                            mBinding.yuanjian.setText("原价 ¥" + treasureItem.getGoods_original_price());
                            if (treasureItem.getGood_scollect_type() != null) {
                                if (treasureItem.getGood_scollect_type().equals("0")) {
                                    isCollect = true;
                                    mBinding.imageView14.setImageResource(R.drawable.coolect_red);
                                    mBinding.collectTv.setTextColor(Color.parseColor("#ff0000"));
                                } else {
                                    isCollect = false;
                                    mBinding.imageView14.setImageResource(R.drawable.coolect_gray);
                                    mBinding.collectTv.setTextColor(Color.parseColor("#707070"));
                                }
                            } else {
                                isCollect = false;
                                mBinding.imageView14.setImageResource(R.drawable.coolect_gray);
                                mBinding.collectTv.setTextColor(Color.parseColor("#707070"));
                            }

                            //banner图片
                            if (treasureItem.getImages() != null) {
                                initBanner();
                            }
                            //设置默认规格
                            if (treasureItem.getSpec() != null && treasureItem.getSpec().size() != 0) {
                                //规格单商品价格
                                if (treasureItem.getSpec().get(0).getSpec_price() != null) {
                                    pice = Double.parseDouble(Double.parseDouble(treasureItem.getSpec().get(0).getSpec_price().toString()) +
                                            Double.parseDouble(treasureItem.getGoods_sale_price()) + "");
                                }

                                //规格id
                                specId = treasureItem.getSpec().get(0).spec_id;
                                mSpec_name = treasureItem.getSpec().get(0).spec_name;/*规格名称*/
                                mBinding.money.setText("¥ " + (pice * 2));
//                                mBinding.fenlei.setText("请选择:数量   库存:"+treasureItem.getSpec().get(0).spec_stock);
                                mBinding.fenlei.setText("请选择:数量");
                                //全局的数量和价钱
                                if (MyApplication.getUser() != null) {
                                    User user = MyApplication.getUser();
                                    user.setNum(2);
                                    user.setAllPirice(pice);
                                    user.setOrder_id(treasureItem.goods_id);
                                    user.setSpec_id(specId);
                                    /*这个地方的存储是为了在进行单个商品跳转时将规格名称记录(为了在适配器中进行显示)*/
                                    user.setSpec_name(mSpec_name);
                                    MyApplication.setUser(user);

                                } else {

                                }

                            }
                            selectGuige(treasureItem);

                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
//                hideLoading();
            }
        });
    }

    public void showBuy() {
        mBinding.rlBuy.setVisibility(View.VISIBLE);
    }

    public void hideBuy() {
        mBinding.rlBuy.setVisibility(View.GONE);
        mBinding.num.setText("2");
    }

    //确定，提示对话框，加入g购物车成功
    public void goPay() {
        if (pice == 0.00) {
            showToast("请选择规格");
        } else if (MyApplication.getUser() == null) {
            showToast("请登录");
        } else {
            if (flag) {
                //全局的数量和价钱
                User user = MyApplication.getUser();
                user.setNum(Integer.parseInt(mBinding.num.getText().toString()));
                user.setAllPirice(pice * Integer.parseInt(mBinding.num.getText().toString()));
                user.setOrder_id(treasureItem.goods_id);
                user.setSpec_id(specId);
                MyApplication.setUser(user);
                isShowDialog(mBinding.num.getText().toString());
                hideBuy();
                RunTime.setData(RunTime.FIRMGOODS, treasureItem);

//                ShowRemindDialog instance = ShowRemindDialog.getInstance();
//                instance.showDialog(mContext, R.drawable.chenggong1, "是否转库存", false, null);
//                instance.setBtNote("否","是");
//                instance.setOnSomeThingClickListener(new OnSomeThingClickListener() {
//                    @Override
//                    public void OnDoSomeThingClickListener(int view_id) {
//                        toGoNext("1");
//                    }
//                });
//                instance.setOnCancleClickListener(new ShowRemindDialog.OnCancleClickListener() {
//                    @Override
//                    public void OnCancleClickListener() {
//                        toGoNext("2");
//                    }
//                });

            } else {
                //添加购物车
                httpaddCart();
            }
        }
    }
    /*判断是否展示转库存弹窗 num所有数量总和*/
    public void isShowDialog(String num) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ISZKC, RequestMethod.POST);
        request.add("number",num);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
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
                            String data = jsonObject.getString("data");
                            if(data.equals("0")){
                                toGoNext("1");
                            }else if(data.equals("1")){
                                ShowRemindDialog instance = ShowRemindDialog.getInstance();
                                instance.showDialog(mContext, R.drawable.chenggong1, "是否转库存", false, null);
                                instance.setBtNote("否", "是");
                                instance.setOnSomeThingClickListener(new OnSomeThingClickListener() {
                                    @Override
                                    public void OnDoSomeThingClickListener(int view_id) {
                                        toGoNext("1");
                                    }
                                });
                                instance.setOnCancleClickListener(new ShowRemindDialog.OnCancleClickListener() {
                                    @Override
                                    public void OnCancleClickListener() {
                                        toGoNext("2");
                                    }
                                });
                            }
                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });

    }





    public void toGoNext(String type) {
        // TODO: 2018/1/27 转库存的话需要调接口--判断是否能够转库存
        Bundle bundle = new Bundle();
        bundle.putString("flag", "one");
        bundle.putParcelable("order", treasureItem);
        /*用来区分是否添加为库存*/
        bundle.putString("type", type);
        startActivity(FirmOrderActivity.class, bundle);
    }

    //加入购物车
    public void addCar() {
        flag = false;
        showBuy();
//        httpaddCart();

    }

    //关闭
    public void finsh() {
        ((BaseActivity) mContext).finish();
    }

    //消息
    public void goMessage() {
        startActivity(MyMessageActivity.class);
    }

    //+
    public void add() {
        String num = mBinding.num.getText().toString();
        mBinding.num.setText(Integer.parseInt(num) + 2 + "");
        mBinding.money.setText("¥ " + pice * Integer.parseInt(mBinding.num.getText().toString()));
        //全局的数量和价钱
        User user = MyApplication.getUser();
        if (user != null) {
            user.setNum(Integer.parseInt(mBinding.num.getText().toString()));
            user.setAllPirice(pice * Integer.parseInt(mBinding.num.getText().toString()));
            user.setSpec_id(specId);
            MyApplication.setUser(user);
        }

    }

    //-
    public void jian() {
        String num = mBinding.num.getText().toString();
        if (Integer.parseInt(num) != 2) {
            mBinding.num.setText(Integer.parseInt(num) - 2 + "");
            mBinding.money.setText("¥ " + pice * Integer.parseInt(mBinding.num.getText().toString()));
            //全局的数量和价钱
            User user = MyApplication.getUser();
            if (user != null) {
                user.setNum(Integer.parseInt(mBinding.num.getText().toString()));
                user.setAllPirice(pice * Integer.parseInt(mBinding.num.getText().toString()));
                user.setSpec_id(specId);
                MyApplication.setUser(user);
            }
        }

    }

    //我的购物车
    public void goShoppingCart() {
        if (MyApplication.getUser() != null) {
            startActivity(ShoppingCartActivity.class);
        } else {
            showToast("请登录");
        }
    }

    /*收藏商品*/
    public void goCollectGoods() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = null;
        if (isCollect) {/*已经收藏*/
            request = NoHttp.createJsonObjectRequest(ConnectUrl.COOLECTDELTE, RequestMethod.GET);
        } else {
            request = NoHttp.createJsonObjectRequest(ConnectUrl.COOLECTGOOD, RequestMethod.POST);
        }
        Gson gson = new Gson();
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("spec_id", specId);
        request.add("goods_id", treasureItem.getGoods_id());
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    String resultCode = null;
                    Boolean success = false;
                    boolean show = false;

                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            if (isCollect) {
                                mBinding.imageView14.setImageResource(R.drawable.coolect_gray);
                                mBinding.collectTv.setTextColor(Color.parseColor("#707070"));
                            } else {
                                mBinding.imageView14.setImageResource(R.drawable.coolect_red);
                                mBinding.collectTv.setTextColor(Color.parseColor("#ff0000"));
                            }
                            isCollect = !isCollect;
                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                showToast("网络异常");

            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }

    //选择规格
    public void selectGuige(final TreasureItem treasureItem) {
        final TagFlowLayout mFlowLayout = mBinding.goodsTags;
        TagAdapter<TreasureItem.SpecBean> tagAdapter = new TagAdapter<TreasureItem.SpecBean>(treasureItem.getSpec()) {
            @Override
            public View getView(FlowLayout parent, int position, TreasureItem.SpecBean guige) {
                CheckBox tv = (CheckBox) LayoutInflater.from(mContext).inflate(R.layout.item_viewgroup_textview, null);

                tv.setText(guige.getSpec_name());
                //全局的规格id
//                ImageUtils.setConrnerImage(mImageView, ConnectUrl.REQUESTURL_IMAGE +mGoods.getOriginal_img());
                return tv;
            }
        };

        mFlowLayout.setAdapter(tagAdapter);
        tagAdapter.setSelectedList(0);/*设置默认选择第几个*/
        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
//                    selectedMap.put(viewHolder.getItemPosition(), selectPosSet);
//                    selectedMap.put(position, selectPosSet);
//                int selectPosition = -1;
                int selectPosition = 0;
                for (int item : mFlowLayout.getSelectedList()) {
                    selectPosition = item;
                }
                //  Toast.makeText(mContext, position + "位置" + selectPosition + "被选中", Toast.LENGTH_SHORT).show();
//                    Log.e("选中", "onSelected: "+ mGoodsTags.get(position).getGuiges().get(Integer.parseInt(mFlowLayout.getSelectedList().toString())));
//                mItemClickListener.onItemClick(position, selectPosition);
                //总价格
                if (treasureItem.getSpec().get(selectPosition).getSpec_price() != null) {
                    //商品单价
                    pice = Double.parseDouble((Double.parseDouble(treasureItem.getSpec().get(selectPosition).getSpec_price().toString()) + Double.parseDouble(treasureItem.getGoods_sale_price())) + "");
                    mBinding.money.setText("¥ " + (pice * 2));
                    //规格id
                    specId = treasureItem.getSpec().get(selectPosition).spec_id;
                    /*这个地方的存储是为了在进行单个商品跳转时将规格名称记录(为了在适配器中进行显示)*/
                    mSpec_name = treasureItem.getSpec().get(selectPosition).spec_name;
                    User user = MyApplication.getUser();
                    user.setSpec_name(mSpec_name);
                    MyApplication.setUser(user);
                    mBinding.num.setText("2");
//                    mBinding.fenlei.setText("请选择:数量   库存:"+treasureItem.getSpec().get(selectPosition).getSpec_stock());
                    mBinding.fenlei.setText("请选择:数量");
                }

            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(int position) {

    }

    //加入购物车api
    private void httpaddCart() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ADDCART, RequestMethod.POST);
        Gson gson = new Gson();
        request.add("spec_id", specId);
        request.add("goods_number", mBinding.num.getText().toString());
        request.add("creator", MyApplication.getUser().user_id);
        request.add("goods_id", treasureItem.getGoods_id());
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {


            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    String resultCode = null;
                    Boolean success = false;
                    boolean show = false;

                    try {
                        JSONObject jsonObject = response.get();
//                        resultCode = jsonObject.getString("code");
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
//                        if (resultCode.equals("200")) {
                        if (success) {
                            initDialog();

                        }
                        if (show) {
                            showToast(jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                showToast("网络异常");

            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }

    //加入购物车dialog
    private Dialog initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View v = LayoutInflater.from(mContext).inflate(R.layout.car_dialog, null);
        TextView success = (TextView) v.findViewById(R.id.success);
        builder.setView(v);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        //设置背景为透明色
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        HashMap hashMap = new HashMap();
        dialog.show();
        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                mBinding.rlBuy.setVisibility(View.GONE);
                //加入购物车接口
                flag = true;
            }
        });
        return dialog;
    }

}
