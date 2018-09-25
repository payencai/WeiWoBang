package com.weiwobang.paotui.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.DeadObjectException;
import android.provider.MediaStore;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.payencai.library.adapter.OnItemClickListener;
import com.payencai.library.adapter.OnLoadMoreListener;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.CommentAdapter;
import com.weiwobang.paotui.adapter.InformAdapter;
import com.weiwobang.paotui.adapter.NewsAdapter;
import com.weiwobang.paotui.adapter.PhotoAdapter;
import com.weiwobang.paotui.api.Api;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Bean;
import com.weiwobang.paotui.bean.Comment;
import com.weiwobang.paotui.bean.Data;
import com.weiwobang.paotui.bean.MediaUrl;
import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.bean.Reply;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.PreferenceManager;
import com.weiwobang.paotui.tools.VideoUtil;
import com.weiwobang.paotui.view.FullyLinearLayoutManager;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @BindView(R.id.tv_inform)
    TextView tv_inform;
    @BindView(R.id.rv_photo)
    RecyclerView mRvPhoto;
    RecyclerView mRecyclerView;
    String contentId = "";
    List<String> selected = new ArrayList<>();
    CommentAdapter mCommentAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private int page = 1;
    boolean isLoadMore = false;
    PhotoAdapter mPhotoAdapter;
    List<Reply> mReplies = new ArrayList<>();
    List<Comment> mComments = new ArrayList<>();

    List<String> commentlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_detail);
        ButterKnife.bind(this);
        ActManager.getAppManager().addActivity(this);
        initNews();
        initView();

    }


    private void initNews() {
        mRecyclerView = findViewById(R.id.recycleview_new);
        FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(this);
        FullyLinearLayoutManager mLayoutManager2 = new FullyLinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager2);
        mRvPhoto.setLayoutManager(mLayoutManager);
        mPhotoAdapter = new PhotoAdapter(R.layout.wwb_item_img);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRvPhoto.setNestedScrollingEnabled(false);
        mCommentAdapter = new CommentAdapter(R.layout.wwb_item_comment);
        mCommentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Comment comment = mCommentAdapter.getItem(position);
                Intent intent = new Intent(DetailActivity.this, ReplyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("comment", comment);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mCommentAdapter.setOnDelListener(new CommentAdapter.onDelListener() {
            @Override
            public void onClick(int index) {
                contentId = mCommentAdapter.getItem(index).getId();
                if (MyAPP.isLogin)
                    showCommentDialog(contentId);
                else
                    startActivity(new Intent(DetailActivity.this, LoginActivity.class));
            }
        });
        mCommentAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                isLoadMore = true;
                page++;
                Log.e("comm", page + "");
                getComment(id, page);
            }
        }, mRecyclerView);

    }

    private void submitCommentInform(List<String> content, String id) {
        Disposable disposable = null;
        String inform = "";
        for (String msg : content) {
            inform = inform + msg + ",";
        }

        disposable = NetWorkManager.getRequest(ApiService.class).postInformComment(id, inform, PreferenceManager.getInstance().getUserinfo().getToken())
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

    public static boolean isMobileNO(String mobiles) {

        // Pattern p =
        // Pattern.compile("^((147)|(17[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    private void initView() {


        layout_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = phone.getText().toString();
                tel = tel.substring(tel.length() - 11, tel.length());
                if (isMobileNO(tel)) {
                    callPhone(tel);
                } else {
                    ToastUtil.showToast(DetailActivity.this, "不符合格式的号码");
                }
            }
        });
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
                if (MyAPP.isLogin)
                    showInformDialog();
                else
                    startActivity(new Intent(DetailActivity.this, LoginActivity.class));
            }
        });
        id = getIntent().getExtras().getString("id");
        //id="25359817-9353-4494-8502-ee2f8e50f337";
        getComment(id, page);
        // Log.e("id", id);
        getDetail(id);

        //getComment(id,page);
    }

    private void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    // List<Comment> mNewComments=new ArrayList<>();
    private void getComment(String id, int page) {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getComment(id, page)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Bean>>() {
                    @Override
                    public void accept(RetrofitResponse<Bean> bean) throws Exception {
                        if (bean.getData().getComment().getComments().size() == 0 && isLoadMore) {
                            mCommentAdapter.loadMoreEnd(true);
                            return;
                        }
                        mComments = bean.getData().getComment().getComments();
                        Bean.ReplyUserInfo replyUserInfo = bean.getData().getReplyUserInfo();
                        for (int i = 0; i < mComments.size(); i++) {
                            Comment comment = mComments.get(i);
                            mReplies = comment.getReplyList();
                            for (int j = 0; j < mReplies.size(); j++) {
                                Reply reply = mReplies.get(j);
                                reply.setName(replyUserInfo.getNickname());
                                reply.setUrl(replyUserInfo.getHeadUri());
                                mReplies.set(j, reply);
                            }
                            comment.setReplyList(mReplies);
                            mComments.set(i, comment);
                        }
                        Log.e("m", mComments.get(0).getCommentContent());

                        if (isLoadMore) {
                            isLoadMore = false;
                            mCommentAdapter.addData(mComments);
                            mCommentAdapter.loadMoreComplete();

                        } else {
                            mCommentAdapter.setNewData(mComments);
                            mRecyclerView.setAdapter(mCommentAdapter);
                            Log.e("ddd", "hhhh");
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
        Disposable
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

        new CompositeDisposable().add(disposable);
        // Toast.makeText(DetailActivity.this, "举报成功"+data.size(), Toast.LENGTH_SHORT).show();
    }

    private void postComment(String content) {
        Disposable
                disposable = NetWorkManager.getRequest(ApiService.class).postAddComment(id, content, PreferenceManager.getInstance().getUserinfo().getToken())
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        page = 1;
                        Toast.makeText(DetailActivity.this, "留言成功", Toast.LENGTH_SHORT).show();
                        isLoadMore = false;
                        getComment(id, page);
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

    List<MediaUrl> mMediaUrls = new ArrayList<>();
    int flag = 0;
    //private String testUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";

    private void getDetail(String id) {

        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getDetail(id)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<News>>() {
                    @Override
                    public void accept(RetrofitResponse<News> retrofitResponse) throws Exception {
                        List<MediaUrl> photoList = new ArrayList<>();
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

                        if (!TextUtils.isEmpty(news.getImage1Type())) {
                            if (news.getImage1Type().equals("1") || news.getImage1Type().equals("2"))
                                photoList.add(new MediaUrl(news.getImage1Type(), news.getImage1Uri()));
                        } else {
                            if (!TextUtils.isEmpty(news.getImage1Uri())) {
                                if (news.getImage1Uri().contains("bbs"))
                                    photoList.add(new MediaUrl("1", news.getImage1Uri()));
                            }
                        }
                        if (!TextUtils.isEmpty(news.getImage2Type())) {
                            if (news.getImage2Type().equals("1") || news.getImage2Type().equals("2"))
                                photoList.add(new MediaUrl(news.getImage2Type(), news.getImage2Uri()));
                        } else {
                            if (!TextUtils.isEmpty(news.getImage2Uri())) {
                                if (news.getImage2Uri().contains("bbs"))
                                    photoList.add(new MediaUrl("1", news.getImage2Uri()));
                            }
                        }
                        if (!TextUtils.isEmpty(news.getImage3Type())) {
                            if (news.getImage3Type().equals("1") || news.getImage3Type().equals("2"))
                                photoList.add(new MediaUrl(news.getImage3Type(), news.getImage3Uri()));
                        } else {
                            if (!TextUtils.isEmpty(news.getImage3Uri())) {
                                if (news.getImage3Uri().contains("bbs"))
                                    photoList.add(new MediaUrl("1", news.getImage3Uri()));
                            }
                        }
                        if (!TextUtils.isEmpty(news.getImage4Type())) {
                            if (news.getImage4Type().equals("1") || news.getImage4Type().equals("2"))
                                photoList.add(new MediaUrl(news.getImage4Type(), news.getImage4Uri()));
                        } else {
                            if (!TextUtils.isEmpty(news.getImage4Uri())) {
                                if (news.getImage4Uri().contains("bbs"))
                                    photoList.add(new MediaUrl("1", news.getImage4Uri()));
                            }
                        }
                        if (!TextUtils.isEmpty(news.getImage5Type())) {
                            if (news.getImage5Type().equals("1") || news.getImage5Type().equals("2"))
                                photoList.add(new MediaUrl(news.getImage5Type(), news.getImage5Uri()));
                        } else {
                            if (!TextUtils.isEmpty(news.getImage5Uri())) {
                                if (news.getImage5Uri().contains("bbs"))
                                    photoList.add(new MediaUrl("1", news.getImage5Uri()));
                            }
                        }
                        if (!TextUtils.isEmpty(news.getImage6Type())) {
                            if (news.getImage6Type().equals("1") || news.getImage6Type().equals("2"))
                                photoList.add(new MediaUrl(news.getImage6Type(), news.getImage6Uri()));
                        } else {
                            if (!TextUtils.isEmpty(news.getImage6Uri())) {
                                if (news.getImage6Uri().contains("bbs"))
                                    photoList.add(new MediaUrl("1", news.getImage6Uri()));
                            }
                        }
                        for (int i = 0; i < photoList.size(); i++) {
                            MediaUrl mediaUrl = photoList.get(i);//如果是视频就调用子线程获取视频第一帧缩略图
                            if (mediaUrl.getType().equals("1")) {

                            }
                        }
                        Log.e("photo", photoList.size() + "");
                        for (int i = 0; i < photoList.size(); i++) {
                            MediaUrl mediaUrl = photoList.get(i);//如果是视频就调用子线程获取视频第一帧缩略图
                            Log.e("media", mediaUrl.getUrl());
                            if (mediaUrl.getType().equals("2")) {
                                mMediaUrls.add(mediaUrl);
                                flag++;
                                if (flag == photoList.size()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mPhotoAdapter.setNewData(mMediaUrls);
                                            mRvPhoto.setAdapter(mPhotoAdapter);
                                        }
                                    });
                                }
                            } else if (mediaUrl.getType().equals("1")) {
                                mMediaUrls.add(mediaUrl);
                                flag++;
                                if (flag == photoList.size()) {
                                    mPhotoAdapter.setNewData(mMediaUrls);
                                    mRvPhoto.setAdapter(mPhotoAdapter);
                                }
                            }

                        }


                        //Glide.with(DetailActivity.this).load(news.getImage1Uri()).into(image);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(DetailActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_LONG).show();

                    }
                });
        new CompositeDisposable().add(disposable);
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

}
