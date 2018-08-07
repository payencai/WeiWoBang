package com.weiwobang.paotui.mvp;

import java.util.List;

public interface Contract {
    interface MvpView<T> extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showData(T data);
        void failed(String error);

    }


    interface Presenter extends BasePresenter {

        void itemclick(int position);

    }
}
