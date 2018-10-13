package com.wwb.paotui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wwb.paotui.R;
import com.wwb.paotui.bean.LocateBean;
import com.wwb.paotui.bean.News;

public class LocateAdapter extends BaseQuickAdapter<LocateBean, BaseViewHolder> {
    public LocateAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocateBean item) {
        helper.setText(R.id.contact, item.getContactName());
        helper.setText(R.id.phone, item.getContactNumber());
        helper.setText(R.id.name, item.getHeading());
        helper.setText(R.id.address, item.getAdress());
    }
}
