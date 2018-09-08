package com.weiwobang.paotui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weiwobang.paotui.R;
import com.weiwobang.paotui.callback.ChartItemListener;
import com.weiwobang.paotui.constant.CommomConstant;


/**
 * Created by Administrator on 2017/12/16 0016.
 */

public class CompChartItem extends FrameLayout {
    public class ViewHolder {
        //private ImageView mImgIcon;
        private LinearLayout mLeftAndCenterLayout;
        public LinearLayout mBtnLayout;
        public TextView mOrderId;
        public TextView mSpentTime;
        public TextView mTaskState;
        public TextView mPickUpTarget;
        public TextView mPickUpAddr;
        public TextView mDeliveryTarget;
        public TextView mDeliveryAddr;
        public TextView mDistance;
        public TextView mCommission;
        public TextView mHandleState;
    }
    public CompChartItem.ViewHolder viewHolder = new CompChartItem.ViewHolder();
    private CommomConstant.TASK_STATE taskState;
    private ChartItemListener chartItemListener;

    public CompChartItem(Context context) {
        super(context);
        init();
    }

    private void init(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.comp_chart_item, this, true);
        viewHolder.mLeftAndCenterLayout = (LinearLayout) view.findViewById(R.id.CompChartItem_mLeftAndCenterLayout);
        viewHolder.mBtnLayout = (LinearLayout) view.findViewById(R.id.CompChartItem_mBtnLayout);
        viewHolder.mOrderId = (TextView) view.findViewById(R.id.CompChartItem_tvOrderId);
        viewHolder.mSpentTime = (TextView) view.findViewById(R.id.CompChartItem_tvSpentTime);
        viewHolder.mTaskState = (TextView) view.findViewById(R.id.CompChartItem_tvTaskState);
        viewHolder.mPickUpTarget = (TextView) view.findViewById(R.id.CompChartItem_tvPickUpTarget);
        viewHolder.mPickUpAddr = (TextView) view.findViewById(R.id.CompChartItem_tvPickUpAddr);
        viewHolder.mDeliveryTarget = (TextView) view.findViewById(R.id.CompChartItem_tvDeliveryTarget);
        viewHolder.mDeliveryAddr = (TextView) view.findViewById(R.id.CompChartItem_tvDeliveryAddr);
        viewHolder.mDistance = (TextView) view.findViewById(R.id.CompChartItem_tvDistance);
        viewHolder.mCommission = (TextView) view.findViewById(R.id.CompChartItem_tvCommission);
        viewHolder.mHandleState = (TextView) view.findViewById(R.id.CompChartItem_tvHandleState);
        viewHolder.mBtnLayout.findViewById(R.id.btnReject).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chartItemListener != null)
                    chartItemListener.onRejectBtnClick(CompChartItem.this);
            }
        });
        viewHolder.mBtnLayout.findViewById(R.id.btnAccept).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chartItemListener != null)
                    chartItemListener.onAcceptBtnClick(CompChartItem.this);
            }
        });
        viewHolder.mLeftAndCenterLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chartItemListener != null)
                    chartItemListener.onItemClick(CompChartItem.this);
            }
        });
    }

    private void setWH(View view,int width,int height){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    public ChartItemListener getChartItemListener() {
        return chartItemListener;
    }

    public void setChartItemListener(ChartItemListener chartItemListener) {
        this.chartItemListener = chartItemListener;
    }
//    public CompChartItemListener getCompChartItemListener() {
//        return chartItemListener;
//    }
//
//    public void setCompChartItemListener(CompChartItemListener chartItemListener) {
//        this.chartItemListener = chartItemListener;
//    }
//
//    public interface CompChartItemListener{
//
//        public void onItemClick(CompChartItem compChartItem);
//        public void onRejectBtnClick(CompChartItem compChartItem);
//        public void onAcceptBtnClick(CompChartItem compChartItem);
//
//    }
}
