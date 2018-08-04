package com.weiwobang.paotui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.Reply;

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
        Glide.with(holder.itemView.getContext()).load(data.getUrl()).into(imageView);
    }

    @Override
    public void bind(BaseViewHolder holder, int layoutRes) {

    }
}
