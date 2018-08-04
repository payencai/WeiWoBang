package com.weiwobang.paotui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.payencai.library.adapter.OnItemClickListener;
import com.payencai.library.adapter.OnLoadMoreListener;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.CommentAdapter;
import com.weiwobang.paotui.adapter.InformAdapter;
import com.weiwobang.paotui.adapter.NewsAdapter;
import com.weiwobang.paotui.api.Api;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Bean;
import com.weiwobang.paotui.bean.Comment;
import com.weiwobang.paotui.bean.Data;
import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.bean.Reply;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.PreferenceManager;
import com.weiwobang.paotui.view.FullyLinearLayoutManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DetailActivity extends AppCompatActivity {
    String id = "";
    @BindView(R.id.detail_title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.comment_input)
    EditText input;
    @BindView(R.id.comment)
    TextView comment;
    @BindView(R.id.layout_contact)
    LinearLayout layout_contact;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.read)
    TextView read;
    @BindView(R.id.liuyan)
    TextView liuyan;
    @BindView(R.id.detail_time)
    TextView time;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tv_inform)
    TextView tv_inform;
    RecyclerView mRecyclerView;
    CommentAdapter mCommentAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int page = 1;
    boolean isLoadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_detail);
        ButterKnife.bind(this);
        ActManager.getAppManager().addActivity(this);
        initNews();
        initView();

    }

    String contentId = "";
    List<String> selected = new ArrayList<>();

    private void initNews() {
        // mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_new);
        mRecyclerView = findViewById(R.id.recycleview_new);
        // mRecyclerView.s(true);
        // mRecyclerView.setHasFixedSize(true);
        FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mCommentAdapter = new CommentAdapter();
        //mKnowAdapter.addHeadLayout(R.layout.header_know);
        mCommentAdapter.openAutoLoadMore(false);
        // mRecyclerView.setAdapter(mCommentAdapter);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                page = 1;
//                //mNewsAdapter.openAutoLoadMore(true);
//                getComment(id,page);
//            }
//        });
        mCommentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int adapterPosition) {
                Comment comment=mCommentAdapter.getData(adapterPosition);
                Intent intent=new Intent(DetailActivity.this,ReplyActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("comment",comment);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mCommentAdapter.setOnDelListener(new CommentAdapter.onDelListener() {
            @Override
            public void onClick(int index) {
                contentId = mCommentAdapter.getData(index).getId();
                if (MyAPP.isLogin)
                    showCommentDialog(contentId);
                else
                    startActivity(new Intent(DetailActivity.this, LoginActivity.class));
            }
        });
        mCommentAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isLoadMore = true;
                page++;
                getComment(id, page);
                // loadData();
            }
        });


    }

    List<String> commentlist = new ArrayList<>();

    private void submitCommentInform(List<String> content, String id) {
        Disposable disposable = null;
        String inform="";
        for(String msg:content){
            inform=inform+msg+",";
        }
        try {
            disposable = NetWorkManager.getRequest(ApiService.class).postInformComment(id, inform,PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse>() {
                        @Override
                        public void accept(RetrofitResponse retrofitResponse) throws Exception {
                           Toast.makeText(DetailActivity.this, "举报成功", Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);
                            //Toast.makeText(PublishActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new CompositeDisposable().add(disposable);
    }

    private void showCommentDialog(String id) {
        List<String> data = new ArrayList<>();
        data.add("内容违法法律法规");
        data.add("泄露他人隐私信息");
        data.add("辱骂、中伤、诽谤他人");
        data.add("存在色情低俗等不适内容");
        data.add("宣传虚假信息，传销信息等内容");

        final boolean[] flag = {false};
        final Dialog dialog = new Dialog(DetailActivity.this, R.style.cdialog);
        View dialogView = LayoutInflater.from(DetailActivity.this).inflate(R.layout.wwb_dialog_report, null);
        //获得dialog的window窗口
        RecyclerView recyclerView = dialogView.findViewById(R.id.recy_inform);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InformAdapter informAdapter = new InformAdapter();
        informAdapter.openAutoLoadMore(false);
        informAdapter.setData(data);
        recyclerView.setAdapter(informAdapter);
        informAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int adapterPosition) {
                ImageView sel = view.findViewById(R.id.sel);
                String value = informAdapter.getData(adapterPosition);
                sel.setVisibility(View.VISIBLE);
                commentlist.add(value);
                view.setEnabled(false);
            }
        });
        dialogView.findViewById(R.id.inform_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                submitCommentInform(commentlist, id);
            }
        });
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);

        dialog.show();
    }


    private void loadData() {
        getComment(id, page);
    }

    private void showInformDialog() {
        List<String> data = new ArrayList<>();
        data.add("内容违法法律法规");
        data.add("泄露他人隐私信息");
        data.add("辱骂、中伤、诽谤他人");
        data.add("存在色情低俗等不适内容");
        data.add("宣传虚假信息，传销信息等内容");

        final boolean[] flag = {false};
        final Dialog dialog = new Dialog(DetailActivity.this, R.style.cdialog);
        View dialogView = LayoutInflater.from(DetailActivity.this).inflate(R.layout.wwb_dialog_report, null);
        //获得dialog的window窗口
        RecyclerView recyclerView = dialogView.findViewById(R.id.recy_inform);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        InformAdapter informAdapter = new InformAdapter();
        informAdapter.openAutoLoadMore(false);
        informAdapter.setData(data);
        recyclerView.setAdapter(informAdapter);
        informAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull View view, int adapterPosition) {
                ImageView sel = view.findViewById(R.id.sel);
                String value = informAdapter.getData(adapterPosition);
                sel.setVisibility(View.VISIBLE);
                selected.add(value);
                view.setEnabled(false);
            }
        });
        dialogView.findViewById(R.id.inform_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                submitInform(selected);
            }
        });
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);

        dialog.show();
    }

    private void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        liuyan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAPP.isLogin)
                    postComment(input.getEditableText().toString());
                else {
                    startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                }
            }
        });
        tv_inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInformDialog();
            }
        });
        id = getIntent().getExtras().getString("id");
        //id="25359817-9353-4494-8502-ee2f8e50f337";
        getComment(id, page);
        // Log.e("id", id);
        getDetail(id);

        //getComment(id,page);
    }
    List<Reply> mReplies=new ArrayList<>();
    List<Comment> mComments = new ArrayList<>();
   // List<Comment> mNewComments=new ArrayList<>();
    private void getComment(String id, int page) {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getComment(id, page)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Bean>>() {
                    @Override
                    public void accept(RetrofitResponse<Bean> bean) throws Exception {
                        mComments = bean.getData().getComment().getComments();
                        Bean.ReplyUserInfo replyUserInfo=bean.getData().getReplyUserInfo();
                        for(int i=0;i<mComments.size(); i++){
                             Comment comment=mComments.get(i);
                             mReplies=comment.getReplyList();
                             for(int j=0;j<mReplies.size();j++){
                                 Reply reply=mReplies.get(j);
                                 reply.setName(replyUserInfo.getNickname());
                                 reply.setUrl(replyUserInfo.getHeadUri());
                                 mReplies.set(j,reply);
                             }
                             comment.setReplyList(mReplies);
                             mComments.set(i,comment);
//                            if(comment.getReplyList().size()>0){
//                                c
//                            }
                        }
                        Log.e("m", mComments.get(0).getCommentContent());
//                        if (mSwipeRefreshLayout.isRefreshing()) {
//                            mSwipeRefreshLayout.setRefreshing(false);
//                        }
                        if (page == 1 && mComments.size() == 0) {
                            //mKnowAdapter.setAlwaysShowHead(true);
                            mCommentAdapter.setData(mComments);
                            mRecyclerView.setAdapter(mCommentAdapter);
                            return;
                        }

                        if (mComments.size() != 0) {
                            if (isLoadMore) {
                                isLoadMore = false;
                                mCommentAdapter.addData(mComments);
                            } else {
                               // Toast.makeText(DetailActivity.this, "获取信息成功", Toast.LENGTH_SHORT).show();
                                mCommentAdapter.setData(mComments);

                                mRecyclerView.setAdapter(mCommentAdapter);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        //Toast.makeText(DetailActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }

    private void submitInform(List<String> data) {
        String content = "";
        for (String str : data) {
            content = content + str + ",";
        }
        Disposable disposable = null;
        try {
            disposable = NetWorkManager.getRequest(ApiService.class).postInformMsg(id, content, PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse>() {
                        @Override
                        public void accept(RetrofitResponse retrofitResponse) throws Exception {

                            Toast.makeText(DetailActivity.this, "举报成功", Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);
                            //Toast.makeText(DetailActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new CompositeDisposable().add(disposable);
        // Toast.makeText(DetailActivity.this, "举报成功"+data.size(), Toast.LENGTH_SHORT).show();
    }

    private void postComment(String content) {
        Disposable disposable = null;
        try {
            disposable = NetWorkManager.getRequest(ApiService.class).postAddComment(id, content, PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse>() {
                        @Override
                        public void accept(RetrofitResponse retrofitResponse) throws Exception {

                            Toast.makeText(DetailActivity.this, "留言成功", Toast.LENGTH_SHORT).show();
                            getComment(id, page);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);
                            //Toast.makeText(DetailActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new CompositeDisposable().add(disposable);
    }

    private void getDetail(String id) {

        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getDetail(id)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<News>>() {
                    @Override
                    public void accept(RetrofitResponse<News> retrofitResponse) throws Exception {
                        News news = retrofitResponse.getData();
                        //2018-08-01 11
                        String t = news.getCreateTime().substring(5, 7)
                                + "月" + news.getCreateTime().substring(8, 10) + "日 "
                                + news.getCreateTime().substring(11, 13) + ":" + news.getCreateTime().substring(14, 16);
                        ;
                        time.setText(t);
                        contact.setText("联系人     " + news.getLinkman());
                        phone.setText("联系方式    " + news.getContactInfomation());
                        title.setText(news.getTitle());
                        content.setText(news.getContent() + "");
                        read.setText(news.getReadNum() + "");
                        comment.setText(news.getCommentNum() + "");
                        if (TextUtils.isEmpty(news.getLinkman()) && TextUtils.isEmpty(news.getContactInfomation())) {
                            layout_contact.setVisibility(View.GONE);
                        }
                        Glide.with(DetailActivity.this).load(news.getImage1Uri()).into(image);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        //Toast.makeText(DetailActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
        new CompositeDisposable().add(disposable);
    }
}
