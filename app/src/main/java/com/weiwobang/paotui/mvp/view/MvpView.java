package com.weiwobang.paotui.mvp.view;

import com.weiwobang.paotui.bean.News;

import java.util.List;

public interface MvpView<T> {
    void showLoading();
    void hideLoading();
    void showData(T data);
    void failed(String error);
}
