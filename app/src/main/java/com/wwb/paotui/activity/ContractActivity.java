package com.wwb.paotui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.wwb.paotui.R;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.tools.PreferenceManager;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ContractActivity extends AppCompatActivity {

    @BindView(R.id.shopname)
    EditText name;
    @BindView(R.id.sellercontact)
    EditText man;
    @BindView(R.id.selleraddr)
    EditText address;
    @BindView(R.id.sellerphone)
    EditText phone;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.back)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_contract);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void submit() {
        String shopname = name.getEditableText().toString();
        String contact = man.getEditableText().toString();
        String tel = phone.getEditableText().toString();
        String addr = address.getEditableText().toString();
        if (TextUtils.isEmpty(shopname)) {
            ToastUtil.showToast(ContractActivity.this, "名称不能为空！");
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            ToastUtil.showToast(ContractActivity.this, "联系人不能为空！");
            return;
        }
        if (TextUtils.isEmpty(tel)) {
            ToastUtil.showToast(ContractActivity.this, "电话不能为空！");
            return;
        }
        if (TextUtils.isEmpty(addr)) {
            ToastUtil.showToast(ContractActivity.this, "地址不能为空！");
            return;
        }

        String token = PreferenceManager.getInstance().getUserinfo().getToken();

        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postAddSeller(shopname, contact, tel, addr, token)
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse>() {
                    @Override
                    public void accept(RetrofitResponse retrofitResponse) throws Exception {
                        if (retrofitResponse.getResultCode() == 0) {
                            Toast.makeText(ContractActivity.this, "申请成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ContractActivity.this, SuccActivity.class));
                            finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(ContractActivity.this, apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }
}
