package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.superc.bannerdairy.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * Created by JCY on 2017/7/14.
 * 说明：发布 选择按图片adpater
 */

public class ReleaseSelectPicAdapter extends CommonAdapter {
    List<String> mList;
    Context mContext;
    ImagesDataChanged mImagesDataChanged;

    public ReleaseSelectPicAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
        this.mList = datas;
        this.mContext = context;
    }


    @Override
    protected void convert(ViewHolder viewHolder, Object item, final int position) {
        ImageView picBtn = viewHolder.getView(R.id.item_photo_grid);
        ImageView deleteBtn = viewHolder.getView(R.id.delete_btn);
        //逻辑判断
        if (TextUtils.isEmpty(mList.get(position)) || mList.get(position).equals("")) {
            Glide.with(mContext).load(R.drawable.icon_addpic_unfocuse).into(picBtn);
            deleteBtn.setVisibility(View.GONE);
        } else {
            Glide.with(mContext).load(mList.get(position)).into(picBtn);
            deleteBtn.setVisibility(View.VISIBLE);
        }
        viewHolder.setOnClickListener(R.id.delete_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.size() == 8 && !mList.get(mList.size() - 1).equals("")) {
                    mList.remove(position);
                    mList.add("");
                } else {
                    mList.remove(position);
                }
                mImagesDataChanged.imagesChanged();
                notifyDataSetChanged();

            }
        });

    }

    public void setImagesDataChanged(ImagesDataChanged imagesDataChanged) {
        mImagesDataChanged = imagesDataChanged;
    }

    public interface ImagesDataChanged {
        void imagesChanged();
    }
}
