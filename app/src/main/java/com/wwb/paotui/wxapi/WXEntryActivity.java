package com.wwb.paotui.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wwb.paotui.MyAPP;
import com.wwb.paotui.R;
import com.wwb.paotui.activity.MainActivity;
import com.wwb.paotui.activity.RegisterActivity;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.bean.Account;
import com.wwb.paotui.bean.Userinfo;
import com.wwb.paotui.tools.ActManager;
import com.wwb.paotui.tools.GsonUtils;
import com.wwb.paotui.tools.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.OPTIONS;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("oncreate","oncreate");
        //如果没回调onResp，八成是这句没有写

//        // 隐藏状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
     //   MyAPP.mWxApi.handleIntent(getIntent(), this);

        //注意：
        //第三方开发者如果使用透明界面来实现WXEntryActivity，需要判断handleIntent的返回值，如果返回值为false，则说明入参不合法未被SDK处理，应finish当前透明界面，避免外部通过传递非法参数的Intent导致停留在透明界面，引起用户的疑惑
        try {
            MyAPP.mWxApi.handleIntent(getIntent(), this);
            //finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        MyAPP.mWxApi.handleIntent(intent, this);
    }
    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        Log.e("req","req");
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {

        Log.e("req",resp.errCode+" code");
        //ToastUtil.showToast(this,resp.errCode+"");
        // LogUtils.sf(resp.errStr);
        // LogUtils.sf("错误码 : " + resp.errCode + "");
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) {
                    //UIUtils.showToast("分享失败");
                } else
                    //UIUtils.showToast("登录失败");
                    break;
            case BaseResp.ErrCode.ERR_OK:
                Log.e("req","res");
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        Log.e("code",code+"");
                        // LogUtils.sf("code = " + code);
                        getOpenId(code,"wx57f1a1cb08d803fe","94571e4d7245fa8433d453f83eac8b06");
                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        // UIUtils.showToast("微信分享成功");
                        finish();
                        break;
                }
                break;
        }
    }

    private void getOpenId(String code,String appid,String appSerect) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weixin.qq.com/sns/oauth2/")
                .build();
        ApiService service = retrofit.create(ApiService.class);

        Call<ResponseBody> call = service.getOpenId(appid,appSerect,code,"authorization_code");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                try {
                    String result = response.body().string();
                    JSONObject jsonObject=new JSONObject(result);
                    String openid=jsonObject.getString("openid");
                    login(openid);
                    Log.e("result",result);
                    //finish();
                    //ToastUtil.showToast(WXEntryActivity.this,result);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

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
                        Log.e("wx",result);
                        JSONObject jsonObject=new JSONObject(result);
                        int code=jsonObject.getInt("resultCode");
                        if(code==6006){
                            Intent intent=new Intent(WXEntryActivity.this,RegisterActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("openid",openId);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            Log.e("code","6006");
                        }
                        if(code==0){
                            JSONObject data=jsonObject.getJSONObject("data");
                            Userinfo userinfo= GsonUtils.parseJSON(data.toString(),Userinfo.class);
                            PreferenceManager.getInstance().setUserinfo(userinfo);
                            MyAPP.isLogin = true;
                            MyAPP.token = userinfo.getBusinessToken();
                            MyAPP.token2 = userinfo.getToken();
                            Account account=new Account();
                            Log.e("token",userinfo.toString());
                            account.setOpenid(userinfo.getOpenId());
                            PreferenceManager.getInstance().setAccount(account);
                            ActManager.getAppManager().finishAllActivity();
                            startActivity(new Intent(WXEntryActivity.this, MainActivity.class));
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
       // MyAPP.mWxApi.unregisterApp();
    }
}