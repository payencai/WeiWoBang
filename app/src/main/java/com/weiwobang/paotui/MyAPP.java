package com.weiwobang.paotui;

import android.app.Application;

import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.weiwobang.paotui.api.Api;
import com.weiwobang.paotui.constant.Constant;
import com.weiwobang.paotui.tools.PreferenceManager;

public class MyAPP extends Application{
    /**
     * release sha1: 2F:39:DB:CA:7F:6C:15:9E:73:EC:EB:A2:20:2A:BA:8F:9C:3A:19:8D
     * debug sha1: 6B:38:39:B6:A5:4A:C7:1D:21:9C:EF:A6:BE:97:64:0C:E0:86:1D:96
     *
     */
    public static boolean isLogin=false;
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkManager.getInstance().init(Api.baseUrl);
        PreferenceManager.init(getApplicationContext());
    }
}
