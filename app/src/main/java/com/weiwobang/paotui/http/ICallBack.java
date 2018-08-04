package com.weiwobang.paotui.http;

/**
 * Created by Administrator on 2017/9/29.
 */

public interface ICallBack {
    void OnSuccess(String result);

    void onFailure(String error);

}
