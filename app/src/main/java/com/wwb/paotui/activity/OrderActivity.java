package com.wwb.paotui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wwb.paotui.R;
import com.wwb.paotui.adapter.OrderAdapter;
import com.wwb.paotui.bean.Order;
import com.wwb.paotui.fragment.ForumFragment;
import com.wwb.paotui.fragment.HomeFragment;
import com.wwb.paotui.fragment.MineFragment;
import com.wwb.paotui.fragment.RemoveOrderFragment;
import com.wwb.paotui.fragment.RunerOrderFragment;
import com.wwb.paotui.mvp.Contract;
import com.wwb.paotui.mvp.presenter.MvpPresenter;
import com.wwb.paotui.tools.PreferenceManager;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends AppCompatActivity {
    private Fragment[] mFragmensts;
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.runner)
    TextView runner;
    @BindView(R.id.remove)
    TextView remove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_order);
        ButterKnife.bind(this);
        initView();
    }

    private void addFragments() {
        mFragmensts = new Fragment[2];
        mFragmensts[0] = RunerOrderFragment.newInstance("");
        mFragmensts[1] = RemoveOrderFragment.newInstance("");
        getSupportFragmentManager().beginTransaction().add(R.id.orderFragment, mFragmensts[0]).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.orderFragment, mFragmensts[1]).commit();
    }

    private void showFragment(int index) {
        getSupportFragmentManager().beginTransaction().show(mFragmensts[index]).commit();
    }

    private void hideFragment(int index) {
        getSupportFragmentManager().beginTransaction().hide(mFragmensts[index]).commit();
    }


    private void initView() {
        addFragments();
        select(0);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(1);
            }
        });
        runner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(0);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void select(int position) {
        switch (position) {
            case 0:
                runner.setTextColor(getResources().getColor(R.color.yellow_1e));
                remove.setTextColor(getResources().getColor(R.color.tv_333));
                hideFragment(1);
                showFragment(0);
                break;
            case 1:
                remove.setTextColor(getResources().getColor(R.color.yellow_1e));
                runner.setTextColor(getResources().getColor(R.color.tv_333));
                hideFragment(0);
                showFragment(1);
                break;
            default:
                break;
        }
    }

}
