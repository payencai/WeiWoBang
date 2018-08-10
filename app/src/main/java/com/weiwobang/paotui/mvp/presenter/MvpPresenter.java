package com.weiwobang.paotui.mvp.presenter;

import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.bean.Userinfo;
import com.weiwobang.paotui.mvp.Contract;
import com.weiwobang.paotui.mvp.model.MvpCallback;
import com.weiwobang.paotui.mvp.model.MvpModel;

import java.util.ArrayList;

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
    public MvpPresenter(Contract.MvpView<T> mvpView,int page,String categoryId) {
        mMvpView = mvpView;
        // mvpView.setPresenter(this);
        mMvpModel=new MvpModel(this,page,categoryId);
    }
    @Override
    public void loadSuccess(T data) {
       mMvpView.showData(data);
    }

    @Override
    public void loadError(String error) {
        mMvpView.failed(error);
    }

    @Override
    public void loadEmpty() {
       // mMvpView.showData(new ArrayList<News>());
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
    public void getTodayNews(){
        mMvpView.showLoading();
        mMvpModel.getTodayNews();
    }
    public void getNewsByType(){
        mMvpView.showLoading();
        mMvpModel.getNewsByType();
    }
}
