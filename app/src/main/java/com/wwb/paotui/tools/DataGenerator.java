package com.wwb.paotui.tools;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wwb.paotui.R;
import com.wwb.paotui.fragment.ForumFragment;
import com.wwb.paotui.fragment.HomeFragment;
import com.wwb.paotui.fragment.MineFragment;
import com.wwb.paotui.fragment.WwbFragment;


public class DataGenerator {
    public static final int []mTabRes = new int[]{R.mipmap.tab_wwb_grey,R.mipmap.wwb_forum_grey,R.mipmap.wwb_my_grey};
    public static final int []mTabResPressed = new int[]{R.mipmap.wwb_home_yellow,R.mipmap.wwb_home_pre,R.mipmap.wwb_my_pre};
    public static final String []mTabTitle = new String[]{"唯我帮","同城信息","个人中心"};

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[3];
        fragments[0] = HomeFragment.newInstance(from);
        fragments[1] = ForumFragment.newInstance(from);
        fragments[2] = MineFragment.newInstance(from);
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context, int position){
        View view = LayoutInflater.from(context).inflate(R.layout.wwb_item_tab,null);
        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
        tabText.setText(mTabTitle[position]);
        return view;
    }
}
