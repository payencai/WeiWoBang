package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.NotifyAdapter;
import com.weiwobang.paotui.adapter.SectionAdapter;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.MySection;
import com.weiwobang.paotui.bean.Notify;
import com.weiwobang.paotui.bean.SellerOrder;
import com.weiwobang.paotui.tools.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class NotifyActivity extends AppCompatActivity {
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rf_notify)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_notify)
    RecyclerView mRecyclerView;
    private int page = 1;
    boolean isLoadMore = false;
    NotifyAdapter mNotifyAdapter;
    SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_notify);
        ButterKnife.bind(this);
        //createData();
        init();
    }

    private void agree(String id) {

        Disposable disposable = NetWorkManager.getReq(ApiService.class).agreeCancel(id, MyAPP.token)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(true);
                        }
                        try {
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("message");
                            int code = object.getInt("resultCode");
                            if (code == 0) {
                                ToastUtil.showToast(NotifyActivity.this, "已取消");
                                mOnRefreshListener.onRefresh();
                            } else {
                                ToastUtil.showToast(NotifyActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(NotifyActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);

    }

    private void init() {
        initAdapter();
        getNotify(page);
    }

    private void reject(String id) {
        Disposable disposable = NetWorkManager.getReq(ApiService.class).refuseCancel(id, MyAPP.token)
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
                                ToastUtil.showToast(NotifyActivity.this, "已拒绝");
                                //mSwipeRefreshLayout.setRefreshing(true);
                                mOnRefreshListener.onRefresh();
                            } else {
                                ToastUtil.showToast(NotifyActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(NotifyActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void initAdapter() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title.setText("通知");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                getNotify(page);
            }
        }, mRecyclerView);
        mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                // ToastUtil.showToast(getContext(),"刷新");
                getNotify(page);
            }
        };
        mNotifyAdapter.setOnItemClickListener(new NotifyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int code, String id) {
                if(code==0){
                    agree(id);
                }else{
                    reject(id);
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        // mRecyclerView.setAdapter(mSectionAdapter);
    }

    List<Notify> mNotifies = new ArrayList<>();

    private void createData() {
        Notify notify = new Notify();
        notify.setBusinessName("gfggf");
        notify.setBusinessTelnum("13202908144");
        notify.setBuyerName("lihg");
        notify.setBuyerTelnum("12343442");
        notify.setCommissionCalculate(10.32);
        notify.setCourierName("fdgf");
        notify.setCourierTelnum("2435454");
        notify.setDistance(143.32);
        notify.setCreateTime("2018-9-3 12:32:21");
        notify.setId("dfdf3r434");
        notify.setState("5");
        Notify.DeliverMapBean deliverMapBean = new Notify.DeliverMapBean();
        deliverMapBean.setAdress("高分的高分");
        deliverMapBean.setHeading("防对方的");
        deliverMapBean.setLatitude("111.32");
        deliverMapBean.setLongitude("34.21");
        notify.setDeliverMap(deliverMapBean);
        mNotifies.add(notify);
    }

    private void getNotify(int page) {
        Log.e("page", page + "");
        Disposable disposable = NetWorkManager.getReq(ApiService.class).getNotice(page, MyAPP.token)
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
                                ToastUtil.showToast(NotifyActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(NotifyActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }
}
