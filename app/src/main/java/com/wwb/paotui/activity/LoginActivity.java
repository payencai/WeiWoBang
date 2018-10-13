package com.wwb.paotui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.ResponseTransformer;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.wwb.paotui.MyAPP;
import com.wwb.paotui.R;
import com.wwb.paotui.api.Api;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.bean.Account;
import com.wwb.paotui.constant.CommomConstant;

import com.wwb.paotui.tools.ActManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.autonavi.ae.pos.LocManager.init;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_type)
    TextView login;
    @BindView(R.id.reg_type)
    TextView reg;
     @BindView(R.id.login_back)
    ImageView back;
     @BindView(R.id.login_weixin)
    RelativeLayout weixin;
    //TextView tv_test;
    private Map<String, String> params = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_login);
        ActManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        init();
        //tv_test = findViewById(R.id.test);
        //login(CommomConstant.Runner.account, CommomConstant.Runner.password);
    }
    /**
     * 登录微信
     *
     * @param api 微信服务api
     */
    public  void loginWeixin(IWXAPI api) {


        // 发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();
        // 应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
         */
        req.state = "app_wechat";
        api.sendReq(req);
    }
    private void init(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,PhoneActivity.class));
            }
        });
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IWXAPI iwxapi= MyAPP.mWxApi;
                if(!iwxapi.isWXAppInstalled()){
                    ToastUtil.showToast(LoginActivity.this,"没有安装微信!");
                    return ;
                }
                //wxLogin();
                loginWeixin(iwxapi);
            }
        });
    }
    public void wxLogin() {
        if (!MyAPP.mWxApi.isWXAppInstalled()) {
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        MyAPP.mWxApi.sendReq(req);
    }
}
