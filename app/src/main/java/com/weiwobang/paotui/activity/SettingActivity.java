package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.weiwobang.paotui.MyAPP;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.tools.ActManager;
import com.weiwobang.paotui.tools.CleanMessageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {


    @BindView(R.id.sett_back)
    ImageView back;
    @BindView(R.id.update_pwd)
    RelativeLayout updatePwd;
    @BindView(R.id.clean_file)
    RelativeLayout clean;
    @BindView(R.id.logout)
    TextView logout;
    @BindView(R.id.cache_size)
    TextView size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_setting);
        ActManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        try {
            size.setText(CleanMessageUtil.getTotalCacheSize(getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        updatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, FindpwdActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAPP.isLogin=false;
                ActManager.getAppManager().finishAllActivity();
                startActivity(new Intent(SettingActivity.this,LoginActivity.class));
            }
        });
        clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.equals("0m",size.getText().toString()))
                 CleanMessageUtil.clearAllCache(getApplicationContext());
                 size.setText("0m");
            }
        });
    }
}
