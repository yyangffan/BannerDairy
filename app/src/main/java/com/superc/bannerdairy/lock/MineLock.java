package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.FragmentMineBinding;
import com.superc.bannerdairy.model.User;
import com.superc.bannerdairy.ui.goods.ShoppingCartActivity;
import com.superc.bannerdairy.ui.login.LoginActivity;
import com.superc.bannerdairy.ui.mine.AboutActivity;
import com.superc.bannerdairy.ui.mine.InviteActivity;
import com.superc.bannerdairy.ui.mine.MyCollectActivity;
import com.superc.bannerdairy.ui.mine.MyMessageActivity;
import com.superc.bannerdairy.ui.mine.PerfectDataActivity;
import com.superc.bannerdairy.ui.mine.SettingActivity;
import com.superc.bannerdairy.ui.mine.StockActivity;
import com.superc.bannerdairy.ui.mine.WalletActivity;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 创建日期：2017/11/9 on 9:49
 * 描述：
 *
 *
 */

public class MineLock extends BaseLock<FragmentMineBinding> {
    public String nick;
    private String mTx_money = "";//提现余额
    private String mMoney = "";//账户余额

    public MineLock(Context context, FragmentMineBinding binding) {
        super(context, binding);
    }

    public MineLock(Context context, FragmentMineBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        //姓名
        if (MyApplication.getUser() != null) {
            if (MyApplication.getUser().getUsername() != null) {
                nick = MyApplication.getUser().getUsername();
                mBinding.textView5.setText(nick);
            } else {
                mBinding.textView5.setText("游客");
            }
        }
        if (mBinding.textView5.getText().equals("登录") || mBinding.textView5.getText().equals("游客")) {
            mBinding.textView5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goLogin();
                }
            });
        }
        if (MyApplication.getUser().getAgency_level() != null) {
            String agency_level = MyApplication.getUser().getAgency_level();
            /*代理级别 => 0：注册用户，1：特约代理，2：门店代理，3：区县代理，4：市级代理，5：省级代理*/
            switch (agency_level) {
                case "0":
                    mBinding.tvLevel.setText(R.string.zhu);
                    break;
                case "1":
                    mBinding.tvLevel.setText(R.string.te);
                    break;
                case "2":
                    mBinding.tvLevel.setText(R.string.men);
                    break;
                case "3":
                    mBinding.tvLevel.setText(R.string.xian);
                    break;
                case "4":
                    mBinding.tvLevel.setText(R.string.shi);
                    break;
                case "5":
                    mBinding.tvLevel.setText(R.string.sheng);
                    break;
                default:
                    mBinding.tvLevel.setText(R.string.zhu);
                    break;
            }
        }
        getUserMsg();
    }
    /*设置界面*/
    public void goSet() {
        startActivity(SettingActivity.class);
    }

    //个人资料
    public void personalData() {
        startActivity(PerfectDataActivity.class);
    }

    //登录
    public void goLogin() {
        startActivity(LoginActivity.class);
    }

    //我的消息
    public void mineMessage() {
        startActivity(MyMessageActivity.class);
    }

    //我的收藏
    public void mineScoring() {
        startActivity(MyCollectActivity.class);
    }

    //我的库存
    public void mineStock() {
        startActivity(StockActivity.class);
    }

    //购物车
    public void mineShopCar() {
        startActivity(ShoppingCartActivity.class);
    }

    //我的邀请
    public void mineInvitation() {
        startActivity(InviteActivity.class);
    }

    //钱包
    public void wallet() {
        Bundle bundle = new Bundle();
        bundle.putString("txMoney", mTx_money);
        bundle.putString("mMoney", mMoney);
        startActivity(WalletActivity.class, bundle);
    }

    //关于
    public void aboutUs() {
        startActivity(AboutActivity.class);
    }

    public void setMsg() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.head)
                .error(R.drawable.head)
                .priority(Priority.HIGH);
        //姓名
        if (MyApplication.getUser() != null) {
            User user = MyApplication.getUser();
            if (user.getUsername() != null) {
                mBinding.textView5.setText(user.getUsername());
            } else {
                mBinding.textView5.setText("游客");
            }
            if (user.getUser_ico() != null) {
//                Glide.with(mContext).load(ConnectUrl.REQUESTURL + user.user_ico).placeholder(R.drawable.head).into(mBinding.imageView5);
                Glide.with(mContext)
                        .load(ConnectUrl.REQUESTURL + user.user_ico)
                        .apply(options)
                        .into(mBinding.imageView5);
            } else {
                Glide.with(mContext).load(R.drawable.head).apply(options).into(mBinding.imageView5);
                mBinding.textView8.setText("0.00");
                mBinding.textView10.setText("0.00");
            }
        }
        getUserMsg();
    }

    public void getUserMsg() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LOOKUSERINFO, RequestMethod.GET);
        request.add("act", "user");
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
                            JSONObject data = jsonObject.getJSONObject("data");
                            mTx_money = data.getString("tx_money");
                            mMoney = data.getString("money");
                            if (mMoney != null && !mMoney.equals("")) {
                                mBinding.textView8.setText(mMoney);
                            }
                            if (mTx_money != null && !mTx_money.equals("")) {
                                mBinding.textView10.setText(mTx_money);
                            }
                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.head)
                                    .error(R.drawable.head)
                                    .priority(Priority.HIGH);
                            if(mContext!=null) {
                                Glide.with(mContext).load(ConnectUrl.REQUESTURL + data.getString("user_ico")).apply(options).into(mBinding.imageView5);
                            }
                            User user = MyApplication.getUser();
                            user.setAgency_level(data.getString("agency_level"));
                            MyApplication.setUser(user);
                            setMsgAgin();
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

    public void setMsgAgin() {
        if (MyApplication.getUser().getAgency_level() != null) {
            String agency_level = MyApplication.getUser().getAgency_level();
            /*代理级别 => 0：注册用户，1：特约代理，2：门店代理，3：区县代理，4：市级代理，5：省级代理*/
            switch (agency_level) {
                case "0":
                    mBinding.tvLevel.setText(R.string.zhu);
                    break;
                case "1":
                    mBinding.tvLevel.setText(R.string.te);
                    break;
                case "2":
                    mBinding.tvLevel.setText(R.string.men);
                    break;
                case "3":
                    mBinding.tvLevel.setText(R.string.xian);
                    break;
                case "4":
                    mBinding.tvLevel.setText(R.string.shi);
                    break;
                case "5":
                    mBinding.tvLevel.setText(R.string.sheng);
                    break;
                default:
                    mBinding.tvLevel.setText(R.string.zhu);
                    break;
            }
        }
    }


}
