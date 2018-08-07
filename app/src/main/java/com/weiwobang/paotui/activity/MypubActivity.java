package com.weiwobang.paotui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.adapter.MypubAdapter;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Data;
import com.weiwobang.paotui.bean.News;
import com.weiwobang.paotui.tools.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MypubActivity extends AppCompatActivity {
    @BindView(R.id.rv_mypub)
    RecyclerView rv_pub;
    MypubAdapter mMypubAdapter;
    List<News> mNews=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wwb_activity_mypub);
        ButterKnife.bind(this);
        initAdapter();
        loadData();
    }
    private void initAdapter(){
        rv_pub.setLayoutManager(new LinearLayoutManager(this));
        mMypubAdapter=new MypubAdapter(R.layout.wwb_item_publish);
        mMypubAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                 News news= (News) adapter.getData().get(position);
                 TextView del=view.findViewById(R.id.item_del);
                 Log.e("news",del.getText().toString());
            }
        });
    }
    private void loadData(){
        Disposable disposable = null;
        try {
            disposable = NetWorkManager.getRequest(ApiService.class).getMine(1, PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse<Data<News>>>() {
                        @Override
                        public void accept(RetrofitResponse<Data<News>> retrofitResponse) throws Exception {
                            Log.e("result",retrofitResponse.getData().getBeanList().get(0).getTitle());
                            if (retrofitResponse.getData().getBeanList().size() != 0) {
                                mMypubAdapter.setNewData(retrofitResponse.getData().getBeanList());
                                rv_pub.setAdapter(mMypubAdapter);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new CompositeDisposable().add(disposable);
    }
}
