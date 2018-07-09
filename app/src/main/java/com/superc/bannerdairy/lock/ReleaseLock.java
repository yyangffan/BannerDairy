package com.superc.bannerdairy.lock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;

import com.google.gson.Gson;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityReleaseBinding;
import com.superc.bannerdairy.ui.mine.ReleaseActivity;
import com.superc.bannerdairy.widget.FileUtil;
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
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Amorr on 2017/11/20.
 * 我的发布界面
 */

public class ReleaseLock extends BaseLock<ActivityReleaseBinding> {
    //选择的本地图片地址
    private static final int LIMIT_PIC_NUM = 8;
    private static final int HTTP_REQUEST = 0;
    private static final int REQUEST_WHAT_DETILES = 2;
    private static final int REQUESTCODE_CUTTIN = 2;    // 图片裁切标记
    public ArrayList<String> imgPaths;

    private int picPosition;
    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private static final String IMAGE_FILE_NAME = "IdleImage.jpg";// 头像文件名称
    private Drawable drawable;
    private String urlpath,image;            // 图片本地路径
    private int imagePosition=1;
    public ReleaseLock(Context context, ActivityReleaseBinding binding) {
        super(context, binding);
    }

    public ReleaseLock(Context context, ActivityReleaseBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock=new AppBarLock(mContext, R.string.my_release);
        appBarLock.barData.isLeft=true;
        appBarLock.barData.titleLeft="返回";
        appBarLock.barData.imsLeft=mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);
        imgPaths=new ArrayList();
    }
    //添加图片
    public void add(){
        selectorImage();

    }
    //发布
    public void send(){
        if(MyApplication.getUser()!=null){
            if(mBinding.tvPersonalData.getText().toString()!=null) {
                httpsend(imgPaths, mBinding.tvPersonalData.getText().toString());
            }else {
                showToast("请输入内容");

            }
        }else {
            showToast("请登录");
        }
    }
    //发布接口
    private void httpsend(List imageList, String content) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();

        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.RELEASE, RequestMethod.POST);
        Gson gson = new Gson();
        request.add("article_image", imageList);
        request.add("article_content", content);

        request.add("creator", MyApplication.getUser().user_id);
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {


            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    String resultCode = null;
                    Boolean success = false;
                    try {
                        JSONObject jsonObject = response.get();
//                        resultCode = jsonObject.getString("code");
                        success = jsonObject.getBoolean("success");

//                        if (resultCode.equals("200")) {
                        if (success) {
                            showToast("发布成功！");
                            ((BaseActivity)mContext).finish();

                        } else if (!success) {
                            showToast(jsonObject.getString("msg"));

                        } else {
                            showToast("网络异常");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                showToast("网络异常");

            }

            @Override
            public void onFinish(int what) {
//                hideLoading();

            }
        });
    }


    /**
     * 相册选择
     *
     * @param
     */
    private void selectorImage() {
        int count = 0;
        if (imgPaths.size() == 0) {
            count = LIMIT_PIC_NUM;
        } else {
            for (int i = 0; i < imgPaths.size(); i++) {

                if (imgPaths.get(i).equals("")) {
                    count = LIMIT_PIC_NUM - imgPaths.size() + 1;
                    break;
                }
                if (i == imgPaths.size()) {
                    count = LIMIT_PIC_NUM - imgPaths.size();
                }
            }
        }

//        Intent intent = new Intent(IdleGoodsReleaseActivity.this, ImageSelectorActivity.class);
//        intent.putExtra(ImageSelectorActivity.EXTRA_MAX_SELECT_NUM, count);
//        intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_MODE, ImageSelectorActivity.MODE_MULTIPLE);
//        intent.putExtra(ImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
//        intent.putExtra(ImageSelectorActivity.EXTRA_ENABLE_PREVIEW, true);
//        intent.putExtra(ImageSelectorActivity.EXTRA_ENABLE_CROP, false);
//        startActivityForResult(intent, Constants.Codes.ALBUM);
//
        //从新写上传图片
        //判断是否开户相册权限
        if (PackageManager.PERMISSION_GRANTED ==   ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA)) {
        }else{
            //提示用户开户权限
            ActivityCompat.requestPermissions((ReleaseActivity)mContext,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        menuWindow = new SelectPicPopupWindow(mContext, itemsOnClick);
        menuWindow.showAtLocation(mBinding.layout,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    ((BaseActivity)mContext).startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    ((BaseActivity)mContext).startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };


}
