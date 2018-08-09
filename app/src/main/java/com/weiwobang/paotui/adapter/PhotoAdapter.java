package com.weiwobang.paotui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;
import com.weiwobang.paotui.R;

public class PhotoAdapter extends BaseAdapter<String> {
    @Override
    public int getLayoutRes(int index) {
        return R.layout.wwb_item_img;
    }

    @Override
    public void convert(BaseViewHolder holder, String data, int index) {
        ImageView img=holder.findImage(R.id.img);
        Glide.with(holder.itemView.getContext()).load(data).into(img);
    }

    @Override
    public void bind(BaseViewHolder holder, int layoutRes) {

    }
}
