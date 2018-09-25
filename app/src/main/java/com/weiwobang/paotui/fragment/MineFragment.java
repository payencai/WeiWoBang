package com.weiwobang.paotui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.payencai.library.http.retrofitAndrxjava.ApiException;
import com.payencai.library.http.retrofitAndrxjava.CustomException;
import com.payencai.library.http.retrofitAndrxjava.NetWorkManager;
import com.payencai.library.http.retrofitAndrxjava.RetrofitResponse;
import com.payencai.library.http.retrofitAndrxjava.schedulers.SchedulerProvider;
import com.weiwobang.paotui.R;
import com.weiwobang.paotui.activity.TelescopeActivity;
import com.weiwobang.paotui.activity.AboutActivity;
import com.weiwobang.paotui.activity.ContractActivity;
import com.weiwobang.paotui.activity.MypublishActivity;
import com.weiwobang.paotui.activity.OrderActivity;
import com.weiwobang.paotui.activity.RebackActivity;
import com.weiwobang.paotui.activity.SellermainActivity;
import com.weiwobang.paotui.activity.SettingActivity;
import com.weiwobang.paotui.activity.UserinfoActivity;
import com.weiwobang.paotui.api.ApiService;
import com.weiwobang.paotui.bean.Userinfo;
import com.weiwobang.paotui.mvp.Contract;
import com.weiwobang.paotui.mvp.presenter.MvpPresenter;
import com.weiwobang.paotui.tools.PreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements Contract.MvpView<Userinfo> {
    @BindView(R.id.order_layout)
    RelativeLayout order;
    @BindView(R.id.mupub_layout)
    RelativeLayout publish;
    @BindView(R.id.runner_layout)
    RelativeLayout runner;
    @BindView(R.id.about_layout)
    RelativeLayout about;
    @BindView(R.id.sugg_layout)
    RelativeLayout sugg;
    @BindView(R.id.tele_layout)
    RelativeLayout telescope;
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
    private MvpPresenter<Userinfo> mUserinfoPresenter;

    public MineFragment() {
        // Required empty public constructor
    }

    Userinfo userinfo;

    public static MineFragment newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("from", from);
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.wwb_mine_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        getData();
        //getUserinfo();
        return view;
    }

    private void getData() {
        Userinfo userinfo = PreferenceManager.getInstance().getUserinfo();
        if (userinfo != null) {
            mUserinfoPresenter = new MvpPresenter(this, userinfo.getToken());
            mUserinfoPresenter.start();
        }

    }

    private void getUserinfo() {
        Disposable
                disposable = NetWorkManager.getRequest(ApiService.class).getUserinfo(PreferenceManager.getInstance().getUserinfo().getToken())
                //.compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Consumer<RetrofitResponse<Userinfo>>() {
                    @Override
                    public void accept(RetrofitResponse<Userinfo> retrofitResponse) throws Exception {
                        Userinfo user = retrofitResponse.getData();
                        PreferenceManager.getInstance().setUserinfo(user);
                        if (user != null) {
                            account.setText(user.getAccount());
                            nickname.setText(user.getNickname());
                            if (user.getHeadingUri() != null) {
                                Glide.with(getContext()).load(user.getHeadingUri()).into(iv_head);
                            } else {

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

        new CompositeDisposable().add(disposable);
    }

    private void initView() {

        back.setVisibility(View.GONE);
        title.setTextColor(getResources().getColor(R.color.color_333));
        title.setText("个人中心");
        confirm.setText("设置");
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OrderActivity.class));
            }
        });
        confirm.setTextColor(getResources().getColor(R.color.color_333));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), UserinfoActivity.class), 1);
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
                //startActivity(new Intent(getActivity(), MypubActivity.class));
                startActivity(new Intent(getActivity(), MypublishActivity.class));
            }
        });
        telescope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TelescopeActivity.class));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });
        runner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (PreferenceManager.getInstance().getUserinfo().getIsBusiness().equals("2"))
                    startActivity(new Intent(getContext(), SellermainActivity.class));
                else {
                    startActivity(new Intent(getContext(), ContractActivity.class));
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            getData();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showData(Userinfo data) {

        PreferenceManager.getInstance().setUserinfo(data);
        if (data != null) {
            account.setText(data.getAccount());
            nickname.setText(data.getNickname());
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.wwb_default_photo) //加载中图片
                    .error(R.mipmap.wwb_default_photo) //加载失败图片
                    .fallback(R.mipmap.wwb_default_photo) //url为空图片
                    .centerCrop();// 填充方式
            //Log.e("ggg",image+name);
            Glide.with(getContext()).load(data.getHeadingUri()).apply(requestOptions).into(iv_head);
        }


    }

    @Override
    public void failed(String error) {

    }

    @Override
    public void setPresenter(Contract.Presenter presenter) {

    }
}
