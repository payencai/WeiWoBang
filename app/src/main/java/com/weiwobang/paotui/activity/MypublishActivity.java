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
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.payencai.library.adapter.OnItemClickListener;
import com.payencai.library.adapter.OnLoadMoreListener;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.MyNewsAdapter;
import com.weiwobang.paotui.adapter.NewsAdapter;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Data;
import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.bean.Order;
import com.weiwobang.paotui.mvp.Contract;
import com.weiwobang.paotui.mvp.presenter.MvpPresenter;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.PreferenceManager;
import com.weiwobang.paotui.view.CommonPopupWindow;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MypublishActivity extends AppCompatActivity implements Contract.MvpView<List<News>> {
    CommonPopupWindow popupWindow;
    RecyclerView mRecyclerView;
    MyNewsAdapter mNewsAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int page = 1;
    boolean isLoadMore = false;
    MvpPresenter<List<News>> mMvpPresenter;
    @BindView(R.id.fabu)
    TextView fabu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_mypublish);
        ButterKnife.bind(this);
        ActManager.getAppManager().addActivity(this);
        initNews();
        getData();
        // loadData();
    }

    private void getData() {
        try {
            mMvpPresenter = new MvpPresenter(this, PreferenceManager.getInstance().getUserinfo().getToken(), page);
            mMvpPresenter.getMyPublish();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initNews() {


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDownPop(view);
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_new);
        mRecyclerView = findViewById(R.id.recycleview_new);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsAdapter = new MyNewsAdapter(R.layout.wwb_item_publish);
        //  mNewsAdapter.openAutoLoadMore(false);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isLoadMore = false;
                getData();
            }
        });
        mNewsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
                page++;
                getData();
            }
        });
        mNewsAdapter.setOnDelListener(new MyNewsAdapter.onDelListener() {
            @Override
            public void onClick(String id, int index) {
                delMsg(id);

            }

            @Override
            public void onEdit(String id, int name) {
                Intent intent = new Intent(MypublishActivity.this, PublishActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("news", mNewsAdapter.getItem(name));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    //向下弹出
    public void showDownPop(View view) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.wwb_dialog_fabu)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.AnimDown)
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        view.findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                            }
                        });
                        view.findViewById(R.id.search_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(MypublishActivity.this, PublishActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", "1");
                                bundle.putString("name", "寻人寻物");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.second_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(MypublishActivity.this, PublishActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", "2");
                                bundle.putString("name", "二手物品");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.work_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(MypublishActivity.this, PublishActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", "3");
                                bundle.putString("name", "工作兼职");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.clean_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(MypublishActivity.this, PublishActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", "4");
                                bundle.putString("name", "家政保洁");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.sell_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(MypublishActivity.this, PublishActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", "6");
                                bundle.putString("name", "房屋装修");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.update_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(MypublishActivity.this, PublishActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", "5");
                                bundle.putString("name", "房屋租售");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.shop_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(MypublishActivity.this, PublishActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", "7");
                                bundle.putString("name", "店铺租赁");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.jieyang_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(MypublishActivity.this, PublishActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", "8");
                                bundle.putString("name", "揭阳杂谈");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });

                    }
                })
                .setOutsideTouchable(true)
                .create();
        popupWindow.showAsDropDown(view);
        //得到button的左上角坐标
//        int[] positions = new int[2];
//        view.getLocationOnScreen(positions);
//        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.NO_GRAVITY, 0, positions[1] + view.getHeight());
    }

    private void delMsg(String id) {
        Disposable disposable = null;
        try {
            disposable = NetWorkManager.getRequest(ApiService.class).postDelMsg(id, PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse>() {
                        @Override
                        public void accept(RetrofitResponse retrofitResponse) throws Exception {
                            Toast.makeText(MypublishActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                            getData();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new CompositeDisposable().add(disposable);
    }

//    private void loadData() {
//        Disposable disposable = null;
//        try {
//            disposable = NetWorkManager.getRequest(ApiService.class).getMine(page, PreferenceManager.getInstance().getUserinfo().getToken())
//                    //.compose(ResponseTransformer.handleResult())
//                    .compose(SchedulerProvider.getInstance().applySchedulers())
//                    .subscribe(new Consumer<RetrofitResponse<Data<News>>>() {
//                        @Override
//                        public void accept(RetrofitResponse<Data<News>> retrofitResponse) throws Exception {
//                            Log.e("result", retrofitResponse.getData().getBeanList().get(0).getTitle());
//                            if (mSwipeRefreshLayout.isRefreshing()) {
//                                mSwipeRefreshLayout.setRefreshing(false);
//                            }
//                            if (page == 1 && retrofitResponse.getData().getBeanList().size() == 0) {
//                                //mKnowAdapter.setAlwaysShowHead(true);
//                                mNewsAdapter.setData(retrofitResponse.getData().getBeanList());
//                                mRecyclerView.setAdapter(mNewsAdapter);
//                                return;
//                            }
//
//                            if (retrofitResponse.getData().getBeanList().size() != 0) {
//                                if (isLoadMore) {
//                                    isLoadMore = false;
//                                    mNewsAdapter.addData(retrofitResponse.getData().getBeanList());
//                                } else {
//                                    mNewsAdapter.setData(retrofitResponse.getData().getBeanList());
//                                    mRecyclerView.setAdapter(mNewsAdapter);
//                                }
//                            }
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            ApiException apiException = CustomException.handleException(throwable);
//
//                        }
//                    });
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        new CompositeDisposable().add(disposable);
//    }

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
               // Log.e("load", "type");
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

    @Override
    public void setPresenter(Contract.Presenter presenter) {

    }
}
