package com.wwb.paotui.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.col.n3.io;
import com.google.gson.JsonObject;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.IpGetUtil;
import com.payencai.library.util.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.wwb.paotui.MyAPP;
import com.wwb.paotui.R;
import com.wwb.paotui.activity.ConfirmActivity;
import com.wwb.paotui.activity.LoginActivity;
import com.wwb.paotui.activity.OrderaddrActivity;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.bean.NewAddr;
import com.wwb.paotui.view.PickerView;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveFragment extends Fragment {
    @BindView(R.id.submit_layout)
    RelativeLayout subLayout;
    @BindView(R.id.remove_addr1)
    RelativeLayout reAddr1;
    @BindView(R.id.remove_addr2)
    RelativeLayout reAddr2;
    @BindView(R.id.ceng)
    TextView ceng;
    @BindView(R.id.ceng2)
    TextView ceng2;
    @BindView(R.id.selTime)
    TextView tv_time;
    @BindView(R.id.time_layout)
    RelativeLayout time;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.now)
    TextView now;
    @BindView(R.id.where)
    TextView where;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.cash)
    TextView cash;
    @BindView(R.id.distance)
    TextView tv_distance;
    NewAddr addrQu = null;
    NewAddr addrSong = null;
    String selectfloor = "无电梯四层";
    String startFloor;
    String endFloor;
    //对话框
    PickerView pickerView;

    public RemoveFragment() {
        // Required empty public constructor
    }


    public static RemoveFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        RemoveFragment fragment = new RemoveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            addrQu = (NewAddr) data.getExtras().getSerializable("addr");
            now.setText(addrQu.getAddress());

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
            where.setText(addrSong.getAddress());
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
                        double distance = (double) retrofitResponse.getData();
                        subLayout.setVisibility(View.VISIBLE);
                        tv_distance.setText(distance + "km");
                        getMoney(distance);
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

    private void getMoney(double distance) {
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getMoney(distance)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        Double money = (Double) retrofitResponse.getData();
                        Log.e("distance", money + "km" + distance);
                        cash.setText("￥" + money);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wwb_fragment_remove, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });
        ceng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShow2(1);
            }
        });
        ceng2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogShow2(2);
            }
        });
        reAddr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAPP.isLogin) {
                    Intent intent = new Intent(getContext(), OrderaddrActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 2);
                    bundle.putInt("flag", 1);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });
        reAddr2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAPP.isLogin) {
                    Intent intent = new Intent(getContext(), OrderaddrActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 2);
                    bundle.putInt("flag", 2);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 2);
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyAPP.isLogin)
                    submit();
                else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });
    }

    private void submit() {
        String contact = etContact.getEditableText().toString();
        String phone = etPhone.getEditableText().toString();
        String t = tv_time.getText().toString();
        String desc = etDesc.getEditableText().toString();
        String floorFrom = ceng.getText().toString();
        String floorTo = ceng2.getText().toString();
        Map<String, Object> params = new HashMap<>();
        if (TextUtils.isEmpty(contact)) {
            ToastUtil.showToast(getContext(), "联系人不能为空");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(getContext(), "联系电话不能为空");
            return;
        }
        if (TextUtils.equals(t, "点击选择计划搬家时间")) {
            ToastUtil.showToast(getContext(), "请选择搬家时间");
            return;
        }
        if (TextUtils.isEmpty(floorFrom)) {
            ToastUtil.showToast(getContext(), "请选择楼层！");
            return;
        }
        if (TextUtils.isEmpty(floorFrom)) {
            ToastUtil.showToast(getContext(), "请选择楼层！");
            return;
        }
        params.put("longitudeFrom", addrQu.getLon());
        params.put("latitudeFrom", addrQu.getLat());
        params.put("addressFrom", addrQu.getName());
        params.put("addressFromDetail", addrQu.getAddress());
        params.put("floorFrom", floorFrom);
        params.put("longitudeTo", addrSong.getLon());
        params.put("latitudeTo", addrSong.getLat());
        params.put("addressTo", addrSong.getName() + "");
        params.put("addressToDetail", addrSong.getAddress() + "");
        params.put("floorTo", floorTo);
        params.put("contactName", contact);
        params.put("telephoneNum", phone);
        params.put("removeTime", t.substring(0, 11));
        params.put("note", desc);
        params.put("spbillCreatIp", IpGetUtil.getIPAddress(getContext()));
        Log.e("remove", params.toString());
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).removeAdd(params, MyAPP.token2)
                // .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody retrofitResponse) throws Exception {
                        JSONObject jsonObject = new JSONObject(retrofitResponse.string());
                        String msg = jsonObject.getString("message");
                        int code = jsonObject.getInt("resultCode");
                        JSONObject data = jsonObject.getJSONObject("data");
                        Log.e("data", data.toString());
                        if (code == 0) {
                            PayReq payReq = new PayReq();
                            payReq.appId = data.getString("appid");
                            payReq.partnerId = data.getString("partnerid");
                            payReq.prepayId = data.getString("prepayid");
                            payReq.packageValue = data.getString("package");
                            payReq.nonceStr = data.getString("noncestr");
                            payReq.timeStamp = data.getString("timestamp");
                            payReq.sign = data.getString("sign");
                            MyAPP.mWxApi.sendReq(payReq);
                        } else {
                            Toast.makeText(getContext(), code + "code", Toast.LENGTH_LONG).show();
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

    private void dialogShow2(int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.wwb_picker_floor, null);

        TextView confirm = v.findViewById(R.id.confirm);
        TextView cancel = v.findViewById(R.id.cancel);
        pickerView = (PickerView) v.findViewById(R.id.picker);
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        pickerView.setData(createArrays());
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (!TextUtils.isEmpty(text))
                    selectfloor = text;
            }
        });
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
        //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (type == 1) {
                    ceng.setText(selectfloor);
                    startFloor = selectfloor;
                } else {
                    ceng2.setText(selectfloor);
                    endFloor = selectfloor;
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

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

    public void dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //初始化自定义布局参数
        LayoutInflater layoutInflater = getLayoutInflater();
        final View customLayout = layoutInflater.inflate(R.layout.wwb_picker_floor, (ViewGroup) getActivity().findViewById(R.id.customDialog));
        //为对话框设置视图
        builder.setView(customLayout);
        pickerView = (PickerView) customLayout.findViewById(R.id.picker);
        TextView confirm = customLayout.findViewById(R.id.confirm);
        TextView cancel = customLayout.findViewById(R.id.cancel);
        //定义滚动选择器的数据项
        ArrayList<String> grade = createArrays();
        final Dialog dialog = builder.create();
        //为滚动选择器设置数据
        pickerView.setData(grade);
        pickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                Log.i("tag", "选择了" + text);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //显示对话框
        builder.show();

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

}
