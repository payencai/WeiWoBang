package com.weiwobang.paotui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.activity.AboutActivity;
import com.weiwobang.paotui.activity.MypublishActivity;
import com.weiwobang.paotui.activity.RebackActivity;
import com.weiwobang.paotui.activity.SettingActivity;
import com.weiwobang.paotui.activity.UserinfoActivity;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Userinfo;
import com.weiwobang.paotui.tools.PreferenceManager;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {
    @BindView(R.id.mupub_layout)
    RelativeLayout publish;
    @BindView(R.id.about_layout)
    RelativeLayout about;
    @BindView(R.id.sugg_layout)
    RelativeLayout sugg;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.wwb_nickname)
    TextView nickname;
    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.iv_back)
    ImageView back;
    @BindView(R.id.header_confirm)
    TextView confirm;
    public MineFragment() {
        // Required empty public constructor
    }
    Userinfo userinfo;
    public static MineFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from",from);
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.wwb_mine_fragment, container, false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void getUserinfo(){
        Disposable disposable = null;
        try {
            disposable = NetWorkManager.getRequest(ApiService.class).getUserinfo(PreferenceManager.getInstance().getUserinfo().getToken())
                    //.compose(ResponseTransformer.handleResult())
                    .compose(SchedulerProvider.getInstance().applySchedulers())
                    .subscribe(new Consumer<RetrofitResponse<Userinfo>>() {
                        @Override
                        public void accept(RetrofitResponse<Userinfo> retrofitResponse) throws Exception {
                            Userinfo user=retrofitResponse.getData();
                            PreferenceManager.getInstance().setUserinfo(user);
                            if(user!=null){
                                account.setText(user.getAccount());
                                nickname.setText(user.getNickname());
                                if (user.getHeadingUri()!=null){
                                          Glide.with(getContext()).load(user.getHeadingUri()).into(iv_head);
                                     }
                                else{

                                }
                            }
                            //Toast.makeText(getActivity(), user.getNickname()+"dfdf", Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            ApiException apiException = CustomException.handleException(throwable);
                            Toast.makeText(getActivity(), apiException.getDisplayMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        new CompositeDisposable().add(disposable);
    }
    private void initView() {

        back.setVisibility(View.GONE);
        title.setTextColor(getResources().getColor(R.color.color_333));
        title.setText("个人中心");
        confirm.setText("设置");

        confirm.setTextColor(getResources().getColor(R.color.color_333));
        getUserinfo();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), UserinfoActivity.class),1);
            }
        });
        sugg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RebackActivity.class));
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MypublishActivity.class));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            getUserinfo();
        }
    }
}
