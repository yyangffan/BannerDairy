package com.superc.bannerdairy.service;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by 浩琦 on 2017/6/20.
 * popwindow显示
 */

public class PopManager {
    private PopupWindow window;
    private int wei;
    private ViewDataBinding dataBinding;
    private Context context;
    private boolean isOver;
    private MissLisenter lisenter;

    public PopManager(Context context) {
        this.context = context;
    }

    public ViewDataBinding showTransparentPop(View v, int resid) {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), resid, null, false);
        windowAttribute(dataBinding.getRoot(), true);
        window.showAtLocation(v, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        setAhpla();
        return dataBinding;
    }

    public ViewDataBinding showPop(View v, int resid) {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), resid, null, false);
        windowAttribute(dataBinding.getRoot(), false);
        window.showAtLocation(v, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        setAhpla();
        return dataBinding;
    }

    public void disPop(View v, final boolean isover) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (window != null) {
                    window.dismiss();
                    window = null;
                }
                isOver = isover;
                if (isover) {
                    ((BaseActivity) context).finish();
                    isOver = false;
                }
            }
        });
    }

    public void stop() {
        if (window != null) {
            window.dismiss();
            window = null;
        }
    }

    public void setMissLisenter(MissLisenter lisenter) {
        this.lisenter = lisenter;
    }

    private void windowAttribute(View popupView, boolean isT) {
        //类型是TYPE_TOAST，像一个普通的Android Toast一样。这样就不需要申请悬浮窗权限了。
//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
        //初始化后不首先获得窗口焦点。不妨碍设备上其他部件的点击、触摸事件。
//        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.height =WindowManager.LayoutParams.WRAP_CONTENT;
        window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        window.setAnimationStyle(R.style.popup_window_anim);
        if (isT) {
            window.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.apl)));
        } else {
            window.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.white)));
        }
//        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.update();
    }

    private void setAhpla() {
        WindowManager.LayoutParams lp = ((BaseActivity) context).getWindow().getAttributes();
        lp.alpha = 0.6f; // 0.0-1.0
        ((BaseActivity) context).getWindow().setAttributes(lp);
        ((BaseActivity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp1 = ((BaseActivity) context).getWindow().getAttributes();
                lp1.alpha = 1.0f; // 0.0-1.0
                ((BaseActivity) context).getWindow().setAttributes(lp1);
                if (isOver) {
                    ((BaseActivity) context).finish();
                }
                if (lisenter != null) {
                    lisenter.miss();
                }
            }
        });
    }

    public interface MissLisenter {
        void miss();
    }

    public void showShare(String shareUrl,boolean isP) {

        OnekeyShare oks = new OnekeyShare();

        // 公用
        oks.disableSSOWhenAuthorize();//关闭sso授权
        oks.setSilent(true);//隐藏编辑页面
//        oks.setTitle("邀请链接:"+shareUrl);
        oks.setTitle(MyApplication.getUser().getUsername()+"邀请您成为旗帜代理");
        oks.setText("注册邀请链接");  // 分享的文字
        if(isP){
            oks.setImagePath("/sdcard/qizhi/erweima.png");//确保SDcard下面存在此张图片
        }else {
            oks.setImagePath("/sdcard/qizhi/logo.png");//确保SDcard下面存在此张图片
        }

        // QQ
        oks.setTitleUrl(shareUrl);  // 分享的网站名
        oks.setComment("我发现一个好玩的应用——旗帜");
        oks.setSite(context.getResources().getString(R.string.app_name));
        oks.setSiteUrl(shareUrl);

        // 微信
        if(shareUrl!=null) {
            oks.setUrl(shareUrl);
            oks.setImageUrl(shareUrl);
        }
//        else if(bitmap!=null){
//            oks.setImageData(bitmap.toString());
//        }
        oks.show(context);
    }


}
