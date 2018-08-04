package com.weiwobang.paotui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;

import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.Comment;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends BaseAdapter<Comment> {
    onDelListener onDelListener;
    public void setOnDelListener(onDelListener onDelListener){
        this.onDelListener=onDelListener;
    }
    public interface onDelListener{
        void onClick(int index);
    }

    @Override
    public int getLayoutRes(int index) {
        return R.layout.wwb_item_comment;
    }

    @Override
    public void convert(BaseViewHolder holder, Comment data, int index) {
        RecyclerView recyclerView =holder.itemView.findViewById(R.id.recy_reply);
        recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        ReplyAdapter replyAdapter = new ReplyAdapter();
        replyAdapter.openAutoLoadMore(false);
        replyAdapter.setData(data.getReplyList());
        recyclerView.setAdapter(replyAdapter);
        //mKnowAdapter.addHeadLayout(R.layout.header_know);
        ImageView repot =holder.itemView.findViewById(R.id.repot);
        repot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDelListener.onClick(holder.getAdapterPosition());
            }
        });
        CircleImageView imageView=holder.itemView.findViewById(R.id.comment_head);
        holder.setText(R.id.nickname,data.getCommentUserNickname());
        holder.setText(R.id.tv_reply,data.getCommentContent());
        holder.setText(R.id.comm_time,data.getCommentTime());
        Glide.with(holder.itemView.getContext()).load(data.getCommentUserHeadingUri()).into(imageView);

    }

    @Override
    public void bind(BaseViewHolder holder, int layoutRes) {

    }
}
