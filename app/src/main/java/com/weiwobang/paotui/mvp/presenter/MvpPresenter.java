package com.weiwobang.paotui.mvp.presenter;

import com.weiwobang.paotui.bean.Userinfo;
import com.weiwobang.paotui.mvp.Contract;
import com.weiwobang.paotui.mvp.model.MvpCallback;
import com.weiwobang.paotui.mvp.model.MvpModel;

public class MvpPresenter<T> implements MvpCallback<T> {
    private Contract.MvpView<T> mMvpView;
    private MvpModel mMvpModel;
    public MvpPresenter(Contract.MvpView<T> mvpView, String token) {
        mMvpView = mvpView;
       // mvpView.setPresenter(this);
        mMvpModel=new MvpModel(token,this);
    }
    public MvpPresenter(Contract.MvpView<T> mvpView, String token,int page) {
        mMvpView = mvpView;
        // mvpView.setPresenter(this);
        mMvpModel=new MvpModel(token,this,page);
    }
    @Override
    public void loadSuccess(T data) {
       mMvpView.showData(data);
    }

    @Override
    public void loadError(String error) {
        mMvpView.failed(error);
    }
    public void start(){
        mMvpView.showLoading();
        mMvpModel.getUserinfo();
    }
    public void getMyOrder(){
        mMvpView.showLoading();
        mMvpModel.getMyOrder();
    }
    public void getMyPublish(){
        mMvpView.showLoading();
        mMvpModel.getMyPublish();
    }

}
