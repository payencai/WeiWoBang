package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;

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
import com.weiwobang.paotui.adapter.PoiAdapter;
import com.weiwobang.paotui.bean.AddrBean;
import com.weiwobang.paotui.bean.NetworkType;
import com.weiwobang.paotui.bean.PoiBean;
import com.weiwobang.paotui.receiver.NetStateChangeReceiver;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelpSendActivity extends BaseActivity  {
    MapView mMapView;
    PoiSearch mPoiSearch;
    PoiSearch.Query mQuery;
    AMap aMap;
    TextView confrim;
    EditText et_name;
    EditText et_phone;
    TextView et_firAddr;
    TextView et_secAddr;
    TextView tv_confirm;
    LinearLayout selectLayout;
    ListView mListView;
    String cityCode = "";
    String keyWord = "";
    boolean isFirst = false;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    GeocodeSearch mGeocodeSearch;
    double lat;
    double lon;
    ImageView back;
    PoiAdapter mAdapter;
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    AddrBean mAddrBean;
    AddrBean mAnotherAddr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_send);
        initMapView(savedInstanceState);
        init();
        locate();

    }

    public void getAddress(LatLonPoint latLonPoint) {
        // 第二参数表示范围200米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery q = new RegeocodeQuery(latLonPoint, 2000, GeocodeSearch.GPS);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 2000, GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    List<PoiBean> mPoiBeanList = new ArrayList<>();

    private void searchPoi(String keyWord) {
        if (mAdapter != null) {
            mAdapter.clear();
            mPoiBeanList.clear();
        }

        mQuery = new PoiSearch.Query(keyWord, "", cityCode);
        mQuery.setPageSize(10);// 设置每页最多返回多少条poiitem
        mQuery.setPageNum(0);//设置查询页码
        mPoiSearch = new PoiSearch(this, mQuery);
        mPoiSearch.searchPOIAsyn();
        mPoiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                boolean flag=false;
                for (PoiItem poiItem : poiResult.getPois()) {
                    PoiBean poiBean = new PoiBean();
                    if(!flag){
                        poiBean.setIsupdateColor(true);
                        et_firAddr.setText(poiItem.getTitle());
                        et_secAddr.setText(poiItem.getSnippet());
                        flag=true;
                    }
                    poiBean.setLat(poiItem.getLatLonPoint().getLatitude());
                    poiBean.setLon(poiItem.getLatLonPoint().getLongitude());
                    poiBean.setAddress(poiItem.getSnippet());
                    poiBean.setName(poiItem.getTitle());

                    Log.e("bbb", poiItem.getSnippet() + "------" + poiItem.getTitle());
                    mPoiBeanList.add(poiBean);
                }
                mAdapter = new PoiAdapter(HelpSendActivity.this, R.layout.wwb_item_poi, mPoiBeanList);

                mListView.setAdapter(mAdapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PoiBean poiBean = mPoiBeanList.get(position);
                        lat=poiBean.getLat();
                        lon=poiBean.getLon();
                        et_firAddr.setText(poiBean.getName());
                        et_secAddr.setText(poiBean.getAddress());
                    }
                });
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

                // poiItem.getTitle();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            if(data!=null){
                mAddrBean=(AddrBean) data.getExtras().getSerializable("addrbean");
                lat=mAddrBean.getLat();
                lon=mAddrBean.getLon();
                et_firAddr.setText(mAddrBean.getFiraddr());
                et_secAddr.setText(mAddrBean.getSecaddr());
            }
        }
    }

    /**
     * by moos on 2017/09/05
     * func:获取屏幕中心的经纬度坐标
     *
     * @return
     */
    public LatLng getMapCenterPoint() {
        int left = mMapView.getLeft();
        int top = mMapView.getTop();
        int right = mMapView.getRight();
        int bottom = mMapView.getBottom();
        // 获得屏幕点击的位置
        int x = (int) (mMapView.getX() + (right - left) / 2);
        int y = (int) (mMapView.getY() + (bottom - top) / 2);
        Projection projection = aMap.getProjection();
        LatLng pt = projection.fromScreenLocation(new Point(x, y));
        return pt;
    }

    private void init() {
        mAnotherAddr= (AddrBean) getIntent().getExtras().getSerializable("address");
        int value=getIntent().getExtras().getInt("value");
        TextView title = findViewById(R.id.tv_title);
        if(value>2){
           title.setText("帮我取");
        }
        et_name=findViewById(R.id.send_name);
        et_phone=findViewById(R.id.send_phone);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        et_firAddr=findViewById(R.id.send_firAddr);
        et_secAddr=findViewById(R.id.send_secAddr);
        confrim = findViewById(R.id.header_confirm);
        mListView = (ListView) findViewById(R.id.www_ipo_listview);
        selectLayout = (LinearLayout) findViewById(R.id.wwb_select_layout);
        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                searchPoi(regeocodeResult.getRegeocodeAddress().getFormatAddress());

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
//        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
//            @Override
//            public void onCameraChange(CameraPosition cameraPosition) {
//
//            }
//
//            @Override
//            public void onCameraChangeFinish(CameraPosition cameraPosition) {
//                LatLng latLng = cameraPosition.target;
//
//                if (!isFirst) {
//                    lat = latLng.latitude;
//                    lon = latLng.longitude;
//                }
//                Log.e("lat", lon + ":" + lat);
//                getAddress(new LatLonPoint(lat, lon));
//                isFirst = false;
//                // Log.e("latlng", lat + " : " + lon);
//                //mPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lon), 5000));
//
//            }
//        });
        selectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(HelpSendActivity.this, SelectAddrActivity.class), 2);
            }
        });


        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getExtras().getInt("value") == 2 || getIntent().getExtras().getInt("value") == 4) {
                    Intent intent=new Intent();
                    Bundle bundle =new Bundle();
                    AddrBean addrBean=new AddrBean();
                    addrBean.setFiraddr(et_firAddr.getText().toString());
                    addrBean.setSecaddr(et_secAddr.getText().toString());
                    addrBean.setLat(lat);
                    addrBean.setLon(lon);
                    addrBean.setName(et_name.getEditableText().toString());
                    addrBean.setPhone(et_phone.getEditableText().toString());
                    bundle.putSerializable("addrbean",addrBean);
                    intent.putExtras(bundle);
                    setResult(1,intent);
                    finish();
                } else {
                    AddrBean addrBean=new AddrBean();
                    addrBean.setLat(lat);
                    addrBean.setLon(lon);
                    addrBean.setName(et_name.getEditableText().toString());
                    addrBean.setPhone(et_phone.getEditableText().toString());
                    addrBean.setFiraddr(et_firAddr.getText().toString());
                    addrBean.setSecaddr(et_secAddr.getText().toString());
                    Intent intent= new Intent(HelpSendActivity.this, ConfirmSendActivity.class);
                    Bundle bundle =new Bundle();
                    bundle.putSerializable("firAddr",addrBean);
                    bundle.putSerializable("secAddr",mAnotherAddr);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 3);
                }
            }
        });
    }

    private void initMapView(Bundle savedInstanceState) {
        mMapView = (MapView) findViewById(R.id.wwb_addr_mapview);
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
                        aMapLocation.getProvince();//省信息
                        aMapLocation.getCity();//城市信息
                        aMapLocation.getDistrict();//城区信息
                        aMapLocation.getStreet();//街道信息
                        aMapLocation.getStreetNum();//街道门牌号信息
                        aMapLocation.getCityCode();//城市编码
                        aMapLocation.getAdCode();//地区编码
                        aMapLocation.getAoiName();//获取当前定位点的AOI信息
                        LatLng marker1 = new LatLng(lat, lon);
                        isFirst = true;
                        cityCode = aMapLocation.getAdCode();
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
                        getAddress(new LatLonPoint(lat, lon));
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
        if (needRegisterNetworkChangeObserver()) {
            NetStateChangeReceiver.registerObserver(this);
        }
    }
    /**
     * 是否需要注册网络变化的Observer,如果不需要监听网络变化,则返回false;否则返回true.默认返回false
     */
    protected boolean needRegisterNetworkChangeObserver() {
        return true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (needRegisterNetworkChangeObserver()) {
            NetStateChangeReceiver.unregisterObserver(this);
        }
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

    @Override
    public void onNetDisconnected() {
        Toast.makeText(this,"网络已断开",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        locate();
    }
}
