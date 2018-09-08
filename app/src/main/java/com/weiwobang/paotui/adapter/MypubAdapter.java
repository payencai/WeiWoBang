package com.weiwobang.paotui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lqm.roundview.RoundImageView;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.News;

import java.util.List;

public class MypubAdapter extends BaseQuickAdapter<News,BaseViewHolder> {

    public MypubAdapter(int layoutResId) {
        super(layoutResId);
    }

    public MypubAdapter(int layoutResId, @Nullable List<News> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, News item) {
        helper.setText(R.id.item_title,item.getTitle())
                .setText(R.id.item_detail,item.getContent())
                .setText(R.id.type,item.getCategoryName())
                .setText(R.id.comment_num,item.getCommentNum()+"")
                .setText(R.id.read_num,item.getReadNum()+"")
                .addOnClickListener(R.id.item_del)
                .addOnClickListener(R.id.item_edit);
        RoundImageView imageView=helper.getView(R.id.item_icon);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.wwb_default_photo) //加载中图片
                .error(R.mipmap.wwb_default_photo) //加载失败图片
                .fallback(R.mipmap.wwb_default_photo) //url为空图片
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop() ;// 填充方式
        Glide.with(helper.itemView.getContext()).load(item.getImage1Uri()).apply(requestOptions).into(imageView);

    }
}
