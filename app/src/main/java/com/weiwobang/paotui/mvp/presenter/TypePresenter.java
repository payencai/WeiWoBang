package com.weiwobang.paotui.mvp.presenter;

import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.mvp.Contract;
import com.weiwobang.paotui.mvp.model.MvpCallback;
import com.weiwobang.paotui.mvp.model.MvpModel;

import java.util.List;

public class TypePresenter implements MvpCallback<List<News>>,Contract.Presenter {
    private Contract.MvpView<List<News>> mMvpView;
    private MvpModel mTypeModel;
    public TypePresenter(Contract.MvpView<List<News>> mvpView,int page, String categoryId){
        mMvpView=mvpView;
        mMvpView.setPresenter(this);
        mTypeModel =new MvpModel(this, page, categoryId);
    }


    @Override
    public void loadSuccess(List<News> data) {
        mMvpView.hideLoading();
        mMvpView.showData(data);
    }

    @Override
    public void loadError(String error) {
         mMvpView.failed(error);
    }
    public void start(){
         mMvpView.showLoading();
         mTypeModel.getNewsByType();
    }

    @Override
    public void itemclick(int position) {

    }
}
