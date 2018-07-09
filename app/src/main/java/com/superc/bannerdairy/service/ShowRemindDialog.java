package com.superc.bannerdairy.service;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.ui.login.LoginActivity;

/**
 * 用来进行提示窗展示的dialog
 */

public class ShowRemindDialog {

    private TextView mTv_cancle;
    private TextView mTv_sure;
    private OnCancleClickListener mOnCancleClickListener;
    private OnSomeThingClickListener mOnSomeThingClickListener;

    private ShowRemindDialog() {

    }

    public static ShowRemindDialog getInstance() {
        return SingetonHolder.instance;
    }

    private static class SingetonHolder {
        private static ShowRemindDialog instance = new ShowRemindDialog();
    }

    public void setOnCancleClickListener(OnCancleClickListener onCancleClickListener) {
        mOnCancleClickListener = onCancleClickListener;
    }

    public void setOnSomeThingClickListener(OnSomeThingClickListener onSomeThingClickListener) {
        mOnSomeThingClickListener = onSomeThingClickListener;
    }

    /**
     *
     * @param context
     * @param drawableId                    提示图片id
     * @param content                       提示内容
     * @param show                          是否显示最上面的提示两个字
     * @param onSomeThingClickListener      点击确定和取消时候的操作
     * @return
     */
    public ShowRemindDialog showDialog(Context context, int drawableId, String content, boolean show, final OnSomeThingClickListener onSomeThingClickListener) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        View dialogV = LayoutInflater.from(context).inflate(R.layout.dialog_collect, null);
        mTv_cancle = dialogV.findViewById(R.id.dialog_tv_cancle);
        mTv_sure = dialogV.findViewById(R.id.dialog_tv_sure);
        TextView tv_content = dialogV.findViewById(R.id.dialog_content);
        TextView tv_title = dialogV.findViewById(R.id.tv_title);
        ImageView imgv = dialogV.findViewById(R.id.dialog_imgv);
        if (content == null || content.equals("")) {
            tv_content.setVisibility(View.GONE);
        } else {
            tv_content.setText(content);
        }
        if (drawableId == 0) {
            imgv.setVisibility(View.GONE);
        } else {
            imgv.setImageResource(drawableId);
        }
        if (show) {
            tv_title.setVisibility(View.VISIBLE);
        } else {
            tv_title.setVisibility(View.GONE);
        }
        dialog.setView(dialogV);
        if(context!=null) {
            dialog.show();
        }
        mTv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnCancleClickListener != null) {
                    mOnCancleClickListener.OnCancleClickListener();
                }
                dialog.dismiss();
            }
        });
        mTv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onSomeThingClickListener != null) {
                    onSomeThingClickListener.OnDoSomeThingClickListener(R.id.dialog_tv_sure);
                }
                if (mOnSomeThingClickListener != null) {
                    mOnSomeThingClickListener.OnDoSomeThingClickListener(R.id.dialog_tv_sure);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    /**
     *
     * @param sure       确定按钮文字的修改
     * @param cancle    取消按钮文字的修改
     */
    public ShowRemindDialog setBtNote(String sure,String cancle) {
        mTv_sure.setText(sure);
        mTv_cancle.setText(cancle);
        return this;
    }

    /**
     * 去登录的简便弹出框
     * @param con 所需Context
     */
    public void gotoLogin(final Context con) {
        ShowRemindDialog.getInstance().showDialog(con, 0, "还未登录请登录", true, new OnSomeThingClickListener() {
            @Override
            public void OnDoSomeThingClickListener(int view_id) {
                Intent intent = new Intent(con, LoginActivity.class);
                con.startActivity(intent);
            }
        });
    }

    /**
     * 是否显示取消按钮
     * @param isShow false-隐藏 true-显示
     */
    public void setCanleGV(boolean isShow) {
        if (isShow) {
            mTv_cancle.setVisibility(View.VISIBLE);
        } else {
            mTv_cancle.setVisibility(View.GONE);
        }
    }

    /**
     * 是否显示取消按钮，且可以设置确定按钮的文字
     * @param isShow    false-隐藏 true-显示
     * @param content   确定按钮的文字显示
     */
    public void setCanleGV(boolean isShow, String content) {
        if (isShow) {
            mTv_cancle.setVisibility(View.VISIBLE);
        } else {
            mTv_cancle.setVisibility(View.GONE);
        }
        mTv_sure.setText(content);
    }

    /**
     * 点击取消按钮时的监听回调
     */
    public interface OnCancleClickListener {
        void OnCancleClickListener();
    }

}
