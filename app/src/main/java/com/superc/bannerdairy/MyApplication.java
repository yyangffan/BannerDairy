package com.superc.bannerdairy;

import android.support.multidex.MultiDexApplication;

import com.google.gson.reflect.TypeToken;
import com.superc.bannerdairy.base.Constant;
import com.superc.bannerdairy.base.SPUtils;
import com.superc.bannerdairy.model.User;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;

import cn.jpush.android.api.JPushInterface;

/**
 * 创建日期：2017/10/30 on 15:23
 * 描述：项目Application，单例设计模式
 * 作者：郭士超
 * QQ：1169380200
 */

public class MyApplication extends MultiDexApplication {

    private volatile static MyApplication instance;
    private static User mUser;
    private IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        InitializationConfig config = InitializationConfig.newBuilder(this).connectionTimeout(30 * 1000).readTimeout(30 * 1000).retry(10).build();
        NoHttp.initialize(config);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        if(this.getUser()==null) {
            SPUtils.put(getInst(), "userInfo", new User());
        }
        api = WXAPIFactory.createWXAPI(this, Constant.WXAPPID);
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5aaf605bb27b0a3765000232");
    }

    public IWXAPI getApi() {
        return api;
    }

    public static MyApplication getInstance() {
        if(instance == null) {
            synchronized(MyApplication.class) {
                if(instance == null) {
                    instance = new MyApplication();
                }
            }
        }
        return instance;
    }
    public static MyApplication getInst() {
        return instance;
    }

    public static User getUser() {
//        User user = (User) SPUtils.get(MyApplication.getInst(), "userInfor", User.class);
        User user = (User) SPUtils.get(getInst(), "userInfo", new TypeToken<User>() {
        }.getType());
        return user;
    }

    public static void setUser(User user) {
        SPUtils.put(getInst(),"userInfo",user);
        mUser = user;
    }

}
