package com.superc.bannerdairy.lock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.constant.RunTime;
import com.superc.bannerdairy.databinding.ActivityKnowledgeDetailBinding;
import com.superc.bannerdairy.db.DownPicUtil;
import com.superc.bannerdairy.model.FinshItem;
import com.superc.bannerdairy.model.KnowledgeItem;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;

/**
 * Created by Amorr on 2017/11/20.
 */

public class KnowledgeDetailLock extends BaseLock<ActivityKnowledgeDetailBinding> {
    public KnowledgeItem knowledgeItem;
    public FinshItem finshItem;
    private String mTitle;

    public KnowledgeDetailLock(Context context, ActivityKnowledgeDetailBinding binding) {
        super(context, binding);
    }

    public KnowledgeDetailLock(Context context, ActivityKnowledgeDetailBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        mTitle="传播专区";
        if (RunTime.getRunTime(RunTime.FINSHION) != null) {


            //传播专区的文章
            if (RunTime.getRunTime(RunTime.FINSHION).equals("1")) {
                if(mBundle!=null){
                    mTitle = mBundle.getString("title");
                }
                AppBarLock appBarLock = new AppBarLock(mContext, mTitle);
                appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
                appBarLock.barData.isLeft = true;
                appBarLock.barData.titleLeft = "返回";
                mBinding.titleBar.setAppBarLock(appBarLock);

                knowledgeItem = (KnowledgeItem) RunTime.getRunTime(RunTime.KNOWLEDGEID);
                mBinding.title.setText(knowledgeItem.article_title);
                mBinding.content.setText(knowledgeItem.article_content);
                mBinding.num.setText(knowledgeItem.pageview);
                mBinding.time.setText(knowledgeItem.created);
//                if (knowledgeItem.getArticle_image() != null) {
//                    Glide.with(mContext).load(ConnectUrl.REQUESTURL + knowledgeItem.getArticle_image()).into(mBinding.imageView7);
//                }
//                mBinding.knowWeb.loadDataWithBaseURL(null, knowledgeItem.getArticle_content(), "text/html", "UTF-8", null); // 加载定义的代码，并设定编码格式和字符集。

                String contentt = knowledgeItem.getArticle_content().replace("<img", "<img height=\"auto\"; width=\"100%\"");
                mBinding.knowWeb.loadDataWithBaseURL(null, contentt, "text/html", "UTF-8", null); // 加载定义的代码，并设定编码格式和字符集。


            } else {
                //买家秀的文章
                AppBarLock appBarLock = new AppBarLock(mContext, R.string.fash_title);
                appBarLock.barData.imsLeft = mContext.getResources().getDrawable(R.drawable.fanhui);
                appBarLock.barData.isLeft = true;
                appBarLock.barData.titleLeft = "返回";
                mBinding.titleBar.setAppBarLock(appBarLock);

                finshItem = (FinshItem) RunTime.getRunTime(RunTime.KNOWLEDGEID);
                mBinding.title.setText(finshItem.article_title);
                mBinding.content.setText(finshItem.article_content);
                mBinding.num.setText(finshItem.creator);
                mBinding.time.setText(finshItem.created);
                if (finshItem.getArticle_image() != null) {
                    Glide.with(mContext).load(ConnectUrl.REQUESTURL + finshItem.getArticle_image()).into(mBinding.imageView7);
                }
            }
        }
// 长按点击事件
        mBinding.knowWeb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final WebView.HitTestResult hitTestResult = mBinding.knowWeb.getHitTestResult();
                // 如果是图片类型或者是带有图片链接的类型
                if(hitTestResult.getType()== WebView.HitTestResult.IMAGE_TYPE||
                        hitTestResult.getType()== WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE){
                    // 弹出保存图片的对话框
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示");
                    builder.setMessage("保存图片到本地");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String url = hitTestResult.getExtra();
                            // 下载图片到本地
                            DownPicUtil.downPic(url, new DownPicUtil.DownFinishListener(){
                                @Override
                                public void getDownPath(String s) {
                                    Toast.makeText(mContext,"保存成功",Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        // 自动dismiss
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });



//        httpKnow();
    }

    //获取文章详情
    private void httpKnow() {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.LIST_ARTICLE, RequestMethod.POST);
        request.add("act", "one");
        //article_category_type   2（买家秀）0（文章列表）
        request.add("article_category_type", 0);
        if (RunTime.getRunTime(RunTime.FINSHION).equals("1")) {
            request.add("article_id", ((KnowledgeItem) RunTime.getRunTime(RunTime.KNOWLEDGEID)).article_id);

        } else {
            request.add("article_id", ((FinshItem) RunTime.getRunTime(RunTime.KNOWLEDGEID)).article_id);

        }

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
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            KnowledgeItem item = new Gson().fromJson(jsonObject1.toString(), KnowledgeItem.class);
                            mBinding.title.setText(item.article_title);
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
