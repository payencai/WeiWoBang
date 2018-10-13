package com.wwb.paotui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wwb.paotui.bean.NetworkType;
import com.wwb.paotui.callback.NetStateChangeObserver;
import com.wwb.paotui.receiver.NetStateChangeReceiver;


public   class BaseActivity extends AppCompatActivity implements NetStateChangeObserver {
    @Override
    protected void onResume() {

        if (needRegisterNetworkChangeObserver()) {
            NetStateChangeReceiver.registerObserver(this);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {

        if (needRegisterNetworkChangeObserver()) {
            NetStateChangeReceiver.unregisterObserver(this);
        }
        super.onStop();
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

    /**
     * 网络断开时候触发
     */
    @Override
    public void onNetDisconnected() {

    }

    @Override
    public void onNetConnected(NetworkType networkType) {

    }
}
