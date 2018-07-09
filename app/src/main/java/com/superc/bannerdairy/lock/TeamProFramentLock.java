package com.superc.bannerdairy.lock;

import android.content.Context;
import android.os.Bundle;

import com.superc.bannerdairy.base.BaseLock;
import com.superc.bannerdairy.databinding.FragmentTeamPerBinding;

/**
 * Created by Amorr on 2017/11/22.
 * 我的团队业绩
 */

public class TeamProFramentLock extends BaseLock<FragmentTeamPerBinding> {
    public TeamProFramentLock(Context context, FragmentTeamPerBinding binding) {
        super(context, binding);
    }

    public TeamProFramentLock(Context context, FragmentTeamPerBinding binding, Bundle bundle) {
        super(context, binding, bundle);
    }

    @Override
    protected void init() {

    }
}
