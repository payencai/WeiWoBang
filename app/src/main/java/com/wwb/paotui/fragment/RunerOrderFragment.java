package com.wwb.paotui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.wwb.paotui.MyAPP;
import com.wwb.paotui.R;
import com.wwb.paotui.activity.RunnerDetailActivity;
import com.wwb.paotui.adapter.OrderAdapter;
import com.wwb.paotui.adapter.RunnerAdapter;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.bean.Data;
import com.wwb.paotui.bean.News;
import com.wwb.paotui.bean.Operation;
import com.wwb.paotui.bean.Order;
import com.wwb.paotui.bean.RunnerModel;
import com.wwb.paotui.mvp.presenter.MvpPresenter;
import com.wwb.paotui.tools.GsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

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
public class RunerOrderFragment extends Fragment {
    @BindView(R.id.sr_order)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_order)
    RecyclerView mRecyclerView;
    RunnerAdapter mRunnerAdapter;
    private int page = 1;
    boolean isLoadMore = false;
    public static RunerOrderFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        RunerOrderFragment fragment = new RunerOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public RunerOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_runer_order, container, false);
        ButterKnife.bind(this,view);
        initView();
        getData();
        return view;
    }
    private void initView(){
      initAdapter();
    }
    private void getData(){
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getRunnerOrders(page, MyAPP.token2)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody retrofitResponse) throws Exception {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        JSONObject jsonObject = new JSONObject(retrofitResponse.string());
                        String msg = jsonObject.getString("message");
                        int code = jsonObject.getInt("resultCode");
                        if (code == 0) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray operation = data.getJSONArray("list");
                            List<RunnerModel> modelList = GsonUtils.jsonToArrayList(operation.toString(), RunnerModel.class);
                            if (isLoadMore) {
                                isLoadMore = false;
                                mRunnerAdapter.loadMoreComplete();
                                mRunnerAdapter.addData(modelList);
                                if ( modelList.size()== 0) {
                                    mRunnerAdapter.loadMoreEnd();
                                }
                            } else {
                                mRunnerAdapter.setNewData(modelList);
                                mRecyclerView.setAdapter(mRunnerAdapter);
                            }

                            Log.e("run",data.toString());
                        } else {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
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
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRunnerAdapter = new RunnerAdapter(R.layout.comp_chart_item);
        mRunnerAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
                //  mOrderAdapter.setEnableLoadMore(true);
                page++;
                getData();
            }
        });
        mRunnerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                RunnerModel runnerModel= (RunnerModel) adapter.getData().get(position);
                String id=runnerModel.getId();
                Intent intent=new Intent(getContext(), RunnerDetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isLoadMore = false;
                getData();
                //loadData();
            }
        });


    }
}
