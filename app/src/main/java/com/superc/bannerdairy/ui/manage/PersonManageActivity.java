package com.superc.bannerdairy.ui.manage;

import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityPersonManageBinding;
import com.superc.bannerdairy.lock.PersonManageLock;

/**
 * 创建日期：2017/11/9 on 17:05
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 * 人员管理界面
 */

public class PersonManageActivity extends BaseActivity{

    private ActivityPersonManageBinding mBinding;
    private PersonManageLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_person_manage);
        mLock = new PersonManageLock(this, mBinding);
        mBinding.setPersonManageLock(mLock);

    }

}
