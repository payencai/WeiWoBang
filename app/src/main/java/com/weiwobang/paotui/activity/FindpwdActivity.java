package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.tools.TimerCount;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class FindpwdActivity extends AppCompatActivity {
    @BindView(R.id.find_phone)
    EditText phone;
    @BindView(R.id.code_in)
    EditText code;
    @BindView(R.id.pwd_in)
    EditText pwd;
    @BindView(R.id.iv_find)
    ImageView back;
    @BindView(R.id.find_code)
    TextView send;
    @BindView(R.id.wwb_find_confirm)
    TextView find_confirm;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_findpwd);
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        flag=getIntent().getExtras().getInt("flag");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerCount timer=new TimerCount(60000,1000,send);
                timer.start();

                getCode(phone.getEditableText().toString());
            }
        });
        find_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String telephone=phone.getEditableText().toString();
                String co=code.getEditableText().toString();
                String pass=pwd.getEditableText().toString();
                updatePwd(telephone,co,pass);
            }
        });
    }
    private void updatePwd(String tele,String co,String pass){
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postResetPwd(tele, co, pass)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        Toast.makeText(FindpwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(FindpwdActivity.this,LoginActivity.class));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(FindpwdActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }
    private  void getCode(String pho){
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getCode(pho)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        Toast.makeText(FindpwdActivity.this, "验证码发送成功，请注意查收", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(FindpwdActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }
}
