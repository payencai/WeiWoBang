package com.wwb.paotui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wwb.paotui.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebmapActivity extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_webmap);
        ButterKnife.bind(this);
        initMap();
    }
    void initMap(){
        webView.getSettings().setJavaScriptEnabled(true);
        // 添加一个客户端到视图上
        // 让 WebView 自己处理所有的 URL 请求
        webView.setWebViewClient(new MyWebViewClient());//这个是设置对webview的点击截取
        // 如果仅仅只是加载页面，如下即可(不需要 WebViewClient 的对象)：
        //webView.loadUrl("http://www.huanlechuangke.com/shop/location");
        webView.loadUrl("http://www.wewobang.com/map/index.html");
    }

    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //根据返回的url分割字符串，
//            http://www.test.com/?loc={"module":"locationPicker","latlng":{"lat":24.8778,"lng":102.83508},"poiaddress":"云南省昆明市呈贡区级行政中心综合服务楼","poiname":"昆明市级行政中心综合服务楼","cityname":"昆明市"}
            if (url.contains("locationPicker")){
                String[] strings = url.split(",");
                String[] strings1 = strings[1].split(":");
                String lat = strings1[strings1.length-1];
                String[] strings2 = strings[2].split(":");
                String lng = strings2[strings2.length-1].substring(0,strings2[strings2.length-1].length()-1);
                String[] strings3 = strings[3].split(":");
                String addr = Uri.decode(strings3[1]);
                addr = addr.substring(1,addr.length()-1);
                String[] strings4 = strings[4].split(":");
//                String addrName = Uri.decode(strings4[1]);
                String addrName = strings4.length>=2 ? Uri.decode(strings4[1]) : Uri.decode(strings[4]);
                addrName = addrName.substring(1,addrName.length()-1);
                Intent intent = new Intent();
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("addr",addr);
                intent.putExtra("name",addrName);
                setResult(RESULT_OK,intent);
                finish();
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
        }
    }

    /**
     *  改写物理按键——返回的逻辑
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }



    @OnClick({R.id.back})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
        }
    }
}
