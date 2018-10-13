package com.wwb.paotui.mvp.view;

import com.wwb.paotui.bean.News;

import java.util.List;

public interface MvpView<T> {
    void showLoading();
    void hideLoading();
    void showData(T data);
    void failed(String error);
}
