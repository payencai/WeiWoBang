package com.weiwobang.paotui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.weiwobang.paotui.bean.NetworkType;
import com.weiwobang.paotui.callback.NetStateChangeObserver;
import com.weiwobang.paotui.receiver.NetStateChangeReceiver;

public abstract class BaseFragment extends Fragment implements NetStateChangeObserver {
    @Override
    public void onResume() {

        if (needRegisterNetworkChangeObserver()) {
            NetStateChangeReceiver.registerObserver(this);
        }
        super.onResume();
    }

    @Override
    public void onStop() {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
