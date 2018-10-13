package com.wwb.paotui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lqm.roundview.RoundImageView;
import com.wwb.paotui.R;
import com.wwb.paotui.bean.News;

import org.feezu.liuli.timeselector.Utils.TextUtil;

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
        helper.setIsRecyclable(true);
        helper.setText(R.id.item_title,item.getTitle())
                .setText(R.id.item_detail,item.getContent())
                .setText(R.id.type,item.getCategoryName())
                .setText(R.id.comment_num,item.getCommentNum()+"")
                .setText(R.id.read_num,item.getReadNum()+"")
                .addOnClickListener(R.id.item_del)
                .addOnClickListener(R.id.item_edit);
        RoundImageView imageView=helper.getView(R.id.item_icon);
        ImageView btnPlay= helper.getView(R.id.video_play);
        btnPlay.setVisibility(View.VISIBLE);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.wwb_default_photo) //加载中图片
                .error(R.mipmap.wwb_default_photo) //加载失败图片
                .fallback(R.mipmap.wwb_default_photo) //url为空图片
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop() ;// 填充方式
        if (!TextUtils.isEmpty(item.getImage1Uri())) {
            Log.e("type2",item.getImage1Type()+"");
            Glide.with(helper.itemView.getContext()).load(item.getImage1Uri()).apply(requestOptions).into(imageView);
            if(!TextUtil.isEmpty(item.getImage1Type())){
                if(TextUtils.equals("1",item.getImage1Type())||TextUtils.equals("null",item.getImage1Type()))
                    btnPlay.setVisibility(View.GONE);
            }
            else{
                btnPlay.setVisibility(View.GONE);
            }
        } else {
            btnPlay.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
    }
}
