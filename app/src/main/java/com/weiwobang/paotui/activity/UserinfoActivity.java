package com.weiwobang.paotui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.image.Compressor;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.api.Api;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Userinfo;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
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


public class UserinfoActivity extends AppCompatActivity {
    @BindView(R.id.myname)
    TextView tv_name;
    @BindView(R.id.nick_layout)
    RelativeLayout nick;
    @BindView(R.id.iv_head)
    CircleImageView head;
    @BindView(R.id.sett_back)
    FrameLayout back;
    Uri photoUri;
    Uri photoOutputUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_userinfo);
        ActManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        tv_name.setText(PreferenceManager.getInstance().getUserinfo().getNickname());
        if (PreferenceManager.getInstance().getUserinfo().getHeadingUri() != null) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.wwb_default_photo) //加载中图片
                    .error(R.mipmap.wwb_default_photo) //加载失败图片
                    .fallback(R.mipmap.wwb_default_photo) //url为空图片
                    .centerCrop();// 填充方式
            Glide.with(this).load(PreferenceManager.getInstance().getUserinfo().getHeadingUri()).apply(requestOptions).into(head);
        }

        //getUserinfo();
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getUserinfo() {
        Disposable
            disposable = NetWorkManager.getRequest(ApiService.class).getUserinfo(PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse<Userinfo>>() {
                        @Override
                        public void accept(RetrofitResponse<Userinfo> retrofitResponse) throws Exception {
                            Userinfo user = retrofitResponse.getData();
                            if (user != null) {
                                tv_name.setText(user.getNickname());
                                Glide.with(UserinfoActivity.this).load(user.getHeadingUri()).into(head);
                            }
                            //Toast.makeText(getActivity(), user.getNickname()+"dfdf", Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);
                            Toast.makeText(UserinfoActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        new CompositeDisposable().add(disposable);
    }

    /**
     * 修改昵称dialog
     */
    private void showInputDialog() {
        /*@setView 装入一个EditView
         */
        final EditText editText = new EditText(this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(this);
        inputDialog.setTitle("修改昵称").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_name.setText(editText.getEditableText().toString());
                        updateName(editText.getEditableText().toString());
                        dialog.dismiss();
                    }
                }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    /**
     * 更新昵称
     *
     * @param name
     */
    private void updateName(String name) {
        Disposable
            disposable = NetWorkManager.getRequest(ApiService.class).postUpdateName(name, PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse>() {
                        @Override
                        public void accept(RetrofitResponse retrofitResponse) throws Exception {
                            Toast.makeText(UserinfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);
                            Toast.makeText(UserinfoActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

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
                        updateHead(data);

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


    //更新头像
    private void updateHead(String data) {
        Disposable
            disposable = NetWorkManager.getRequest(ApiService.class).postUpdateHead(data, PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse>() {
                        @Override
                        public void accept(RetrofitResponse retrofitResponse) throws Exception {
                            //getUserinfo();
                            Toast.makeText(UserinfoActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                            //finish();
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

    /**
     * 上传头像对话框
     */
    private void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.dialog);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.wwb_select_head, null);
        //获得dialog的window窗口
        Window window = dialog.getWindow();
        //设置dialog在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置dialog弹出时的动画效果，从屏幕底部向上弹出
        window.setWindowAnimations(R.style.mypopwindow_anim_style);
        window.getDecorView().setPadding(0, 0, 0, 0);
        //获得window窗口的属性
        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        window.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(dialogView);
        dialog.findViewById(R.id.tv_select_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.tv_select_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                startCamera();
            }
        });
        dialog.findViewById(R.id.tv_select_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent mIntent = new Intent(Intent.ACTION_GET_CONTENT);
                mIntent.addCategory(Intent.CATEGORY_OPENABLE);
                mIntent.setType("image/*");
                startActivityForResult(mIntent, 2);
            }
        });
        dialog.show();
    }

    /**
     * 剪裁照片
     *
     * @param inputUri
     */
    public void cropPhoto(Uri inputUri) {
        // 调用系统裁剪图片的 Action
        Intent cropPhotoIntent = new Intent("com.android.camera.action.CROP");
        // 设置数据Uri 和类型
        cropPhotoIntent.setDataAndType(inputUri, "image/*");
        // 授权应用读取 Uri，这一步要有，不然裁剪程序会崩溃
        cropPhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // 设置图片的最终输出目录
        cropPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                photoOutputUri = Uri.parse("file:////sdcard/image_output.jpg"));
        startActivityForResult(cropPhotoIntent, 3);
    }

    private void startCamera() {
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }
        if (savePath == null || "".equals(savePath)) {
            System.out.println("无法保存照片，请检查SD卡是否挂载");
            return;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //照片命名
        String fileName = timeStamp + ".png";
        File file = new File(savePath, fileName);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /** * 因 Android 7.0 开始，不能使用 file:// 类型的 Uri 访问跨应用文件，否则报异常， * 因此我们这里需要使用内容提供器，FileProvider 是 ContentProvider 的一个子类， * 我们可以轻松的使用 FileProvider 来在不同程序之间分享数据(相对于 ContentProvider 来说) */
//        if (Build.VERSION.SDK_INT >= 24) {
//            photoUri = FileProvider.getUriForFile(getContext(), "com.example.meimeng.fileprovider", file);
//        } else {
        photoUri = Uri.fromFile(file); // Android 7.0 以前使用原来的方法来获取文件的 Uri
//        }
        // 打开系统相机的 Action，等同于："android.media.action.IMAGE_CAPTURE"
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 设置拍照所得照片的输出目录
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(takePhotoIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Log.e("req",requestCode+"");
        if (requestCode == 1) {
            cropPhoto(photoUri);
//            File file=new File(photoUri.getPath());
//            Log.e("req",photoUri.getPath()+"");
//           upImage(Api.testUrl,file);
        }
        if (requestCode == 2) {
            cropPhoto(data.getData());
//            Uri uri = data.getData();
//            File file=new File(uri.getPath());
//            upImage(Api.testUrl,file);
            // Log.e("req",uri.getPath()+"");
        }
        if (requestCode == 3) {
            File file = new File(photoOutputUri.getPath());
            if (file.exists()) {
                Glide.with(this).load(photoOutputUri).into(head);
                File newFile = null;
                try {
                    newFile = new Compressor(UserinfoActivity.this)
                            .compressToFile(file, file.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                upLoadImg(newFile);
            } else {
                Toast.makeText(this, "找不到照片", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
