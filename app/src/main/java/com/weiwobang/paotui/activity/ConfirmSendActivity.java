package com.weiwobang.paotui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.weiwobang.paotui.bean.AddrBean;
import com.weiwobang.paotui.view.MyDialog;


import java.util.ArrayList;
import java.util.List;
import com.weiwobang.paotui.R;
public class ConfirmSendActivity extends AppCompatActivity {
    MapView mMapView;
    AMap aMap;
    AddrBean fa;
    AddrBean shou;
    TextView fa_fir;
    TextView fa_sec;
    TextView shou_fir;
    TextView shou_sec;
    TextView fa_name;
    TextView fa_phone;
    TextView confirm;
    TextView shou_name;
    TextView shou_phone;
    TextView distance;
    RouteSearch routeSearch;
    private UiSettings mUiSettings;//定义一个UiSettings对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_confirmsend);
        initMapView(savedInstanceState);
        initView();
        calDistance(fa,shou);
        setMarker(shou_marker,shou,R.layout.wwb_marker_shou);
        setMarker(fa_marker,fa,R.layout.wwb_marker_fa);
        setCenter(fa,shou);
        drawLine();
    }
    private void drawLine(){
       // RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(new RouteSearch.FromAndTo(new LatLonPoint(fa.getLat(),fa.getLon()),new LatLonPoint(shou.getLat(),shou.getLon())) );
        //RouteSearch.DriveRouteQuery query1 = new RouteSearch.DriveRouteQuery(new RouteSearch.FromAndTo(),, null, null, "");
        //routeSearch.calculateWalkRouteAsyn(query);//开始算路
        PolylineOptions mPolylineOptions = new PolylineOptions();
        mPolylineOptions.setDottedLine(false);//设置是否为虚线
        mPolylineOptions.geodesic(false);//是否为大地曲线
        mPolylineOptions.visible(true);//线段是否可见
        mPolylineOptions.useGradient(false);//设置线段是否使用渐变色

        //设置线颜色，宽度
        mPolylineOptions.color(getResources().getColor(R.color.bg_blue)).width(10);
        //aMap.setMapTextZIndex(2);
        List<LatLng> latLngs = new ArrayList<LatLng>();
        latLngs.add(new LatLng(fa.getLat(),fa.getLon()));
        latLngs.add(new LatLng(shou.getLat(),shou.getLon()));
        Polyline polyline =aMap.addPolyline(mPolylineOptions.
                addAll(latLngs));
    }
    private void setCenter(AddrBean fa,AddrBean shou){
        double lat,lon;
        lat=(fa.getLat()+shou.getLat())/2;
        lon=(fa.getLon()+shou.getLon())/2;
        LatLng center=new LatLng(lat,lon);
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(center));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
    }
    private void calDistance(AddrBean fa,AddrBean shou){

        final DistanceSearch distanceSearch=new DistanceSearch(this);
        distanceSearch.setDistanceSearchListener(new DistanceSearch.OnDistanceSearchListener() {
            @Override
            public void onDistanceSearched(DistanceResult distanceResult, int i) {
               float dis=distanceResult.getDistanceResults().get(0).getDistance()/1000;
                    distance.setText("跑腿距离 ： "+dis+"km");
            }
        });
        LatLonPoint start = new LatLonPoint(fa.getLat(), fa.getLon());
        LatLonPoint dest = new LatLonPoint(shou.getLat(), shou.getLon());
        DistanceSearch.DistanceQuery distanceQuery=new DistanceSearch.DistanceQuery();
        List<LatLonPoint> latLonPoints = new ArrayList<LatLonPoint>();
        latLonPoints.add(start);
        distanceQuery.setOrigins(latLonPoints);
        distanceQuery.setDestination(dest);
        distanceQuery.setType(DistanceSearch.TYPE_DRIVING_DISTANCE);
        distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
    }
    Marker fa_marker;
    Marker shou_marker;
    private void setMarker(Marker marker,AddrBean addrBean,int resId){
        View markerView = LayoutInflater.from(this).inflate(resId, mMapView, false);
        marker = aMap.addMarker(new MarkerOptions()
                .position(new LatLng(addrBean.getLat(), addrBean.getLon()))
                .icon(BitmapDescriptorFactory.fromView(markerView)));

    }
    private void initView() {
        distance=findViewById(R.id.wwb_distance);
        confirm = findViewById(R.id.header_confirm);
        shou = (AddrBean) getIntent().getExtras().getSerializable("firAddr");
        fa = (AddrBean) getIntent().getExtras().getSerializable("secAddr");
        fa_fir = findViewById(R.id.fa_fir_addr);
        fa_sec = findViewById(R.id.fa_sec_addr);
        shou_fir = findViewById(R.id.shou_fir_addr);
        shou_sec = findViewById(R.id.shou_sec_addr);
        fa_name = findViewById(R.id.fa_name);
        fa_phone = findViewById(R.id.fa_phone);
        shou_name = findViewById(R.id.shou_name);
        shou_phone = findViewById(R.id.shou_phone);
        fa_name.setText(fa.getName());
        fa_phone.setText(fa.getPhone());
        shou_name.setText(shou.getName());
        shou_phone.setText(shou.getPhone());
        if (shou != null && fa != null) {
            fa_fir.setText(fa.getFiraddr());
            fa_sec.setText(fa.getSecaddr());
            shou_fir.setText(shou.getFiraddr());
            shou_sec.setText(shou.getSecaddr());
        }
        confirm.setVisibility(View.GONE);
        findViewById(R.id.wwb_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPayDialog();
            }
        });
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }

            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });

    }

    private void showPayDialog() {
        View view = View.inflate(this, R.layout.wwb_dialog_pay, null);
        Dialog dialog = new MyDialog(this, 200, 100, view, R.style.dialog);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d =getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 宽度设置为屏幕的0.8
        lp.height=(int)(d.heightPixels*0.4);
        dialogWindow.setAttributes(lp);


//        Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.width = 600; // 宽度
//        lp.height = 600; // 高度
//        lp.alpha = 0.7f; // 透明度
// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
// dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);
    }

    private void initMapView(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.confirm_mapview);
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
        aMap = mMapView.getMap();
        routeSearch = new RouteSearch(this);
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setScaleControlsEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
