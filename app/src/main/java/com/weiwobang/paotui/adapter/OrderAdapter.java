package com.weiwobang.paotui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.Order;

public class OrderAdapter extends BaseQuickAdapter<Order,BaseViewHolder> {
    public OrderAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Order item) {
//         helper.setText(R.id.order_id)
//                 .se
    }
}
