package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityOnePersonBinding;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import static com.superc.bannerdairy.R.id.subordinateCount;

/**
 * Created by Amorr on 2017/11/24.
 * 县级列表里面单个人的信息界面
 */

public class OnePersonLock extends BaseLock<ActivityOnePersonBinding> {
    private String mUser_id;
    private String mUsername;
    private String mSuperiors_name;
    private AppBarLock mAppBarLock;

    public OnePersonLock(Context context, ActivityOnePersonBinding binding) {
        super(context, binding);
    }

    public OnePersonLock(Context context, ActivityOnePersonBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        mUser_id = mBundle.getString("user_id");
        String teamCount = mBundle.getString("teamCount");
        mBinding.subordinateCount.setText(teamCount==null?"- -":teamCount);
        //0:注册用户 1：特约代理，2：门店代理，3：区县代理，4：市级代理，5：省级代理',
        if (mBundle.getString("flag").equals("zero")) {
            mAppBarLock = new AppBarLock(mContext, R.string.zhu);
            mBinding.titleBar.setAppBarLock(mAppBarLock);
        }  else if (mBundle.getString("flag").equals("one")) {
            mAppBarLock = new AppBarLock(mContext, R.string.te);
        } else if (mBundle.getString("flag").equals("two")) {
            mAppBarLock= new AppBarLock(mContext, R.string.men);
        } else if (mBundle.getString("flag").equals("three")) {
            mAppBarLock = new AppBarLock(mContext, R.string.xian);
        } else if (mBundle.getString("flag").equals("four")) {
            mAppBarLock= new AppBarLock(mContext, R.string.shi);
        } else if (mBundle.getString("flag").equals("five")) {
            mAppBarLock= new AppBarLock(mContext, R.string.sheng);
        }
       mAppBarLock.barData.isLeft = true;
       mAppBarLock.barData.titleLeft = "返回";
       mAppBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(mAppBarLock);
        // 获取数据
        httpData();
    }

    public void goPersonNext() {
        // TODO: 2018/1/24 去掉了继续向下跳转
//        if(!mBinding.subordinateCount.getText().toString().equals("0")) {
//            Bundle bundle = new Bundle();
//            bundle.putString("flag", "name");
//            bundle.putString("name", mSuperiors_name);
//            bundle.putString("user_id", mUser_id);
//            startActivity(SonLevelActivity.class, bundle);
//        }
    }

    private void httpData() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.GETTEAMUSER, RequestMethod.POST);
        final Gson gson = new Gson();
        if (mUser_id != null) {
            request.add("user_id", mUser_id);
        }

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
                            //填充数据
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            //本日和本月新增会员
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("userInfo");
                            mUsername = jsonObject2.getString("username");
                            mBinding.name.setText(mUsername);
                            String mobile = jsonObject2.getString("mobile");
                            String user_ico = jsonObject2.getString("user_ico");
                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.head)
                                    .error(R.drawable.head)
                                    .priority(Priority.HIGH);
                            if (user_ico != null&&mContext!=null) {
                                Glide.with(mContext).load(ConnectUrl.REQUESTURL + user_ico).apply(options).into(mBinding.onePerImg);
                            }
                            //电话
                            mBinding.onePersonPhone.setText(mobile);
                            //上级昵称
                            mSuperiors_name = jsonObject2.getString("superiors_name");
                            mBinding.superiorsName.setText(mSuperiors_name);
                            //邀请人昵称
                            String referee_name = jsonObject2.getString("referee_name");
                            mBinding.refereeName.setText(referee_name);
                            //下级人数--修改成上一界面传过来
//                            String subordinateCount = jsonObject2.getString("subordinateCount");
//                            mBinding.subordinateCount.setText(subordinateCount);
                            //差价收益
                            String differencePrice = jsonObject2.getString("differencePrice");
                            mBinding.differencePrice.setText("￥" + differencePrice);
                            // //'代理级别 => 0：注册用户，1：特约代理，2：门店代理，3：区县代理，4：市级代理，5：省级代理',
                            int agency_level = jsonObject2.getInt("agency_level");
                            switch (agency_level) {
                                case 0:
                                    mBinding.agencyLevel.setText(R.string.zhu);
                                    break;
                                case 1:
                                    mBinding.agencyLevel.setText(R.string.te);
                                    break;
                                case 2:
                                    mBinding.agencyLevel.setText(R.string.men);
                                    break;
                                case 3:
                                    mBinding.agencyLevel.setText(R.string.xian);
                                    break;
                                case 4:
                                    mBinding.agencyLevel.setText(R.string.shi);
                                    break;
                                case 5:
                                    mBinding.agencyLevel.setText(R.string.sheng);
                                    break;
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
}
