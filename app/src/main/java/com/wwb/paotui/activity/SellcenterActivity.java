package com.wwb.paotui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.wwb.paotui.MyAPP;
import com.wwb.paotui.R;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.bean.Event;
import com.wwb.paotui.bean.SellerInfo;
import com.wwb.paotui.bean.Userinfo;
import com.wwb.paotui.tools.BottomBar;
import com.wwb.paotui.tools.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class SellcenterActivity extends AppCompatActivity {
    @BindView(R.id.back)
    FrameLayout flBack;
    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.tvShop)
    TextView tvShop;
    @BindView(R.id.tvShopId)
    TextView tvShopId;
    @BindView(R.id.tvContact)
    TextView tvContact;
    @BindView(R.id.tvContactTel)
    TextView tvContactTel;
    @BindView(R.id.tvSellerAddr)
    TextView tvSellerAddr;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.btnRefund)
    FrameLayout btnRefund;

    @BindView(R.id.name_layout)
    FrameLayout nameLayout;
    @BindView(R.id.phone_layout)
    FrameLayout telLayout;
    @BindView(R.id.address_layout)
    LinearLayout addrLayout;
    private String lat;
    private String lng;
    private String address;
    private String addrName;
    private int payType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_sellcenter);
        ButterKnife.bind(this);
        getUserinfo();
    }

    private void initView(SellerInfo sellerInfo) {
        flBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showApplyDialog();
            }
        });
        nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDialog(0);
            }
        });
        telLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUpdateDialog(1);
            }
        });
        addrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SellcenterActivity.this, WebmapActivity.class), 1);
            }
        });
        tvTitle.setText("商家中心");
        tvShop.setText(sellerInfo.getData().getData().getBusinessName());
        tvShopId.setText(sellerInfo.getData().getData().getNo());
        tvContact.setText(sellerInfo.getData().getTel().getName());
        tvContactTel.setText(sellerInfo.getData().getTel().getTelNumber());
        tvSellerAddr.setText(sellerInfo.getData().getMap().getAdress());
        tvBalance.setText(sellerInfo.getData().getData().getBalance() + "");
    }

    public void showUpdateDialog(int type) {
        Dialog dialog = new Dialog(this, R.style.cdialog);
        dialog.setCanceledOnTouchOutside(false);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_update, null);
        EditText input = view.findViewById(R.id.et_input);
        TextView title = view.findViewById(R.id.title);
        TextView left = view.findViewById(R.id.left);

        if (type == 0) {
            title.setText("请输入联系人");
            left.setText("姓名");
        } else {
            title.setText("请输入联系电话");
            left.setText("电话");
            //input.setInputType(InputType.TYPE_CLASS_PHONE);
        }
        TextView cancel = view.findViewById(R.id.cancel);
        TextView confirm = view.findViewById(R.id.confirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String content = input.getEditableText().toString();
                if (!TextUtils.isEmpty(content))
                    if (type == 0)
                        updateName(content);
                    else if (type == 1)
                        updateTel(content);
                    else {
                        ToastUtil.showToast(SellcenterActivity.this, "输入不能为空");
                    }
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();

    }

    private void showApplyDialog() {
        Dialog dialog = new Dialog(this, R.style.DialogStyleNoTitle);
        dialog.setCanceledOnTouchOutside(false);
        View view = getLayoutInflater().inflate(R.layout.dialog_seller_apply, null);
        FrameLayout fl = (FrameLayout) view.findViewById(R.id.flMask);
        fl.getBackground().mutate().setAlpha((int)(255*0.6));
        FrameLayout alipay = view.findViewById(R.id.flMethodAlipay);
        FrameLayout bank = view.findViewById(R.id.flMethodBank);
        FrameLayout submit = view.findViewById(R.id.submit);
        ImageView back=view.findViewById(R.id.imgClose);
        EditText money=((EditText) view.findViewById(R.id.edtWithdrawMoney));
        EditText name=((EditText) view.findViewById(R.id.edtWithdrawPerson));
        EditText account= ((EditText) view.findViewById(R.id.edtWithdrawAccount));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  finish();
                dialog.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sWithdrawCash = money.getText().toString();
                String sWithdrawPerson = name.getText().toString();
                String sWithdrawAccount =account.getText().toString();
                if (TextUtils.isEmpty(sWithdrawCash)) {
                    ToastUtil.showToast(SellcenterActivity.this, "金额不能为空");
                    return;
                }
                if (TextUtils.isEmpty(sWithdrawPerson)) {
                    ToastUtil.showToast(SellcenterActivity.this, "姓名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(sWithdrawAccount)) {
                    ToastUtil.showToast(SellcenterActivity.this, "账号不能为空");
                    return;
                }
                apply(sWithdrawCash, sWithdrawPerson, sWithdrawAccount);
            }
        });
        alipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payType = 1;
                alipay.findViewWithTag("BG").setVisibility(View.VISIBLE);
                bank.findViewWithTag("BG").setVisibility(View.INVISIBLE);
                ((TextView) alipay.findViewWithTag("TXT"))
                        .setTextColor(getResources().getColor(R.color.yellow_1e));
                ((TextView) bank.findViewWithTag("TXT"))
                        .setTextColor(getResources().getColor(R.color.white));

            }
        });
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payType = 2;
                alipay.findViewWithTag("BG").setVisibility(View.INVISIBLE);
                bank.findViewWithTag("BG").setVisibility(View.VISIBLE);
                ((TextView) alipay.findViewWithTag("TXT"))
                        .setTextColor(getResources().getColor(R.color.white));
                ((TextView) bank.findViewWithTag("TXT"))
                        .setTextColor(getResources().getColor(R.color.yellow_1e));
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public void getUserinfo() {
        Disposable disposable = NetWorkManager.getReq(ApiService.class).getPesonalCenter(MyAPP.token)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        SellerInfo sellerInfo = GsonUtils.parseJSON(result, SellerInfo.class);
                        if (sellerInfo.getResultCode() == 0) {
                            initView(sellerInfo);
                        } else {
                            ToastUtil.showToast(SellcenterActivity.this, sellerInfo.getMessage());
                        }
                        Log.e("body", result);

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(SellcenterActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            address = data.getExtras().getString("addr");
            addrName = data.getExtras().getString("name");
            lng = data.getExtras().getString("lng");
            lat = data.getExtras().getString("lat");
            tvSellerAddr.setText(address);
            updateAddress();

        }
    }

    private void updateAddress() {
        Disposable disposable = NetWorkManager.getReq(ApiService.class).addMap(lng, lat, address, addrName, MyAPP.token)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        try {
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("message");
                            int code = object.getInt("resultCode");
                            if (code == 0) {
                                ToastUtil.showToast(SellcenterActivity.this, "地址更新成功");
                            } else {
                                ToastUtil.showToast(SellcenterActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(SellcenterActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void updateName(String name) {
        Log.e("name", "dffgfg" + name);
        Disposable disposable = NetWorkManager.getReq(ApiService.class).addName(name, MyAPP.token)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        try {
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("message");
                            int code = object.getInt("resultCode");
                            if (code == 0) {
                                ToastUtil.showToast(SellcenterActivity.this, "名字更新成功");
                                tvContact.setText(name);
                                //tvContact.setTextColor(getResources().getColor(R.color.black_33));
                            } else {
                                ToastUtil.showToast(SellcenterActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(SellcenterActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void updateTel(String tel) {
        Disposable disposable = NetWorkManager.getReq(ApiService.class).addTel(tel, MyAPP.token)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        try {
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("message");
                            int code = object.getInt("resultCode");
                            if (code == 0) {
                                tvContactTel.setText(tel);
                                ToastUtil.showToast(SellcenterActivity.this, "电话更新成功");
                            } else {
                                ToastUtil.showToast(SellcenterActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(SellcenterActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void apply(String money, String name, String account) {
        Disposable disposable = NetWorkManager.getReq(ApiService.class).apply(Integer.parseInt(money), payType + "", name, account, MyAPP.token)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        try {
                            JSONObject object = new JSONObject(result);
                            String msg = object.getString("message");
                            int code = object.getInt("resultCode");
                            if (code == 0) {
                                ToastUtil.showToast(SellcenterActivity.this, "申请已经提交成功");
                            } else {
                                ToastUtil.showToast(SellcenterActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(SellcenterActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }
}
