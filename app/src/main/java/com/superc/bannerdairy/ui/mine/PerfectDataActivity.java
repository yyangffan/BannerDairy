package com.superc.bannerdairy.ui.mine;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityPerfectDataBinding;
import com.superc.bannerdairy.lock.PerfectDataLock;
import com.superc.bannerdairy.model.User;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.bannerdairy.widget.FileUtil;
import com.yanzhenjie.nohttp.Binary;
import com.yanzhenjie.nohttp.FileBinary;
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

import static com.superc.bannerdairy.lock.PerfectDataLock.IMAGE_FILE_NAME;

/**
 * 资料修改界面
 */

public class PerfectDataActivity extends BaseActivity {

    private ActivityPerfectDataBinding mBinding;
    private PerfectDataLock mLock;
    private Drawable drawable;
    private String headPath = "";
    private String headPathOld = "";
    private String posId = "";
    List<Binary> fileList = new ArrayList<>();
    private Uri mUritempFile;
    private List<LocalMedia> selectList = new ArrayList<>();

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_perfect_data);
        mLock = new PerfectDataLock(this, mBinding);
        mBinding.setPerfectDataLock(mLock);
        headPathOld= (String) MyApplication.getUser().getUser_ico();
        mBinding.tvModifyHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSelect();
            }
        });
    }

    public void toSelect() {
        PictureSelector.create(PerfectDataActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(true)// 是否裁剪
                .compress(false)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                .circleDimmedLayer(true)// 是否圆形裁剪
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .openClickSound(false)// 是否开启点击声音
//                .selectionMedia(selectList)// 是否传入已选图片
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                .rotateEnabled(true) // 裁剪是否可旋转图片
                .scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PerfectDataLock.REQUESTCODE_PICK:// 直接从相册获取
                    try {
                        startPhotoZoom(data.getData());
                        //  ImageUtils.setCircleDefImage(mHeaderTV,data.getData().getPath());

                    } catch (NullPointerException e) {
                        e.printStackTrace();// 用户点击取消操作
                    }
                    break;
                case PerfectDataLock.REQUESTCODE_TAKE:// 调用相机拍照
                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(temp));
                    //  ImageUtils.setCircleDefImage(mHeaderTV,Uri.fromFile(temp).getPath());

                    break;
                case PerfectDataLock.REQUESTCODE_CUTTING:// 取得裁剪后的图片
                    if (data != null) {
                        setPicToView(data);
                    }

                    break;
//
                case PerfectDataLock.ALBUM:
                    break;
                case PerfectDataLock.REQUESTCODE_POS:
                    if (data != null) {
                        posId = data.getStringExtra("posid");
                    }
                    break;
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getCutPath());
                    }
                    headPath=selectList.get(0).getCutPath();
                    Glide.with(this).load(headPath).into(mBinding.imageView7);
                    break;
            }

        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            drawable = new BitmapDrawable(null, photo);
            headPath = FileUtil.saveFile(PerfectDataActivity.this, System.currentTimeMillis() + "idlehead.jpg", photo);
            Glide.with(this).load(headPath).into(mBinding.imageView7);
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
//        intent.putExtra("return-data", true);
        //uritempFile为Uri类变量，实例化uritempFile，转化为uri方式解决问题
        mUritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        //   ImageUtils.setCircleDefImage(mHeaderTV,uri.getPath());
        startActivityForResult(intent, PerfectDataLock.REQUESTCODE_CUTTIN);
    }

    //修改个人资料
    public void fix(View v) {
        if (headPath.equals("")) {
            if(headPathOld.equals("")){
                showToast("请选择头像");
                return;
            }
        }
        if (mBinding.edtName.getText().toString() == null || mBinding.edtName.getText().toString().equals("")) {
            showToast("请填写姓名");
            return;
        }
        if (mBinding.edtIdNum.getText().toString() == null || mBinding.edtIdNum.getText().toString().equals("")) {
            showToast("请填写身份证号");
            return;
        }
        if (mBinding.edtPhoneNum.getText().toString() == null || mBinding.edtPhoneNum.getText().toString().equals("")) {
            showToast("请填写电话");
            return;
        }
        httpFixUser();
    }

    //修改个人资料
    private void httpFixUser() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.USERINFO, RequestMethod.POST);
        //收货地址id
//        request.add("user_address_id", posId);
        //头像
        if(headPath.equals("")){
            request.add("user_ico", headPathOld);
        }else {
            fileList.add(new FileBinary(new File(headPath)));
            request.add("user_ico", fileList);
        }
        //姓名
        request.add("username", mBinding.edtName.getText().toString());
        //身份证号
        request.add("identity_number", mBinding.edtIdNum.getText().toString());
        request.add("user_id", MyApplication.getUser().user_id);

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
                            User user = MyApplication.getUser();
                            user.setUsername(mBinding.edtName.getText().toString());
                            user.setIdentity_number(mBinding.edtIdNum.getText().toString());
                            user.setUser_ico(jsonObject.getJSONObject("data").getString("user_ico"));
                            MyApplication.setUser(user);
                            if(PerfectDataActivity.this!=null) {
                                ShowRemindDialog.getInstance().showDialog(PerfectDataActivity.this, R.drawable.chenggong1, "修改成功", false, new OnSomeThingClickListener() {
                                    @Override
                                    public void OnDoSomeThingClickListener(int view_id) {
                                        PerfectDataActivity.this.finish();
                                    }
                                }).setCanleGV(false);
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

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
