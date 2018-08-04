package com.weiwobang.paotui.callback;


import com.weiwobang.paotui.bean.NetworkType;

public interface NetStateChangeObserver {
    void onNetDisconnected();

    void onNetConnected(NetworkType networkType);
}
