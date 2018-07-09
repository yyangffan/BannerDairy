package com.superc.bannerdairy.ui.manage;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityMyTeamBinding;
import com.superc.bannerdairy.lock.MyTeamLock;

/**
 * 创建日期：2017/11/13 on 17:57
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 * 我的团队界面
 */

public class MyTeamActivity extends BaseActivity {
    private ActivityMyTeamBinding mBinding;
    private MyTeamLock mLock;

    @Override
    protected void initBinding() {
// 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_team);
        mLock = new MyTeamLock(this, mBinding);
        mBinding.setMyTeamLock(mLock);
    }



}
