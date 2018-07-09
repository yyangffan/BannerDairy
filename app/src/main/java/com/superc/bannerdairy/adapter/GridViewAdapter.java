package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.photoview.PhotoView;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.ui.home.picture_show.MyViewPagDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/12/16.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<Map<String, Object>> mMapList;
    private Context mContext;
    private LayoutInflater mInflater;

    public GridViewAdapter(Context context, List<Map<String, Object>> mapList) {
        mMapList = mapList;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mMapList == null ? 0 : mMapList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMapList == null ? null : i;
    }

    @Override
    public long getItemId(int i) {
        return mMapList == null ? 0 : i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if (view == null) {
            vh = new ViewHolder();
            view = mInflater.inflate(R.layout.item_grid, viewGroup, false);
            vh.mImageView = view.findViewById(R.id.grid_image);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.zhanweib)
                .error(R.drawable.zhanweib)
                .centerInside()
                .priority(Priority.HIGH);
        Glide.with(mContext).load(mMapList.get(i).get("image")).apply(options).into(vh.mImageView);
        /*点击图片进行图片的放大显示(所有的该条目下的图片都在里面)*/
        vh.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*第一种轮播(两种都可以只不过是用户体验问题)*/
//                MyWindowDialog myWindowDialog=new MyWindowDialog(mContext,mMapList,i);
//                myWindowDialog.show();
                /*第二种轮播(两种都可以只不过是用户体验问题)*/
                List<View> mlist = new ArrayList<>();
                List<String> mdizhi=new ArrayList<>();
                for (Map<String, Object> map : mMapList) {
                    PhotoView photoView = new PhotoView(mContext);
                    Glide.with(mContext).load(map.get("image")).into(photoView);
                    mlist.add(photoView);
                    mdizhi.add((String) map.get("image"));
                }
                MyViewPagDialog myViewPagDialog = new MyViewPagDialog(mContext, mlist,mdizhi, i);
                myViewPagDialog.show();


            }
        });

        return view;
    }


    class ViewHolder {
        ImageView mImageView;
    }

}
