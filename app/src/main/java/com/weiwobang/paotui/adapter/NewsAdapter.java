package com.weiwobang.paotui.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqm.roundview.RoundImageView;
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
        RoundImageView imageView=helper.getView(R.id.item_icon);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon) //加载中图片
                .error(R.mipmap.app_icon) //加载失败图片
                .fallback(R.mipmap.app_icon) //url为空图片
                //.override(200,200)
                .centerCrop() ;// 填充方式
        Glide.with(helper.itemView.getContext()).load(item.getImage1Uri()).apply(requestOptions).into(imageView);
    }
}
