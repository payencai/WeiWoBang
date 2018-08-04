package com.weiwobang.paotui.adapter;

import android.content.Intent;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.activity.DetailActivity;
import com.weiwobang.paotui.bean.News;

public class MyNewsAdapter extends BaseAdapter<News> {
    onDelListener onDelListener;
    public void setOnDelListener(onDelListener onDelListener){
        this.onDelListener=onDelListener;
    }
    public interface onDelListener{
        void onClick(String id,int index);
        void onEdit(String id,int name);
    }

    @Override
    public int getLayoutRes(int index) {
        return R.layout.wwb_item_publish;
    }

    @Override
    public void convert(BaseViewHolder holder, News data, int index) {
        TextView del=holder.findText(R.id.item_del);
        TextView edit=holder.findText(R.id.item_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDelListener.onEdit(data.getId(),holder.getAdapterPosition());
                //holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), DetailActivity.class));
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDelListener.onClick(data.getId(),index);
            }
        });
        holder.setText(R.id.item_title,data.getTitle());
        holder.setText(R.id.item_detail,data.getContent());
        holder.setText(R.id.type,data.getCategoryName());
        holder.setText(R.id.comment_num,data.getCommentNum()+"");
        holder.setText(R.id.read_num,data.getReadNum()+"");
        ImageView imageView=holder.findImage(R.id.item_icon);
        Glide.with(holder.itemView.getContext()).load(data.getImage1Uri()).into(imageView);
    }

    @Override
    public void bind(BaseViewHolder holder, int layoutRes) {

    }


}
