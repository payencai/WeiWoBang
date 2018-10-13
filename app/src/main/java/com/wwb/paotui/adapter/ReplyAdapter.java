package com.wwb.paotui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;
import com.wwb.paotui.R;
import com.wwb.paotui.bean.Reply;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReplyAdapter extends BaseAdapter<Reply> {
    @Override
    public int getLayoutRes(int index) {
        return R.layout.wwb_item_reply;
    }

    @Override
    public void convert(BaseViewHolder holder, Reply data, int index) {

        CircleImageView imageView=holder.itemView.findViewById(R.id.comment_head);
        holder.setText(R.id.nickname,data.getName());
        holder.setText(R.id.tv_reply,data.getReplyContent());
        holder.setText(R.id.comm_time,data.getReplyTime());
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.wwb_default_photo) //加载中图片
                .error(R.mipmap.wwb_default_photo) //加载失败图片
                .fallback(R.mipmap.wwb_default_photo) //url为空图片
                .centerCrop() ;// 填充方式
        Glide.with(holder.itemView.getContext()).load(data.getUrl()).apply(requestOptions).into(imageView);
    }

    @Override
    public void bind(BaseViewHolder holder, int layoutRes) {

    }
}
