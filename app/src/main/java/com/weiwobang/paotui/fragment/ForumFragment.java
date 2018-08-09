package com.weiwobang.paotui.fragment;


import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.weiwobang.paotui.activity.TypeActivity;
import com.weiwobang.paotui.adapter.NewsAdapter;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Data;
import com.weiwobang.paotui.bean.News;
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
public class ForumFragment extends Fragment {
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
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.header_confirm)
    TextView confirm;
    @BindView(R.id.iv_back)
    ImageView back;
    RecyclerView mRecyclerView;
    NewsAdapter mNewsAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int page=1;
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
        String from=args.getString("from");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wwb_forum_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        initNews(view);
        loadData();
        // Inflate the layout for this fragment
        return view;
    }
    private void initNews(View view){
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_new);
        mRecyclerView = view.findViewById(R.id.recycleview_new);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                Intent intent=new Intent(getActivity(),DetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    private void loadData(){
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getToday(page)
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
    private void init() {
        back.setVisibility(View.GONE);
        title.setText("同城信息");
        title.setTextColor(getResources().getColor(R.color.color_333));
        confirm.setText("发布");
        confirm.setTextColor(getResources().getColor(R.color.black_33));
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TypeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id","1");
                bundle.putString("name","寻人寻物");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TypeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id","2");
                bundle.putString("name","二手物品");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TypeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id","3");
                bundle.putString("name","工作兼职");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TypeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id","4");
                bundle.putString("name","家政保洁");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TypeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id","5");
                bundle.putString("name","房屋租售");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TypeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id","6");
                bundle.putString("name","房屋装修");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TypeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id","7");
                bundle.putString("name","店铺租赁");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        jieyang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),TypeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id","8");
                bundle.putString("name","揭阳杂谈");
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
                                Intent intent=new Intent(getActivity(),PublishActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id","1");
                                bundle.putString("name","寻人寻物");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.second_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent=new Intent(getActivity(),PublishActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id","2");
                                bundle.putString("name","二手物品");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.work_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent=new Intent(getActivity(),PublishActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id","3");
                                bundle.putString("name","工作兼职");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.clean_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent=new Intent(getActivity(),PublishActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id","4");
                                bundle.putString("name","家政保洁");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.sell_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent=new Intent(getActivity(),PublishActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id","5");
                                bundle.putString("name","房屋租售");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.update_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent=new Intent(getActivity(),PublishActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id","6");
                                bundle.putString("name","房屋装修");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.shop_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent=new Intent(getActivity(),PublishActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id","7");
                                bundle.putString("name","店铺租赁");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        view.findViewById(R.id.jieyang_layout).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                Intent intent=new Intent(getActivity(),PublishActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("id","8");
                                bundle.putString("name","揭阳杂谈");
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

    /**
     * 判断是否登录，如果没有登录跳转到登录界面
     * @return
     */
    public boolean isLogin() {
        if (true) {

        }
        return false;
    }
}
