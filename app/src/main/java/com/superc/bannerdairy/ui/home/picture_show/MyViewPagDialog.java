package com.superc.bannerdairy.ui.home.picture_show;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.superc.bannerdairy.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by user on 2018/2/8.
 */

public class MyViewPagDialog extends Dialog {

    private View mView;
    private ViewPager mvp;
    private TextView mtv;
    private TextView mtv_save;
    private MvpAdapter mMvpAdapter;
    private List<View> mViewList;
    private int pos = 0;
    private Context mContext;
    private List<String> mDizhi;

    public MyViewPagDialog(@NonNull Context context, List<View> viewList, List<String> mdizhi, int position) {
        super(context);
        mContext = context;
        mViewList = viewList;
        mDizhi = mdizhi;
        this.pos = position;
        mMvpAdapter = new MvpAdapter(context, mViewList, mOnClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_viewpage, null);
        setContentView(mView);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x0000ffff));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mvp = mView.findViewById(R.id.dialog_vp);
        mtv = mView.findViewById(R.id.dialog_num);
        mtv_save = mView.findViewById(R.id.dialog_save);
        mvp.setAdapter(mMvpAdapter);
        mvp.setCurrentItem(pos);
        mtv.setText((pos + 1) + "/" + mViewList.size());
        initListener();

    }

    MvpAdapter.OnClickListener mOnClickListener = new MvpAdapter.OnClickListener() {
        @Override
        public void OnClickListener() {
            MyViewPagDialog.this.dismiss();
        }
    };

    public void initListener() {
        mvp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mtv.setText((position + 1) + "/" + mViewList.size());
                pos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mtv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "图片已保存,稍后请到相册查看", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bp = returnBitMap(mDizhi.get(pos));
                        saveImageToPhotos(mContext, bp);
                    }
                }).start();
            }
        });
    }

    /**
     * 保存图片到本地
     */
    private void saveImageToPhotos(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "qizhi");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            Log.e("保存图片","图片保存失败");
//            return;
//        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        Log.i("保存图片","图片保存成功");

    }

    /**
     * 将URL转化成bitmap形式
     * @param url
     * @return bitmap type
     */
    public final static Bitmap returnBitMap(String url) {
        URL myFileUrl;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }



}
