package com.weiwobang.paotui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.weiwobang.paotui.bean.NetworkType;
import com.weiwobang.paotui.callback.NetStateChangeObserver;
import com.weiwobang.paotui.receiver.NetStateChangeReceiver;


public class BaseActivity extends AppCompatActivity implements NetStateChangeObserver {
    @Override
    protected void onResume() {
        super.onResume();
        if (needRegisterNetworkChangeObserver()) {
            NetStateChangeReceiver.registerObserver(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (needRegisterNetworkChangeObserver()) {
            NetStateChangeReceiver.unregisterObserver(this);
        }
    }

    /**
     * 是否需要注册网络变化的Observer,如果不需要监听网络变化,则返回false;否则返回true.默认返回false
     */
    protected boolean needRegisterNetworkChangeObserver() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onNetDisconnected() {

    }

    @Override
    public void onNetConnected(NetworkType networkType) {

    }
}
