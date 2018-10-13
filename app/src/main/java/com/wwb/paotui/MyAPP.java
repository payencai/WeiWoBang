package com.wwb.paotui;

import android.app.Application;
import android.content.Context;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wwb.paotui.JPush.JpushConfig;
import com.wwb.paotui.api.Api;
import com.wwb.paotui.constant.Constant;
import com.wwb.paotui.tools.PreferenceManager;

import org.androidannotations.annotations.App;
import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;

public class MyAPP extends Application {
    /**96c59f850e67610e3416f43c77956fb9
     * d0c78291dbb8536ead37630b9f07c645
     *
     * release sha1: 2F:39:DB:CA:7F:6C:15:9E:73:EC:EB:A2:20:2A:BA:8F:9C:3A:19:8D
     * debug sha1: 6B:38:39:B6:A5:4A:C7:1D:21:9C:EF:A6:BE:97:64:0C:E0:86:1D:96
     */
    public static IWXAPI mWxApi;
    private static MyAPP INSTANCE;
    public static boolean isLogin = false;
    public static String alias;//推送别名
    public static String token;
    public static String token2;
    public static boolean isDebug = false;
    public static final String APP_ID="wx57f1a1cb08d803fe";
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        LitePal.initialize(this);
        NetWorkManager.getInstance().initUrl(Api.rootUrl);//初始化旧版商家模块的网络请求
        NetWorkManager.getInstance().init(Api.webNewUrl);//初始化新版唯我帮网络请求
        PreferenceManager.init(getApplicationContext());
        JpushConfig.getInstance().initJpush();
        JpushConfig.getInstance().stopJpush();//先停止，在需要的地方调用开启接收推送
        registerToWX();
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);

    }

    public static synchronized MyAPP getInstance() {
        return INSTANCE;
    }

    private void registerToWX() {
        //第二个参数是指你应用在微信开放平台上的AppID
        mWxApi = WXAPIFactory.createWXAPI(this, APP_ID, true);
        // 将该app注册到微信
        mWxApi.registerApp(APP_ID);

    }

}
