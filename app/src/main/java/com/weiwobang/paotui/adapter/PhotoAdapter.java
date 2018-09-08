package com.weiwobang.paotui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lqm.roundview.RoundImageView;
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
        RoundImageView img= (RoundImageView) holder.findImage(R.id.img);

        RequestOptions requestOptions = new RequestOptions()

                //.override(200,200)
                .centerCrop() ;// 填充方式
        Glide.with(holder.itemView.getContext()).load(data).apply(requestOptions).into(img);
    }

    @Override
    public void bind(BaseViewHolder holder, int layoutRes) {

    }
}
