package com.wwb.paotui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wwb.paotui.R;
import com.wwb.paotui.adapter.OrderAdapter;
import com.wwb.paotui.bean.Order;
import com.wwb.paotui.mvp.Contract;
import com.wwb.paotui.mvp.presenter.MvpPresenter;
import com.wwb.paotui.tools.PreferenceManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveOrderFragment extends Fragment implements Contract.MvpView<List<Order>>{

    @BindView(R.id.sr_order)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_order)
    RecyclerView mRecyclerView;
    OrderAdapter mOrderAdapter;
    MvpPresenter<List<Order>> mMvpPresenter;
    private int page = 1;
    private boolean isLoadMore = false;
    public RemoveOrderFragment() {
        // Required empty public constructor
    }

    public static RemoveOrderFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        RemoveOrderFragment fragment = new RemoveOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_remove_order, container, false);
        ButterKnife.bind(this,view);
        initAdapter();
        getData();
        return view;
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                isLoadMore = false;
                getData();
                //loadData();
            }
        });


    }



    private void getData() {
        mMvpPresenter = new MvpPresenter(this, PreferenceManager.getInstance().getUserinfo().getToken(), page);
        mMvpPresenter.getMyOrder();


    }
    @Override
    public void showLoading() {

    }
    @Override
    public void hideLoading() {

    }

    @Override
    public void showData(List<Order> data) {
        Log.e("page", data.get(0) + "");
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
            mOrderAdapter.setEnableLoadMore(true);
        }
        if (data.size() == 0) {
            if (isLoadMore) {
                Log.e("load", "type");
                //没有更多数据
                mOrderAdapter.loadMoreEnd();
            } else {
                mOrderAdapter.setNewData(data);
                mRecyclerView.setAdapter(mOrderAdapter);
            }
        } else {
            if (isLoadMore) {
                isLoadMore = false;
                mOrderAdapter.addData(data);
                mOrderAdapter.loadMoreComplete();
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
