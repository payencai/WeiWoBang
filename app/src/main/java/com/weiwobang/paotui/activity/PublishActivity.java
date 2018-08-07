package com.weiwobang.paotui.activity;

import android.content.Intent;
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
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.ImgAdapter;
import com.weiwobang.paotui.api.Api;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_publish);
        ButterKnife.bind(this);
        //ImageSelectorUtils.op
        ActManager.getAppManager().addActivity(this);
        initView();
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
        }
        title.setText(categoryName);
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
                        for (String image : selected) {
                            Log.e("iamge", image);
                            upImage(Api.testUrl, new File(image));
                            //uploadImg(image);
                        }
                    }
                }else{
                    publishUpdate();
                }

//                for(int i=0;i<selected.size();i++){
//                    //Log.e("url",selected.get(i));
//                   // uploadImg(selected.get(0));
//                    upImage(Api.baseUrl+Api.User.sUpLoadImg,new File(selected.get(i)));
//                 }

            }
        });
    }
    private void publishUpdate(){
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
    String image1;
    String image2;
    String image3;
    String image4;
    String image5;
    String image6;
    int flag = 0;


    public void upImage(String url, File file) {
        Log.d("leng", file.length() / 1000 + "");
        OkHttpClient mOkHttpClent = new OkHttpClient();

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image",
                        RequestBody.create(MediaType.parse("image/png"), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClent.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                flag++;
                String string = response.body().string();
                try {
                    JSONObject object = new JSONObject(string);
                    String data = object.getString("data");
                    switch (flag) {
                        case 1:
                            image1 = data;
                            break;
                        case 2:
                            image2 = data;
                            break;
                        case 3:
                            image2 = data;
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
                        publish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("upload", "onResponse: " + string);


            }
        });
    }

    private void uploadImg(String path) {
        //Uri uri=new Uri()
        Uri uri = Uri.parse(path);
        File file = new File(path);

        Log.d("leng", file.length() / 1000 + "");
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

// MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);

// 添加描述
        String descriptionString = "hello, 这是文件描述";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        Disposable disposable = NetWorkManager.getRequest(ApiService.class).uploadImg(body)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        Log.e("image", retrofitResponse.getData().toString());
                        flag++;
                        Toast.makeText(PublishActivity.this, "上传成功", Toast.LENGTH_LONG).show();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(PublishActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
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
