package com.wwb.paotui.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.wwb.paotui.MyAPP;
import com.wwb.paotui.R;
import com.wwb.paotui.adapter.OrderHandleListAdapter;
import com.wwb.paotui.adapter.RunnerOrderAdapter;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.bean.Operation;
import com.wwb.paotui.bean.RunnerModel;
import com.wwb.paotui.bean.SellerOrder;
import com.wwb.paotui.tools.GsonUtils;
import com.wwb.paotui.view.CompChartCell;
import com.wwb.paotui.view.CompCourierCell;
import com.wwb.paotui.view.ListViewForScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

public class RunnerDetailActivity extends AppCompatActivity {
    private String id;
    private RunnerModel mSellerOrder;
    private List<Operation> mOperations;
    private RunnerOrderAdapter mOrderHandleListAdapter;
    @BindView(R.id.title)
    TextView titlle;
    @BindView(R.id.back)
    FrameLayout back;
    @BindView(R.id.compDelivery)
    CompChartCell delivery;
    @BindView(R.id.compCourier)
    CompCourierCell runner;
    @BindView(R.id.tvDistance)
    TextView distance;
    @BindView(R.id.tvCommission)
    TextView money;
    @BindView(R.id.listview)
    ListViewForScrollView listview;
    @BindView(R.id.btnApplyForCancel)
    TextView btnApplyForCancel;
    @BindView(R.id.btnComplain)
    FrameLayout btnComplain;
    @BindView(R.id.goodsName)
    TextView goodsName;
    @BindView(R.id.note)
    TextView note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner_detail);
        ButterKnife.bind(this);
        init();
        getDeatil();
    }

    void init() {
        id = getIntent().getStringExtra("id");
        btnApplyForCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelDialog();
            }
        });
        titlle.setText("单号: "+id);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // view = getLayoutInflater().inflate(R.layout.dialog_seller_complain, null);
                //final SimpleRatingBar myRatingBar = (SimpleRatingBar)view.findViewById(R.id.ratingBar);

                showComplainDialog();
            }
        });
    }

    public void showCancelDialog() {
        Dialog dialog = new Dialog(this, R.style.DialogStyleNoTitle);
        dialog.setCanceledOnTouchOutside(false);
        View view = getLayoutInflater().inflate(R.layout.dialog_cancel_order, null);
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
                dialog.dismiss();
                cancel();

            }
        });
        dialog.setContentView(view);
        dialog.show();

    }

    Dialog dialog;

    public void showComplainDialog() {
        dialog = new Dialog(this, R.style.DialogStyleNoTitle);
        dialog.setCanceledOnTouchOutside(false);
        View view = getLayoutInflater().inflate(R.layout.dialog_seller_complain, null);
        TextView submit=view.findViewById(R.id.submit);
        EditText et_content=view.findViewById(R.id.edtText);
        ImageView del=view.findViewById(R.id.imgClose);
        SimpleRatingBar simpleRatingBar=view.findViewById(R.id.ratingBar);
        SimpleRatingBar.AnimationBuilder builder = simpleRatingBar.getAnimationBuilder()
                .setRatingTarget(5)
                .setDuration(1000)
                .setInterpolator(new BounceInterpolator());
        builder.start();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                complain(String.valueOf(simpleRatingBar.getRating()),et_content.getText().toString());
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.show();

    }

    private void complain(String level, String content) {
        Log.e("token", MyAPP.token);
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postRunnerComplain(id, level, content, MyAPP.token2)
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
                                ToastUtil.showToast(RunnerDetailActivity.this, "举报成功");
                                getDeatil();
                                mOrderHandleListAdapter.notifyDataSetChanged();
                                // finish();
                            } else {
                                ToastUtil.showToast(RunnerDetailActivity.this,  msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(RunnerDetailActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void cancel() {
        Log.e("token", MyAPP.token2);
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).postRunnerCancel(id, MyAPP.token2)
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
                                ToastUtil.showToast(RunnerDetailActivity.this, "取消成功");
                                setResult(1);
                                finish();
                            } else {
                                ToastUtil.showToast(RunnerDetailActivity.this, id + msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(RunnerDetailActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void getDeatil() {
        Log.e("token", id+"");
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getRunnerDetail(id, MyAPP.token2)
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
                                Log.e("orderdel", object.toString());
                                object = object.getJSONObject("data");
                                JSONObject order = object.getJSONObject("order");
                                JSONArray operation = object.getJSONArray("operation");
                                mSellerOrder = GsonUtils.parseJSON(order.toString(), RunnerModel.class);
                                mOperations = GsonUtils.jsonToArrayList(operation.toString(), Operation.class);
                                Log.e("json", mOperations.toString());
                                setData();
                                mOrderHandleListAdapter = new RunnerOrderAdapter(RunnerDetailActivity.this);
                                mOrderHandleListAdapter.setLists(mOperations);
                                listview.setAdapter(mOrderHandleListAdapter);
                            } else {
                                ToastUtil.showToast(RunnerDetailActivity.this, id + msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(RunnerDetailActivity.this, apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void setData() {
        String state=mSellerOrder.getState();

        if(TextUtils.equals("5",state)||TextUtils.equals("4",state)||TextUtils.equals("3",state)||TextUtils.equals("2",state)){
            btnApplyForCancel.setVisibility(View.GONE);
        }
        titlle.setText("单号: "+mSellerOrder.getTrackingNumber());
        if(!TextUtils.isEmpty(mSellerOrder.getGoodsName())){
            goodsName.setText("货物名称: "+mSellerOrder.getGoodsName());
            goodsName.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(mSellerOrder.getNote())){
            note.setText("留言: "+mSellerOrder.getNote());
            note.setVisibility(View.VISIBLE);
        }
        distance.setText(mSellerOrder.getDistance() + "km");
        money.setText(mSellerOrder.getCommissionCalculate() + "元");
        runner.viewHolder.mTextName.setText(mSellerOrder.getCourierName());
        runner.viewHolder.mTextDescription.setText(mSellerOrder.getCourierTelNum());
        delivery.viewHolder.mTextName.setText(mSellerOrder.getDeliverMap().getSpare1());
        delivery.viewHolder.mTextDescription.setText(mSellerOrder.getDeliverMap().getAdress());
        delivery.viewHolder.mLabelPerson.setText(mSellerOrder.getBuyerName());
        delivery.viewHolder.mLabelTel.setText(mSellerOrder.getBuyerTelnum());

    }
}
