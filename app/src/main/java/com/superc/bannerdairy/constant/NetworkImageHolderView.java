package com.superc.bannerdairy.constant;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.superc.bannerdairy.R;


/**
 * Created by Administrator on 2016/11/14.
 */
public class NetworkImageHolderView implements Holder {
    private ImageView mImageView;


    @Override
    public View createView(Context context) {
        mImageView = new ImageView(context);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return mImageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Object data) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.zhanweib)
                .error(R.drawable.zhanweib)
                .priority(Priority.HIGH);
        Glide.with(context).load(data).apply(options).into(mImageView);
    }
}
