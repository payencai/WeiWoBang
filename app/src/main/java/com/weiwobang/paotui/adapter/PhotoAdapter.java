package com.weiwobang.paotui.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lqm.roundview.RoundImageView;
import com.payencai.library.adapter.BaseAdapter;
import com.payencai.library.adapter.BaseViewHolder;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.Comment;
import com.weiwobang.paotui.bean.MediaUrl;
import com.weiwobang.paotui.tools.VideoUtil;
import com.weiwobang.paotui.view.SampleCoverVideo;

import java.util.HashMap;



public class PhotoAdapter extends BaseQuickAdapter<MediaUrl, com.chad.library.adapter.base.BaseViewHolder> {
    private String testUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    private SampleCoverVideo videoView;
    private Context mContext;
    int count = 0;
    String path = "";
    private String image = "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640";

    public PhotoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(com.chad.library.adapter.base.BaseViewHolder helper, MediaUrl item) {
        count++;
        helper.setIsRecyclable(false);
        Log.e("count", count + "");
        RoundImageView img = (RoundImageView) helper.getView(R.id.img);
        //StandardGSYVideoPlayer videoPlayer=holder.find(R.id.detail_player);
        videoView = helper.getView(R.id.detail_video);
        RequestOptions requestOptions = new RequestOptions()
                .error(R.mipmap.logo)
                .centerCrop();// 填充方式

        if (item.getType().equals("1")) {
            if(!TextUtils.isEmpty(item.getUrl())&&!TextUtils.equals("null",item.getUrl())){
                Glide.with(helper.itemView.getContext()).load(item.getUrl()).apply(requestOptions).into(img);
            }

        } else  {
            img.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            videoView.setUpLazy(item.getUrl(),true,null,null,"");
            videoView.getTitleTextView().setVisibility(View.GONE);
            videoView.getBackButton().setVisibility(View.GONE);

            videoView.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoView.startWindowFullscreen(helper.itemView.getContext(), false, true);
                }
            });
            videoView.loadCoverImage(item.getUrl(),R.mipmap.app_icon);
            //videoView.setThumbPlay(true);
           // videoView.setUp(item.getUrl(), "测试视频", Jzvd.SCREEN_WINDOW_NORMAL);
        }
    }
}
