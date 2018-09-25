package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Account;
import com.weiwobang.paotui.bean.Userinfo;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.GsonUtils;
import com.weiwobang.paotui.tools.PreferenceManager;
import com.weiwobang.paotui.tools.TimerCount;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

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
    @BindView(R.id.wwb_reg_pwd)
    RelativeLayout pwdLayout;
    @BindView(R.id.xieyi)
    LinearLayout xieyi;
    @BindView(R.id.title)
            TextView title;
    String openid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_register);
        ActManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        openid=getIntent().getExtras().getString("openid");
        if(!TextUtils.isEmpty(openid)){
            title.setText("绑定手机号");
            pwdLayout.setVisibility(View.GONE);
            xieyi.setVisibility(View.GONE);
            submit.setText("确定绑定");
        }
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
                if (!TextUtils.isEmpty(telehpone) && !TextUtils.isEmpty(code1) ){
                    if(TextUtils.isEmpty(openid))
                        register(telehpone, code1, password);
                    else{
                        isBind(telehpone,code1);
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void isBind(String tel,String code){
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).isBindWechat(tel, code)
                // .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        //Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        String result=responseBody.string();
                        JSONObject jsonObject=new JSONObject(result);
                        int type=jsonObject.getInt("data");
                        if(type==0){
                            Intent intent=new Intent(RegisterActivity.this, WechatregActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("code",code);
                            bundle.putString("tel",tel);
                            bundle.putString("openid",openid);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else{
                            bindWechat(tel,code);
                        }
                        Log.e("str",result);
                        //finish();
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
    private void register(String telephone, String code, String password) {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postRegister(telephone, code, password,"")
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
    private void bindWechat(String tel,String code){
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).bindWechat(tel, code,1,openid)
                // .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        //Toast.makeText(RegisterActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();
                        String result=responseBody.string();
                        JSONObject jsonObject=new JSONObject(result);
                        int type=jsonObject.getInt("resultCode");
                        if(type==0){
                            login(openid);
                        }else{

                        }
                        Log.e("bind",result);
                        //finish();
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
    private void login(String openId){
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).loginByWechat(1, openId)
                // .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        // Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        String result=responseBody.string();
                        Log.e("login",result);
                        JSONObject jsonObject=new JSONObject(result);

                        int code=jsonObject.getInt("resultCode");
                        if(code==6006){
                            Intent intent=new Intent(RegisterActivity.this,RegisterActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("openid",openId);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        if(code==0){
                            JSONObject data=jsonObject.getJSONObject("data");
                            Userinfo userinfo=GsonUtils.parseJSON(data.toString(),Userinfo.class);
                            PreferenceManager.getInstance().setUserinfo(userinfo);
                            MyAPP.isLogin = true;
                            MyAPP.token = userinfo.getBusinessToken();
                            MyAPP.token2 = userinfo.getToken();
                            Account account=new Account();
                            account.setOpenid(userinfo.getOpenId());
                            PreferenceManager.getInstance().setAccount(account);
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            ActManager.getAppManager().finishAllActivity();
                        }
                        //finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        //Toast.makeText(RegisterActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }
}
