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

import com.payencai.library.adapter.OnItemClickListener;
import com.payencai.library.adapter.OnLoadMoreListener;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.NewsAdapter;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Data;
import com.weiwobang.paotui.bean.News;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class TypeActivity extends AppCompatActivity {
    @BindView(R.id.type_title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    RecyclerView mRecyclerView;
    NewsAdapter mNewsAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int page=1;
    boolean isLoadMore = false;
    String categoryId="";
    String categoryName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_type);
        ButterKnife.bind(this);
        initView();
        initNews();
        loadData();
    }
    private void initNews(){
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_new);
        mRecyclerView = findViewById(R.id.recycleview_new);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsAdapter = new NewsAdapter();
        //mKnowAdapter.addHeadLayout(R.layout.header_know);
        mNewsAdapter.openAutoLoadMore(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                //mNewsAdapter.openAutoLoadMore(true);
                loadData();
            }
        });
        mNewsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isLoadMore = true;
                page++;
                loadData();
            }
        });
        mNewsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int adapterPosition) {
                String id=mNewsAdapter.getData(adapterPosition).getId();
                Intent intent=new Intent(TypeActivity.this,DetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }
    private void loadData() {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getMsgByType(page,categoryId)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Data<News>>>() {
                    @Override
                    public void accept(RetrofitResponse<Data<News>> retrofitResponse) throws Exception {
                        Log.e("result",retrofitResponse.getData().getBeanList().get(0).getTitle());
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if(page==1&&retrofitResponse.getData().getBeanList().size()==0){
                            //mKnowAdapter.setAlwaysShowHead(true);
                            mNewsAdapter.setData(retrofitResponse.getData().getBeanList());
                            mRecyclerView.setAdapter(mNewsAdapter);
                            return;
                        }

                        if (retrofitResponse.getData().getBeanList().size() != 0) {
                            if (isLoadMore) {
                                isLoadMore = false;
                                mNewsAdapter.addData(retrofitResponse.getData().getBeanList());
                            } else {
                                mNewsAdapter.setData(retrofitResponse.getData().getBeanList());
                                mRecyclerView.setAdapter(mNewsAdapter);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);

                    }
                });
        new CompositeDisposable().add(disposable);
    }

    private void initView() {
        Bundle bundle=getIntent().getExtras();
        categoryId=bundle.getString("id");
        categoryName=bundle.getString("name");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText(categoryName);

    }
}
