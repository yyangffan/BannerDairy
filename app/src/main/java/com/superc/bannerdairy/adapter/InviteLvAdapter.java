package com.superc.bannerdairy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.model.InviteItem;

import java.util.List;

/**
 * Created by user on 2018/1/26.
 */

public class InviteLvAdapter extends BaseAdapter {
    private Context mContext;
    private List<InviteItem.DataBean> mDataBeen;
    private LayoutInflater mInflater;

    public InviteLvAdapter(Context context, List<InviteItem.DataBean> dataBeen) {
        mContext = context;
        mDataBeen = dataBeen;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDataBeen == null ? 0 : mDataBeen.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataBeen == null ? null : mDataBeen.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mDataBeen == null ? 0 : i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolderr vh = null;
        if (view == null) {
            vh = new ViewHolderr();
            view = mInflater.inflate(R.layout.item_invite_lv, viewGroup, false);
            vh.mTextView = view.findViewById(R.id.invite_item_tv);
            vh.mtv_name=view.findViewById(R.id.invite_item_name);

            view.setTag(vh);
        } else {
            vh = (ViewHolderr) view.getTag();
        }
        InviteItem.DataBean dataBean = mDataBeen.get(i);
        vh.mTextView.setText(dataBean.getInvitationPrice());
        String name = dataBean.getInvitationLevel();
        vh.mtv_name.setText(name);
        return view;
    }

    class ViewHolderr {
        private TextView mTextView;
        private TextView mtv_name;


    }

}
