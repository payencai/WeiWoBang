package com.wwb.paotui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.payencai.library.view.bottombar.BottomNaviView;
import com.payencai.library.view.bottombar.entity.BottomItem;
import com.wwb.paotui.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {
    @BindView(R.id.bottombar)
    BottomNaviView mBottomNaviView;
    private List<BottomItem> mBottomItems = new ArrayList<>();
    public static final int[] mTabRes = new int[]{R.mipmap.tab_wwb_grey, R.mipmap.wwb_forum_grey, R.mipmap.wwb_my_grey};
    public static final int[] mTabResPressed = new int[]{R.mipmap.wwb_home_yellow, R.mipmap.wwb_home_pre, R.mipmap.wwb_my_pre};
    public static final String[] mTabTitle = new String[]{"唯我帮", "同城信息", "个人中心"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        for (int i = 0; i < mTabTitle.length; i++) {
            BottomItem bottomItem = new BottomItem();
            if (i == 2)
                bottomItem.setHasRedDot(true);
            if (i == 1)
                bottomItem.setItemCount(7);
            else
                bottomItem.setItemCount(0);
            bottomItem.setItemTitle(mTabTitle[i]);
            bottomItem.setPressIcon(mTabResPressed[i]);
            bottomItem.setUnPressIcon(mTabRes[i]);
            mBottomItems.add(bottomItem);
        }
        mBottomNaviView.setTabCount(mTabTitle.length).setTabData(mBottomItems).build();
        mBottomNaviView.setOnTabItemClickListener(new BottomNaviView.onTabItemClickListener() {
            @Override
            public void onClick(View view, BottomItem bottomItem) {
                TextView tabMsgnum = view.findViewById(com.payencai.library.R.id.item_msgNum);
                if (bottomItem.getItemCount() > 0) {
                    int count = bottomItem.getItemCount() + 1;
                    bottomItem.setItemCount(count);
                    tabMsgnum.setText(count+"");
                }
                View tabDot = view.findViewById(com.payencai.library.R.id.item_dot);
                if (bottomItem.isHasRedDot()) {
                    tabDot.setVisibility(View.GONE);
                }
            }
        });
    }
}
