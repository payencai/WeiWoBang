package com.weiwobang.paotui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.donkingliang.imageselector.utils.DateUtils;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.TimeUtil;
import com.payencai.library.util.ToastUtil;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.activity.NotifyActivity;
import com.weiwobang.paotui.activity.OrderdelActivity;
import com.weiwobang.paotui.adapter.NotifyAdapter;
import com.weiwobang.paotui.adapter.SectionAdapter;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.FinishOrder;
import com.weiwobang.paotui.bean.MySection;
import com.weiwobang.paotui.bean.Notify;
import com.weiwobang.paotui.bean.SellerOrder;
import com.weiwobang.paotui.tools.GsonUtils;

import org.feezu.liuli.timeselector.Utils.DateUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    @BindView(R.id.rf_history)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_history)
    RecyclerView mRecyclerView;
    SectionAdapter mSectionAdapter;
    List<MySection> mMySections = new ArrayList<>();
    private int page = 1;
    boolean isLoadMore = false;
    NotifyAdapter mNotifyAdapter;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNotifyAdapter = new NotifyAdapter(R.layout.comp_seller_chart_item);
        mNotifyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        mNotifyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore = true;
                getData();
            }
        }, mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                // ToastUtil.showToast(getContext(),"刷新");
                getData();
            }
        });
        // mRecyclerView.setAdapter(mSectionAdapter);
    }

    private void getData() {
        Log.e("page", page + "");
        Disposable disposable = NetWorkManager.getReq(ApiService.class).getFinished(page, MyAPP.token)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();

                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        try {
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("message");
                            int code = object.getInt("resultCode");
                            if (code == 0) {

                                //ToastUtil.showToast("下单成功");
                                Log.e("notify", result.toString());
                                object = object.getJSONObject("data");
                                JSONArray data = object.getJSONArray("list");
                                List<Notify> notifyList = GsonUtils.jsonToArrayList(data.toString(), Notify.class);
                                if (notifyList.size() == 0) {
                                    if (isLoadMore) {
                                        isLoadMore = false;
                                        mNotifyAdapter.loadMoreEnd(true);
                                    } else {

                                    }
                                } else {
                                    if (isLoadMore) {
                                        isLoadMore = false;
                                        mNotifyAdapter.addData(notifyList);
                                        mNotifyAdapter.loadMoreComplete();
                                    } else {
                                        mNotifyAdapter.setNewData(notifyList);
                                        mRecyclerView.setAdapter(mNotifyAdapter);

                                    }
                                }
                            } else {
                                ToastUtil.showToast(getActivity(), msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(getContext(), apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSectionAdapter = new SectionAdapter(R.layout.comp_chart_item, R.layout.wwb_section_order, mMySections);
//        View view = getLayoutInflater().inflate(R.layout.wwb_empty_header, null);
//        mSectionAdapter.addHeaderView(view);
        mSectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MySection mySection = (MySection) adapter.getData().get(position);
                if (!mySection.isHeader) {
                    MySection section = (MySection) adapter.getItem(position);
                    Intent intent = new Intent(getContext(), OrderdelActivity.class);
                    intent.putExtra("id", section.t.getId());
                    startActivityForResult(intent, 1);
                }

            }
        });
        mSectionAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                isLoadMore = true;
                getHistory(page);
            }
        }, mRecyclerView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getHistory(page);
            }
        });
        mRecyclerView.setAdapter(mSectionAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.wwb_fragment_history, container, false);
        ButterKnife.bind(this, view);
        initAdapter();
        //initData();
        getHistory(page);
        //getData();
        return view;
    }

    private void getHistory(int page) {
        Log.e("page", page + "");
        Disposable disposable = NetWorkManager.getReq(ApiService.class).getFinished(page, MyAPP.token)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        String result = responseBody.string();
                        try {
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("message");
                            int code = object.getInt("resultCode");
                            if (code == 0) {
                                object = object.getJSONObject("data");
                                JSONArray data = object.getJSONArray("list");
                                Log.e("page", result.toString());
                                List<FinishOrder> finishOrders = GsonUtils.jsonToArrayList(data.toString(), FinishOrder.class);
                                if (finishOrders.size() == 0) {
                                    if (isLoadMore) {
                                        isLoadMore = false;
                                        mSectionAdapter.loadMoreEnd(true);
                                    }
                                } else {
                                    String flagDate = "";
                                    List<MySection> mMySections = new ArrayList<>();
                                    int len = mSectionAdapter.getData().size();
                                    if (len > 0) {
                                        //有数据是加载更多,和上一页最后的一条数据比较
                                        flagDate = mSectionAdapter.getData().get(len - 1).t.getCreateTime().substring(5, 10);
                                    }
                                    for (int i = 0; i < finishOrders.size(); i++) {
                                        FinishOrder finishOrder = finishOrders.get(i);
                                        String currDate = finishOrder.getCreateTime().substring(5, 10);
                                        if (!TextUtils.equals(flagDate, currDate))
                                        {
                                            flagDate = finishOrders.get(i).getCreateTime().substring(5, 10);
                                            mMySections.add(new MySection(true, flagDate));
                                        }
                                        mMySections.add(new MySection(finishOrder));
                                    }
                                    if (isLoadMore) {

                                        isLoadMore = false;
                                        mSectionAdapter.addData(mMySections);
                                        mSectionAdapter.loadMoreComplete();
                                    } else {
                                        mSectionAdapter.setNewData(mMySections);
                                    }
                                }

                            } else {
                                ToastUtil.showToast(getContext(), msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(getContext(), apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

}
