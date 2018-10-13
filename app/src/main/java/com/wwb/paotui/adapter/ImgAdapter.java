package com.wwb.paotui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lqm.roundview.RoundImageView;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;
import com.payencai.library.adapter.ViewCallback;
import com.payencai.library.mediapicker.entity.Media;
import com.wwb.paotui.R;

import java.util.List;

public class ImgAdapter extends BaseAdapter<Media> {
    onDelListener onDelListener;

    public void setOnDelListener(onDelListener onDelListener) {
        this.onDelListener = onDelListener;
    }

    public interface onDelListener {
        void onClick(int index);
    }

    @Override
    public int getLayoutRes(int index) {
        return R.layout.wwb_item_photo;
    }

    @Override
    public void convert(BaseViewHolder holder, Media data, int index) {

        ImageView imageView = holder.findImage(R.id.photo);
        ImageView del = holder.findImage(R.id.photo_del);
        ImageView video = holder.findImage(R.id.play_view);
        if (data.mediaType != 1) {
            video.setVisibility(View.VISIBLE);

        }
        Glide.with(holder.itemView.getContext()).load(data.path).into(imageView);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDelListener.onClick(holder.getPosition());
            }
        });
    }

    @Override
    public void bind(BaseViewHolder holder, int layoutRes) {

    }
}
