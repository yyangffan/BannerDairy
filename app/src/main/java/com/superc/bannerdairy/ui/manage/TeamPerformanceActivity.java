package com.superc.bannerdairy.ui.manage;

import android.app.Activity;
import android.databinding.DataBindingUtil;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityTeamPerformanceBinding;
import com.superc.bannerdairy.lock.TeamPerLock;

//团队业绩界面
public class TeamPerformanceActivity extends BaseActivity {

    private ActivityTeamPerformanceBinding mBinding;
    private TeamPerLock mLock;

    @Override
    protected void initBinding() {
// 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_team_performance);
        mLock = new TeamPerLock(this, mBinding);
        mBinding.setTeamPerLock(mLock);
    }

}
