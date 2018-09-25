package com.weiwobang.paotui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.activity.ConfirmActivity;
import com.weiwobang.paotui.activity.OrderaddrActivity;
import com.weiwobang.paotui.activity.RegisterActivity;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Car;
import com.weiwobang.paotui.bean.NewAddr;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryFragment extends Fragment {
    @BindView(R.id.submit_layout)
    RelativeLayout subLayout;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.vp_car)
    ViewPager mViewPager;
    @BindView(R.id.delv_addr1)
    RelativeLayout quAddr;
    @BindView(R.id.cash)
    TextView cash;
    @BindView(R.id.delv_addr2)
    RelativeLayout songAddr;
    @BindView(R.id.now)
    TextView now;
    @BindView(R.id.where)
    TextView where;
    @BindView(R.id.tv_addr1)
    TextView quAdress;
    @BindView(R.id.tv_addr2)
    TextView songAddress;
    @BindView(R.id.tv_contact1)
    TextView quContact;
    @BindView(R.id.tv_contact2)
    TextView songContact;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.et_good)
    EditText goodname;
    @BindView(R.id.et_beizhu)
    EditText desc;
    @BindView(R.id.sel_time)
    RelativeLayout re_time;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.et_km)
    EditText weight;
    @BindView(R.id.et_volume)
    EditText volume;
    @BindView(R.id.et_jianshu)
    EditText num;
    @BindView(R.id.distance)
    TextView tv_distance;
    NewAddr addrQu = null;
    NewAddr addrSong = null;
    double distance;
    private List<View> viewList = new ArrayList<>(7);
    private String[] strs = {"摩托车", "三轮车", "小轿车", "小面包车", "中面包车", "小货车", "中货车"};
    private List<Car> mCars = new ArrayList<>();
    private int position = 0;

    public DeliveryFragment() {
        // Required empty public constructor
    }

    private void addView() {
        getCars();

        for (int i = 0; i < 7; i++) {
            View view = getLayoutInflater().inflate(R.layout.layout_car_detail, null);
            TextView tv_weight = view.findViewById(R.id.car_detail_tv_weight);
            TextView tv_size = view.findViewById(R.id.car_detail_tv_size);
            TextView tv_volume = view.findViewById(R.id.car_detail_tv_volume);
            ImageView car = view.findViewById(R.id.car_detail_iv_type);
            tv_weight.setText(mCars.get(i).getWeight());
            tv_size.setText(mCars.get(i).getSize());
            tv_volume.setText(mCars.get(i).getVolume());
            car.setImageResource(mCars.get(i).getResId());
            //mTabLayout.addTab(mTabLayout.newTab().setCustomView(view));
            viewList.add(view);
        }


    }

    private void initEvn() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showTimerDialog() {
        TimeSelector timeSelector = new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                tv_time.setText(time);
            }
        }, "2018-01-01 00:00", "2020-12-31 23:59:59");

        timeSelector.setIsLoop(false);//设置不循环,true循环
        timeSelector.setMode(TimeSelector.MODE.YMDHM);//显示 年月日时分（默认）
        timeSelector.show();

    }

    private void submit(double distance) {
        String good = goodname.getText().toString();
        String beizhu = desc.getEditableText().toString();
        String time = tv_time.getText().toString();
        String w = weight.getEditableText().toString();
        String vol = volume.getEditableText().toString();
        String n = num.getEditableText().toString();
        Car car = mCars.get(position);

        if (TextUtils.isEmpty(w)) {
            ToastUtil.showToast(getContext(), "单重不能为空");
            return;
        }
        if (TextUtils.isEmpty(vol)) {
            ToastUtil.showToast(getContext(), "单体不能为空");
            return;
        }
        if (TextUtils.isEmpty(n)) {
            ToastUtil.showToast(getContext(), "件数不能为空！");
            return;
        }
        if (TextUtils.isEmpty(good)) {
            ToastUtil.showToast(getContext(), "请填写货物！");
            return;
        }
        if (TextUtils.isEmpty(time)) {
            ToastUtil.showToast(getContext(), "请选择时间！");
            return;
        }

        double we = Double.parseDouble(weight.getEditableText().toString());
        double vo = Double.parseDouble(volume.getEditableText().toString());
        int nu = Integer.parseInt(num.getEditableText().toString());
        Map<String, Object> params = new HashMap<>();
        params.put("goodsNum", nu);
        params.put("goodesWeight", we);
        params.put("goodsVolume", vo);
        params.put("goodsName", good);
        params.put("addressFrom", addrQu.getAddress());
        params.put("addressDetailFrom", addrQu.getDetail());
        params.put("lngFrom", addrQu.getLon() + "");
        params.put("latFrom", addrQu.getLat() + "");
        params.put("contactNameFrom", addrQu.getContact());
        params.put("contactNumberFrom", addrQu.getPhone());
        params.put("addressTo", addrSong.getAddress());
        params.put("addressDetailTo", addrSong.getDetail());
        params.put("lngTo", addrSong.getLon() + "");
        params.put("latTo", addrSong.getLat() + "");
        params.put("contactNameTo", addrSong.getContact());
        params.put("contactNumberTo", Integer.parseInt(addrSong.getPhone()));
        params.put("orderTime", time.substring(0, 11));
        params.put("note", beizhu);
        params.put("carTypeId", (position + 1) + "");
        params.put("carTypeName", car.getName());
        params.put("carLoad", car.getWeight());
        params.put("carVolume", car.getSize());
        params.put("carLoadVolume", car.getVolume());
        params.put("distance", distance);
        Log.e("param", params.toString());
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).addCityOrder(params, MyAPP.token2)
                // .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        if (retrofitResponse.getResultCode() == 0) {
                            Toast.makeText(getContext(), "下单成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), retrofitResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(getContext(), apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);

    }

    private Map<String, Object> getParam() {
        Map<String, Object> params = new HashMap<>();

        return params;
    }

    private void initView() {
        initEvn();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(distance);
            }
        });
        re_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });
        quAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderaddrActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 3);
                bundle.putInt("flag", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        songAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderaddrActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 3);
                bundle.putInt("flag", 2);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = viewList.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return strs[position];
            }
        });

    }


    private void getCars() {
        Car bike = new Car("摩托车", "1.9*0.8*1.1米", "200公斤", "0.8方", R.mipmap.motuoche);
        Car sanlun = new Car("三轮车", "3.5*1.2*1.8米", "370公斤", "5.8方", R.mipmap.sanlunche);
        Car jiaoche = new Car("小轿车", "3.5*1.5*1.5米", "450公斤", "1.6方", R.mipmap.jiaoche);
        Car smallTruck = new Car("小货车", "2.7*1.5*1.7米", "1吨", "6.9方", R.mipmap.lcl_xiaohuoche);
        Car middleTruck = new Car("中货车", "4.2*1.8*1.8米", "1.8吨", "13.6方", R.mipmap.lcl_zhonghuoche);
        Car smallVan = new Car("小面包车", "1.8*1.3*1.1米", "500公斤", "2.6方", R.mipmap.lcl_xiaomianbaoche);
        Car middleVan = new Car("中面包车", "2.7*1.4*1.2米", "1吨", "4.5方", R.mipmap.lcl_zhongmianbaoche);
        mCars.add(bike);
        mCars.add(sanlun);
        mCars.add(jiaoche);
        mCars.add(smallVan);
        mCars.add(middleVan);
        mCars.add(smallTruck);
        mCars.add(middleTruck);
    }

    public static DeliveryFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        DeliveryFragment fragment = new DeliveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wwb_fragment_delivery, container, false);
        ButterKnife.bind(this, view);
        addView();
        initView();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            addrQu = (NewAddr) data.getExtras().getSerializable("addr");
            now.setVisibility(View.GONE);
            quContact.setText(addrQu.getContact() + " " + addrQu.getPhone());
            quContact.setVisibility(View.VISIBLE);
            quAdress.setText(addrQu.getAddress());
            quAdress.setVisibility(View.VISIBLE);
            if (addrSong != null) {
                submit.setClickable(true);
                submit.setEnabled(true);
                submit.setBackgroundResource(R.color.yellow);
                submit.setTextColor(getResources().getColor(R.color.color_333));
                getCaldata(addrQu, addrSong);
            }
        }
        if (requestCode == 2 && data != null) {
            addrSong = (NewAddr) data.getExtras().getSerializable("addr");
            where.setVisibility(View.GONE);
            songContact.setText(addrSong.getContact() + " " + addrSong.getPhone());
            songContact.setVisibility(View.VISIBLE);
            songAddress.setText(addrSong.getAddress());
            songAddress.setVisibility(View.VISIBLE);
            if (addrQu != null) {
                submit.setClickable(true);
                submit.setEnabled(true);
                submit.setBackgroundResource(R.color.yellow);
                submit.setTextColor(getResources().getColor(R.color.color_333));
                getCaldata(addrQu, addrSong);
            }
        }
    }

    private void getCaldata(NewAddr addrQu, NewAddr addrSong) {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getDistance(addrQu.getLon(), addrQu.getLat(), addrSong.getLon(), addrSong.getLat())
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        if (retrofitResponse.getResultCode() == 0) {
                            distance = (double) retrofitResponse.getData();
                            subLayout.setVisibility(View.VISIBLE);
                            tv_distance.setText(distance + "km");
                            getMoney(distance);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(getContext(), apiException.getDisplayMessage() + "fdfdfdfewewrqewfewf", Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }

    private void getMoney(double distance) {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getMoney(distance)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                         Object money= (double) retrofitResponse.getData();
                        if (retrofitResponse.getResultCode() == 0) {
                            cash.setText("￥"+money.toString());
                        }else{
                            Toast.makeText(getContext(), retrofitResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(getContext(), apiException.getDisplayMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }
}
