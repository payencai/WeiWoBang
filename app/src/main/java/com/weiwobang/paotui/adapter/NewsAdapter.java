package com.weiwobang.paotui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.News;

public class NewsAdapter extends BaseQuickAdapter<News, com.chad.library.adapter.base.BaseViewHolder> {


    public NewsAdapter(int layoutResId) {
        super(layoutResId);
    }




    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, News item) {
        helper.setText(R.id.item_title,item.getTitle());
        helper.setText(R.id.item_detail,item.getContent());
        helper.setText(R.id.type,item.getCategoryName());
        helper.setText(R.id.comment_num,item.getCommentNum()+"");
        helper.setText(R.id.read_num,item.getReadNum()+"");
        ImageView imageView=helper.getView(R.id.item_icon);
        Glide.with(helper.itemView.getContext()).load(item.getImage1Uri()).into(imageView);
    }
}
