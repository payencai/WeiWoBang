package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.JsonObject;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.image.Compressor;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.ImgAdapter;
import com.weiwobang.paotui.api.Api;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.PreferenceManager;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;
import com.zyao89.view.zloading.star.StarBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishActivity extends AppCompatActivity {
    @BindView(R.id.titlepub)
    EditText et_title;
    @BindView(R.id.titlecomm)
    EditText comment;
    @BindView(R.id.contactpub)
    EditText con;
    @BindView(R.id.sel_num)
    TextView num;
    @BindView(R.id.phonepub)
    EditText phone;
    @BindView(R.id.pub_back)
    ImageView back;
    @BindView(R.id.title_pub)
    TextView title;
    @BindView(R.id.tv_publish)
    TextView publish;
    @BindView(R.id.gridview)
    RecyclerView recyclerView;
    @BindView(R.id.upload_img)
    ImageView add;
    ImgAdapter imgAdapter;
    ArrayList<String> selected = new ArrayList<>();
    String categoryId = "";
    String categoryName = "";
    private News mNews;
    String image1;
    String image2;
    String image3;
    String image4;
    String image5;
    String image6;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_publish);
        ButterKnife.bind(this);
        //ImageSelectorUtils.op
        //loading();
        ActManager.getAppManager().addActivity(this);
        initView();

    }

    ZLoadingDialog dialog;

    private void loading() {
        dialog = new ZLoadingDialog(PublishActivity.this);
        dialog.setLoadingBuilder(Z_TYPE.STAR_LOADING)//设置类型
                .setLoadingColor(Color.BLACK)//颜色
                .setHintText("发布中...")
                .setHintTextSize(12)

                .setCanceledOnTouchOutside(false)
                .show();


    }

    private void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mNews = (News) bundle.getSerializable("news");
            categoryId = bundle.getString("id");
            categoryName = bundle.getString("name");
        }
        if (mNews != null) {
            et_title.setText(mNews.getTitle());
            et_title.setEnabled(false);
            comment.setText(mNews.getContent());
            con.setText(mNews.getLinkman());
            phone.setText(mNews.getContactInfomation());
            publish.setText("确定修改");
            title.setText(mNews.getCategoryName());
        } else {
            title.setText(categoryName);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        imgAdapter = new ImgAdapter();
        imgAdapter.setOnDelListener(new ImgAdapter.onDelListener() {
            @Override
            public void onClick(int index) {
                selected.remove(index);
                num.setText("图片(" + selected.size() + "/6)");
                imgAdapter.setData(selected);
                recyclerView.setAdapter(imgAdapter);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelectorUtils.openPhoto(PublishActivity.this, 1, false, 6, selected);
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNews == null) {
                    Log.e("url", selected.size() + "");
                    if (selected.size() == 0) {
                        publish();
                    } else {
                        loading();
                        for (String image : selected) {
                            File file=new File(image);
                            File newFile = null;
                            try {
                                newFile = new Compressor(PublishActivity.this)
                                        .compressToFile(file,file.getName());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            upLoadImg(newFile);
                        }
                    }
                } else {
                    if (selected.size() == 0) {
                        publishUpdate();
                    } else {
                        loading();
                        for (String image : selected) {
                            File file=new File(image);
                            File newFile = null;
                            try {
                                newFile = new Compressor(PublishActivity.this)
                                        .compressToFile(file,file.getName());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            upLoadImg(newFile);
                        }
                    }

                }

            }
        });
    }

    private void publishUpdate() {
        Disposable disposable = null;
        try {
            disposable = NetWorkManager.getRequest(ApiService.class).postEdit(
                    mNews.getId(), image1, image2,
                    image3, image4, image5, image6,
                    con.getEditableText().toString(), phone.getEditableText().toString(), comment.getEditableText().toString(), PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse>() {
                        @Override
                        public void accept(RetrofitResponse retrofitResponse) throws Exception {
                            dialog.dismiss();
                            Toast.makeText(PublishActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);
                            Toast.makeText(PublishActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new CompositeDisposable().add(disposable);
    }

    private void upLoadImg(File file) {

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postHeadImg(body)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        String data = retrofitResponse.getData().toString();
                        flag++;
                        switch (flag) {
                            case 1:
                                image1 = data;
                                break;
                            case 2:
                                image2 = data;
                                break;
                            case 3:
                                image3 = data;
                                break;
                            case 4:
                                image4 = data;
                                break;
                            case 5:
                                image5 = data;
                                break;
                            case 6:
                                image6 = data;
                                break;
                        }
                        if (flag == selected.size() && selected.size() != 0) {
                            if (mNews == null)
                                publish();
                            else {

                                publishUpdate();
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        // Toast.makeText(RegisterActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }

    private void publish() {
        Disposable disposable = null;
        try {
            disposable = NetWorkManager.getRequest(ApiService.class).postPublic(
                    et_title.getEditableText().toString(), categoryId, categoryName, image1, image2,
                    image3, image4, image5, image6,
                    con.getEditableText().toString(), phone.getEditableText().toString(), comment.getEditableText().toString(), PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse>() {
                        @Override
                        public void accept(RetrofitResponse retrofitResponse) throws Exception {
                            dialog.dismiss();
                            Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);
                            Toast.makeText(PublishActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new CompositeDisposable().add(disposable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(
                    ImageSelectorUtils.SELECT_RESULT);

            selected = images;
            num.setText("图片(" + selected.size() + "/6)");
            imgAdapter.setData(selected);
            recyclerView.setAdapter(imgAdapter);


        }

    }
}
