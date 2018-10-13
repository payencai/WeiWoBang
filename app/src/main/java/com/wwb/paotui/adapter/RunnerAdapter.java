package com.wwb.paotui.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wwb.paotui.R;
import com.wwb.paotui.bean.FinishOrder;
import com.wwb.paotui.bean.News;
import com.wwb.paotui.bean.RunnerModel;
import com.wwb.paotui.bean.SellerOrder;

public class RunnerAdapter extends BaseQuickAdapter<RunnerModel, BaseViewHolder> {
    public RunnerAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, RunnerModel item) {
        helper.setText(R.id.CompChartItem_tvOrderId, item.getTrackingNumber());
        TextView orderId=(TextView)helper.getView(R.id.CompChartItem_tvOrderId);
        orderId.setTextColor(helper.itemView.getContext().getResources().getColor(R.color.c888));
        String state = "";
        TextView time=helper.getView(R.id.CompChartItem_tvSpentTime);
        helper.getView(R.id.order).setVisibility(View.GONE);
        time.setVisibility(View.GONE);
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

                break;
            case "5":
                state = "已取消";
                break;
        }
        RunnerModel.DeliverMapBean pickMap=item.getPickMap();
        if (pickMap!=null){
            helper.setText(R.id.CompChartItem_tvPickUpTarget,pickMap.getSpare1());
            helper.setText(R.id.CompChartItem_tvPickUpAddr, pickMap.getAdress() );
        }
        else{
            helper.setText(R.id.CompChartItem_tvPickUpTarget,"");
        }
        helper.setText(R.id.CompChartItem_tvTaskState, state);
        if("5".equals(item.getState())){
            TextView stat=helper.getView(R.id.CompChartItem_tvTaskState);
            stat.setTextColor(helper.itemView.getContext().getResources().getColor(R.color.c888));
        }
        helper.setText(R.id.CompChartItem_tvDeliveryTarget, item.getDeliverMap().getSpare1());
        helper.setText(R.id.CompChartItem_tvDeliveryAddr, item.getDeliverMap().getAdress());
        helper.setText(R.id.CompChartItem_tvDistance, item.getDistance() + "km");
        helper.setText(R.id.CompChartItem_tvCommission, item.getCommissionCalculate() + "元");
    }

}
