package com.weiwobang.paotui.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;


import com.chad.library.adapter.base.BaseViewHolder;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.Order;

public class OrderAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {



    public OrderAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
        helper.setText(R.id.order_id,item.getOrderNum());
        helper.setText(R.id.order_addr1,item.getAddressFrom());
        helper.setText(R.id.order_addr2,item.getAddressTo());
        helper.setText(R.id.order_time,item.getRemoveTime().substring(0,10));
        switch (Integer.parseInt(item.getStatus())){
            case 1:
                helper.setText(R.id.order_status,"预约中");
                break;
            case 2:
                helper.setText(R.id.order_status,"预约成功");
                break;
            case 3:
                helper.setText(R.id.order_status,"已完成");
                break;
            case 4:
                helper.setText(R.id.order_status,"已取消");
                break;
        }
    }
}
