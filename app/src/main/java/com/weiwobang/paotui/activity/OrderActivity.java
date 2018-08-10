package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.payencai.library.adapter.OnItemClickListener;
import com.payencai.library.adapter.OnLoadMoreListener;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.NewsAdapter;
import com.weiwobang.paotui.adapter.OrderAdapter;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Data;
import com.weiwobang.paotui.bean.Order;
import com.weiwobang.paotui.mvp.Contract;
import com.weiwobang.paotui.mvp.presenter.MvpPresenter;
import com.weiwobang.paotui.mvp.presenter.TypePresenter;
import com.weiwobang.paotui.tools.PreferenceManager;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OrderActivity extends AppCompatActivity implements Contract.MvpView<List<Order>>{
    @BindView(R.id.sr_order)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_order)
    RecyclerView mRecyclerView;
    OrderAdapter mOrderAdapter;
    @BindView(R.id.back)
    ImageView back;
    MvpPresenter<List<Order>> mMvpPresenter;
    private int page=1;
    private boolean isLoadMore=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_order);
        ButterKnife.bind(this);
        initView();
        initAdapter();
        getData();
        //loadData();
    }
    private void  initAdapter(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderAdapter = new OrderAdapter(R.layout.wwb_item_order);
        mOrderAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
              //  mOrderAdapter.setEnableLoadMore(true);
                page++;
                getData();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 page = 1;
                 isLoadMore=false;
                 getData();
                //loadData();
            }
        });

//        mOrderAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull View view, int adapterPosition) {
//              ///  mPresenter.itemclick(adapterPosition);
//                String id = mOrderAdapter.getData(adapterPosition).getId();
//                Intent intent = new Intent(TypeActivity.this, DetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("id", id);
//                intent.putExtras(bundle);
//                startActivity(intent);
//
//            }
//        });

    }
    private void initView() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData() {
        try {
            mMvpPresenter = new MvpPresenter(this, PreferenceManager.getInstance().getUserinfo().getToken(),page);
            mMvpPresenter.getMyOrder();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
    private void loadData(){
//        Log.e("laod","dfdgfg");
//        Disposable disposable = null;
//        try {
//            disposable = NetWorkManager.getRequest(ApiService.class).getMyOrder(page, PreferenceManager.getInstance().getUserinfo().getToken())
//                    //.compose(ResponseTransformer.handleResult())
//                    .compose(SchedulerProvider.getInstance().applySchedulers())
//                    .subscribe(new Consumer<RetrofitResponse<Data<Order>>>() {
//                        @Override
//                        public void accept(RetrofitResponse<Data<Order>> retrofitResponse) throws Exception {
//                            Log.e("gggg","gggg"+retrofitResponse.getData().getBeanList().size());
//                            if (mSwipeRefreshLayout.isRefreshing()) {
//                                mSwipeRefreshLayout.setRefreshing(false);
//                            }
//                            if (retrofitResponse.getData().getBeanList().size() != 0) {
//                                if (isLoadMore) {
//                                    isLoadMore = false;
//                                    mOrderAdapter.addData(retrofitResponse.getData().getBeanList());
//                                    //mRecyclerView.setAdapter(mOrderAdapter);
//                                } else {
//                                    mOrderAdapter.setData(retrofitResponse.getData().getBeanList());
//                                    mRecyclerView.setAdapter(mOrderAdapter);
//                                }
//                            }
//
//
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            ApiException apiException = CustomException.handleException(throwable);
//                            Log.e("tag",apiException.getDisplayMessage());
//                           // mMvpCallback.loadError(apiException.getDisplayMessage());
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        new CompositeDisposable().add(disposable);
   }
    @Override
    public void showData(List<Order> data) {
        Log.e("page",page+"");
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            mOrderAdapter.setEnableLoadMore(true);
        }
        if(data.size()==0){
            //没有更多数据
            mOrderAdapter.loadMoreEnd();
            mOrderAdapter.loadMoreComplete();
            Log.e("load","load");
            mOrderAdapter.setEnableLoadMore(false);
            return;
        }else{
            if (isLoadMore) {
                isLoadMore = false;
                mOrderAdapter.addData(data);
                mOrderAdapter.loadMoreComplete();
                //mOrderAdapter.setEnableLoadMore(false);
                // mRecyclerView.setAdapter(mOrderAdapter);
            } else {
                mOrderAdapter.setNewData(data);
                mRecyclerView.setAdapter(mOrderAdapter);

            }
        }

    }

    @Override
    public void failed(String error) {

    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {

    }
}
