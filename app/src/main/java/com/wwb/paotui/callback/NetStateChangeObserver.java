package com.wwb.paotui.callback;


import com.wwb.paotui.bean.NetworkType;

public interface NetStateChangeObserver {
    void onNetDisconnected();

    void onNetConnected(NetworkType networkType);
}
