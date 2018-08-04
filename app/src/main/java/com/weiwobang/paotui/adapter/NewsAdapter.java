package com.weiwobang.paotui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.News;

public class NewsAdapter extends BaseAdapter<News> {
    @Override
    public int getLayoutRes(int index) {
        return R.layout.wwb_item_forum;
    }

    @Override
    public void convert(BaseViewHolder holder, News data, int index) {
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
