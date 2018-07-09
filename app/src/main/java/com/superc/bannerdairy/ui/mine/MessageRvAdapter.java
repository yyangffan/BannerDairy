package com.superc.bannerdairy.ui.mine;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.Toast;

import com.superc.bannerdairy.MyApplication;
import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.ConnectUrl;
import com.superc.bannerdairy.databinding.ItemMyMessageBinding;
import com.superc.bannerdairy.model.MyMessageItem;
import com.superc.bannerdairy.service.OnSomeThingClickListener;
import com.superc.bannerdairy.service.ShowRemindDialog;
import com.superc.cframework.base.ui.CBaseRecyclerViewAdapter;
import com.superc.cframework.utils.ToastUtil;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 2017/12/18.
 */

public class MessageRvAdapter extends CBaseRecyclerViewAdapter<MyMessageItem> {
    private Context mContext;
    private List<MyMessageItem> datalist;

    /**
     * 构造函数
     *
     * @param context    上下文
     * @param dataList   数据列表
     * @param layoutId   单布局
     * @param variableId DataBinding的BR
     */
    public MessageRvAdapter(Context context, List<MyMessageItem> dataList, int layoutId, int variableId) {
        super(context, dataList, layoutId, variableId);
        mContext = context;
        this.datalist = dataList;
    }

    @Override
    protected void subTask(ViewDataBinding binding, final int position) {
        super.subTask(binding, position);
        ItemMyMessageBinding mBinding = (ItemMyMessageBinding) binding;
        mBinding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowRemindDialog.getInstance().showDialog(mContext, R.mipmap.babycry, "确认删除?", false, new OnSomeThingClickListener() {
                    @Override
                    public void OnDoSomeThingClickListener(int view_id) {
                        deleteGo(datalist.get(position).getNoticeId(), position);
                    }
                });
            }
        });


    }

    /*开始删除某一条消息*/
    public void deleteGo(String notice_id, final int pos) {
        RequestQueue mRequestQueue = NoHttp.newRequestQueue();
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(ConnectUrl.DELETEONENOTICE, RequestMethod.GET);
        request.add("notice_id", notice_id);
        if (MyApplication.getUser().getUser_id() != null) {
            request.add("user_id", MyApplication.getUser().getUser_id());
        }
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
                            datalist.remove(pos);
                            MessageRvAdapter.this.notifyDataSetChanged();
                        }
                        if (show) {
                            ToastUtil.show(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
//                hideLoading();
                ToastUtil.show(mContext, "网络异常", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFinish(int what) {
//                hideLoading();
            }
        });
    }


}
