package com.wwb.paotui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.wwb.paotui.MyAPP;
import com.wwb.paotui.R;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.bean.NewAddr;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class TakeOrderActivity extends AppCompatActivity {
    @BindView(R.id.addr_layout)
    RelativeLayout quAddr;
    @BindView(R.id.addr2_layout)
    RelativeLayout songAddr;
    @BindView(R.id.goodname)
    EditText goodname;
    @BindView(R.id.desc)
    EditText desc;
    @BindView(R.id.now)
    TextView now;
    @BindView(R.id.where)
    TextView where;
    @BindView(R.id.quAddress)
    TextView quAdress;
    @BindView(R.id.songAddress)
    TextView songAddress;
    @BindView(R.id.quContact)
    TextView quContact;
    @BindView(R.id.songContact)
    TextView songContact;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.submit_layout)
    RelativeLayout subLayout;
    RelativeLayout seladdr;
    NewAddr addrQu = null;
    NewAddr addrSong = null;
    @BindView(R.id.cash)
    TextView cash;
    @BindView(R.id.distance)
    TextView tv_distance;
    @BindView(R.id.orderback)
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        ButterKnife.bind(this);
        initView();
    }
    private void initView(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                if (TextUtils.isEmpty(goodname.getText().toString())) {
//                    ToastUtil.showToast(TakeOrderActivity.this, "货物名称不能为空！");
//                    return;
//                }
                submit();
            }
        });
        quAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TakeOrderActivity.this, OrderaddrActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putInt("flag", 1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
        songAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TakeOrderActivity.this, OrderaddrActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putInt("flag", 2);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
            }
        });
    }
    private void submit(){

        Map<String, Object> params = new HashMap<>();
        String good = goodname.getEditableText().toString();
        String liuyan = desc.getEditableText().toString();
        String nameFrom = addrQu.getContact();
        String telnumFrom = addrQu.getPhone();
        String adressFrom = addrQu.getAddress();
        String headingFrom = addrQu.getName();
        String longitudeFrom = addrQu.getLon() + "";
        String latitudeFrom = addrQu.getLat() + "";
        String nameTo = addrSong.getContact();
        String telnumTo = addrSong.getPhone();
        String adressTo = addrSong.getAddress() ;
        String headingTo = addrSong.getName();
        String longitudeTo = addrSong.getLon() + "";
        String latitudeTo = addrSong.getLat() + "";
        params.put("buyerName",nameTo);
        params.put("buyerTelnum",telnumTo);
        params.put("longitude",longitudeTo);
        params.put("latitude",latitudeTo);
        params.put("adress",adressTo);
        params.put("heading",headingTo);
        params.put("nameFrom",nameFrom);
        params.put("telnumFrom",telnumFrom);
        params.put("longitudeFrom",longitudeFrom);
        params.put("latitudeFrom",latitudeFrom);
        params.put("adressFrom",adressFrom);
        params.put("headingFrom",headingFrom);
        params.put("goodsName",good);
        params.put("note",liuyan);
        Log.e("pa",params.toString());
        Disposable disposable = NetWorkManager.getReq(ApiService.class).addOrder(params, MyAPP.token)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        try {
                            String result = responseBody.string();
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("message");
                            int code = object.getInt("resultCode");
                            if (code == 0) {

                                ToastUtil.showToast(TakeOrderActivity.this, "下单成功");
                                //getHomePage();

                            } else {
                                ToastUtil.showToast(TakeOrderActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(TakeOrderActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
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
        Log.e("dis",addrQu.getLat()+"-"+addrQu.getLon()+","+addrSong.getLat()+"-"+addrSong.getLon());
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getDistance(addrQu.getLon(), addrQu.getLat(), addrSong.getLon(), addrSong.getLat())
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        double distance = (double) retrofitResponse.getData();
                        subLayout.setVisibility(View.VISIBLE);
                        tv_distance.setText(distance + "km");
                        getMoney(distance);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(TakeOrderActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
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
                        Double money = (double) retrofitResponse.getData();
                        cash.setText("￥" + money);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(TakeOrderActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }
}
