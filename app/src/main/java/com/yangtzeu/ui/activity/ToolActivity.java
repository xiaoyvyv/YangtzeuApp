package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;

import com.ad.OnAdListener;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.xiaomi.ad.common.pojo.AdType;
import com.yangtzeu.R;
import com.yangtzeu.app.MyApplication;
import com.yangtzeu.presenter.ToolPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.ToolAdapter;
import com.yangtzeu.ui.view.ToolView;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class ToolActivity extends BaseActivity implements ToolView {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ToolPresenter presenter;
    private ToolAdapter adapter;
    private Switch switchBtn;
    private FrameLayout ad_container;
    private IAdWorker mAdWorker;

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
        ad_container = findViewById(R.id.ad_container);

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



        try {
            mAdWorker = AdWorkerFactory.getAdWorker(this, ad_container, new OnAdListener() {
                @Override
                public void onResultListener(int state, Object info) {
                    LogUtils.i(state, info);
                    switch (state) {
                        case OnAdListener.AdDismissed:
                        case OnAdListener.AdFailed:
                            ad_container.setVisibility(View.GONE);
                            break;
                    }
                }
            }, AdType.AD_BANNER);
            mAdWorker.loadAndShow(MyApplication.PORTRAIT_Q_BANNER_POSITION_ID);
        } catch (Exception ignored) {}
    }

    public void onToolClick(View v) {
        switch (v.getId()) {
            case R.id.like:
                presenter.setQQLikeEvent();
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
    protected void onDestroy() {
        try {
            mAdWorker.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();

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
