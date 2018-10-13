package com.wwb.paotui.adapter;

import android.view.View;
import android.widget.ImageView;

import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;
import com.wwb.paotui.R;

public class InformAdapter extends BaseAdapter<String> {
    @Override
    public int getLayoutRes(int index) {
        return R.layout.wwb_item_report;
    }

    @Override
    public void convert(BaseViewHolder holder, String data, int index) {
        holder.setText(R.id.reprot_content,data);
        ImageView sel=holder.findImage(R.id.sel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sel.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void bind(BaseViewHolder holder, int layoutRes) {

    }
}
