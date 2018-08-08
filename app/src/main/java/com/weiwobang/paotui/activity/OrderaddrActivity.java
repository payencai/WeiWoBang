package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.AddrBean;
import com.weiwobang.paotui.bean.OrderAddr;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderaddrActivity extends AppCompatActivity {
    int addr;
    @BindView(R.id.tv_addr)
    TextView tv_addr;
    @BindView(R.id.sel_addr)
    RelativeLayout seladdr;
    AddrBean addrBean;
    @BindView(R.id.in_addr1)
    TextView addr1;
    @BindView(R.id.in_addrdet)
    EditText detail;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.back)
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_orderaddr);
        ButterKnife.bind(this);
        initView();
    }
    void initView(){
       addr= getIntent().getExtras().getInt("addr");
       if(addr>0){
           tv_addr.setText("目的地地址");
       }
       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
        seladdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(OrderaddrActivity.this,SelectAddrActivity.class),1);
            }
        });
       confirm.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(addrBean==null){
                   Toast.makeText(OrderaddrActivity.this,"你还没有选择地址",Toast.LENGTH_LONG).show();
                   return;
               }
               Intent intent=new Intent();
               Bundle bundle=new Bundle();
               OrderAddr orderAddr=new OrderAddr(addrBean.getFiraddr()+addrBean.getSecaddr(),detail.getEditableText().toString(),addrBean.getLat(),addrBean.getLon());
               bundle.putSerializable("addr",orderAddr);
               intent.putExtras(bundle);
               setResult(1,intent);
               finish();
           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&data!=null){
             addrBean= (AddrBean) data.getExtras().getSerializable("addrbean");
             addr1.setText(addrBean.getSecaddr());
        }
    }
}
