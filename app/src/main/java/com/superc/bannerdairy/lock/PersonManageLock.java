package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.databinding.ActivityPersonManageBinding;
import com.superc.bannerdairy.ui.manage.MyTeamActivity;
import com.superc.bannerdairy.ui.manage.TeamPerformanceActivity;
import com.superc.bannerdairy.ui.mine.InviteActivity;
import com.superc.bannerdairy.ui.mine.MyInviteActivity;

/**
 * 创建日期：2017/11/9 on 17:05
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 * 人员管理界面
 */

public class PersonManageLock extends BaseLock<ActivityPersonManageBinding> {
    public static final int BIND = 1001;

    private String[] titles;

    public PersonManageLock(Context context, ActivityPersonManageBinding binding) {
        super(context, binding);
    }

    public PersonManageLock(Context context, ActivityPersonManageBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {
        AppBarLock appBarLock=new AppBarLock(mContext, R.string.managePage);
        appBarLock.barData.isLeft=true;
        appBarLock.barData.titleLeft="返回";
        appBarLock.setRight(BIND);
        appBarLock.barData.isRight=false;
        appBarLock.barData.imsRight=mContext.getResources().getDrawable(R.drawable.xiaoxi);
        appBarLock.barData.imsLeft=mContext.getResources().getDrawable(R.drawable.fanhui);
        mBinding.titleBar.setAppBarLock(appBarLock);

    }
    //我的团队
    public void myTeam(){
        startActivity(MyTeamActivity.class);
    }
    //团队业绩
    public void teamAchieve(){
        startActivity(TeamPerformanceActivity.class);
    }
    //我的邀请
    public void myInvitation(){
        startActivity(MyInviteActivity.class);

    }
    //邀请代理
    public void invitationAgent(){
        startActivity(InviteActivity.class);
    }

}
