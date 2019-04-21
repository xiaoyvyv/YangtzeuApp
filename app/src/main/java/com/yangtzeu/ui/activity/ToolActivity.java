package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.yangtzeu.R;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.ToolPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.ToolAdapter;
import com.yangtzeu.ui.view.ToolView;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.MyUtils;

public class ToolActivity extends BaseActivity implements ToolView {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ToolPresenter presenter;
    private ToolAdapter adapter;
    private Switch switchBtn;
    private LinearLayout googleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        recyclerView = findViewById(R.id.recyclerView);
        toolbar = findViewById(R.id.toolbar);
        switchBtn = findViewById(R.id.switchBtn);
        googleView = findViewById(R.id.googleView);

    }

    @Override
    public void setEvents() {

        adapter = new ToolAdapter(this);
        presenter = new ToolPresenter(this, this);
        RecyclerView.LayoutManager manger = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manger);
        recyclerView.setAdapter(adapter);

        boolean b = SPUtils.getInstance("app_info").getBoolean("listen_qq", true);
        switchBtn.setChecked(b);
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ToastUtils.showShort("QQ图片监听服务：" + isChecked);
                SPUtils.getInstance("app_info").put("listen_qq", isChecked);
            }
        });


        AdView adView1 = GoogleUtils.newBannerView(this, AdSize.LARGE_BANNER);
        adView1.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView1);
        AdView adView2 = GoogleUtils.newBannerView(this, AdSize.LARGE_BANNER);
        adView2.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView2);

        GoogleUtils.loadInterstitialAd();
    }

    public void onToolClick(View v) {
        switch (v.getId()) {
            case R.id.like:
                GoogleUtils.showInterstitialAd(new OnResultListener<Boolean>() {
                    @Override
                    public void onResult(Boolean s) {
                        presenter.setQQLikeEvent();
                    }
                });
                break;
            case R.id.cehui:
                presenter.getCeHuiData();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getCeHuiData();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public ToolAdapter getAdapter() {
        return adapter;
    }


}
