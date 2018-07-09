package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.databinding.ActivitySettingBinding;
import com.superc.bannerdairy.jiguang.SetJPushAlias;
import com.superc.bannerdairy.model.User;
import com.superc.bannerdairy.ui.login.ChangePwdActivity;
import com.superc.bannerdairy.ui.login.LoginActivity;
import com.superc.bannerdairy.ui.mine.SettingActivity;

/**
 * Created by user on 2017/12/22.
 */

public class SettingLock extends BaseLock<ActivitySettingBinding> {

    public SettingLock(Context context, ActivitySettingBinding binding) {
        super(context, binding);
    }

    public SettingLock(Context context, ActivitySettingBinding binding, Bundle bundle) {
        super(context, binding, bundle);

    }


    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.settting);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
    }

    /*退出登录*/
    public void toGetOut() {
//取消收藏
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View dialogV = LayoutInflater.from(mContext).inflate(R.layout.dialog_collect, null);
        TextView tv_cancle = dialogV.findViewById(R.id.dialog_tv_cancle);
        TextView tv_sure = dialogV.findViewById(R.id.dialog_tv_sure);
        TextView tv_content = dialogV.findViewById(R.id.dialog_content);
        ImageView imgv = dialogV.findViewById(R.id.dialog_imgv);
        imgv.setImageResource(R.mipmap.babycry);
        tv_content.setText("确定退出？");
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
                startActivity(LoginActivity.class);
                new SetJPushAlias("", mContext).cancleAlias();
                MyApplication.getInstance().setUser(new User());
                ((SettingActivity) mContext).finish();
                dialog.dismiss();
            }
        });
    }

    /*修改密码界面*/
    public void toChangePwd() {
        startActivity(ChangePwdActivity.class);
    }

}
