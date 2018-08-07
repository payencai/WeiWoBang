package com.weiwobang.paotui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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
        ImageView imageView=helper.getView(R.id.item_icon);
        Glide.with(helper.itemView.getContext()).load(item.getImage1Uri()).into(imageView);

    }
}
