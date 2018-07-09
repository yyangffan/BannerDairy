package com.superc.bannerdairy.ui.home.picture_show;

import android.content.Context;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.photoview.PhotoView;
import com.luck.picture.lib.photoview.PhotoViewAttacher;


/**
 * Created by Administrator on 2016/11/14.
 */
public class MyImageHolderView implements Holder {
    PhotoView mPhotoView;
    PhotoViewAttacher mAttacher;

    @Override
    public View createView(Context context) {
        mPhotoView=new PhotoView(context);
        return mPhotoView;
    }

    @Override
    public void UpdateUI(Context context, int position, Object data) {
//        RequestOptions options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.drawable.head)
//                .error(R.drawable.xiangqingtu)
//                .priority(Priority.HIGH);
        mAttacher = new PhotoViewAttacher(mPhotoView);
        Glide.with(context).load(data).into(mPhotoView);
        mAttacher.update();
    }
}
