package com.superc.bannerdairy.lock;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityPerfectDataBinding;
import com.superc.bannerdairy.model.User;
import com.superc.bannerdairy.ui.mine.BdZhActivity;
import com.superc.bannerdairy.ui.mine.PerfectDataActivity;
import com.superc.bannerdairy.ui.order.SelectAddressActivity;
import com.superc.bannerdairy.widget.SelectPicPopupWindow;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 创建日期：2017/11/9 on 15:03
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class PerfectDataLock extends BaseLock<ActivityPerfectDataBinding> {
    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
    public static final String IMAGE_FILE_NAME = "IdleImage.jpg";// 头像文件名称
    public static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    public static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    public static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    public static final int REQUESTCODE_CUTTIN = 2;    // 图片裁切标记
    public static final int REQUESTCODE_POS = 110;
    public static final int ALBUM = 0x0007; //相册
    private AlertDialog mAlertDialog;

    public PerfectDataLock(Context context, ActivityPerfectDataBinding binding) {
        super(context, binding);
    }

    public PerfectDataLock(Context context, ActivityPerfectDataBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock = new AppBarLock(mContext, R.string.my_info);
        appBarLock.barData.isLeft = true;
        appBarLock.barData.titleLeft = "返回";
        appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
        mBinding.edtPhoneNum.requestFocus();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.head)
                .error(R.drawable.head)
                .priority(Priority.HIGH);
        httpUser();
        //进入选择地址
        mBinding.edtReceivingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SelectAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isPerfect", "y");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        if (MyApplication.getUser().getUser_ico() != null) {
//            Glide.with(mContext).load(ConnectUrl.REQUESTURL + MyApplication.getUser().getUser_ico()).placeholder(R.drawable.head).into(mBinding.imageView7);
            Glide.with(mContext)
                    .load((ConnectUrl.REQUESTURL + MyApplication.getUser().getUser_ico()))
                    .apply(options)
                    .into(mBinding.imageView7);
        }
    }

    /*选择头像*/
    public void choseHead() {
        selectorImage();
    }

    /*跳转到绑定账号*/
    public void bdZhanghao() {
        startActivity(BdZhActivity.class);
    }

    private void selectorImage() {
        //从新写上传图片
        //判断是否开户相册权限
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA)) {
        } else {
            //提示用户开户权限
            ActivityCompat.requestPermissions((Activity) mContext,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
//        menuWindow = new SelectPicPopupWindow(mContext, itemsOnClick);
//        menuWindow.showAtLocation(mBinding.layout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog_pic, null);
        TextView take_bt = view.findViewById(R.id.takePhotoBtn);
        TextView pick_bt = view.findViewById(R.id.pickPhotoBtn);
        TextView cancle_bt = view.findViewById(R.id.cancelBtn);
        take_bt.setOnClickListener(itemsOnClick);
        pick_bt.setOnClickListener(itemsOnClick);
        cancle_bt.setOnClickListener(itemsOnClick);

        builder.setView(view);
        mAlertDialog = builder.create();
        Window window = mAlertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mAlertDialog.show();


    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            menuWindow.dismiss();
            mAlertDialog.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    ((PerfectDataActivity) mContext).startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    ((PerfectDataActivity) mContext).startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };


    //获取个人资料接口
    public void httpUser() {
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
                            User user = new Gson().fromJson(jsonObject.getJSONObject("data").toString(), User.class);
                            if (user.getUsername() != null) {
                                mBinding.edtName.setText(user.getUsername());
                            }
                            mBinding.edtIdNum.setText(user.identity_number);
                            mBinding.edtPhoneNum.setText(user.getMobile());
                            RequestOptions options = new RequestOptions().centerCrop().placeholder(R.drawable.head).error(R.drawable.head).priority(Priority.HIGH);
                            if(mContext!=null) {
                                Glide.with(mContext).load(ConnectUrl.REQUESTURL + user.getUser_ico()).apply(options).into(mBinding.imageView7);
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
