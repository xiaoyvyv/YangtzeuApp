package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.model.SplashModel;
import com.yangtzeu.presenter.SplashPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.SplashView;
import com.yangtzeu.utils.AppIconUtils;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

public class SplashActivity extends BaseActivity implements SplashView {

    private ImageView adView;
    private SplashPresenter presenter;
    private TextView adTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    @Override
    public void findViews() {
        adView = findViewById(R.id.adView);
        adTitle = findViewById(R.id.adTitle);
    }

    @Override
    public void setEvents() {
        new AppIconUtils().pmTest(this);

        //进入窗口创建文件夹
        MyUtils.createSDCardDir("A_Tool/");
        MyUtils.createSDCardDir("A_Tool/Download/");

        presenter = new SplashPresenter(this, this);
        presenter.loadPermission(new SplashModel.OnPermissionCallBack() {
            @Override
            public void OnGranted() {
                YangtzeuUtils.getBanUser(SplashActivity.this);
                presenter.loadAdImage();
                presenter.loadUser();
            }
        });

    }

    @Override
    public ImageView getAdView() {
        return adView;
    }

    @Override
    public TextView getAdTitle() {
        return adTitle;
    }
}
