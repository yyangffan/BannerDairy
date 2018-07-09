package com.superc.bannerdairy.lock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;

import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityMainBinding;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.home.FashionSellerFragment;
import com.superc.bannerdairy.ui.home.FragmentAdapter;
import com.superc.bannerdairy.ui.home.HomeFragment;
import com.superc.bannerdairy.ui.home.LiaoTianFragment;
import com.superc.bannerdairy.ui.home.MainActivity;
import com.superc.bannerdairy.ui.home.MineFragment;
import com.superc.cframework.utils.LogUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期：2017/11/7 on 14:15
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class MainLock extends BaseLock<ActivityMainBinding> {

    private HomeFragment mHomeFragment;
    private MineFragment mMineFragment;
    private FashionSellerFragment mFashionSellerFragment;

    private List<Fragment> mFragmentList;
    private FragmentAdapter mFragmentAdapter;
    private RadioButton radio;
    //首页消息
    public static final int BIND = 1001;
    //邀请代理
    public static final int YAOQING = 2000;
    //我的收藏
    public static final int COLLECT = 2001;
    //我的发布
    public static final int RESULT = 2002;
    //库存流水
    public static final int KUCUNLIUSHUI = 110;

    private String mPage = "0";
    private String category_id = "";
    private LiaoTianFragment mLiaoTianFragment;

    public MainLock(Context context, ActivityMainBinding binding) {
        super(context, binding);
    }

    public MainLock(Context context, ActivityMainBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        initBottom();
        if (mBundle != null) {
            mPage = mBundle.getString("page", "0");
        }
        if (mPage == null) {
            mPage = "0";
        }
        //主页
        mHomeFragment = new HomeFragment();
        //客服
        mLiaoTianFragment = new LiaoTianFragment();
        //卖家秀
        mFashionSellerFragment = new FashionSellerFragment();
        //我的
        mMineFragment = new MineFragment();
        mFragmentList = new ArrayList<Fragment>();
        // 将页面添加到容器里面
        mFragmentList.add(mHomeFragment);
        mFragmentList.add(mLiaoTianFragment);
        mFragmentList.add(mFashionSellerFragment);

        mFragmentList.add(mMineFragment);

        mFragmentAdapter = new FragmentAdapter(((MainActivity) mContext).getSupportFragmentManager(), mFragmentList);
        //ViewPager绑定监听器
        mBinding.mainPage.setAdapter(mFragmentAdapter);
        //ViewPager设置默认当前的项
        mBinding.mainPage.setCurrentItem(Integer.parseInt(mPage));
        mBinding.mainPage.setScrollble(false);
        initViewPage();
//        radio=mBinding.mainHome;
//        flushBar();
        saveBitmap();
    }

    /**
     * 保存方法
     */
    public void saveBitmap() {
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo);
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "qizhi");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "logo.png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void homePage(View view) {
        mBinding.mainPage.setCurrentItem(0);
    }

    public void customerPage(View view) {
        if (MyApplication.getUser().user_id == null) {
            ShowRemindDialog.getInstance().gotoLogin(mContext);
        } else {
            mBinding.mainPage.setCurrentItem(1);
        }

    }

    /*买家秀*/
    public void showPage(View view) {
        if (MyApplication.getUser().user_id == null) {
            ShowRemindDialog.getInstance().gotoLogin(mContext);
        } else {
            mBinding.mainPage.setCurrentItem(2);
            if (mFashionSellerFragment != null) {
                mFashionSellerFragment.onResume();
            }
        }

    }

    /*栏目跳转过来--跳转到买家秀(可以进行别的操作)*/
    public void showPageMsg(String cata_id) {
        if (MyApplication.getUser().user_id == null) {
            ShowRemindDialog.getInstance().gotoLogin(mContext);
        } else {
            category_id = cata_id;
            mBinding.mainPage.setCurrentItem(2);
        }

    }


    public void minePage(View view) {
        if (MyApplication.getUser().user_id == null) {
            ShowRemindDialog.getInstance().gotoLogin(mContext);
        } else {
            mBinding.mainPage.setCurrentItem(3);
        }
    }

    private void initViewPage() {
        //ViewPager设置监听器，需要重写onPageScrollStateChanged，onPageScrolled，onPageSelected三个方法
        mBinding.mainPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
             */
            @Override
            public void onPageScrollStateChanged(int state) {
//                LogUtil.i("PageScroll：", "onPageScrollStateChanged" + ":" + state);
            }

            /**
             * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
             * offsetPixels:当前页面偏移的像素位置
             */
            @Override
            public void onPageScrolled(int position, float offset,
                                       int offsetPixels) {
//                LogUtil.i("mOffset", "offset:" + offset + ",position:" + position);
            }

            /**
             * 将当前选择的页面的标题设置字体颜色为蓝色
             */
            @Override
            public void onPageSelected(int position) {
                LogUtil.i("PageScroll：", "onPageSelected" + ":" + position);
                resetTextView();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    switch (position) {
                        case 0:
                            hideInput(mContext,mBinding.mainHome);
                            mBinding.mainHome.setTextColor(mContext.getColor(R.color.main_color));
                            setImage(mBinding.mainHome, mContext.getDrawable(R.drawable.zhuye2));
                            AppBarLock appBarLock = new AppBarLock(mContext, R.string.main_titlet);
                            appBarLock.barData.isLeft = true;
                            appBarLock.setLeft(BIND);
                            appBarLock.setRight(YAOQING);
                            appBarLock.barData.isRight = true;
                            appBarLock.barData.titleRight = "邀请代理";
                            appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.xiaoxi);
                            getMsgNum(appBarLock);
                            mBinding.titleBar.setAppBarLock(appBarLock);
                            break;
                        case 1:
                            mBinding.mainCustomer.setTextColor(mContext.getColor(R.color.main_color));
                            setImage(mBinding.mainCustomer, mContext.getDrawable(R.drawable.customer2));
                            mBinding.titleBar.setAppBarLock(new AppBarLock(mContext, R.string.mine_title));

                            AppBarLock appBarLockk = new AppBarLock(mContext, R.string.main_kefu);
                            appBarLockk.barData.isLeft = false;
                            appBarLockk.barData.isRight = false;
//                            appBarLockk.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.xiaoxi);
                            mBinding.titleBar.setAppBarLock(appBarLockk);
                            break;
                        case 2:
                            hideInput(mContext,mBinding.mainShow);
                            mBinding.mainShow.setTextColor(mContext.getColor(R.color.main_color));
                            setImage(mBinding.mainShow, mContext.getDrawable(R.drawable.show2));
                            AppBarLock appBarLoc = new AppBarLock(mContext, R.string.fash_title);
                            appBarLoc.barData.isLeft = false;/*去掉了左侧我的收藏--true  false来决定*/
                            appBarLoc.barData.isRight = true;
                            appBarLoc.setLeft(COLLECT);
                            appBarLoc.setRight(RESULT);
                            appBarLoc.barData.titleLeft = "我的收藏";
                            appBarLoc.barData.titleRight = "发布";
                            mBinding.titleBar.setAppBarLock(appBarLoc);
                            if (mFashionSellerFragment != null) {
                                mFashionSellerFragment.onResume();
                            }
                            break;
                        case 3:
                            hideInput(mContext,mBinding.mainMine);
                            mBinding.mainMine.setTextColor(mContext.getColor(R.color.main_color));
                            setImage(mBinding.mainMine, mContext.getDrawable(R.drawable.mine2));
                            mBinding.titleBar.setAppBarLock(new AppBarLock(mContext, R.string.mine_title));

                            break;
                    }
                } else {
                    switch (position) {
                        case 0:
                            hideInput(mContext,mBinding.mainHome);
                            mBinding.mainHome.setTextColor(Color.RED);
                            setImage(mBinding.mainHome, mContext.getResources().getDrawable(R.drawable.zhuye2));
                            AppBarLock appBarLock = new AppBarLock(mContext, R.string.main_titlet);
                            appBarLock.barData.isLeft = true;
                            appBarLock.setLeft(BIND);
                            appBarLock.setRight(YAOQING);

                            appBarLock.barData.isRight = true;
                            appBarLock.barData.titleRight = "邀请代理";
                            appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.xiaoxi);
                            mBinding.titleBar.setAppBarLock(appBarLock);

                            break;
                        case 1:
                            mBinding.mainCustomer.setTextColor(Color.RED);
                            setImage(mBinding.mainCustomer, mContext.getResources().getDrawable(R.drawable.customer2));
                            mBinding.titleBar.setAppBarLock(new AppBarLock(mContext, R.string.mine_title));
                            AppBarLock appBarLockk = new AppBarLock(mContext, R.string.main_kefu);
                            appBarLockk.barData.isLeft = false;
                            appBarLockk.barData.isRight = false;
//                            appBarLockk.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.xiaoxi);
                            mBinding.titleBar.setAppBarLock(appBarLockk);
                            break;
                        case 2:
                            hideInput(mContext,mBinding.mainShow);
                            mBinding.mainShow.setTextColor(Color.RED);
                            setImage(mBinding.mainShow, mContext.getResources().getDrawable(R.drawable.show2));
                            AppBarLock appBarLoc = new AppBarLock(mContext, R.string.fash_title);
                            appBarLoc.barData.isLeft = false;/*去掉了左侧我的收藏--true  false来决定*/
                            appBarLoc.barData.isRight = true;
                            appBarLoc.barData.titleLeft = "我的收藏";
                            appBarLoc.barData.titleRight = "发布";
                            appBarLoc.setLeft(COLLECT);
                            appBarLoc.setRight(RESULT);

                            mBinding.titleBar.setAppBarLock(appBarLoc);
                            break;
                        case 3:
                            hideInput(mContext,mBinding.mainMine);
                            mBinding.mainMine.setTextColor(Color.RED);
                            setImage(mBinding.mainMine, mContext.getResources().getDrawable(R.drawable.mine2));
                            mBinding.titleBar.setAppBarLock(new AppBarLock(mContext, R.string.mine_title));

                            break;
                    }
                }
            }
        });
    }

    private void initBottom() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setImage(mBinding.mainHome, mContext.getDrawable(R.drawable.zhuye2));
            setImage(mBinding.mainCustomer, mContext.getDrawable(R.drawable.customer1));
            setImage(mBinding.mainShow, mContext.getDrawable(R.drawable.show1));
            setImage(mBinding.mainMine, mContext.getDrawable(R.drawable.mine1));
        } else {
            setImage(mBinding.mainHome, mContext.getResources().getDrawable(R.drawable.zhuye2));
            setImage(mBinding.mainCustomer, mContext.getResources().getDrawable(R.drawable.customer1));
            setImage(mBinding.mainShow, mContext.getResources().getDrawable(R.drawable.show1));
            setImage(mBinding.mainMine, mContext.getResources().getDrawable(R.drawable.mine1));
        }
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.main_titlet);
        appBarLock.barData.isLeft = true;
        appBarLock.setLeft(BIND);
        appBarLock.barData.isRight = true;
        appBarLock.setRight(YAOQING);

        appBarLock.barData.titleRight = "邀请代理";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.xiaoxi);
        getMsgNum(appBarLock);
        mBinding.titleBar.setAppBarLock(appBarLock);


    }

    public void RefreshNum(){
        if(mHomeFragment.isVisible()) {
            hideInput(mContext, mBinding.mainHome);
            mBinding.mainHome.setTextColor(mContext.getResources().getColor(R.color.main_color));
            setImage(mBinding.mainHome, mContext.getResources().getDrawable(R.drawable.zhuye2));
            AppBarLock appBarLock = new AppBarLock(mContext, R.string.main_titlet);
            appBarLock.barData.isLeft = true;
            appBarLock.setLeft(BIND);
            appBarLock.setRight(YAOQING);
            appBarLock.barData.isRight = true;
            appBarLock.barData.titleRight = "邀请代理";
            appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.xiaoxi);
            getMsgNum(appBarLock);
            mBinding.titleBar.setAppBarLock(appBarLock);
        }
    }

    public void refresh() {
        if (mMineFragment.isVisible()){
            mMineFragment.onResume();
        }
        if(mHomeFragment.isVisible()){
            mHomeFragment.refreshData();
        }
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBinding.mainHome.setTextColor(mContext.getColor(R.color.main_bottom_text));
            mBinding.mainCustomer.setTextColor(mContext.getColor(R.color.main_bottom_text));
            mBinding.mainShow.setTextColor(mContext.getColor(R.color.main_bottom_text));
            mBinding.mainMine.setTextColor(mContext.getColor(R.color.main_bottom_text));
            setImage(mBinding.mainHome, mContext.getDrawable(R.drawable.zhuye1));
            setImage(mBinding.mainCustomer, mContext.getDrawable(R.drawable.customer1));
            setImage(mBinding.mainShow, mContext.getDrawable(R.drawable.show1));
            setImage(mBinding.mainMine, mContext.getDrawable(R.drawable.mine1));
        } else {
            mBinding.mainHome.setTextColor(Color.GRAY);
            mBinding.mainCustomer.setTextColor(Color.GRAY);
            mBinding.mainShow.setTextColor(Color.GRAY);
            mBinding.mainMine.setTextColor(Color.GRAY);
            setImage(mBinding.mainHome, mContext.getResources().getDrawable(R.drawable.zhuye1));
            setImage(mBinding.mainCustomer, mContext.getResources().getDrawable(R.drawable.customer1));
            setImage(mBinding.mainShow, mContext.getResources().getDrawable(R.drawable.show1));
            setImage(mBinding.mainMine, mContext.getResources().getDrawable(R.drawable.mine1));
        }
    }

    private void setImage(RadioButton radioButton, Drawable image) {
        image.setBounds(1, 1, 60, 60);
        radioButton.setCompoundDrawables(null, image, null, null);
    }

    public void onResume() {
        if (MyApplication.getUser() != null && MyApplication.getUser().getUser_id() == null) {
            mBinding.mainPage.setCurrentItem(0);
            if (mMineFragment != null) {
                mMineFragment.onResume();
            }
        }
    }

    public void getMsgNum(final AppBarLock appBarLock) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.UNREADMSG, RequestMethod.GET);
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
                        if (success) {
                            String data = jsonObject.getString("data");
                            if(data.equals("0")) {
                                appBarLock.msg_num = 0;
                                appBarLock.number="0";
                                mBinding.titleBar.setAppBarLock(appBarLock);
                            }else {
                                appBarLock.msg_num = Integer.parseInt(data);
                                appBarLock.number = data;
                                mBinding.titleBar.setAppBarLock(appBarLock);
                            }
                        }else {
                            appBarLock.msg_num = 0;
                            appBarLock.number="0";
                            mBinding.titleBar.setAppBarLock(appBarLock);
                        }
                        show = jsonObject.getBoolean("show");
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
                if (MyApplication.getUser().user_id != null) {
                    showToast("网络异常");
                }
            }

            @Override
            public void onFinish(int what) {
            }
        });

    }

    private void hideInput(Context context,View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }

}
