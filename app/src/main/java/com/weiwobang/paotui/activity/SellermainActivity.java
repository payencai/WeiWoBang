package com.weiwobang.paotui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.weiwobang.paotui.JPush.JpushConfig;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Event;
import com.weiwobang.paotui.bean.SellerInfo;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.BottomBar;
import com.weiwobang.paotui.tools.DataGenerator;
import com.weiwobang.paotui.tools.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class SellermainActivity extends AppCompatActivity {
    @BindView(R.id.imgSettingOfSeller)
    ImageView center;
    @BindView(R.id.imgNotificationOfSeller)
    ImageView notify;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.btnDispatch)
    FrameLayout order;
    @BindView(R.id.tvNameOfSeller)
    TextView shopname;
    @BindView(R.id.tvNumOfSeller)
    TextView tv_balance;
    private TabLayout mTabLayout;
    private Fragment[] mFragmensts;
    private int num = 0;
    private String addrName;
    private String address;
    private String lat;
    private String lon;
    private Dialog dialog;
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_sellermain);
        ButterKnife.bind(this);
        ActManager.getAppManager().addActivity(this);
        mFragmensts = BottomBar.getFragments("TabLayout Tab");
        addFragments();
        initView();
    }


    private void getHomePage() {
        Log.e("token", MyAPP.token);
        Disposable disposable = NetWorkManager.getReq(ApiService.class).getHomePage(MyAPP.token)
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
                                //ToastUtil.showToast("下单成功");
                                Log.e("home", object.toString());
                                object = object.getJSONObject("data");
                                JSONObject info = object.getJSONObject("info");
                                JSONObject data = object.getJSONObject("data");
                                MyAPP.alias = info.getString("alias");
                                String balance = data.getDouble("balcance") + "";
                                int finishedCount = data.getInt("finishedCount");
                                int inprocessCount = data.getInt("inprocessCount");
                                String bussName = info.getString("bussName");

                                JpushConfig.getInstance().resumeJPush();
                                JpushConfig.getInstance().setAlias(MyAPP.alias);
                                JpushConfig.getInstance().setTag(MyAPP.alias);
                                // 提供自定义的布局添加Tab
                                if (isFirst) {
                                    isFirst = false;
                                    for (int i = 0; i < 2; i++) {
                                        if (i == 0)
                                            num = inprocessCount;
                                        else
                                            num = finishedCount;
                                        mTabLayout.addTab(mTabLayout.newTab().setCustomView(BottomBar.getTabView(SellermainActivity.this, i, num)));
                                    }
                                } else {
                                    EventBus.getDefault().post(new Event(0));//告诉MainActivity,fragment1中的按钮点击了
                                    // ToastUtil.showToast(SellermainActivity.this,"hhhhhhhhhhhhhhhh");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            View tab0 = mTabLayout.getTabAt(0).getCustomView();
                                            TextView now = tab0.findViewById(R.id.tab_num);
                                            now.setText(inprocessCount + "");
                                        }
                                    });


                                }

                                tv_balance.setText("余额 : " + balance);
                                shopname.setText(bussName);

                            } else {
                                ToastUtil.showToast(SellermainActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(SellermainActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellermainActivity.this, SellcenterActivity.class));
            }
        });
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SellermainActivity.this, NotifyActivity.class));
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    TextView num = (TextView) view.findViewById(R.id.tab_num);
                    TextView name = (TextView) view.findViewById(R.id.tab_name);
                    if (i == tab.getPosition()) { // 选中状态
                        num.setTextColor(getResources().getColor(R.color.yellow_1e));
                        name.setTextColor(getResources().getColor(R.color.yellow_1e));
                    } else {// 未选中状态
                        num.setTextColor(getResources().getColor(R.color.tv_666));
                        name.setTextColor(getResources().getColor(R.color.tv_666));
                    }
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                Log.e("pos", tab.getPosition() + "");
//                if (tab.getPosition() == 2 && !MyAPP.isLogin) {
//                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//
//                } else {
//
//                }
            }
        });
        getHomePage();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }

    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            addrName = data.getExtras().getString("name");
            address = data.getExtras().getString("addr");
            lon = data.getExtras().getString("lng");
            lat = data.getExtras().getString("lat");
            ((TextView) dialog.findViewById(R.id.tvReceiveAddr)).setText(address);
        }
        if (requestCode == 2 && data != null) {
            ToastUtil.showToast(SellermainActivity.this, "测试");
            // getHomePage();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void FragmentButtonClicked(Event code) {
        switch (code.getCode()) {
            case 1:
                View tab0 = mTabLayout.getTabAt(0).getCustomView();
                View tab1 = mTabLayout.getTabAt(1).getCustomView();
                TextView his = tab1.findViewById(R.id.tab_num);
                TextView now = tab0.findViewById(R.id.tab_num);
                int curr_num = Integer.parseInt(now.getText().toString());
                int his_num = Integer.parseInt(his.getText().toString());
                his_num++;
                if (curr_num > 0) {
                    curr_num--;
                    now.setText(curr_num + "");
                }
                his.setText(his_num + "");
                break;
            default:
                break;
        }
    }

    private void submit(String name, String phone) {
        Disposable disposable = NetWorkManager.getReq(ApiService.class).addOrder(name, phone, lon, lat, address, addrName, MyAPP.token)
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
                                dialog.dismiss();
                                ToastUtil.showToast(SellermainActivity.this, "下单成功");
                                getHomePage();

                            } else {
                                ToastUtil.showToast(SellermainActivity.this, msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(SellermainActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void showDialog() {

        dialog = new Dialog(this, R.style.DialogStyleNoTitle);
        View view = getLayoutInflater().inflate(R.layout.dialog_seller_dispatch, null);
        ((TextView) view.findViewById(R.id.oneKeyCall)).setText(Html.fromHtml("<big><big>一键呼叫</big></big><br>（收货信息由跑腿输入）"));
        FrameLayout fl = (FrameLayout) view.findViewById(R.id.flMask);
        fl.getBackground().mutate().setAlpha((int)(255*0.6));
        dialog.setCanceledOnTouchOutside(false);
        EditText name = view.findViewById(R.id.edtReceiver);
        EditText phone = view.findViewById(R.id.edtTel);
        view.findViewById(R.id.flReceiveAddr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SellermainActivity.this, WebmapActivity.class), 1);
            }
        });
        view.findViewById(R.id.imgClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(name.getText().toString())) {
                    ToastUtil.showToast(dialog.getContext(), "姓名不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    ToastUtil.showToast(dialog.getContext(), "电话不能为空！");
                    return;
                }
//                if (TextUtils.isEmpty(lat)) {
//                    ToastUtil.showToast(dialog.getContext(),"地址不能为空！");
//                    return ;
//                }
                submit(name.getText().toString(), phone.getText().toString());
            }
        });
        view.findViewById(R.id.oneKeyCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(name.getText().toString(), phone.getText().toString());
            }
        });
        dialog.setContentView(view);
        dialog.show();

    }


    private void onTabItemSelected(int position) {

        switch (position) {
            case 0:
                hideFragment(1);
                showFragment(0);
                break;
            case 1:
                hideFragment(0);
                showFragment(1);

                break;


        }
//        if (fragment != null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).commit();
//        }
    }

    private void addFragments() {
        getSupportFragmentManager().beginTransaction().add(R.id.main_middle, mFragmensts[0]).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_middle, mFragmensts[1]).commit();

    }

    private void showFragment(int index) {
        getSupportFragmentManager().beginTransaction().show(mFragmensts[index]).commit();
    }

    private void hideFragment(int index) {
        getSupportFragmentManager().beginTransaction().hide(mFragmensts[index]).commit();
    }
}
