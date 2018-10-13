package com.wwb.paotui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.wwb.paotui.JPush.JpushConfig;
import com.wwb.paotui.MyAPP;
import com.wwb.paotui.R;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.bean.Account;
import com.wwb.paotui.bean.Userinfo;
import com.wwb.paotui.tools.ActManager;
import com.wwb.paotui.tools.PreferenceManager;


import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class PhoneActivity extends AppCompatActivity {
    @BindView(R.id.tv_forget)
    TextView findPwd;
    @BindView(R.id.phone_back)
    ImageView back;
    @BindView(R.id.wwb_reg_confirm)
    TextView submit;
    @BindView(R.id.login_phone)
    EditText et_phone;
    @BindView(R.id.login_pwd)
    TextView et_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_phone);
        ActManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Account account = PreferenceManager.getInstance().getAccount();
        if (MyAPP.isDebug) {
            et_phone.setText("13202908144");
            et_pwd.setText("123456");
            login(et_phone.getText().toString(), et_pwd.getText().toString());
        }
        if (account.getPassword() != null) {
            et_pwd.setText(account.getPassword());
            et_phone.setText(account.getUsername());
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = et_phone.getEditableText().toString();
                String pwd = et_pwd.getEditableText().toString();
                if(TextUtils.isEmpty(phone)){
                    ToastUtil.showToast(PhoneActivity.this,"请输入账号");
                    return;
                }
                if(TextUtils.isEmpty(pwd)){
                    ToastUtil.showToast(PhoneActivity.this,"请输入密码！");
                    return;
                }
                login(phone, pwd);
            }
        });
    }

    @OnClick({R.id.tv_forget, R.id.phone_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                Intent intent = new Intent(PhoneActivity.this, FindpwdActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.phone_back:
                finish();
                break;
        }
    }

    public void login(String account, String password) {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postLogin(account, password)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Userinfo>>() {
                    @Override
                    public void accept(RetrofitResponse<Userinfo> userinfoRetrofitResponse) throws Exception {
                        if (userinfoRetrofitResponse.getResultCode() == 0) {
                            Userinfo userinfo = userinfoRetrofitResponse.getData();
                            PreferenceManager.getInstance().setUserinfo(userinfo);
                            MyAPP.isLogin = true;
                            MyAPP.token = userinfo.getBusinessToken();
                            MyAPP.token2 = userinfo.getToken();
                            PreferenceManager.getInstance().setAccount(new Account(account,password));
                            ActManager.getAppManager().finishAllActivity();
                            startActivity(new Intent(PhoneActivity.this, MainActivity.class));

                        } else {
                            ToastUtil.showToast(PhoneActivity.this, userinfoRetrofitResponse.getMessage());
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Log.e("error", apiException.getDisplayMessage());
                        Toast.makeText(PhoneActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        new CompositeDisposable().add(disposable);


    }
}
