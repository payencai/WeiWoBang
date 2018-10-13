package com.wwb.paotui.tools;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wwb.paotui.R;
import com.wwb.paotui.fragment.ForumFragment;
import com.wwb.paotui.fragment.HistoryFragment;
import com.wwb.paotui.fragment.MineFragment;
import com.wwb.paotui.fragment.OrderFragment;
import com.wwb.paotui.fragment.WwbFragment;

public class BottomBar {

    public static final String []mTabTitle = new String[]{"当前派单","派单记录"};

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[2];
        fragments[0] = OrderFragment.newInstance(from);
        fragments[1] = HistoryFragment.newInstance(from);
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context, int position,int num){
        View view = LayoutInflater.from(context).inflate(R.layout.wwb_item_bottom,null);
        TextView tabNum = (TextView) view.findViewById(R.id.tab_num);
        TextView tabText = (TextView) view.findViewById(R.id.tab_name);
        if(num>=99)
           tabNum.setText("99+");
        else if(num>=0){
            tabNum.setText(""+num);
        }
        tabText.setText(mTabTitle[position]);
        return view;
    }
}
