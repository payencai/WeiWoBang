package com.weiwobang.paotui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.weiwobang.paotui.R;
import com.weiwobang.paotui.tools.ActManager;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_about);
        ActManager.getAppManager().addActivity(this);
    }
}
