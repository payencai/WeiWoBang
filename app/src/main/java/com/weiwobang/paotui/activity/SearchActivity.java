package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.weiwobang.paotui.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.cancel_back)
    TextView back;
    @BindView(R.id.search_btn)
    TextView search;
    @BindView(R.id.search_et)
    EditText content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SearchActivity.this,SearchresActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("content",content.getEditableText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

}
