package com.wwb.paotui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.wwb.paotui.R;
import com.wwb.paotui.tools.ActManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProtocalActivity extends AppCompatActivity {
    @BindView(R.id.pro_back)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_protocal);
        ButterKnife.bind(this);
        ActManager.getAppManager().addActivity(this);
        init();
    }
    private void init(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
