package com.superc.bannerdairy.adapter;

import android.content.ClipboardManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.tools.ScreenUtils;
import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemFinshionBinding;
import com.superc.bannerdairy.model.FinshItem;
import com.superc.bannerdairy.ui.home.picture_show.VideoPlayActivity;
import com.superc.bannerdairy.ui.mine.MyCollectActivity;
import com.superc.cframework.base.ui.CBaseViewHolder;
import com.superc.cframework.utils.ToastUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Amorr on 2017/11/22.
 * 传播专区文章列表
 */

public class FashionAdapter extends RecyclerView.Adapter {
    protected List<FinshItem> mDataList;// 数据列表
    protected Context mContext; // 上下文
    private int layoutId; // 单布局
    private int variableId; // DataBinding的BR
    private OnItemClickListener mListener; // ItemClick监听
    private SimpleAdapter sim_adapter;
    private GridViewAdapter grid_adapter;
    boolean iss = true;
    private int mAttr = 0;
    private ItemFinshionBinding mItemBinding;
    private OnRefreshMsg mOnRefreshMsg;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            Bitmap mBitmap = data.getParcelable("bitmap");
            if (mBitmap != null) {
                mItemBinding.finshionImage.setImageBitmap(mBitmap);
            }
        }
    };

    /**
     * 构造函数
     *
     * @param context    上下文
     * @param dataList   数据列表
     * @param layoutId   单布局
     * @param variableId DataBinding的BR
     */
    public FashionAdapter(Context context, List<FinshItem> dataList, int layoutId, int variableId) {
        this.mContext = context;
        this.mDataList = dataList;
        this.layoutId = layoutId;
        this.variableId = variableId;
    }

    public void setOnRefreshMsg(OnRefreshMsg onRefreshMsg) {
        mOnRefreshMsg = onRefreshMsg;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, parent, false);
        return new CBaseViewHolder(mContext, mBinding.getRoot(), mBinding);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        mItemBinding = DataBindingUtil.findBinding(holder.itemView);
        mItemBinding.setVariable(variableId, mDataList.get(position));
        if (mContext instanceof MyCollectActivity) {/*如果是我的收藏跳转过来的*/
            mItemBinding.llFinshion.setVisibility(View.GONE);
            mItemBinding.tvCancleCol.setVisibility(View.VISIBLE);
            mItemBinding.imgvDelete.setVisibility(View.VISIBLE);
        } else {
            mItemBinding.llFinshion.setVisibility(View.VISIBLE);
            mItemBinding.tvCancleCol.setVisibility(View.GONE);
            mItemBinding.imgvDelete.setVisibility(View.GONE);
        }
        //判断是否点赞
        mItemBinding.itemFashionZan.setText("点赞(" + mDataList.get(position).getDianzan() + ")");
        if (mDataList.get(position).getArticle_comment_type() != null) {
            if (mDataList.get(position).getArticle_comment_type().equals("1")) {
                //已经点赞了
                mItemBinding.itemFashionZan.setTextColor(Color.RED);
                mItemBinding.imZan.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zan2));
            } else {
                //还没点赞
                mItemBinding.itemFashionZan.setTextColor(Color.BLACK);
                mItemBinding.imZan.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zan));

            }
        }
        //判断是否收藏
        mItemBinding.itemFashionCollect.setText("收藏(" + mDataList.get(position).getShoucang() + ")");
        if (mDataList.get(position).getCollect() != null) {
            if (mDataList.get(position).getCollect().equals("0")) {
                //已经收藏了
                mItemBinding.itemFashionCollect.setTextColor(Color.RED);
                mItemBinding.imCollect.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shoucang));

            } else {
                //还没收藏
                mItemBinding.itemFashionCollect.setTextColor(Color.BLACK);
                mItemBinding.imCollect.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shoucang2));
            }
        } else {
            mItemBinding.itemFashionCollect.setTextColor(Color.BLACK);
            mItemBinding.imCollect.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shoucang2));
        }
        //GridView
        List<FinshItem.ArticalImage> article_image = mDataList.get(position).getArticle_image();
        if (article_image != null) {
            List<Map<String, Object>> data_list = new ArrayList();
            for (int i = 0; i < article_image.size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                mAttr = article_image.get(i).getAttr();
                if (mAttr == 0) {//0 为图片  1为视频
                    mItemBinding.grid.setVisibility(View.VISIBLE);
                    mItemBinding.finshionImage.setVisibility(View.GONE);
                    map.put("attr", "0");
                    map.put("image", ConnectUrl.REQUESTURL + article_image.get(i).getArticleImage());
                    data_list.add(map);
                } else if (mAttr == 1) {
                    mItemBinding.grid.setVisibility(View.GONE);
                    mItemBinding.finshionImage.setVisibility(View.VISIBLE);
                    final String articleImage = article_image.get(i).getArticleImage();
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.shipin)
                            .error(R.drawable.shipin)
                            .priority(Priority.HIGH);
                    Glide.with(mContext).load(ConnectUrl.REQUESTURL + articleImage).apply(options).into(mItemBinding.finshionImage);
                }
            }
            if (mAttr == 0) {
                grid_adapter = new GridViewAdapter(mContext, data_list);
                mItemBinding.grid.setAdapter(grid_adapter);
            }

        }

        mItemBinding.finshionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*第一种播放视频(都可用只是体验问题)*/
//                PictureSelector.create((Activity) mContext).externalPictureVideo(ConnectUrl.REQUESTURL+mDataList.get(position).getArticle_image().get(0).getArticleImage());
                /*第二种播放视频(都可用只是体验问题)*/
                mContext.startActivity(new VideoPlayActivity().callIntent(mContext, mDataList.get(position).getArticle_image().get(0).getArticleImage()));
                /*第三种播放视频(都可用只是体验问题)*/
//                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_video, null);
//                JZVideoPlayerStandard video = v.findViewById(R.id.videoplayer);
//                Glide.with(mContext).load(ConnectUrl.REQUESTURL + mDataList.get(position).getArticle_image().get(0).getArticleImage()).into(video.thumbImageView);
//                video.setUp(ConnectUrl.REQUESTURL + mDataList.get(position).getArticle_image().get(0).getArticleImage(), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
//                video.startVideo();
//                builder.setView(v);
//                AlertDialog alertDialog = builder.create();
//                alertDialog.setCanceledOnTouchOutside(false);
//                alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                alertDialog.show();
//                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x0000ffff));
//                alertDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {/*当dialog消失的时候停止一切播放并释放资源*/
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        JZVideoPlayer.releaseAllVideos();
//                    }
//                });
            }
        });
        int whidth = ScreenUtils.getScreenWidth(mContext) - ScreenUtils.dip2px(mContext, 16 * 2);
        mItemBinding.itemFashionContent.initWidth(whidth);
//        try {
        mItemBinding.itemFashionContent.setCloseText(mDataList.get(position).getArticle_content());
//        } catch (StringIndexOutOfBoundsException e) {
//           Log.e("买家秀设置字数时出现问题",e.toString());
//        }
        mItemBinding.itemFashionContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View v = LayoutInflater.from(mContext).inflate(R.layout.pop_note, null);
                TextView textView = v.findViewById(R.id.pop_tv_note);

                builder.setView(v);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
//                alertDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(mDataList.get(position).getArticle_content());
                        Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });

                return true;
            }
        });

//        查看全文
        mItemBinding.itemFashionMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iss) {
                    mItemBinding.itemFashionContent.setText(mDataList.get(position).getArticle_content());
                } else {
                    mItemBinding.itemFashionContent.setText(mDataList.get(position).getArticle_contentMine());
                }
                iss = !iss;
            }
        });
        //点赞
        mItemBinding.llZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataList.get(position).getArticle_comment_type() != null) {
                    if (mDataList.get(position).getArticle_comment_type().equals("1")) {
                        //已经点赞了
                        delectZAN(mDataList.get(position).article_id, mItemBinding, position);
                    } else {
                        //还没点赞
                        postZAN(mDataList.get(position).article_id, mItemBinding, position);
                    }
                }

            }
        });
        //收藏
        mItemBinding.llCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDataList.get(position).getCollect() != null) {
                    if (mDataList.get(position).getCollect().equals("0")) {
                        //已经收藏了
                        delectCollect(mDataList.get(position).article_id, mItemBinding, false, position);
                    } else {
                        //还没收藏
                        postCollect(mDataList.get(position).article_id, mItemBinding, position);
                    }
                } else {
                    postCollect(mDataList.get(position).article_id, mItemBinding, position);
                }
            }
        });
        //转发
        mItemBinding.llZhuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showLong(mContext, "未开发");

            }
        });
        //取消收藏
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View dialogV = LayoutInflater.from(mContext).inflate(R.layout.dialog_collect, null);
        TextView tv_cancle = dialogV.findViewById(R.id.dialog_tv_cancle);
        TextView tv_sure = dialogV.findViewById(R.id.dialog_tv_sure);
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
                delectCollect(mDataList.get(position).article_id, mItemBinding, true, position);
                dialog.dismiss();
            }
        });
        mItemBinding.tvCancleCol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        subTask(mItemBinding, position);
    }

    /**
     * 其它操作
     *
     * @param binding  绑定
     * @param position 列表位置
     */
    private void subTask(final ViewDataBinding binding, final int position) {
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(binding, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    /**
     * 设置监听Item点击事件
     *
     * @param onItemClickListener 监听
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    /**
     * Item点击事件接口
     */
    public interface OnItemClickListener {
        void onItemClick(ViewDataBinding binding, int position);
    }


    //文章收藏
    public void postCollect(String id, final ItemFinshionBinding itemFinshionBinding, final int pos) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ZAN, RequestMethod.POST);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("article_id", id);
        //article_category_type   1（点赞）0（收藏）
        request.add("article_comment_type", 0);
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            itemFinshionBinding.itemFashionCollect.setTextColor(Color.RED);
                            itemFinshionBinding.imCollect.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shoucang));
                            mDataList.get(pos).setCollect("0");
                            FashionAdapter.this.notifyDataSetChanged();
                            if(mOnRefreshMsg!=null){
                                mOnRefreshMsg.OnRefreshMsg();
                            }
                        }
                        if (show) {
                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                ToastUtil.showShort(mContext, "网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }

    //文章取消收藏
    public void delectCollect(String id, final ItemFinshionBinding itemFinshionBinding, final boolean isCol, final int pos) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.DELECTZAN, RequestMethod.POST);
        if (MyApplication.getUser() != null) {
            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("article_id", id);
        //article_category_type   1（点赞）0（收藏）
        request.add("article_comment_type", 0);
        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            itemFinshionBinding.itemFashionCollect.setTextColor(Color.BLACK);
                            itemFinshionBinding.imCollect.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shoucang2));
                            mDataList.get(pos).setCollect("1");
                            if (isCol) {
                                mDataList.remove(pos);
                                FashionAdapter.this.notifyDataSetChanged();
                            }
                            FashionAdapter.this.notifyDataSetChanged();
                            if(mOnRefreshMsg!=null){
                                mOnRefreshMsg.OnRefreshMsg();
                            }
                        }
                        if (show) {
                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                ToastUtil.showShort(mContext, "网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }

    //文章点赞
    public void postZAN(String id, final ItemFinshionBinding itemFinshionBinding, final int pos) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.ZAN, RequestMethod.POST);
        if (MyApplication.getUser() != null) {

            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("article_id", id);
        //article_category_type   1（点赞）0（收藏）
        request.add("article_comment_type", 1);

        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            itemFinshionBinding.itemFashionZan.setTextColor(Color.RED);
                            itemFinshionBinding.imZan.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zan2));
                            mDataList.get(pos).setArticle_comment_type("1");
                            FashionAdapter.this.notifyDataSetChanged();
                            if(mOnRefreshMsg!=null){
                                mOnRefreshMsg.OnRefreshMsg();
                            }
                        }
                        if (show) {
                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                ToastUtil.showShort(mContext, "网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }

    //文章取消点赞
    public void delectZAN(String id, final ItemFinshionBinding itemFinshionBinding, final int pos) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.DELECTZAN, RequestMethod.POST);
        if (MyApplication.getUser() != null) {

            request.add("user_id", MyApplication.getUser().user_id);
        }
        request.add("article_id", id);
        //article_category_type   1（点赞）0（收藏）
        request.add("article_comment_type", 1);

        mRequestQueue.add(1, request, new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {
            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                if (what == 1) {
                    Boolean success = false;
                    boolean show = false;
                    try {
                        JSONObject jsonObject = response.get();
                        success = jsonObject.getBoolean("success");
                        show = jsonObject.getBoolean("show");
                        if (success) {
                            itemFinshionBinding.itemFashionZan.setTextColor(Color.BLACK);
                            itemFinshionBinding.imZan.setImageDrawable(mContext.getResources().getDrawable(R.drawable.zan));
                            mDataList.get(pos).setArticle_comment_type("0");
                            FashionAdapter.this.notifyDataSetChanged();
                            if(mOnRefreshMsg!=null){
                                mOnRefreshMsg.OnRefreshMsg();
                            }
                        }
                        if (show) {
                            ToastUtil.showShort(mContext, jsonObject.getString("msg"));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                ToastUtil.showShort(mContext, "网络异常");
            }

            @Override
            public void onFinish(int what) {
            }
        });
    }

    public interface OnRefreshMsg {
        void OnRefreshMsg();
    }


}

