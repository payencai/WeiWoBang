package com.wwb.paotui.view.slideshowview;

/**
 * Created by Administrator on 2016/8/10.
 */

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wwb.paotui.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 *
 *
 */
public class SlideShowPageView extends FrameLayout implements View.OnClickListener {

    // 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar
    private ImageLoader imageLoader = ImageLoader.getInstance();

    //自动轮播启用开关
    private final static boolean isAutoPlay = false;

    //自定义轮播图的资源
//    private List<Map<String,String>> imageUrls;
    //轮播视图数量
    private int VIEW_COUNT = 0;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 20;
    //放轮播图片的ImageView 的list
    private List<View> viewList = null;
    //放圆点的View的list
    private List<View> dotViewsList;
    private LinearLayout dotLayout;
    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;
    //当前轮播页
    private int currentItem  = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;
//    private ArrayList<String> urlList = new ArrayList<>();
    private SlideShowListener slideShowListener;

    //Handler
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    public SlideShowPageView(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
    }
    public SlideShowPageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }
    public SlideShowPageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        initImageLoader(context);

        if(isAutoPlay){
            startPlay();
        }

    }
    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 5, TimeUnit.SECONDS);
    }
    /**
     * 停止轮播图切换
     */
    private void stopPlay(){
        scheduledExecutorService.shutdown();
    }
    /**
     * 初始化相关Data
     */
    private void initData(){
//        viewList = new ArrayList<View>();
        dotViewsList = new ArrayList<View>();
        initUI(context);
        viewPager.setCurrentItem(currentItem);
        setDotViewsBg(currentItem);
    }

    public void setViews(List<View> viewList){
        this.viewList = viewList;
        setVIEW_COUNT(viewList.size());
        initData();
    }

    public int getVIEW_COUNT() {
        return VIEW_COUNT;
    }

    public void setVIEW_COUNT(int VIEW_COUNT) {
        this.VIEW_COUNT = VIEW_COUNT;
    }

    @Override
    public void onClick(View v) {
        // 轮播图点击事件
    }

    /**
     * 初始化Views等UI
     */
    @SuppressWarnings("ResourceType")
    private void initUI(Context context){
        if(viewList == null || viewList.size() == 0)
            return;

        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);

//        LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);
        dotLayout = (LinearLayout)findViewById(R.id.dotLayout);
        initDotView();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        myPagerAdapter = new MyPagerAdapter();
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    private void initDotView() {
        dotViewsList.clear();
        dotLayout.removeAllViews();

        // 热点个数与图片特殊相等
        for (int i = 0; i < viewList.size(); i++) {
            ImageView dotView =  new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = 16;
            params.width = 16;
            params.leftMargin = 16;
            params.rightMargin = 16;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }
    }

    /**
     * 填充ViewPager的页面适配器
     *
     */
    private class MyPagerAdapter  extends PagerAdapter {

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager)container).removeView((View)object);
            //((ViewPager)container).removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            View view = viewList.get(position);
            ((ViewPager)container).addView(view);
            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }
    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     *
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = false;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕，空闲中
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
//                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
//                        viewPager.setCurrentItem(0);
//                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
//                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
//                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
//                    }
                    if(slideShowListener != null) {
                        slideShowListener.onSlideShow(viewList.get(currentItem));
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub

            currentItem = pos;
            setDotViewsBg(pos);
//            for(int i=0;i < dotViewsList.size();i++){
//                if(i == pos){
//                    ((View)dotViewsList.get(pos)).setBackgroundResource(R.mipmap.circle_yellow);
//                }else {
//                    ((View)dotViewsList.get(i)).setBackgroundResource(R.mipmap.circle_grey);
//                }
//            }
        }

    }

    private void setDotViewsBg(int pos) {
        for(int i=0;i < dotViewsList.size();i++){
            if(i == pos){
                ((View)dotViewsList.get(pos)).setBackgroundResource(R.mipmap.circle_yellow);
            }else {
                ((View)dotViewsList.get(i)).setBackgroundResource(R.mipmap.circle_grey);
            }
        }
    }

    /**
     *执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                currentItem = (currentItem+1)%viewList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }


    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    public MyPagerAdapter getMyPagerAdapter() {
        return myPagerAdapter;
    }

    public void notifyDataChanged() {
        VIEW_COUNT = viewList.size();
        myPagerAdapter.notifyDataSetChanged();
        initDotView();
        setDotViewsBg(currentItem);
    }

    public void clear() {
        if(viewList != null && viewList.size() > 0) {
            viewList.clear();
            VIEW_COUNT = 0;
        }
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public SlideShowListener getSlideShowListener() {
        return slideShowListener;
    }

    public void setSlideShowListener(SlideShowListener slideShowListener) {
        this.slideShowListener = slideShowListener;
    }

    public interface SlideShowListener {
        public void onSlideShow(View view);
    }
}