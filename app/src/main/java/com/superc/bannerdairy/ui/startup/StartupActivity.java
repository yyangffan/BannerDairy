package com.superc.bannerdairy.ui.startup;

import android.os.CountDownTimer;

import com.superc.bannerdairy.base.BaseActivity;
import com.superc.bannerdairy.base.UpdateManager;
import com.superc.bannerdairy.ui.home.MainActivity;

/**
 * 创建日期：2017/11/6 on 15:29
 * 描述：
 * 作者：郭士超
 * QQ：1169380200
 * 启动页
 */

public class StartupActivity extends BaseActivity {

    @Override
    protected void initBinding() {

    }

    @Override
    protected void init() {
        checkUpdate();
//        new CountDownTimer(1000, 1000) {
//            public void onTick(long millisUntilFinished) {
//            }
//            public void onFinish() {
//                startActivity(MainActivity.class);
//                finish();
//            }
//        }.start();
    }

    public void checkUpdate() {
        UpdateManager updateManager = new UpdateManager(this, false);
        updateManager.checkUpdate();
        updateManager.setOnCancelClickListener(new UpdateManager.OnCancelClickListener() {
            @Override
            public void OnCancelClickListener() {
                StartupActivity.this.finish();
            }
        });
        updateManager.setIsUpDateListener(new UpdateManager.IsUpDateListener() {
            @Override
            public void IsUpDateListener(boolean isUpdate) {
                if (!isUpdate) {
                    new CountDownTimer(1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            startActivity(MainActivity.class);
                            finish();
                        }
                    }.start();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkUpdate();
    }

}
