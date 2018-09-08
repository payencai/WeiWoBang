package com.weiwobang.paotui.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.activity.ConfirmActivity;
import com.weiwobang.paotui.activity.HelpSendActivity;
import com.weiwobang.paotui.activity.LoginActivity;
import com.weiwobang.paotui.activity.OrderaddrActivity;
import com.weiwobang.paotui.bean.AddrBean;
import com.weiwobang.paotui.bean.NetworkType;
import com.weiwobang.paotui.bean.OrderAddr;
import com.weiwobang.paotui.callback.NetStateChangeObserver;
import com.weiwobang.paotui.receiver.NetStateChangeReceiver;
import com.wx.wheelview.widget.WheelViewDialog;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WwbFragment extends Fragment implements NetStateChangeObserver {

    public double lat = 39.986919;
    public double lon = 116.353369;
    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption = null;
    ImageView iv_locate;
    ImageView iv_inform;
    TextureMapView mMapView;
    TextView tv_send;
    TextView tv_get;
    LinearLayout send_layout;
    LinearLayout get_layout;
    RelativeLayout send_first;
    RelativeLayout send_sec;
    RelativeLayout get_first;
    RelativeLayout get_sec;
    TextView send_firstAddr;
    TextView send_secAddr;
    TextView send_mude;
    TextView get_firstAddr;
    TextView get_secAddr;
    TextView get_mude;
    TextView now;
    TextView where;
    TextView tv_ceng;
    TextView tv_ceng2;
    AMap aMap;
    Marker marker;
    AddrBean mAddrBean;
    OrderAddr nowAddr;
    OrderAddr whereAddr;


    public WwbFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //删除指定Marker
    private void clearMarkers() {
        //获取地图上所有Marker
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        for (int i = 0; i < mapScreenMarkers.size(); i++) {
            Marker marker1 = mapScreenMarkers.get(i);
            if (marker1.equals(marker)) {
                marker1.remove();//移除当前Marker
                Log.e("mar", marker.getTitle());
            }
        }
    }

    private void locate() {
        aMap.clear();
        mlocationClient = new AMapLocationClient(getContext());
        mLocationOption = new AMapLocationClientOption();
        //mLocationOption.setGpsFirst(true);

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
                        send_firstAddr.setText(aMapLocation.getStreet());
                        send_secAddr.setText(aMapLocation.getAddress());
                        get_firstAddr.setText(aMapLocation.getStreet());
                        get_secAddr.setText(aMapLocation.getAddress());
                        aMapLocation.getDistrict();//城区信息
                        aMapLocation.getStreet();//街道信息
                        aMapLocation.getStreetNum();//街道门牌号信息
                        aMapLocation.getCityCode();//城市编码
                        aMapLocation.getAdCode();//地区编码
                        aMapLocation.getAoiName();//获取当前定位点的AOI信息
                        View markerView = LayoutInflater.from(getContext()).inflate(R.layout.wwb_marker_layout, mMapView, false);
                        marker = aMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lon))
                                .icon(BitmapDescriptorFactory.fromView(markerView)));
                        LatLng marker1 = new LatLng(lat, lon);
                        //设置中心点和缩放比例
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));

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

    public static WwbFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        WwbFragment fragment = new WwbFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.wwb_wwb_fragment, container, false);
        Log.e("sha", sHA1(getContext()));
        initView(view);
        initMapView(view, savedInstanceState);
        locate();

        return view;
    }

    public void showDialog(View view, int flag) {
        WheelViewDialog dialog = new WheelViewDialog(getContext());
        dialog.setOnDialogItemClickListener(new WheelViewDialog.OnDialogItemClickListener() {
            @Override
            public void onItemClick(int position, String s) {
                dialog.dismiss();
                if (flag == 0) {
                    tv_ceng.setText(s);
                } else {
                    tv_ceng2.setText(s);
                }
                if (isJump()) {
                    Intent intent = new Intent(getContext(), ConfirmActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fa", nowAddr);
                    bundle.putSerializable("shou", whereAddr);
                    bundle.putString("fir", tv_ceng.getText().toString());
                    bundle.putString("sec", tv_ceng2.getText().toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,7);
                }

            }
        });
        dialog.setTitle("楼层选择内容").setItems(createArrays()).setButtonText("确定").setDialogStyle(Color
                .parseColor("#6699ff")).setCount(5).show();

    }

    private ArrayList<String> createArrays() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("有电梯");
        list.add("无电梯一层");
        list.add("无电梯二层");
        list.add("无电梯三层");
        list.add("无电梯四层");
        list.add("无电梯五层");
        list.add("无电梯六层");
        list.add("无电梯七层及以上");


        return list;
    }

    private boolean isJump() {
        if (TextUtils.equals("楼层", tv_ceng.getText()))
            return false;
        if (TextUtils.equals("楼层", tv_ceng2.getText()))
            return false;
        if (nowAddr == null)
            return false;
        if (whereAddr == null)
            return false;
        return true;
    }

    private void initView(View view) {
        iv_locate = (ImageView) view.findViewById(R.id.iv_home_locate);
        iv_inform = (ImageView) view.findViewById(R.id.iv_home_msg);
        iv_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locate();
            }
        });
        now = view.findViewById(R.id.now);
        where = view.findViewById(R.id.where);
        tv_send = (TextView) view.findViewById(R.id.wwb_send_tv);
        tv_get = (TextView) view.findViewById(R.id.wwb_get_tv);
        send_layout = (LinearLayout) view.findViewById(R.id.wwb_send_layout);
        get_layout = (LinearLayout) view.findViewById(R.id.wwb_getforme_layout);
        send_first = (RelativeLayout) view.findViewById(R.id.wwb_shou_re);
        send_sec = (RelativeLayout) view.findViewById(R.id.wwb_shou_fa);
        get_first = (RelativeLayout) view.findViewById(R.id.wwb_qu_layout);
        get_sec = (RelativeLayout) view.findViewById(R.id.wwb_sou_layout);
        send_firstAddr = (TextView) view.findViewById(R.id.wwb_address_first);
        send_secAddr = (TextView) view.findViewById(R.id.wwb_sec_address);
        send_mude = (TextView) view.findViewById(R.id.wwb_mude_address);
        get_firstAddr = (TextView) view.findViewById(R.id.wwb_locate_first);
        get_secAddr = (TextView) view.findViewById(R.id.wwb_locate_sec);
        get_mude = (TextView) view.findViewById(R.id.wwb_shou_address);
        tv_ceng = view.findViewById(R.id.ceng);
        tv_ceng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAPP.isLogin)
                    showDialog(view, 0);
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        tv_ceng2 = view.findViewById(R.id.ceng2);
        tv_ceng2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAPP.isLogin)
                    showDialog(view, 1);
                else
                    startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        view.findViewById(R.id.helpme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        send_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpSendActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("value", 1);
                AddrBean addrBean = new AddrBean();
                addrBean.setLat(lat);
                addrBean.setLon(lon);
                addrBean.setName(name);
                addrBean.setPhone(phone);
                addrBean.setFiraddr(send_firstAddr.getText().toString());
                addrBean.setSecaddr(send_secAddr.getText().toString());
                bundle.putSerializable("address", addrBean);
                bundle.putString("addr", send_firstAddr.getText().toString() + ":" + send_secAddr.getText().toString());
                bundle.putString("latlon", lat + ":" + lon);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        send_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HelpSendActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("value", 2);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
            }
        });
        tv_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_layout.setVisibility(View.GONE);
                get_layout.setVisibility(View.VISIBLE);
                tv_send.setTextColor(getResources().getColor(R.color.color_333));
                tv_get.setTextColor(getResources().getColor(R.color.press_yellow));
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_layout.setVisibility(View.VISIBLE);
                get_layout.setVisibility(View.GONE);
                tv_send.setTextColor(getResources().getColor(R.color.press_yellow));
                tv_get.setTextColor(getResources().getColor(R.color.color_333));
            }
        });
        get_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HelpSendActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("value", 3);
                AddrBean addrBean = new AddrBean();
                addrBean.setLat(lat);
                addrBean.setLon(lon);
                addrBean.setName(name);
                addrBean.setPhone(phone);
                addrBean.setFiraddr(get_firstAddr.getText().toString());
                addrBean.setSecaddr(get_secAddr.getText().toString());
                bundle.putSerializable("address", addrBean);
                bundle.putString("addr", get_firstAddr.getText().toString() + ":" + get_secAddr.getText().toString());
                bundle.putString("latlon", lat + ":" + lon);

                //bundle.putSerializable("address",mAddrBean);
                intent.putExtras(bundle);
                startActivityForResult(intent, 3);
            }
        });
        get_sec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HelpSendActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("value", 4);
                intent.putExtras(bundle);
                startActivityForResult(intent, 4);
            }
        });
        view.findViewById(R.id.now).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAPP.isLogin) {
                    Intent intent = new Intent(getContext(), OrderaddrActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("addr", 0);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 5);
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }

            }
        });
        view.findViewById(R.id.where).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAPP.isLogin) {
                    Intent intent = new Intent(getContext(), OrderaddrActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("addr", 1);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 6);
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });
    }

    String name;
    String phone;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 2) {
            Log.e("data", 1 + "");
            if (data != null) {
                mAddrBean = (AddrBean) data.getExtras().getSerializable("addrbean");
                lat = mAddrBean.getLat();
                lon = mAddrBean.getLon();
                send_firstAddr.setText(mAddrBean.getFiraddr());
                send_secAddr.setText(mAddrBean.getSecaddr());
                name = mAddrBean.getName();
                phone = mAddrBean.getPhone();
            }
        }
        if (requestCode == 4) {
            if (data != null) {
                mAddrBean = (AddrBean) data.getExtras().getSerializable("addrbean");
                lat = mAddrBean.getLat();
                lon = mAddrBean.getLon();
                get_firstAddr.setText(mAddrBean.getFiraddr());
                get_secAddr.setText(mAddrBean.getSecaddr());
                name = mAddrBean.getName();
                phone = mAddrBean.getPhone();
            }
            Log.e("data", 1 + "");
        }
        if (requestCode == 5) {
            if (data != null) {
                nowAddr = (OrderAddr) data.getExtras().getSerializable("addr");
                now.setText(nowAddr.getAddress());
                if (isJump()) {
                    Intent intent = new Intent(getContext(), ConfirmActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fa", nowAddr);
                    bundle.putSerializable("shou", whereAddr);
                    bundle.putString("fir", tv_ceng.getText().toString());
                    bundle.putString("sec", tv_ceng2.getText().toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,8);
                }
            }
        }
        if (requestCode == 6) {
            if (data != null) {
                whereAddr = (OrderAddr) data.getExtras().getSerializable("addr");
                where.setText(whereAddr.getAddress());
                if (isJump()) {
                    Intent intent = new Intent(getContext(), ConfirmActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fa", nowAddr);
                    bundle.putSerializable("shou", whereAddr);
                    bundle.putString("fir", tv_ceng.getText().toString());
                    bundle.putString("sec", tv_ceng2.getText().toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent,9);
                }
            }
        }
        if(requestCode>=7){
            clearData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clearData() {
        nowAddr=null;
        whereAddr=null;
        now.setText("你目前在哪里");
        where.setText("你要搬去哪儿");
        tv_ceng.setText("楼层");
        tv_ceng2.setText("楼层");
    }

    private void initMapView(View view, Bundle savedInstanceState) {
        mMapView = (TextureMapView) view.findViewById(R.id.gaomap);
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
        aMap = mMapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        //aMap.getUiSettings().setScrollGesturesEnabled(false);
        aMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locate();
            } else {
                // Permission Denied
            }
        }


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
        NetStateChangeReceiver.registerObserver(this);
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
        NetStateChangeReceiver.unregisterObserver(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onNetDisconnected() {
        Toast.makeText(getContext(), "网络已断开", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        locate();
    }
}
