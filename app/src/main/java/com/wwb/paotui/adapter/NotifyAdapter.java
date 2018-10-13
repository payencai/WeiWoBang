package com.wwb.paotui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wwb.paotui.R;
import com.wwb.paotui.bean.Notify;

public class NotifyAdapter extends BaseQuickAdapter<Notify, BaseViewHolder> {
    public NotifyAdapter(int layoutResId) {
        super(layoutResId);
    }

    public interface OnItemClickListener {
        void onClick(int code, String id);
    }

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, Notify item) {
        helper.setText(R.id.CompChartItem_tvOrderId, item.getTrackingNumber());
        String state = "";
        helper.setText(R.id.CompChartItem_tvSpentTime, "用时0分");
        switch (item.getState()) {
            case "1":
                state = "待派单";
                break;
            case "2":
                state = "待取货";
                break;
            case "3":
                state = "取货中";
                break;
            case "4":
                state = "已完成";
                // helper.setText(R.id.CompChartItem_tvSpentTime, "用时" + item.get() + "分");
                break;
            case "5":
                state = "已取消";
                break;
        }
        if (!TextUtils.isEmpty(item.getCourierName()))
            helper.setText(R.id.CompChartItem_tvRunner, item.getCourierName());
        else {
            helper.setText(R.id.CompChartItem_tvRunner, "");
        }
        LinearLayout linearLayout = helper.getView(R.id.CompChartItem_mBtnLayout);
        // linearLayout.setVisibility();
        if (!TextUtils.isEmpty(item.getCourierTelnum()))
            helper.setText(R.id.CompChartItem_tvRunnerTel, item.getCourierTelnum());
        helper.setText(R.id.state, "订单"+state+"");
        if (item.getDeliverMap() != null) {
            helper.setText(R.id.CompChartItem_tvDeliveryTarget, item.getDeliverMap().getHeading());
            helper.setText(R.id.CompChartItem_tvDeliveryAddr, item.getDeliverMap().getAdress());
        }
        helper.setText(R.id.time,item.getCreateTime().substring(0,10));
        helper.setText(R.id.CompChartItem_tvDistance, item.getDistance() + "km");
        helper.setText(R.id.CompChartItem_tvCommission, item.getCommissionCalculate() + "元");
        FrameLayout agree = helper.getView(R.id.btnAccept);
        FrameLayout reject = helper.getView(R.id.btnReject);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onClick(0, item.getId());
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onClick(1, item.getId());
            }
        });
    }
}
