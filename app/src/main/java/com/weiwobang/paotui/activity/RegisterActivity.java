package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.ResponseTransformer;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.TimerCount;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.tv_procol)
    TextView protocol;
    @BindView(R.id.reg_back)
    ImageView back;
    @BindView(R.id.send_code)
    TextView send;
    @BindView(R.id.input_phone)
    EditText phone;
    @BindView(R.id.wwb_reg_confirm)
    TextView submit;
    @BindView(R.id.input_pwd)
    EditText pwd;
    @BindView(R.id.input_code)
    EditText code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_register);
        ActManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, ProtocalActivity.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerCount timer = new TimerCount(60000, 1000, send);
                timer.start();
                String telephone = phone.getEditableText().toString();
                getCode(telephone);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telehpone = phone.getEditableText().toString();
                String code1 = code.getEditableText().toString();
                String password = pwd.getEditableText().toString();
                if (!TextUtils.isEmpty(telehpone) && !TextUtils.isEmpty(code1) && !TextUtils.isEmpty(password))
                    register(telehpone, code1, password);
                else {
                    Toast.makeText(RegisterActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void register(String telephone, String code, String password) {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postRegister(telephone, code, password)
               // .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(RegisterActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }

    private void getCode(String telephone) {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getCode(telephone)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        Toast.makeText(RegisterActivity.this, "验证码发送成功，请注意查收", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(RegisterActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);

    }

}
