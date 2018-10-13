package com.wwb.paotui.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wwb.paotui.R;
import com.wwb.paotui.adapter.NewsAdapter;
import com.wwb.paotui.bean.News;
import com.wwb.paotui.mvp.Contract;
import com.wwb.paotui.mvp.presenter.MvpPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchresActivity extends AppCompatActivity implements Contract.MvpView<List<News>> {
    private int page = 1;
    private boolean isLoadMore = false;
    private String content;
    @BindView(R.id.cancel)
    ImageView back;
    @BindView(R.id.content)
    EditText et_content;
    @BindView(R.id.rv_result)
    RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    private MvpPresenter<List<News>> mMvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_searchres);
        ButterKnife.bind(this);
        initView();
        initNews();
        getData();
    }

    private void initNews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsAdapter = new NewsAdapter(R.layout.wwb_item_forum);

        mNewsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
                page++;
                getData();
            }
        });

        mNewsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String id = mNewsAdapter.getItem(position).getId();
                Intent intent = new Intent(SearchresActivity.this, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void getData() {
        mMvpPresenter = new MvpPresenter(this);
        mMvpPresenter.getSearchResult(page, content);
    }

    private void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        content = getIntent().getExtras().getString("content");
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                page = 1;
                content = charSequence.toString();
                getData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showData(List<News> data) {
        if (data.isEmpty()) {
            if (isLoadMore) {
                mNewsAdapter.loadMoreEnd();
            } else {
                //查询不到数据
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
