package com.superc.bannerdairy.ui.home;

import android.databinding.DataBindingUtil;
import android.view.KeyEvent;
import android.widget.Toast;

import com.superc.bannerdairy.R;
import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.databinding.ActivityMainBinding;
import com.superc.bannerdairy.jiguang.SetJPushAlias;
import com.superc.bannerdairy.lock.MainLock;
import com.superc.cframework.utils.ToastUtil;

/**
 * 创建日期：2017/11/6 on 17:58
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 */

public class MainActivity extends BaseActivity {

    private ActivityMainBinding mBinding;
    public MainLock mLock;

    @Override
    protected void initBinding() {
        // 数据绑定操作，可以套用代码
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mLock = new MainLock(this, mBinding,mBundle);
        mBinding.setMainLock(mLock);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mLock!=null) {
            mLock.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Glide.with(this).pauseRequests();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mLock.RefreshNum();
        mLock.refresh();
    }

    long starT=0;
    long endT=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        starT=System.currentTimeMillis();
        if(starT-endT>=2000){
            ToastUtil.show(this,"双击退出", Toast.LENGTH_SHORT);
            endT=starT;
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
