package com.weiwobang.paotui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.activity.LoginActivity;
import com.weiwobang.paotui.activity.MainActivity;
import com.weiwobang.paotui.tools.DataGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    @BindView(R.id.bottom_tab_layout)
    TabLayout mTabLayout;
    private Fragment[] mFragmensts;

    public static final String[] mTabTitle = new String[]{"我要跑腿", "我要搬家"};

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.wwb_fragment_home, container, false);
        ButterKnife.bind(this, view);
        mFragmensts = getFragments("tab");
        addFragments();
        initView();
        return view;
    }

    public static Fragment[] getFragments(String from) {
        Fragment fragments[] = new Fragment[2];
        fragments[0] = RunnerFragment.newInstance(from);
        fragments[1] = RemoveFragment.newInstance(from);
        //fragments[2] = DeliveryFragment.newInstance(from);
        return fragments;
    }

    /**
     * 获取Tab 显示的内容
     *
     * @param context
     * @param position
     * @return
     */
    public static View getTabView(Context context, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.wwb_item_top, null);
        TextView tabText = (TextView) view.findViewById(R.id.itemtop);
        tabText.setText(mTabTitle[position]);
        return view;
    }

    private void addFragments() {
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content, mFragmensts[0]).commit();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content, mFragmensts[1]).commit();
        //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content, mFragmensts[2]).commit();
    }

    private void showFragment(int index) {
        getActivity().getSupportFragmentManager().beginTransaction().show(mFragmensts[index]).commit();
    }

    private void hideFragment(int index) {
        getActivity().getSupportFragmentManager().beginTransaction().hide(mFragmensts[index]).commit();
    }
    private void onTabItemSelected(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = mFragmensts[0];
                hideFragment(1);
                //hideFragment(2);
                showFragment(0);
                break;
            case 1:
                fragment = mFragmensts[1];
                hideFragment(0);
                showFragment(1);
               // hideFragment(2);
                break;
//            case 2:
//                fragment = mFragmensts[2];
//                hideFragment(0);
//                hideFragment(1);
//                //showFragment(2);
//                break;

        }
    }

    private void initView() {

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("pos", tab.getPosition() + "");

                onTabItemSelected(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    TextView text = (TextView) view.findViewById(R.id.itemtop);
                    if (i == tab.getPosition()) { // 选中状态
                        text.setTextColor(getResources().getColor(R.color.press_yellow));
                    } else {// 未选中状态
                        text.setTextColor(getResources().getColor(R.color.tv_666));
                    }
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // 提供自定义的布局添加Tab
        for (int i = 0; i < 2; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTabView(getContext(), i)));
        }

    }
}
