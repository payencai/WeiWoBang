package com.wwb.paotui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.wwb.paotui.MyAPP;
import com.wwb.paotui.R;
import com.wwb.paotui.adapter.CommentAdapter;
import com.wwb.paotui.adapter.LocateAdapter;
import com.wwb.paotui.api.ApiService;
import com.wwb.paotui.bean.AddrBean;
import com.wwb.paotui.bean.Comment;
import com.wwb.paotui.bean.LocateBean;
import com.wwb.paotui.bean.NewAddr;
import com.wwb.paotui.bean.OrderAddr;
import com.wwb.paotui.bean.RunnerModel;
import com.wwb.paotui.tools.GsonUtils;
import com.wwb.paotui.view.FullyLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;

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
    @BindView(R.id.rv_locate)
    RecyclerView rv_locate;
    LocateAdapter mLocateAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_orderaddr);
        ButterKnife.bind(this);
        initView();
        initData();
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
                NewAddr newAddr = new NewAddr(addr1.getText().toString() ,detail.getEditableText().toString(), addrBean.getLat(), addrBean.getLon(),contact.getEditableText().toString(),tel.getEditableText().toString());
                //newAddr.setDetail(detail.getEditableText().toString());
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
            addr1.setText(addrBean.getFiraddr()+"");
            detail.setText(addrBean.getSecaddr()+"");
        }
    }
    private void initData(){
        FullyLinearLayoutManager mLayoutManager2 = new FullyLinearLayoutManager(this);
        rv_locate.setLayoutManager(mLayoutManager2);
        rv_locate.setNestedScrollingEnabled(false);
        mLocateAdapter = new LocateAdapter(R.layout.wwb_addr_locate);
        mLocateAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LocateBean locateBean = mLocateAdapter.getItem(position);
                Log.e("locate",locateBean.toString());
                if(addrBean==null){
                    addrBean=new AddrBean();
                }
                addrBean.setLat(Double.parseDouble(locateBean.getLatitude()));
                addrBean.setLon(Double.parseDouble(locateBean.getLongitude()));
                addr1.setText(locateBean.getHeading()+"");
                detail.setText(locateBean.getAdress()+"");
                contact.setText(locateBean.getContactName());
                tel.setText(locateBean.getContactNumber());
            }
        });
        getMostLocate();
    }
    private void getMostLocate(){
        //Log.e("ordertoken",MyAPP.token2);
        Disposable disposable = NetWorkManager.getRequest(ApiService.class).getMostLocate(MyAPP.token2)
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody retrofitResponse) throws Exception {

                        JSONObject jsonObject = new JSONObject(retrofitResponse.string());
                        //String msg = jsonObject.getString("message");
                        int code = jsonObject.getInt("resultCode");
                        if (code == 0) {
                            JSONArray data = jsonObject.getJSONArray("data");
                            List<LocateBean> locateBeans = GsonUtils.jsonToArrayList(data.toString(),LocateBean.class);
                            mLocateAdapter.setNewData(locateBeans);
                            rv_locate.setAdapter(mLocateAdapter);
                            Log.e("rn",data.toString());
                        } else {
                           // Toast.makeText(OrderaddrActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ApiException apiException = CustomException.handleException(throwable);
                        Toast.makeText(OrderaddrActivity.this, apiException.getCode()+"code", Toast.LENGTH_SHORT).show();
                    }
                });
        new CompositeDisposable().add(disposable);
    }
}
