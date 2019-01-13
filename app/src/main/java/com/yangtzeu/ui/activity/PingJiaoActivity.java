package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.yangtzeu.R;
import com.yangtzeu.presenter.PingJiaoPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.PingJiaoView;
import com.yangtzeu.utils.MyUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class PingJiaoActivity extends BaseActivity implements PingJiaoView {
    private LinearLayout container;
    private Toolbar toolbar;
    private PingJiaoPresenter pingJiaoPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingjiao);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.container);
    }

    @Override
    public void setEvents() {
        pingJiaoPresenter = new PingJiaoPresenter(PingJiaoActivity.this, PingJiaoActivity.this);
    }

    @Override
    protected void onResume() {
        pingJiaoPresenter.loadData();
        super.onResume();
    }

    @Override
    public LinearLayout getContainer() {
        return container;
    }
}
