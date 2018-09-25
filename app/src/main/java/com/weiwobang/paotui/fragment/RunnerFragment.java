package com.weiwobang.paotui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.TextureMapView;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.activity.ContractActivity;
import com.weiwobang.paotui.activity.LoginActivity;
import com.weiwobang.paotui.activity.OrderaddrActivity;
import com.weiwobang.paotui.activity.SellermainActivity;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.NewAddr;
import com.weiwobang.paotui.tools.PreferenceManager;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class RunnerFragment extends Fragment {

    @BindView(R.id.rl_callRunner)
    RelativeLayout callRunner;
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
    public RunnerFragment() {
        // Required empty public constructor
    }

    public static RunnerFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        RunnerFragment fragment = new RunnerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.wwb_fragment_runner, container, false);
        ButterKnife.bind(this, view);
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
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getDistance(addrQu.getLon(), addrQu.getLat(), addrSong.getLon(),addrSong.getLat())
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        double distance= (double) retrofitResponse.getData();
                        subLayout.setVisibility(View.VISIBLE);
                        tv_distance.setText(distance+"km");
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
                        Double money= (double) retrofitResponse.getData();
                        cash.setText("￥"+money);
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

    private void submit() {
        String good = goodname.getEditableText().toString();
        String liuyan = desc.getEditableText().toString();
    }

    private void initView() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(goodname.getText().toString())) {
                    ToastUtil.showToast(getContext(), "货物名称不能为空！");
                    return;
                }
                submit();
            }
        });
        quAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderaddrActivity.class);
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
                Intent intent = new Intent(getContext(), OrderaddrActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putInt("flag", 2);
                intent.putExtras(bundle);
                startActivityForResult(intent, 2);
            }
        });
        callRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyAPP.isLogin) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                } else {

                        if (PreferenceManager.getInstance().getUserinfo().getIsBusiness().equals("2"))
                            startActivity(new Intent(getContext(), SellermainActivity.class));
                        else {
                            startActivity(new Intent(getContext(), ContractActivity.class));
                        }

                }
            }
        });
    }

}
