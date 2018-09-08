package com.weiwobang.paotui;

import android.app.Application;
import android.content.Context;

import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.weiwobang.paotui.JPush.JpushConfig;
import com.weiwobang.paotui.api.Api;
import com.weiwobang.paotui.constant.Constant;
import com.weiwobang.paotui.tools.PreferenceManager;

import cn.jpush.android.api.JPushInterface;

public class MyAPP extends Application{
    /**
     * release sha1: 2F:39:DB:CA:7F:6C:15:9E:73:EC:EB:A2:20:2A:BA:8F:9C:3A:19:8D
     * debug sha1: 6B:38:39:B6:A5:4A:C7:1D:21:9C:EF:A6:BE:97:64:0C:E0:86:1D:96
     *
     */
    private static MyAPP INSTANCE;
    public static boolean isLogin=false;
    public static String alias;
    public static String token;
    public static String token2;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE=this;
        NetWorkManager.getInstance().initUrl(Api.rootUrl);//初始化旧版商家模块的网络请求
        NetWorkManager.getInstance().init(Api.localNewUrl);//初始化新版唯我帮网络请求
        PreferenceManager.init(getApplicationContext());
        JpushConfig.getInstance().initJpush();
        JpushConfig.getInstance().stopJpush();//先停止，在需要的地方调用开启接收推送
    }
    public static synchronized MyAPP getInstance() {
        return INSTANCE;
    }

}
