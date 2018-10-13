package com.wwb.paotui.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;

import com.wwb.paotui.R;
import com.wwb.paotui.bean.Comment;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends BaseQuickAdapter<Comment, com.chad.library.adapter.base.BaseViewHolder> {
    onDelListener onDelListener;

    public CommentAdapter(int layoutResId) {
        super(layoutResId);
    }

    public void setOnDelListener(onDelListener onDelListener) {
        this.onDelListener = onDelListener;
    }

    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, Comment item) {
        RecyclerView recyclerView = helper.itemView.findViewById(R.id.recy_reply);
        recyclerView.setLayoutManager(new LinearLayoutManager(helper.itemView.getContext()));
        recyclerView.setNestedScrollingEnabled(false);
        ReplyAdapter replyAdapter = new ReplyAdapter();
        replyAdapter.openAutoLoadMore(false);
        replyAdapter.setData(item.getReplyList());
        recyclerView.setAdapter(replyAdapter);
        //mKnowAdapter.addHeadLayout(R.layout.header_know);
        ImageView repot = helper.itemView.findViewById(R.id.repot);
        repot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDelListener.onClick(helper.getAdapterPosition());
            }
        });
        CircleImageView imageView = helper.itemView.findViewById(R.id.comment_head);
        helper.setText(R.id.nickname, item.getCommentUserNickname());
        helper.setText(R.id.tv_reply, item.getCommentContent());
        helper.setText(R.id.comm_time, item.getCommentTime());
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.wwb_default_photo) //加载中图片
                .error(R.mipmap.wwb_default_photo) //加载失败图片
                .fallback(R.mipmap.wwb_default_photo) //url为空图片
                .centerCrop() ;// 填充方式
        //Log.e("ggg",image+name);
        Glide.with(helper.itemView.getContext()).load(item.getCommentUserHeadingUri()).apply(requestOptions).into(imageView);
    }

    public interface onDelListener {
        void onClick(int index);
    }

}
