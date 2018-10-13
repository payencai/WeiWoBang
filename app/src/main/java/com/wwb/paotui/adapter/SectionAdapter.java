package com.wwb.paotui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.wwb.paotui.R;
import com.wwb.paotui.bean.FinishOrder;
import com.wwb.paotui.bean.MySection;

import java.util.List;

public class SectionAdapter extends BaseSectionQuickAdapter<MySection, BaseViewHolder> {

    public SectionAdapter(int layoutResId, int sectionHeadResId, List<MySection> data) {
        super(layoutResId, sectionHeadResId, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, MySection item) {
        // item.header
        helper.setText(R.id.item_date, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        helper.setText(R.id.CompChartItem_tvOrderId, item.t.getTrackingNumber());
        String state = "";
        helper.setText(R.id.CompChartItem_tvSpentTime, "用时0分");
        switch (item.t.getState()) {
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
                helper.setText(R.id.CompChartItem_tvSpentTime, "用时" + item.t.getMinute() + "分");
                break;
            case "5":
                state = "已取消";
                break;
        }
        FinishOrder.DeliverMapBean pickMap=item.t.getPickMap();
        if (pickMap!=null){
            helper.setText(R.id.CompChartItem_tvPickUpTarget,pickMap.getHeading());
            helper.setText(R.id.CompChartItem_tvPickUpAddr, pickMap.getAdress() );
        }
        else{
            helper.setText(R.id.CompChartItem_tvPickUpTarget,"");
        }
        helper.setText(R.id.CompChartItem_tvTaskState, state);
        helper.setText(R.id.CompChartItem_tvDeliveryTarget, item.t.getDeliverMap().getHeading());
        helper.setText(R.id.CompChartItem_tvDeliveryAddr, item.t.getDeliverMap().getAdress());
        helper.setText(R.id.CompChartItem_tvDistance, item.t.getDistance() + "km");
        helper.setText(R.id.CompChartItem_tvCommission, item.t.getCommissionCalculate() + "元");
    }


}
