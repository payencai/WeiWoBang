package com.weiwobang.paotui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.payencai.library.util.ToastUtil;
import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.activity.OrderdelActivity;
import com.weiwobang.paotui.activity.SellermainActivity;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Event;
import com.weiwobang.paotui.bean.SellerOrder;
import com.weiwobang.paotui.constant.CommomConstant;
import com.weiwobang.paotui.tools.BottomBar;
import com.weiwobang.paotui.tools.GsonUtils;
import com.weiwobang.paotui.view.CompChartCell;
import com.weiwobang.paotui.view.CompCourierCell;
import com.weiwobang.paotui.view.slideshowview.SlideShowPageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    @BindView(R.id.slideshowView)
    SlideShowPageView slideShowView;
    View rootView;
    private List<View> mViews = new ArrayList<>();

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.wwb_fragment_order, container, false);
        ButterKnife.bind(this,rootView);
        init(rootView);
        getOrderList();
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){//加上判断
            EventBus.getDefault().register(this);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void FragmentButtonClicked(Event code) {
        switch (code.getCode()) {
            case 0:
                Log.e("dff","dfff");
                getOrderList();
                break;
            default:
                break;
        }
    }
    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    private void init(View view) {


    }

    private void getOrderList() {
        Log.e("token", MyAPP.token);
        Disposable disposable = NetWorkManager.getReq(ApiService.class).getInProcess(MyAPP.token)
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

                                Log.e("home", object.toString());
                                object = object.getJSONObject("data");
                                JSONArray data = object.getJSONArray("data");
                                List<SellerOrder> orderList = GsonUtils.jsonToArrayList(data.toString(), SellerOrder.class);
                                if (orderList.size() > 0)
                                    setData(orderList);

                            } else {
                                ToastUtil.showToast(getContext(), msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        ToastUtil.showToast(getContext(), apiException.getDisplayMessage());
                    }
                });

        new CompositeDisposable().add(disposable);
    }

    private void setData(List<SellerOrder> orderList) {
        mViews.clear();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        SellerOrder sellerOrder;
        View view;
        for(int i = 0; i < orderList.size(); i++) {
            sellerOrder = orderList.get(i);
            view = inflater.inflate(R.layout.page_current_dipatch, null);
            // orderId
            ((TextView)view.findViewById(R.id.tvOrderId)).setText(sellerOrder.getTrackingNumber());
            ((TextView)view.findViewById(R.id.tvPickUpCode)).setText(sellerOrder.getIdentifyingCode());
            if(String.valueOf(CommomConstant.TASK_STATE.PROCESS_PICKUP.getValue()).equals(sellerOrder.getState()) ||
                    String.valueOf(CommomConstant.TASK_STATE.PROCESS_DELIVERY.getValue()).equals(sellerOrder.getState())) {
                ((ImageView) view.findViewById(R.id.imgPickUp)).setImageResource(R.mipmap.hold_y);
                ((TextView) view.findViewById(R.id.tvPickUp)).setTextColor(getResources().getColor(R.color.yellow_1e));
                if(String.valueOf(CommomConstant.TASK_STATE.PROCESS_DELIVERY.getValue()).equals(sellerOrder.getState())) {
                    ((ImageView) view.findViewById(R.id.imgDelivery)).setImageResource(R.mipmap.hold_y);
                    ((TextView) view.findViewById(R.id.tvDelivery)).setTextColor(getResources().getColor(R.color.yellow_1e));
                }
            }
            CompCourierCell compCourier = (CompCourierCell) view.findViewById(R.id.compCourier);
            compCourier.viewHolder.mTextName.setText("".equals(sellerOrder.getCourierName()) ? "暂无" : sellerOrder.getCourierName());
            compCourier.viewHolder.mTextDescription.setText(sellerOrder.getCourierTelNum());
            CompChartCell compDelivery = (CompChartCell)view.findViewById(R.id.compDelivery);
            if(sellerOrder.getDeliverMap() != null) {
                compDelivery.viewHolder.mTextName.setText(sellerOrder.getDeliverMap().getHeading());
                compDelivery.viewHolder.mTextDescription.setText(sellerOrder.getDeliverMap().getAdress());
                compDelivery.viewHolder.mLabelPerson.setText(sellerOrder.getBuyerName());
                compDelivery.viewHolder.mLabelTel.setText(sellerOrder.getBuyerTelnum());
                ((TextView) view.findViewById(R.id.tvDistance)).setText(getString(R.string.distance_str, sellerOrder.getDistance() + ""));
                ((TextView) view.findViewById(R.id.tvCommission)).setText(getString(R.string.commission_str, sellerOrder.getCommissionCalculate() + ""));
            }
            String id=sellerOrder.getId();
            view.findViewById(R.id.btnProcessTask).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getContext(), OrderdelActivity.class);
                    intent.putExtra("id",id);
                    startActivityForResult(intent,2);
                }
            });
            view.findViewById(R.id.btnProcessTask).setTag(sellerOrder);
            mViews.add(view);
        }
        if(slideShowView.getVIEW_COUNT() == 0)
            slideShowView.setViews(mViews);
        slideShowView.notifyDataChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==1){
            getOrderList();
            EventBus.getDefault().post(new Event(1));
        }
    }
}
