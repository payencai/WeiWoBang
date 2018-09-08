package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.payencai.library.adapter.OnItemClickListener;
import com.payencai.library.adapter.OnLoadMoreListener;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.NewsAdapter;
import com.weiwobang.paotui.adapter.ReplyAdapter;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Comment;
import com.weiwobang.paotui.tools.PreferenceManager;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ReplyActivity extends AppCompatActivity {
    @BindView(R.id.comment_input)
    EditText input;
    @BindView(R.id.reply)
    TextView reply;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.tv_reply)
    TextView tv_content;
    @BindView(R.id.comm_time)
    TextView time;
    RecyclerView mRecyclerView;
    ReplyAdapter mReplyAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int page = 1;
    boolean isLoadMore = false;
    Comment mComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_reply);
        ButterKnife.bind(this);
        initView();
        initNews();
        getReply();
    }

    private void initView() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mComment = (Comment) getIntent().getExtras().getSerializable("comment");
        nickname.setText(mComment.getCommentUserNickname());
        tv_content.setText(mComment.getCommentContent());
        time.setText(mComment.getCommentTime());
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!mComment.getCommentUserId().equals(PreferenceManager.getInstance().getUserinfo().getId())) {
                        Toast.makeText(ReplyActivity.this, "抱歉！，只支持回复自己发布的消息的评论。", Toast.LENGTH_LONG).show();
                    } else {
                        postReply();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void postReply() {
        Disposable disposable = null;
        try {
            disposable = NetWorkManager.getRequest(ApiService.class).postReplyComment(mComment.getId(), input.getEditableText().toString(), PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse>() {
                        @Override
                        public void accept(RetrofitResponse retrofitResponse) throws Exception {
                            Toast.makeText(ReplyActivity.this, "回复成功", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);
                            //Toast.makeText(RegisterActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new CompositeDisposable().add(disposable);
    }

    private void initNews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_new);
        mRecyclerView = findViewById(R.id.recycleview_new);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReplyAdapter = new ReplyAdapter();
        //mKnowAdapter.addHeadLayout(R.layout.header_know);
        mReplyAdapter.openAutoLoadMore(false);

        mReplyAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isLoadMore = true;
                page++;
                getReply();
            }
        });
        mReplyAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int adapterPosition) {


            }
        });
    }

    private void getReply() {
        mReplyAdapter.setData(mComment.getReplyList());
        mRecyclerView.setAdapter(mReplyAdapter);
    }
}
