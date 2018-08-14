package com.weiwobang.paotui.fragment;


import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.activity.DetailActivity;
import com.weiwobang.paotui.activity.LoginActivity;
import com.weiwobang.paotui.activity.PublishActivity;
import com.weiwobang.paotui.activity.RegisterActivity;
import com.weiwobang.paotui.activity.SearchActivity;
import com.weiwobang.paotui.activity.TypeActivity;
import com.weiwobang.paotui.adapter.NewsAdapter;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Data;
import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.bean.Order;
import com.weiwobang.paotui.mvp.Contract;
import com.weiwobang.paotui.mvp.presenter.MvpPresenter;
import com.weiwobang.paotui.tools.PreferenceManager;
import com.weiwobang.paotui.view.CommonPopupWindow;
import com.weiwobang.paotui.view.MyDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment implements Contract.MvpView<List<News>> {
    @BindView(R.id.search_layout)
    LinearLayout search;
    @BindView(R.id.second_layout)
    LinearLayout second;
    @BindView(R.id.work_layout)
    LinearLayout work;
    @BindView(R.id.clean_layout)
    LinearLayout clean;
    @BindView(R.id.sell_layout)
    LinearLayout sell;
    @BindView(R.id.update_layout)
    LinearLayout update;
    @BindView(R.id.shop_layout)
    LinearLayout shop;
    @BindView(R.id.jieyang_layout)
    LinearLayout jieyang;
    @BindView(R.id.head)
    RelativeLayout head;

    @BindView(R.id.header_confirm)
    TextView confirm;
    @BindView(R.id.layout_search)
    LinearLayout search_layout;
    RecyclerView mRecyclerView;
    NewsAdapter mNewsAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MvpPresenter<List<News>> mMvpPresenter;
    private int page = 1;
    boolean isLoadMore = false;

    public ForumFragment() {
        // Required empty public constructor
    }

    public static ForumFragment newInstance(String from) {

        Bundle args = new Bundle();
        args.putString("from", from);
        ForumFragment fragment = new ForumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        String from = args.getString("from");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wwb_forum_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        initNews(view);
        getData();

        return view;
    }

    private void initNews(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_new);
        mRecyclerView = view.findViewById(R.id.recycleview_new);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsAdapter = new NewsAdapter(R.layout.wwb_item_forum);
        //  mNewsAdapter.setPreLoadNumber(1);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mNewsAdapter.setEnableLoadMore(true);
                isLoadMore = false;
                getData();
            }
        });
        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        mNewsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
                page++;
                Log.e("page", page + "");
                getData();
            }
        }, mRecyclerView);

        mNewsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String id = mNewsAdapter.getItem(position).getId();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        mMvpPresenter = new MvpPresenter(this, "", page);
        mMvpPresenter.getTodayNews();
    }

    private void init() {


        confirm.setText("发布");
        confirm.setTextColor(getResources().getColor(R.color.black_33));
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "1");
                bundle.putString("name", "寻人寻物");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "2");
                bundle.putString("name", "二手物品");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "3");
                bundle.putString("name", "工作兼职");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "4");
                bundle.putString("name", "家政保洁");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "5");
                bundle.putString("name", "房屋租售");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "6");
                bundle.putString("name", "房屋装修");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "7");
                bundle.putString("name", "店铺租赁");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        jieyang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TypeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "8");
                bundle.putString("name", "揭阳杂谈");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAPP.isLogin) {
                    showDialog();
                    // startActivity(new Intent(getActivity(), PublishActivity.class));
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });

    }

    private CommonPopupWindow popupWindow;

    //向下弹出
    public void showDownPop(View view) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        popupWindow = new CommonPopupWindow.Builder(getContext())
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
                                Intent intent = new Intent(getActivity(), PublishActivity.class);
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
                                Intent intent = new Intent(getActivity(), PublishActivity.class);
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
                                Intent intent = new Intent(getActivity(), PublishActivity.class);
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
                                Intent intent = new Intent(getActivity(), PublishActivity.class);
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
                                Intent intent = new Intent(getActivity(), PublishActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", "5");
                                bundle.putString("name", "房屋租售");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.update_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(getActivity(), PublishActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("id", "6");
                                bundle.putString("name", "房屋装修");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.shop_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent = new Intent(getActivity(), PublishActivity.class);
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
                                Intent intent = new Intent(getActivity(), PublishActivity.class);
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

    private void showDialog() {
        showDownPop(head);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showData(List<News> data) {
        Log.e("time", page + "");
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            mNewsAdapter.setEnableLoadMore(true);
        }
        if (data == null) {
            mNewsAdapter.loadMoreEnd();
            //mNewsAdapter.setEnableLoadMore(false);
        }
        if (isLoadMore) {
            isLoadMore = false;
            mNewsAdapter.loadMoreComplete();
            mNewsAdapter.addData(data);

        } else {
            mNewsAdapter.setNewData(data);
            mRecyclerView.setAdapter(mNewsAdapter);
        }


    }

    @Override
    public void failed(String error) {

    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {

    }

    private void getData() {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getToday(page)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Data<News>>>() {
                    @Override
                    public void accept(RetrofitResponse<Data<News>> retrofitResponse) throws Exception {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        if (isLoadMore) {
                            isLoadMore = false;
                            mNewsAdapter.loadMoreComplete();
                            mNewsAdapter.addData(retrofitResponse.getData().getBeanList());
                            if(retrofitResponse.getData().getBeanList().size()==0){
                                Log.e("empty","empty");
                                mNewsAdapter.loadMoreEnd();

                            }
                            //mRecyclerView.setAdapter(mOrderAdapter);
                        } else {
                            mNewsAdapter.setNewData(retrofitResponse.getData().getBeanList());
                            mRecyclerView.setAdapter(mNewsAdapter);
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        //mMvpCallback.loadError(apiException.getDisplayMessage());
                    }
                });
        new CompositeDisposable().add(disposable);
    }
}
