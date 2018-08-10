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
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.NewsAdapter;

import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.mvp.Contract;
import com.weiwobang.paotui.mvp.presenter.MvpPresenter;
import com.weiwobang.paotui.tools.PreferenceManager;


import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TypeActivity extends AppCompatActivity implements Contract.MvpView<List<News>> {
    @BindView(R.id.type_title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    RecyclerView mRecyclerView;
    NewsAdapter mNewsAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MvpPresenter<List<News>> mMvpPresenter;
    private Contract.Presenter mPresenter;
    private int page = 1;
    boolean isLoadMore = false;
    String categoryId = "";
    String categoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_type);
        ButterKnife.bind(this);
        initView();
        initNews();
        getData();
    }

    private void getData() {
        mMvpPresenter = new MvpPresenter(this, page, categoryId);
        mMvpPresenter.getNewsByType();
    }

    private void initNews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_new);
        mRecyclerView = findViewById(R.id.recycleview_new);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsAdapter = new NewsAdapter(R.layout.wwb_item_forum);
        //mKnowAdapter.addHeadLayout(R.layout.header_know);
        //mNewsAdapter.openAutoLoadMore(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isLoadMore = false;
                getData();
            }
        });
        //mNewsAdapter.disableLoadMoreIfNotFullPage();
        mNewsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
                page++;
                getData();
            }
        });

        mNewsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String id = mNewsAdapter.getItem(position).getId();
                Intent intent = new Intent(TypeActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//        mNewsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                isLoadMore = true;
//                page++;
//                getData();
//                //loadData();
//            }
//        });
//        mNewsAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(@NonNull View view, int adapterPosition) {
//                mPresenter.itemclick(adapterPosition);
//                String id = mNewsAdapter.getData(adapterPosition).getId();
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
        Bundle bundle = getIntent().getExtras();
        categoryId = bundle.getString("id");
        categoryName = bundle.getString("name");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText(categoryName);

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showData(List<News> data) {
        Log.e("page", page + "");
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            mNewsAdapter.setEnableLoadMore(true);
        }
        if (data.size() == 0) {
            if (isLoadMore) {
                Log.e("load", "type");
                //没有更多数据
                mNewsAdapter.loadMoreEnd();
            }else{
                mNewsAdapter.setNewData(data);
                mRecyclerView.setAdapter(mNewsAdapter);
            }
        } else {
            if (isLoadMore) {
                isLoadMore = false;
                mNewsAdapter.addData(data);
                mNewsAdapter.loadMoreComplete();
            } else {
                mNewsAdapter.setNewData(data);
                mRecyclerView.setAdapter(mNewsAdapter);

            }
        }

    }

    @Override
    public void failed(String error) {

    }
//    private void loadData() {
//        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getMsgByType(page, categoryId)
//                //.compose(ResponseTransformer.handleResult())
//                .compose(SchedulerProvider.getInstance().applySchedulers())
//                .subscribe(new Consumer<RetrofitResponse<Data<News>>>() {
//                    @Override
//                    public void accept(RetrofitResponse<Data<News>> retrofitResponse) throws Exception {
//                        Log.e("result", retrofitResponse.getData().getBeanList().get(0).getTitle());
//                        if (mSwipeRefreshLayout.isRefreshing()) {
//                            mSwipeRefreshLayout.setRefreshing(false);
//                        }
//                        if (page == 1 && retrofitResponse.getData().getBeanList().size() == 0) {
//                            //mKnowAdapter.setAlwaysShowHead(true);
//                            mNewsAdapter.setData(retrofitResponse.getData().getBeanList());
//                            mRecyclerView.setAdapter(mNewsAdapter);
//                            return;
//                        }
//
//                        if (retrofitResponse.getData().getBeanList().size() != 0) {
//                            if (isLoadMore) {
//                                isLoadMore = false;
//                                mNewsAdapter.addData(retrofitResponse.getData().getBeanList());
//                            } else {
//                                mNewsAdapter.setData(retrofitResponse.getData().getBeanList());
//                                mRecyclerView.setAdapter(mNewsAdapter);
//                            }
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        ApiException apiException = CustomException.handleException(throwable);
//
//                    }
//                });
//        new CompositeDisposable().add(disposable);
//    }


    @Override
    public void setPresenter(Contract.Presenter presenter) {
        mPresenter = presenter;
    }
}
