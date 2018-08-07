package com.weiwobang.paotui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.OrderAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity {
    @BindView(R.id.sr_order)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_order)
    RecyclerView mRecyclerView;
    OrderAdapter mOrderAdapter;
    @BindView(R.id.back)
    ImageView back;
    private int page;
    private boolean isRefresh=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_order);
        ButterKnife.bind(this);
        initView();
        getData();
    }
    private void  initAdapter(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mOrderAdapter=new OrderAdapter(R.layout.wwb_item_order);

    }
    private void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void getData(){

    }
}
