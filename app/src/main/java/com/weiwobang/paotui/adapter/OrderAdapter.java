package com.weiwobang.paotui.adapter;


import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.Order;

public class OrderAdapter extends BaseAdapter<Order> {

    @Override
    public int getLayoutRes(int index) {
        return R.layout.wwb_item_order;
    }

    @Override
    public void convert(BaseViewHolder holder, Order data, int index) {
        holder.setText(R.id.order_id,data.getOrderNum());
        holder.setText(R.id.order_addr1,data.getAddressFrom());
        holder.setText(R.id.order_addr2,data.getAddressTo());
        holder.setText(R.id.order_time,data.getRemoveTime().substring(0,10));
        switch (Integer.parseInt(data.getStatus())){
            case 1:
                holder.setText(R.id.order_status,"预约中");
                break;
            case 2:
                holder.setText(R.id.order_status,"预约成功");
                break;
            case 3:
                holder.setText(R.id.order_status,"已完成");
                break;
            case 4:
                holder.setText(R.id.order_status,"已取消");
                break;
        }
//        holder.setText(R.id.comment_num,data.getCommentNum()+"");
//        holder.setText(R.id.read_num,data.getReadNum()+"");
    }

    @Override
    public void bind(BaseViewHolder holder, int layoutRes) {

    }
}
