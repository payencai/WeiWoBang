package com.wwb.paotui.callback;

import android.view.View;

public interface ChartItemListener {
    public void onItemClick(View chartItem);
    public void onRejectBtnClick(View chartItem);
    public void onAcceptBtnClick(View chartItem);
}