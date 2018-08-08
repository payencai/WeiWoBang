package com.weiwobang.paotui.activity;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;


import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.AddrBean;
import com.weiwobang.paotui.bean.NetworkType;
import com.weiwobang.paotui.bean.OrderAddr;
import com.weiwobang.paotui.receiver.NetStateChangeReceiver;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmActivity extends AppCompatActivity {

    public double lat = 39.986919;
    public double lon = 116.353369;
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    com.amap.api.maps2d.AMap aMap;
    Marker now;
    Marker where;
    OrderAddr fa;
    OrderAddr shou;
    @BindView(R.id.gaomap)
    MapView mMapView;
    @BindView(R.id.view)
    TextView view;
    @BindView(R.id.layout_back)
    LinearLayout back;
    @BindView(R.id.now)
    TextView tv_now;
    @BindView(R.id.where)
    TextView tv_where;
    @BindView(R.id.ceng)
    TextView tv_ceng;
    @BindView(R.id.ceng2)
    TextView tv_ceng2;
    @BindView(R.id.sel_time)
    TextView tv_time;
    String firceng;
    String secceng;
    RouteSearch mRouteSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_confirm);
        ButterKnife.bind(this);
        initView();
        initMapView(savedInstanceState);
    }

    private void initMapView(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
        aMap = mMapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        //aMap.getUiSettings().setScrollGesturesEnabled(false);
        //aMap.getUiSettings().setZoomGesturesEnabled(false);
        if (fa != null && shou != null) {
            //setMarker(where, shou, R.layout.wwb_marker_shou);
           // setMarker(now, fa, R.layout.wwb_marker_fa);
           // setCenter(fa, shou);
        }
        mRouteSearch=new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
            @Override
            public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

            }
            @Override
            public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
                  List<DrivePath> drivePaths=driveRouteResult.getPaths();
                //DrivingRouteOverlay
                List<LatLonPoint> latLonPoints=new ArrayList<>();
                latLonPoints.add(driveRouteResult.getStartPos());
                latLonPoints.add(driveRouteResult.getTargetPos());
                Log.e("fddg","sdfg");
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(ConfirmActivity.this,aMap,drivePaths.get(0),driveRouteResult.getStartPos(),driveRouteResult.getTargetPos());
                aMap.clear();

                drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.setThroughPointIconVisibility(false);
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
            }

            @Override
            public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

            }

            @Override
            public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

            }
        });
        LatLonPoint from=new LatLonPoint(fa.getLat(),fa.getLon());
        LatLonPoint to=new LatLonPoint(shou.getLat(),shou.getLon());
        RouteSearch.FromAndTo fromAndTo=new RouteSearch.FromAndTo(from,to);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_MULTI_STRATEGY_FASTEST_SHORTEST_AVOID_CONGESTION, null, null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fa = (OrderAddr) bundle.getSerializable("fa");
            shou = (OrderAddr) bundle.getSerializable("shou");
            firceng=bundle.getString("fir");
            secceng=bundle.getString("sec");
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_now.setText(fa.getAddress());
        tv_where.setText(shou.getAddress());
        tv_ceng.setText(firceng);
        tv_ceng2.setText(secceng);
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });
    }
    private void showTimerDialog(){
        TimeSelector timeSelector = new TimeSelector(ConfirmActivity.this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                tv_time.setText(time);
               // Toast.makeText(MainActivity.this, time, Toast.LENGTH_SHORT).show();
            }
        }, "2018-01-01 00:00", "2020-12-31 23:59:59");

        timeSelector.setIsLoop(false);//设置不循环,true循环
        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
        timeSelector.show();

    }
    private void setCenter(OrderAddr fa, OrderAddr shou) {
        double lat, lon;
        lat = (fa.getLat() + shou.getLat()) / 2;
        lon = (fa.getLon() + shou.getLon()) / 2;
        LatLng center = new LatLng(lat, lon);
        //设置中心点和缩放比例
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(center));
//        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    private void setMarker(Marker marker, OrderAddr addrBean, int resId) {
        View markerView = LayoutInflater.from(this).inflate(resId, mMapView, false);
//        marker = aMap.addMarker(new MarkerOptions()
//                .position(new LatLng(addrBean.getLat(), addrBean.getLon()))
//                .icon(BitmapDescriptorFactory.fromView(markerView)));

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
        //NetStateChangeReceiver.registerObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


}
