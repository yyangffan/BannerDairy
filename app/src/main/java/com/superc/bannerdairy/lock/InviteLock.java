package com.superc.bannerdairy.lock;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.InviteLvAdapter;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityInviteBinding;
import com.superc.bannerdairy.model.InviteItem;
import com.superc.bannerdairy.service.PopManager;
import com.uuzuche.lib_zxing.activity.CodeUtils;
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
 * Created by Amorr on 2017/11/18.
 */

public class InviteLock extends BaseLock<ActivityInviteBinding> {

    private String mUrl = "";//链接地址
    private Bitmap mImage;
    private InviteItem mInviteItem;
    private List<InviteItem.DataBean> mDataBeen;
    private InviteLvAdapter mInviteLvAdapter;
    private int one_heigth = 0;

    public InviteLock(Context context, ActivityInviteBinding binding) {
        super(context, binding);
    }

    public InviteLock(Context context, ActivityInviteBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.my_invite);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.head)
                .error(R.drawable.head)
                .priority(Priority.HIGH);
        if (MyApplication.getUser().getUser_ico() != null) {
//            Glide.with(mContext).load(ConnectUrl.REQUESTURL + MyApplication.getUser().getUser_ico()).placeholder(R.drawable.head).into(mBinding.inviteImgv);
            Glide.with(mContext)
                    .load(ConnectUrl.REQUESTURL + MyApplication.getUser().getUser_ico())
                    .apply(options)
                    .into(mBinding.inviteImgv);
        }
        if (MyApplication.getUser().getUsername() != null) {
            mBinding.inviteName.setText(MyApplication.getUser().getUsername());
        } else {
            mBinding.inviteName.setText("");
        }
        if(MyApplication.getUser().getMobile()!=null){
            mBinding.invitePhone.setText("("+MyApplication.getUser().getMobile()+")");
        }else {
            mBinding.invitePhone.setText("");
        }

        mDataBeen = new ArrayList<>();
        mInviteLvAdapter = new InviteLvAdapter(mContext, mDataBeen);
        mBinding.inviteLv.setAdapter(mInviteLvAdapter);
        httpInvite();
        initListener();

    }

    public void initListener() {
        mBinding.inviteLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startFenxiang(mDataBeen.get(i).getInvitationUrl());
            }
        });

    }

    /*这个方法主要就是用来生成一下不同链接的地址*/
    public void startFenxiang(String url) {
        if (url != null && !url.equals("")) {/*生成分享图片时候的图片*/
            mImage = CodeUtils.createImage("http://" + url, 400, 400, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logoo));
            saveBitmap();
        }
        InviteZhuan(url);
    }

    /*点击弹出转发弹窗*/
    public void InviteZhuan(final String url) {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_invite, null);
        ImageView imgv_cancle = v.findViewById(R.id.dialog_invite_cancle);
        LinearLayout ll_lianjie = v.findViewById(R.id.dialog_invite_lianjie);
        LinearLayout ll_erweima = v.findViewById(R.id.dialog_invite_erweima);
        dialog.setView(v);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        imgv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ll_lianjie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PopManager(mContext).showShare(url, false);
                dialog.dismiss();
            }
        });
        ll_erweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImage != null) {/*二维码图片不为空*/
                    new PopManager(mContext).showShare(null, true);
                } else {
                    Toast.makeText(mContext, "二维码生成错误", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }

    /*邀请代理*/
    private void httpInvite() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.INVITEAGENT, RequestMethod.GET);
        final Gson gson = new Gson();
        if (MyApplication.getUser().getUser_id() != null) {
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
                        mInviteItem = gson.fromJson(response.get().toString(), InviteItem.class);
                        success = mInviteItem.isSuccess();
                        show = mInviteItem.isShow();
                        if (success) {
                            mDataBeen.addAll(mInviteItem.getData());
                            mInviteLvAdapter.notifyDataSetChanged();
                            ViewGroup.LayoutParams llWaibu = mBinding.inviteLlWaibu.getLayoutParams();
                            llWaibu.width = ViewGroup.LayoutParams.MATCH_PARENT;
                            /*这里是动态来给后面的布局设置高度*/
                            llWaibu.height = mBinding.inviteLlNeibu.getHeight() + (mDataBeen.size() * mBinding.centerTv.getHeight());
                            mBinding.inviteLlWaibu.setLayoutParams(llWaibu);
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

    /**
     * 保存二维码图片
     */
    public void saveBitmap() {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "qizhi");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "erweima.png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            mImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
