package com.wwb.paotui.adapter;

import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqm.roundview.RoundImageView;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;
import com.wwb.paotui.R;
import com.wwb.paotui.bean.News;
import com.wwb.paotui.view.SampleCoverVideo;

public class NewsAdapter extends BaseQuickAdapter<News, com.chad.library.adapter.base.BaseViewHolder> {

    int count = 0;

    // private SampleCoverVideo videoView;
    public NewsAdapter(int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, News item) {
        //count++;
        //不设置会卡顿，设置了会错乱
        helper.setIsRecyclable(true);
        //videoView = helper.getView(R.id.video_item);
        //Log.e("num", count + "");
        helper.setText(R.id.item_title, item.getTitle());
        helper.setText(R.id.item_detail, item.getContent());
        helper.setText(R.id.type, item.getCategoryName());
        helper.setText(R.id.comment_num, item.getCommentNum() + "");
        helper.setText(R.id.read_num, item.getReadNum() + "");
        RoundImageView imageView = helper.getView(R.id.item_icon);
        ImageView btnPlay=helper.getView(R.id.btn_play);
        String tag= (String) btnPlay.getTag();
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.app_icon) //加载中图片
                .error(R.mipmap.app_icon) //加载失败图片
                .fallback(R.mipmap.app_icon) //url为空图片
                .centerCrop();// 填充方式
        btnPlay.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(item.getImage1Uri())) {
                Glide.with(helper.itemView.getContext()).load(item.getImage1Uri()).apply(requestOptions).into(imageView);
                imageView.setVisibility(View.VISIBLE);
                if(item.getImage1Type().contains("bbs")){
                    btnPlay.setVisibility(View.VISIBLE);
                    //helper.setTag(R.id.btn_play,"2");
                }
        } else {
            helper.getView(R.id.btn_play).setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }
//        else {
////            if (!TextUtils.isEmpty(item.getImage1Uri())) {
////                if (item.getImage1Uri().contains("bbs"))
////                    Glide.with(helper.itemView.getContext()).load(item.getImage1Uri()).apply(requestOptions).into(imageView);
////                else if(item.getImage1Uri().contains("shipin")){
////                   // imageView.setVisibility(View.GONE);
//////                    videoView.setVisibility(View.VISIBLE);
//////                    videoView.setUpLazy(item.getImage1Uri(),true,null,null,"");
//////                    videoView.getTitleTextView().setVisibility(View.GONE);
//////                    videoView.getBackButton().setVisibility(View.GONE);
//////                    videoView.loadCoverImage(item.getImage1Uri(),R.mipmap.app_icon);
////
////                }else{
////                    imageView.setVisibility(View.GONE);
////                }
////            } else {
////                imageView.setVisibility(View.GONE);
////            }
//        }
    }
}
