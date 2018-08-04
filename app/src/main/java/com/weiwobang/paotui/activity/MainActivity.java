package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.DataGenerator;


public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private Fragment[] mFragmensts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wwb_activity_main);
        ActManager.getAppManager().addActivity(this);
        mFragmensts = DataGenerator.getFragments("TabLayout Tab");
        initView();
    }

    private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int pos = (int) view.getTag();
            TabLayout.Tab tab = mTabLayout.getTabAt(pos);
            if (pos==1) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                tab.select();
            }

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("pos", tab.getPosition() + "");
                if (tab.getPosition() == 1 && !MyAPP.isLogin) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                    View tabView = (View) tab.getCustomView().getParent();
//                    tabView.setTag(1);
//                    tabView.setOnClickListener(mTabOnClickListener);

                } else {
                    onTabItemSelected(tab.getPosition());

                    // Tab 选中之后，改变各个Tab的状态
                    for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                        View view = mTabLayout.getTabAt(i).getCustomView();
                        ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                        TextView text = (TextView) view.findViewById(R.id.tab_content_text);
                        if (i == tab.getPosition()) { // 选中状态
                            icon.setImageResource(DataGenerator.mTabResPressed[i]);
                            text.setTextColor(getResources().getColor(R.color.press_yellow));
                        } else {// 未选中状态
                            icon.setImageResource(DataGenerator.mTabRes[i]);
                            text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        }
                    }
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("pos", tab.getPosition() + "");
                if (tab.getPosition() == 1 && !MyAPP.isLogin) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));

                }else{

                }
            }
        });
        // 提供自定义的布局添加Tab
        for (int i = 0; i < 2; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenerator.getTabView(this, i)));
        }

    }

    private void onTabItemSelected(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = mFragmensts[0];
                break;
            case 1:
                fragment = mFragmensts[1];
                break;

        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).commit();
        }
    }




}
