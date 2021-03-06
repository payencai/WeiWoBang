package com.wwb.paotui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.JsonObject;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.mediapicker.PickerActivity;
import com.payencai.library.mediapicker.PickerConfig;
import com.payencai.library.mediapicker.entity.Media;
import com.payencai.library.util.ToastUtil;
import com.payencai.library.util.image.Compressor;
import com.wwb.paotui.MyAPP;
import com.wwb.paotui.R;
import com.wwb.paotui.adapter.ImgAdapter;
import com.wwb.paotui.api.Api;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.bean.AddrBean;
import com.wwb.paotui.bean.FileType;
import com.wwb.paotui.bean.News;
import com.wwb.paotui.tools.ActManager;
import com.wwb.paotui.tools.PreferenceManager;
import com.wwb.paotui.tools.VideoUtil;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;
import com.zyao89.view.zloading.star.StarBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.select_addr)
    RelativeLayout select_addr;
    @BindView(R.id.sel_num)
    TextView num;
    @BindView(R.id.addLayout)
    RelativeLayout addLayout;
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
    int imgNum = 0;
    int videoNum = 0;
    FileType image1 = new FileType();
    FileType image2 = new FileType();
    FileType image3 = new FileType();
    FileType image4 = new FileType();
    FileType image5 = new FileType();
    FileType image6 = new FileType();
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

    File firstFile = null;

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
                defaultSelect.remove(index);
                num.setText("图片或视频(" + defaultSelect.size() + "/6)");
                imgAdapter.setData(defaultSelect);
                recyclerView.setAdapter(imgAdapter);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go();
                // ImageSelectorUtils.openPhoto(PublishActivity.this, 1, false, 6, selected);
            }
        });
        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go();
            }
        });
        select_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PublishActivity.this, SelectAddrActivity.class), 1);
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mNews == null) {
                    String title = et_title.getEditableText().toString();
                    String comm = comment.getEditableText().toString();
                    if(TextUtils.isEmpty(title)){
                        ToastUtil.showToast(PublishActivity.this,"标题不能为空");
                        return;
                    }
                    if(TextUtils.isEmpty(comm)){
                        ToastUtil.showToast(PublishActivity.this,"内容不能为空");
                        return;
                    }
                    loading();
                    if (defaultSelect.size() == 0) {
                        publish();
                    } else {
                        for (int i = 0; i < defaultSelect.size(); i++) {
                            Media media = defaultSelect.get(i);
                            File file = new File(media.path);
                            int j = i + 1;
                            if (media.mediaType == 1) {
                                File newFile = null;
                                try {
                                    newFile = new Compressor(PublishActivity.this)
                                            .compressToFile(file, file.getName());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                upLoadImg(newFile, "1", j);
                            } else {
                                if (i == 0) {
                                    firstFile = file;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Bitmap bitmap = VideoUtil.voidToFirstBitmap(media.path);
                                            String path = VideoUtil.bitmapToStringPath(PublishActivity.this, bitmap);
                                            Log.e("path", path);
                                            File f = new File(path);
                                            upLoadImg(f, "2", j);
                                        }
                                    }).start();
                                } else {
                                    uploadVideo(file, "2", j);
                                }
                            }
                        }


                    }
                } else {
                    loading();
                    if (defaultSelect.size() == 0) {
                        publishUpdate();
                    } else {
                        //loading();
                        for (int i = 0; i < defaultSelect.size(); i++) {
                            Media media = defaultSelect.get(i);
                            File file = new File(media.path);
                            int j = i + 1;

                            if (media.mediaType == 1) {
                                File newFile = null;
                                try {
                                    newFile = new Compressor(PublishActivity.this)
                                            .compressToFile(file, file.getName());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                upLoadImg(newFile, "1", j);
                            } else {
                                if (i == 0) {
                                    firstFile = file;
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Bitmap bitmap = VideoUtil.voidToFirstBitmap(media.path);
                                            String path = VideoUtil.bitmapToStringPath(PublishActivity.this, bitmap);
                                            Log.e("path", path);
                                            File f = new File(path);
                                            upLoadImg(f, "2", j);
                                        }
                                    }).start();
                                } else {
                                    uploadVideo(file, "2", j);
                                }
                            }

                        }
                    }

                }

            }
        });
    }

    private void uploadVideo(File file, String type, int position) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).upLoadVideo(body)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        if (retrofitResponse.getResultCode() == 0) {
                            String data = retrofitResponse.getData().toString();
                            Log.e("video", "video" + data + position);
                            flag++;
                            switch (position) {
                                case 1:
                                    image1.setData(data);
                                    image1.setType(type);
                                    break;
                                case 2:
                                    image2.setData(data);
                                    image2.setType(type);
                                    break;
                                case 3:
                                    image3.setData(data);
                                    image3.setType(type);
                                    break;
                                case 4:
                                    image4.setData(data);
                                    image4.setType(type);
                                    break;
                                case 5:
                                    image5.setData(data);
                                    image5.setType(type);
                                    break;
                                case 6:
                                    image6.setData(data);
                                    image6.setType(type);
                                    break;
                            }

                            if (defaultSelect.size() == flag) {
                                if (mNews == null)
                                    publish();
                                else {
                                    publishUpdate();
                                }
                            }
                        } else {
                            Log.e("video", retrofitResponse.getMessage());
                        }


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

    private void publishUpdate() {
        Map<String, Object> params = new HashMap<>();
        String linkman = con.getEditableText().toString();
        String tel = phone.getEditableText().toString();
        String comm = comment.getEditableText().toString();

        if (TextUtils.isEmpty(comm)) {
            return;
        }
        params.put("id", mNews.getId());
        params.put("image1", image1.getData());
        params.put("image1Type", image1.getType() + "");
        params.put("image2", image2.getData());
        params.put("image2Type", image2.getType() + "");
        params.put("image3", image3.getData());
        params.put("image3Type", image3.getType() + "");
        params.put("image4", image4.getData());
        params.put("image4Type", image4.getType() + "");
        params.put("image5", image5.getData());
        params.put("image5Type", image5.getType() + "");
        params.put("image6", image6.getData());
        params.put("image6Type", image6.getType() + "");
        params.put("linkman", linkman);
        params.put("contactInfomation", tel);
        params.put("content", comm);
        Log.e("st", params.toString());
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postEdit(params
                , PreferenceManager.getInstance().getUserinfo().getToken())
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        if (retrofitResponse.getResultCode() == 0) {
                            Toast.makeText(PublishActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        } else {
                            Toast.makeText(PublishActivity.this, retrofitResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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

    private void upLoadImg(File file, String type, int position) {

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
                        if (retrofitResponse.getResultCode() == 0) {
                            flag++;
                            String data = retrofitResponse.getData().toString();
                            if (TextUtils.equals("2", type)) {
                                Log.e("small", data);
                                flag--;
                                uploadVideo(firstFile, data, position);
                            }
                            Log.e("img", data);
                            switch (position) {
                                case 1:
                                    if (TextUtils.equals("1", type))
                                        image1.setData(data);
                                    image1.setType(type);
                                    break;
                                case 2:
                                    image2.setData(data);
                                    image2.setType(type);
                                    break;
                                case 3:
                                    image3.setData(data);
                                    image3.setType(type);
                                    break;
                                case 4:
                                    image4.setData(data);
                                    image4.setType(type);
                                    break;
                                case 5:
                                    image5.setData(data);
                                    image5.setType(type);
                                    break;
                                case 6:
                                    image6.setData(data);
                                    image6.setType(type);
                                    break;
                            }
                            if (TextUtils.equals("1", type))
                                if (defaultSelect.size() == flag) {
                                    if (mNews == null)
                                        publish();
                                    else {
                                        publishUpdate();
                                    }
                                }
                        } else {
                            Log.e("img", retrofitResponse.getMessage());
                        }


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

        Map<String, Object> params = new HashMap<>();
        String title = et_title.getEditableText().toString();
        String linkman = con.getEditableText().toString();
        String tel = phone.getEditableText().toString();
        String comm = comment.getEditableText().toString();

        params.put("title", title);
        params.put("categoryId", categoryId);
        params.put("categoryName", categoryName);
        params.put("image1", image1.getData());
        params.put("image1Type", image1.getType() + "");
        params.put("image2", image2.getData());
        params.put("image2Type", image2.getType() + "");
        params.put("image3", image3.getData());
        params.put("image3Type", image3.getType() + "");
        params.put("image4", image4.getData());
        params.put("image4Type", image4.getType() + "");
        params.put("image5", image5.getData());
        params.put("image5Type", image5.getType() + "");
        params.put("image6", image6.getData());
        params.put("image6Type", image6.getType() + "");
        params.put("linkman", linkman);
        params.put("lng", mAddrBean.getLon() + "");
        params.put("lat", mAddrBean.getLat() + "");
        params.put("address", mAddrBean.getFiraddr());
        params.put("addressDetail", mAddrBean.getSecaddr());
        params.put("contactInfomation", tel);
        params.put("content", comm);

        //remove(params,defaultSelect.size());
        Log.e("params", params.toString());
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postPublic(params, MyAPP.token2)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        //   dialog.dismiss();
                        if (retrofitResponse.getResultCode() == 0) {
                            Toast.makeText(PublishActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        } else {
                            Log.e("msg", retrofitResponse.getMessage());
                            Toast.makeText(PublishActivity.this, retrofitResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(PublishActivity.this, apiException.getDisplayMessage() + "", Toast.LENGTH_LONG).show();
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void remove(Map<String, Object> params, int code) {
        if (code == 5) {
            params.remove("image6");
            params.remove("image6Type");
        }
        if (code == 4) {
            params.remove("image5");
            params.remove("image5Type");
            params.remove("image6");
            params.remove("image6Type");
        }
        if (code == 3) {
            params.remove("image4");
            params.remove("image4Type");
            params.remove("image5");
            params.remove("image5Type");
            params.remove("image6");
            params.remove("image6Type");
        }
        if (code == 2) {
            params.remove("image3");
            params.remove("image3Type");
            params.remove("image4");
            params.remove("image4Type");
            params.remove("image5");
            params.remove("image5Type");
            params.remove("image6");
            params.remove("image6Type");
        }
        if (code == 1) {
            params.remove("image2");
            params.remove("image2Type");
            params.remove("image3");
            params.remove("image3Type");
            params.remove("image4");
            params.remove("image4Type");
            params.remove("image5");
            params.remove("image5Type");
            params.remove("image6");
            params.remove("image6Type");
        }
        if (code == 0) {
            params.remove("image1");
            params.remove("image1Type");
            params.remove("image2");
            params.remove("image2Type");
            params.remove("image3");
            params.remove("image3Type");
            params.remove("image4");
            params.remove("image4Type");
            params.remove("image5");
            params.remove("image5Type");
            params.remove("image6");
            params.remove("image6Type");
        }
    }

    ArrayList<Media> defaultSelect = new ArrayList<>();

    void go() {
        Intent intent = new Intent(PublishActivity.this, PickerActivity.class);
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE_VIDEO);//default image and video (Optional)
        long maxSize = 10485760L;//long long long long类型
        intent.putExtra(PickerConfig.MAX_SELECT_SIZE, maxSize); //default 10MB (Optional)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 6);  //default 40 (Optional)
        intent.putExtra(PickerConfig.DEFAULT_SELECTED_LIST, defaultSelect); //(Optional)默认选中的照片
        startActivityForResult(intent, 200);
    }

    AddrBean mAddrBean = new AddrBean();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            mAddrBean = (AddrBean) data.getExtras().getSerializable("addrbean");
            tv_address.setText(mAddrBean.getSecaddr());
//            //获取选择器返回的数据
//            ArrayList<String> images = data.getStringArrayListExtra(
//                    ImageSelectorUtils.SELECT_RESULT);
//
//            selected = images;
//            num.setText("图片或视频(" + selected.size() + "/6)");
//            imgAdapter.setData(defaultSelect);
//            recyclerView.setAdapter(imgAdapter);

        }
        if (requestCode == 200 && data != null) {
            defaultSelect = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            Log.i("select", "select.size" + defaultSelect.size());
            for (Media media : defaultSelect) {
                selected.add(media.path);
//                if(media.mediaType==1){
//                    imgNum++;
//                }else{
//                    videoNum++;
//                }

            }
            num.setText("图片或视频(" + defaultSelect.size() + "/6)");
            imgAdapter.setData(defaultSelect);
            recyclerView.setAdapter(imgAdapter);
        }

    }

}
