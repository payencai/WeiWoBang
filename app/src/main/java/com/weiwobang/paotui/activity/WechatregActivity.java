package com.weiwobang.paotui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.api.ApiService;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WechatregActivity extends AppCompatActivity {
    @BindView(R.id.input_pwd)
    EditText pwd;
    @BindView(R.id.submit)
    TextView submit;
    String openid="";
    String tel="";
    String code="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_wechatreg);
        ButterKnife.bind(this);
    }
    private void initView(){
        Bundle bundle=getIntent().getExtras();
        openid=bundle.getString("openid");
        tel=bundle.getString("tel");
        code=bundle.getString("code");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password=pwd.getEditableText().toString();
                register(tel,code,password,openid);
            }
        });
    }
    private void register(String telephone, String code, String password,String openid) {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postRegister(telephone, code, password,openid)
                // .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        Toast.makeText(WechatregActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(WechatregActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }
}
