package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.PoiAdapter;
import com.weiwobang.paotui.bean.AddrBean;
import com.weiwobang.paotui.bean.PoiBean;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.HotCity;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectAddrActivity extends AppCompatActivity {
    MapView mMapView;
    PoiSearch mPoiSearch;
    PoiSearch.Query mQuery;
    List<PoiBean> mPoiBeanList=new ArrayList<>();
    AMap aMap;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    GeocodeSearch mGeocodeSearch;
    double lat;
    double lon;
    boolean isFirst=false;
    PoiAdapter mAdapter;
    TextView confirm;
    TextView title;
    AddrBean mAddrBean;
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_selectaddr);
        initMapView(savedInstanceState);
        init();
    }
    private void init(){
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mListView= (ListView) findViewById(R.id.select_listview);
        mGeocodeSearch=new GeocodeSearch(this);
        title=findViewById(R.id.tv_title);
        title.setText("选择地址");
        findViewById(R.id.header_confirm).setVisibility(View.GONE);
        mGeocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                searchPoi(regeocodeResult.getRegeocodeAddress().getFormatAddress());

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LatLng latLng = cameraPosition.target;

                if (!isFirst) {
                    lat = latLng.latitude;
                    lon = latLng.longitude;
                }
                Log.e("lat", lon + ":" + lat);
                getAddress(new LatLonPoint(lat,lon));
                isFirst = false;
                // Log.e("latlng", lat + " : " + lon);
                //mPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lon), 5000));

            }
        });
        findViewById(R.id.citypicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFindCity();
               //startActivityForResult(new Intent(SelectAddrActivity.this,CityPickerActivity.class),4);
            }
        });
        locate();
    }

    ListView mListView;
    String cityCode="";

    private void searchPoi(String keyWord) {
        if (mAdapter!=null){
            mAdapter.clear();
            mPoiBeanList.clear();
        }
        mQuery = new PoiSearch.Query(keyWord, "",cityCode);
        mQuery.setPageSize(10);// 设置每页最多返回多少条poiitem
        mQuery.setPageNum(0);//设置查询页码
        mPoiSearch = new PoiSearch(this, mQuery);
        mPoiSearch.searchPOIAsyn();
        mPoiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                boolean flag=false;
                for(PoiItem poiItem:poiResult.getPois()){
                    PoiBean poiBean=new PoiBean();
                    poiBean.setLon(poiItem.getLatLonPoint().getLongitude());
                    poiBean.setLat(poiItem.getLatLonPoint().getLatitude());
                    poiBean.setAddress(poiItem.getSnippet());
                    poiBean.setName(poiItem.getTitle());
                    if(!flag){
                        flag=true;
                        poiBean.setIsupdateColor(true);
                    }
                    Log.e("bbb", poiItem.getSnippet()+"------"+poiItem.getTitle());
                    mPoiBeanList.add(poiBean);
                }
                mAdapter=new PoiAdapter(SelectAddrActivity.this,R.layout.wwb_item_poi,mPoiBeanList);

                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PoiBean poiBean = mPoiBeanList.get(position);
                        Intent intent=new Intent();
                        Bundle bundle =new Bundle();
                        AddrBean addrBean=new AddrBean();
                        addrBean.setFiraddr(poiBean.getName());
                        addrBean.setSecaddr(poiBean.getAddress());
                        addrBean.setLat(lat);
                        addrBean.setLon(lon);
                        bundle.putSerializable("addrbean",addrBean);
                        intent.putExtras(bundle);
                        setResult(3,intent);
                        finish();

                    }
                });
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

                // poiItem.getTitle();
            }
        });

    }
    public void getAddress(LatLonPoint latLonPoint) {
        // 第二参数表示范围200米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery q = new RegeocodeQuery(latLonPoint, 2000, GeocodeSearch.GPS);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 2000, GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }
    private void initMapView(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.select_mapview);
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
        aMap = mMapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setScaleControlsEnabled(false);
    }
    private void locate() {

        mlocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置一次定位
        mLocationOption.setOnceLocation(true);
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        lat = aMapLocation.getLatitude();//获取纬度
                        lon = aMapLocation.getLongitude();//获取经度
                        Log.e("lat", lat + ":" + lon);
                        aMapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(aMapLocation.getTime());
                        df.format(date);//定位时间
                        aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                        aMapLocation.getCountry();//国家信息
                        province=aMapLocation.getProvince();//省信息
                        city=aMapLocation.getCity();//城市信息
                        aMapLocation.getDistrict();//城区信息
                        aMapLocation.getStreet();//街道信息
                        aMapLocation.getStreetNum();//街道门牌号信息
                        cityCode=aMapLocation.getCityCode();//城市编码
                        aMapLocation.getAdCode();//地区编码
                        aMapLocation.getAoiName();//获取当前定位点的AOI信息
                        LatLng marker1 = new LatLng(lat, lon);
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        Log.e("add", aMapLocation.getAddress()+"---"+aMapLocation.getStreet());
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }

            }
        });
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }
    String city = "xxx";
    String province = "xxx";
    private void startFindCity() {
        List<HotCity> hotCities;
        hotCities = new ArrayList<>();
        hotCities.add(new HotCity("北京", "北京", "101010100"));
        hotCities.add(new HotCity("上海", "上海", "101020100"));
        hotCities.add(new HotCity("广州", "广东", "101280101"));
        hotCities.add(new HotCity("深圳", "广东", "101280601"));
        hotCities.add(new HotCity("杭州", "浙江", "101210101"));
        CityPicker.getInstance()
                .setFragmentManager(getSupportFragmentManager())    //此方法必须调用
                .enableAnimation(true)    //启用动画效果
                .setLocatedCity(new LocatedCity(city, province, "101210101"))
                .setHotCities(null)
                .setOnPickListener(new OnPickListener() {
                    @Override
                    public void onPick(int position, City data) {

                    }

                    @Override
                    public void onLocate() {
                        //开始定位，这里模拟一下定位
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                CityPicker.getInstance()
//                                        .locateComplete(new LocatedCity("深圳", "广东", "101280601"), LocateState.SUCCESS);
//                            }
//                        }, 2000);
                    }
                })
                .show();
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
