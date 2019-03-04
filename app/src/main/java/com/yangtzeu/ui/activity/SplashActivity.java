package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yangtzeu.R;
import com.yangtzeu.model.SplashModel;
import com.yangtzeu.presenter.SplashPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.SplashView;
import com.yangtzeu.utils.AppIconUtils;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.ALiOssUtils;
import com.yangtzeu.utils.YangtzeuUtils;

public class SplashActivity extends BaseActivity implements SplashView {

    private ImageView adView;
    private SplashPresenter presenter;
    private TextView adTitle;
    private TextView times;


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
        times = findViewById(R.id.times);

    }

    @Override
    public void setEvents() {
        new AppIconUtils().pmTest(this);

        presenter = new SplashPresenter(this, this);
        presenter.checkCopyRight();

        presenter.loadPermission(new SplashModel.OnPermissionCallBack() {
            @Override
            public void OnGranted() {
                //进入窗口创建文件夹
                MyUtils.createSDCardDir("A_Tool/");
                MyUtils.createSDCardDir("A_Tool/Download/");
                MyUtils.createSDCardDir(SPUtils.getInstance("app_info").getString("save_path", "A_Tool/Download/"));

                YangtzeuUtils.getBanUser(SplashActivity.this);
                presenter.loadAdImage();
                presenter.loadUser();


                //总共打开次数
                TextView tv = new TextView(SplashActivity.this);
                tv.setText(R.string.app_name);
                YangtzeuUtils.getOnClickTimes(tv, times, true);
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
