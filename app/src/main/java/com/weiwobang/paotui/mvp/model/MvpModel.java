package com.weiwobang.paotui.mvp.model;

import android.util.Log;

import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Data;
import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.bean.Order;
import com.weiwobang.paotui.bean.Userinfo;
import com.weiwobang.paotui.tools.PreferenceManager;

import java.io.IOException;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MvpModel<T> {
    private String token;
    private int page;
    private String categoryId;
    private String content;
    private MvpCallback<T> mMvpCallback;

    public MvpModel(String token, MvpCallback<T> userCallBack) {
        this.token = token;
        mMvpCallback = userCallBack;
    }

    public MvpModel(String token, MvpCallback<T> userCallBack, int page) {
        this.token = token;
        mMvpCallback = userCallBack;
        this.page = page;
    }
    public MvpModel( MvpCallback<T> userCallBack, int page,String categoryId) {
        this.categoryId = categoryId;
        mMvpCallback = userCallBack;
        this.page = page;
    }
    public MvpModel( MvpCallback<T> userCallBack) {
        mMvpCallback = userCallBack;
    }

    /**
    * 获取搜索结果
    */

     public  void getSearchResult(int page,String content){
         Disposable disposable = NetWorkManager.getRequest(ApiService.class).getSearchMsg(page, content)
                 //.compose(ResponseTransformer.handleResult())
                 .compose(SchedulerProvider.getInstance().applySchedulers())
                 .subscribe(new Consumer<RetrofitResponse<Data<News>>>() {
                     @Override
                     public void accept(RetrofitResponse<Data<News>> retrofitResponse) throws Exception {
                         mMvpCallback.loadSuccess((T) retrofitResponse.getData().getBeanList());
                     }
                 }, new Consumer<Throwable>() {
                     @Override
                     public void accept(Throwable throwable) throws Exception {
                         ApiException apiException = CustomException.handleException(throwable);
                         mMvpCallback.loadError(apiException.getDisplayMessage());
                     }
                 });
         new CompositeDisposable().add(disposable);
     }
    /**
     * 获取用户信息
     */

    public void getUserinfo() {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getUserinfo(token)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Userinfo>>() {
                    @Override
                    public void accept(RetrofitResponse<Userinfo> retrofitResponse) throws Exception {
                        mMvpCallback.loadSuccess((T) retrofitResponse.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        mMvpCallback.loadError(apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    /**
     * 根据类型获取消息
     */
    public void getNewsByType() {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getMsgByType(page, categoryId)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Data<News>>>() {
                    @Override
                    public void accept(RetrofitResponse<Data<News>> retrofitResponse) throws Exception {
                        mMvpCallback.loadSuccess((T) retrofitResponse.getData().getBeanList());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        mMvpCallback.loadError(apiException.getDisplayMessage());
                    }
                });
        new CompositeDisposable().add(disposable);
    }

    /**
     * 获取我的订单
     */
    public void getMyOrder() {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getMyOrder(page, token)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Data<Order>>>() {
                    @Override
                    public void accept(RetrofitResponse<Data<Order>> retrofitResponse) throws Exception {

                        mMvpCallback.loadSuccess((T) retrofitResponse.getData().getBeanList());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        mMvpCallback.loadError(apiException.getDisplayMessage());
                    }
                });
        new CompositeDisposable().add(disposable);
    }

    /**
     * 获取我的发布
     */
    public void getMyPublish() {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getMine(page, token)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Data<News>>>() {
                    @Override
                    public void accept(RetrofitResponse<Data<News>> retrofitResponse) throws Exception {
                        mMvpCallback.loadSuccess((T) retrofitResponse.getData().getBeanList());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        mMvpCallback.loadError(apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }
    /**
     * 获取今日消息
     */
    public void getTodayNews(){
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getToday(page)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Data<News>>>() {
                    @Override
                    public void accept(RetrofitResponse<Data<News>> retrofitResponse) throws Exception {
                       // Log.e("result",retrofitResponse.getData().getBeanList().get(0).getTitle());
                        mMvpCallback.loadSuccess((T) retrofitResponse.getData().getBeanList());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        mMvpCallback.loadError(apiException.getDisplayMessage());
                    }
                });
        new CompositeDisposable().add(disposable);
    }
}
