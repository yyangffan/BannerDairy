package com.superc.bannerdairy.ui.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.adapter.ReleaseSelectPicAdapter;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ActivityReleaseBinding;
import com.superc.bannerdairy.lock.ReleaseLock;
import com.superc.bannerdairy.ui.mine.picture_select.FullyGridLayoutManager;
import com.superc.bannerdairy.ui.mine.picture_select.GridImageAdapter;
import com.superc.bannerdairy.widget.FileUtil;
import com.superc.bannerdairy.widget.SelectPicPopupWindow;
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

import static cn.jpush.android.api.JPushInterface.a.i;

// 我的发布页面
public class ReleaseActivity extends BaseActivity {
    private ActivityReleaseBinding mBinding;
    private ReleaseLock mLock;
    public static final int ALBUM = 0x0007; //相册
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
    private String urlpath, image;            // 图片本地路径
    private int imagePosition = 1;
    ReleaseSelectPicAdapter mAdapter;

    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private int maxSelectNum = 9;
    private boolean mIsPic=true;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_release);
        mLock = new ReleaseLock(this, mBinding);
        mBinding.setReleaseLock(mLock);
        imgPaths = new ArrayList<>();
        mAdapter = new ReleaseSelectPicAdapter(this, R.layout.item_photo, imgPaths);
        mBinding.gridview.setAdapter(mAdapter);
        //添加相册
        mBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectorImage();

            }
        });
        mBinding.releaseTvStone.requestFocus();
        initListener();
        initRecy();
    }

    /*如下添加*/
    public void initRecy() {
        //发布
        mBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.getUser() != null) {
                    if (mBinding.tvPersonalData.getText().toString() != null) {
                        if (selectList != null) {
                            if (selectList.size() != 0) {
                                for (LocalMedia media : selectList) {
                                    imgPaths.add(media.getPath());
                                }
                                httpsend(mBinding.tvPersonalData.getText().toString());
                            } else {
                                showToast("请选择图片");
                            }
                        } else {
                            showToast("请选择图片");
                        }
                    } else {
                        showToast("请输入内容");

                    }
                } else {
                    showToast("请登录");
                }
            }
        });
        FullyGridLayoutManager manager = new FullyGridLayoutManager(ReleaseActivity.this, 4, GridLayoutManager.VERTICAL, false);
        mBinding.recycler.setLayoutManager(manager);
        adapter = new GridImageAdapter(ReleaseActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        mBinding.recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(ReleaseActivity.this).externalPicturePreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(ReleaseActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(ReleaseActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseActivity.this);
            View v = LayoutInflater.from(ReleaseActivity.this).inflate(R.layout.dialog_item_picture, null);
            TextView mtv_pic = v.findViewById(R.id.dialog_pictrue);
            TextView mtv_vid = v.findViewById(R.id.dialog_video);
            builder.setView(v);
            final AlertDialog alertDialog = builder.create();
            if(selectList.size()==0) {
                alertDialog.show();
            }else {
                toSelect();
            }
            mtv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIsPic = true;
                    toSelect();
                    alertDialog.dismiss();
                }
            });
            mtv_vid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mIsPic = false;
                    toSelect();
                    alertDialog.dismiss();
                }
            });

        }

    };

    public void toSelect() {
        adapter.setSelectMax(mIsPic?maxSelectNum:1);
        PictureSelector.create(ReleaseActivity.this)
                .openGallery(mIsPic ? PictureMimeType.ofImage() : PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(mIsPic?maxSelectNum:1)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(mIsPic ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                .enableCrop(false)// 是否裁剪
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
                .selectionMedia(selectList)// 是否传入已选图片
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


/*如上添加*/

    private void initListener() {
        mBinding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                if (imgPaths.get(position).equals("")) {
                    // 调用 相册 拍照
                    picPosition = position;
                    selectorImage();

                } else if (!imgPaths.get(position).equals("")) {
                    // 放大图

//                    ArrayList<String> picString = new ArrayList<String>();
//                    for (int i = 0; i < imgPaths.size(); i++) {
//                        if (!imgPaths.get(i).equals("")) {
//                            picString.add(imgPaths.get(i));
//                        }
//                    }
//                    //开始预览
//                    Intent intent = new Intent(this, MultiPhotoViewActivity.class);
//                    intent.putExtra(Constants.Keys.IMAGE_ITEMS, picString);//传递所有数据
//                    intent.putExtra("position", position);//确定当前是哪张图片
//                    startActivity(intent);
                }

            }

        });
        mAdapter.setImagesDataChanged(new ReleaseSelectPicAdapter.ImagesDataChanged() {
            @Override
            public void imagesChanged() {
                initSelectPicView();
            }
        });
    }

    //发布接口
    private void httpsend(String content) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        View dialogV = LayoutInflater.from(this).inflate(R.layout.dialog_collect, null);
        TextView tv_cancle = dialogV.findViewById(R.id.dialog_tv_cancle);
        TextView tv_sure = dialogV.findViewById(R.id.dialog_tv_sure);
        TextView tv_content = dialogV.findViewById(R.id.dialog_content);
        ImageView imge = dialogV.findViewById(R.id.dialog_imgv);
        imge.setImageResource(R.drawable.chenggong1);
        tv_cancle.setVisibility(View.GONE);
        tv_content.setText("发布成功");
        dialog.setView(dialogV);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReleaseActivity.this.finish();
                dialog.dismiss();
            }
        });
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.RELEASE, RequestMethod.POST);
        Gson gson = new Gson();
//        List<Binary> fileList = new ArrayList<>();
        request.add("article_content", content);
        request.add("creator", MyApplication.getUser().user_id);
        for (int i = 0; i < imgPaths.size(); i++) {
            if (new File(imgPaths.get(i)).exists()) {
                Log.e("上传参数 article_image",imgPaths.get(i)+"\n");
                request.add("article_image"+(i+1), new FileBinary(new File(imgPaths.get(i))));
//                fileList.add(new FileBinary(new File(imgPaths.get(i))));
            }
        }
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
                showLoadPop();
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                hideLoadPop();
                if (what == 1) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            dialog.show();
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
                hideLoadPop();
                showToast("网络异常");
            }

            @Override
            public void onFinish(int what) {
                hideLoadPop();
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
        if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)) {
        } else {
            //提示用户开户权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        menuWindow = new SelectPicPopupWindow(this, itemsOnClick);
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
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUESTCODE_PICK:// 直接从相册获取
                    try {
                        startPhotoZoom(data.getData());
                        //  ImageUtils.setCircleDefImage(mHeaderTV,data.getData().getPath());
                    } catch (NullPointerException e) {
                        e.printStackTrace();// 用户点击取消操作
                    }
                    break;
                case REQUESTCODE_TAKE:// 调用相机拍照
                    File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(temp));
                    //  ImageUtils.setCircleDefImage(mHeaderTV,Uri.fromFile(temp).getPath());

                    break;
                case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                    if (data != null) {
                        setPicToView(data);
                    }

                    break;
//
                case ALBUM:
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
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
            urlpath = FileUtil.saveFile(ReleaseActivity.this, System.currentTimeMillis() + "idlehead.jpg", photo);


            ArrayList<String> mSelectPath = new ArrayList<String>();
            mSelectPath.add(urlpath);
            if (mSelectPath != null && mSelectPath.size() != 0) {
                if (imgPaths.size() != 0) {
                    if (imgPaths.get(imgPaths.size() - 1).equals(""))
                        imgPaths.remove(imgPaths.size() - 1);
                }
            }
            for (int i = 0; i < mSelectPath.size(); i++) {
//                    mList.set(picPosition + i, new Pic(mSelectPath.get(i)));
                imgPaths.add(mSelectPath.get(i));
            }
            if (imgPaths.size() == LIMIT_PIC_NUM) {

            } else if (picPosition < LIMIT_PIC_NUM) {
                imgPaths.add("");
            }
            initSelectPicView();
            imagePosition++;
            mAdapter.notifyDataSetChanged();

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
        intent.putExtra("return-data", true);
        //   ImageUtils.setCircleDefImage(mHeaderTV,uri.getPath());

        startActivityForResult(intent, REQUESTCODE_CUTTIN);
    }

    /**
     * 设置图片选择布局
     */
    private void initSelectPicView() {
        if (imgPaths.size() == 1 && imgPaths.get(0).equals("")) {
            mBinding.gridview.setVisibility(View.GONE);
            mBinding.add.setVisibility(View.VISIBLE);
        } else if (imgPaths.size() == 0) {
            mBinding.gridview.setVisibility(View.GONE);
            mBinding.add.setVisibility(View.VISIBLE);
        } else {
            // TODO: 2018/1/18 修改成不显示
//            mBinding.gridview.setVisibility(View.VISIBLE);
            mBinding.gridview.setVisibility(View.GONE);
            mBinding.add.setVisibility(View.GONE);
        }
    }

}
