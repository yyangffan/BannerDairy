package com.superc.bannerdairy.base;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupWindow;

import com.superc.bannerdairy.R;
import com.superc.cframework.base.ui.CBaseActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 创建日期：2017/11/3 on 11:27
 * 描述：BaseActivity
 * 作者：郭士超
 * QQ：1169380200
 */

public abstract class BaseActivity extends CBaseActivity {
    private static final String TAG = "BaseActivity";
    private PopupWindow mPopupWindow;

    // 在setContentView前的初始化
    protected void initSys() {
        initTransition();
        initState();
    }

    // 初始化操作
    @Override
    protected void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    // 初始化设置过渡动画
    protected void initTransition() {

    }

    // 判断是否使用沉浸式状态栏
    private void initState() {
        //这一行注意！看本文最后的说明！！！！
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*加载Loading 如下两个*/
    public void showLoadPop() {
        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_load_popup, null));
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public void hideLoadPop() {
        if(mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if(mPopupWindow!=null&&mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
