package com.wwb.paotui.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;


import com.chad.library.adapter.base.BaseViewHolder;
import com.wwb.paotui.R;
import com.wwb.paotui.bean.Order;

public class OrderAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {



    public OrderAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        helper.setText(R.id.CompChartItem_tvOrderId,item.getOrderNum());
        helper.setText(R.id.CompChartItem_tvPickUpTarget,item.getAddressFrom());
        helper.setText(R.id.CompChartItem_tvPickUpAddr,item.getAddressFromDetail());
        helper.setText(R.id.CompChartItem_tvDeliveryTarget,item.getAddressTo());
        helper.setText(R.id.CompChartItem_tvDeliveryAddr,item.getAddressToDetail());
        helper.setText(R.id.CompChartItem_tvDistance,item.getDistance()+"km");
        helper.setText(R.id.CompChartItem_tvCommission,"￥"+item.getCost()+"");
        helper.setText(R.id.from,item.getFloorFrom()+"");
        helper.setText(R.id.to,item.getFloorTo()+"");
        helper.setText(R.id.contact,"联系人: "+item.getContactName()+"");
        helper.setText(R.id.time,"预约时间: "+item.getRemoveTime().substring(0,10)+"");
        helper.setText(R.id.note,"备注: "+item.getNote()+"");
        switch (Integer.parseInt(item.getStatus())){
            case 1:
                helper.setText(R.id.CompChartItem_tvTaskState,"预约中");
                break;
            case 2:
                helper.setText(R.id.CompChartItem_tvTaskState,"预约成功");
                break;
            case 3:
                helper.setText(R.id.CompChartItem_tvTaskState,"已完成");
                break;
            case 4:
                helper.setText(R.id.CompChartItem_tvTaskState,"已取消");
                break;
            default:
                break;
        }
    }
}
