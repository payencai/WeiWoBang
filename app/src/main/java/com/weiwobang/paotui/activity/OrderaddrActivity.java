package com.weiwobang.paotui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weiwobang.paotui.R;
import com.weiwobang.paotui.bean.AddrBean;
import com.weiwobang.paotui.bean.NewAddr;
import com.weiwobang.paotui.bean.OrderAddr;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderaddrActivity extends AppCompatActivity {
    int type;
    @BindView(R.id.addrContact)
    RelativeLayout addrContact;
    @BindView(R.id.addrPhone)
    RelativeLayout addrPhone;
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
    @BindView(R.id.et_name)
    EditText contact;
    @BindView(R.id.et_phone)
    EditText tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_orderaddr);
        ButterKnife.bind(this);
        initView();
    }

    void initView() {
        type = getIntent().getExtras().getInt("type");
        int flag = getIntent().getExtras().getInt("flag");
        if (type == 2) {
            addrContact.setVisibility(View.GONE);
            addrPhone.setVisibility(View.GONE);
            if (flag == 1) {
                tv_addr.setText("当前地址");
            } else if (flag == 2) {
                tv_addr.setText("目的地地址");
            }
        } else if (type == 1 || type == 3) {
            if (flag == 1) {
                tv_addr.setText("取货人信息");
            }
            if (flag == 2) {
                tv_addr.setText("送货人信息");
            }

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
                startActivityForResult(new Intent(OrderaddrActivity.this, SelectAddrActivity.class), 1);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addrBean == null) {
                    Toast.makeText(OrderaddrActivity.this, "你还没有选择地址", Toast.LENGTH_LONG).show();
                    return;
                }
                if(type!=2){
                    if(TextUtils.isEmpty(contact.getText().toString())){
                        Toast.makeText(OrderaddrActivity.this, "联系人不能为空", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(TextUtils.isEmpty(tel.getText().toString())){
                        Toast.makeText(OrderaddrActivity.this, "联系电话不能为空", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                NewAddr newAddr = new NewAddr(addrBean.getFiraddr() ,addrBean.getSecaddr(), addrBean.getLat(), addrBean.getLon(),contact.getEditableText().toString(),tel.getEditableText().toString());
                newAddr.setDetail(detail.getEditableText().toString());
                bundle.putSerializable("addr", newAddr);
                intent.putExtras(bundle);
                setResult(1, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            addrBean = (AddrBean) data.getExtras().getSerializable("addrbean");
            addr1.setText(addrBean.getSecaddr());
        }
    }
}
