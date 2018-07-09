package com.superc.bannerdairy.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityMessageDetailBinding;
import com.superc.bannerdairy.lock.MessageDetailLock;


public class MessageDetailActivity extends BaseActivity {
    public static final String NOTICEID = "notice_id";
    public static final String NOTICETITLE = "notice_title";
    public static final String CREATOR = "creator";
    private ActivityMessageDetailBinding mBinding;


    @Override
    protected void initBinding() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_message_detail);
        MessageDetailLock messageDetailLock = new MessageDetailLock(this, mBinding, mBundle);
        mBinding.setMessageLock(messageDetailLock);

    }

    public static Intent getCallIntent(Context context, String notice_id, String notice_title, String creator) {
        Intent intent = new Intent(context, MessageDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(NOTICEID, notice_id);
        bundle.putString(NOTICETITLE, notice_title);
        bundle.putString(CREATOR, creator);
        intent.putExtras(bundle);
        return intent;

    }


}
