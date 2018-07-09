package com.superc.bannerdairy.lock;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.ui.mine.InviteActivity;
import com.superc.bannerdairy.ui.mine.MyCollectActivity;
import com.superc.bannerdairy.ui.mine.MyMessageActivity;
import com.superc.bannerdairy.ui.mine.ReleaseActivity;
import com.superc.bannerdairy.ui.mine.SotckLiushuiActivity;
import com.superc.cframework.utils.ToastUtil;

import static com.superc.bannerdairy.lock.MainLock.KUCUNLIUSHUI;


/**
 * Created by 浩琦 on 2017/6/16.
 * 所有Bar
 */

public class AppBarLock {

    private Context context;
    private int leftType;
    private int rightType;
    public AppBarData barData;
    //通用返回
    public static final int BACK = 0x1000;
    //首页消息
    public static final int BIND = 1001;
    //邀请代理
    public static final int YAOQING = 2000;
    //我的收藏
    public static final int COLLECT = 2001;
    //我的发布
    public static final int RESULT = 2002;
    public int msg_num=0;
    public String number="0";

    public AppBarLock(Context context, int title) {
        this.context = context;
        barData = new AppBarData(context.getResources().getString(title), context.getResources().getDrawable(R.drawable.black_background), null, null, null, true, false);
        leftType = BACK;
    }
    public AppBarLock(Context context, String title) {
        this.context = context;
        barData = new AppBarData(title, context.getResources().getDrawable(R.drawable.black_background), null, null, null, true, false);
        leftType = BACK;
    }



    public AppBarLock(Context context, int title, int imsLeft, int imsRight, boolean isLeft, boolean isRight) {
        this.context = context;
        if (imsLeft == 0) {
            barData = new AppBarData(context.getResources().getString(title), null, context.getResources().getDrawable(imsRight), null, null, isLeft, isRight);
        } else if (imsRight == 0) {
            barData = new AppBarData(context.getResources().getString(title), context.getResources().getDrawable(imsLeft), null, null, null, isLeft, isRight);
        } else {
            barData = new AppBarData(context.getResources().getString(title), context.getResources().getDrawable(imsLeft), context.getResources().getDrawable(imsRight), null, null, isLeft, isRight);
        }
        leftType = BACK;
    }

    public AppBarLock(Context context, int title, int titleLeft, int titleRight, boolean isLeft, boolean isRight, boolean over) {
        this.context = context;
        if (titleLeft == 0) {
            barData = new AppBarData(context.getResources().getString(title), null, null, null, context.getResources().getString(titleRight), isLeft, isRight);

        } else if (titleRight == 0) {
            barData = new AppBarData(context.getResources().getString(title), null, null, context.getResources().getString(titleLeft), null, isLeft, isRight);

        } else {
            barData = new AppBarData(context.getResources().getString(title), null, null, context.getResources().getString(titleLeft), context.getResources().getString(titleRight), isLeft, isRight);
        }
    }

    public AppBarLock(Context context, int title, int imsLeft, int titleRight) {
        this.context = context;
        barData = new AppBarData(context.getResources().getString(title), context.getResources().getDrawable(imsLeft), null, null, context.getResources().getString(titleRight), true, true);
        leftType = BACK;
    }

    public AppBarLock(Context context, String title, int imsLeft, int titleRight) {
        this.context = context;
        barData = new AppBarData(title, context.getResources().getDrawable(imsLeft), context.getResources().getDrawable(titleRight), null, null, true, true);
        leftType = BACK;
    }

    public AppBarLock setRight(int type) {
        this.rightType = type;
        return this;
    }

    public AppBarLock setLeft(int type) {
        this.leftType = type;
        return this;
    }

    public AppBarLock setMsg_num(int msg_num) {
        this.msg_num = msg_num;
        return this;
    }

    public AppBarLock setNumber(String number) {
        this.number = number;
        return this;
    }

    public void leftClick() {
        switch (leftType) {
            case BACK:
                back();
                break;
            case BIND:
                //首页消息
                if(MyApplication.getUser().user_id==null) {
                    ShowRemindDialog.getInstance().gotoLogin(context);
                }else {
                    goMessage();
                }
                break;
            case COLLECT:
                //我的收藏
                if(MyApplication.getUser().user_id==null) {
                    ShowRemindDialog.getInstance().gotoLogin(context);
                }else {
                    goCollect();
                }
                break;
        }
    }


    public void rightClick() {
        switch (rightType) {
            case BACK:
                break;
            case YAOQING:
                //我的邀请
                if(MyApplication.getUser().user_id==null) {
                    ShowRemindDialog.getInstance().gotoLogin(context);
                }else {
                    goInvite();
                }
                break;
            case RESULT:
                //我的发布
                if(MyApplication.getUser().user_id==null) {
                    ShowRemindDialog.getInstance().gotoLogin(context);
                }else {
                    goRelease();
                }
                break;
            case BIND:
                //人员管理消息
                if(MyApplication.getUser().user_id==null) {
                    ShowRemindDialog.getInstance().gotoLogin(context);
                }else {
                    goMessage();
                }
                break;
            case KUCUNLIUSHUI:
                context.startActivity(new Intent(context, SotckLiushuiActivity.class));
                break;
        }
    }
    private void back() {
        ((BaseActivity) context).finish();
    }
    //消息列表
    private void goMessage(){
        if(MyApplication.getUser().user_id==null) {
            ShowRemindDialog.getInstance().gotoLogin(context);
        }else {
            Intent intent=new Intent((BaseActivity) context,MyMessageActivity.class);
            ((BaseActivity) context).startActivity(intent);
        }
    }
    //我的邀请
    private void goInvite(){
        if(MyApplication.getUser().user_id==null) {
            ShowRemindDialog.getInstance().gotoLogin(context);
        }else {
            Intent intent=new Intent((BaseActivity) context,InviteActivity.class);
            ((BaseActivity) context).startActivity(intent);
        }
    }
    //我的收藏
    private void goCollect(){
        if(MyApplication.getUser().user_id==null) {
            ShowRemindDialog.getInstance().gotoLogin(context);
        }else {
            Intent intent=new Intent((BaseActivity) context,MyCollectActivity.class);
            ((BaseActivity) context).startActivity(intent);
        }
    }
    //我的发布
    private void goRelease(){
        if(MyApplication.getUser().user_id==null) {
            ShowRemindDialog.getInstance().gotoLogin(context);
        }else {
            Intent intent=new Intent((BaseActivity) context,ReleaseActivity.class);
            ((BaseActivity) context).startActivity(intent);
        }
    }
    public class AppBarData extends BaseObservable {
        public AppBarData(String title, Drawable imsLeft, Drawable imsRight, String titleLeft, String titleRight, boolean isLeft, boolean isRight) {
            this.title = title;
            this.imsLeft = imsLeft;
            this.imsRight = imsRight;
            this.isLeft = isLeft;
            this.isRight = isRight;
            this.titleLeft = titleLeft;
            this.titleRight = titleRight;
        }

        public String title;
        public String titleLeft;
        public String titleRight;
        public Drawable imsLeft;
        public Drawable imsRight;
        public boolean isLeft;
        public boolean isRight;
    }

}
