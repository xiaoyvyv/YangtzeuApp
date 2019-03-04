package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.chat.common.UserManager;
import com.yangtzeu.R;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.InfoPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.InfoView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import androidx.appcompat.widget.Toolbar;

public class InfoActivity extends BaseActivity implements InfoView {
    private InfoPresenter presenter;
    private Toolbar toolbar;
    private ImageView headerView;
    private TextView name;
    private TextView number;
    private String idStr;
    private String nameStr;
    private LinearLayout mineInfoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idStr = getIntent().getStringExtra("id");
        nameStr = getIntent().getStringExtra("name");
        setContentView(R.layout.activity_info);

        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        headerView = findViewById(R.id.headerView);
        name = findViewById(R.id.name);
        number= findViewById(R.id.number);
        mineInfoLayout = findViewById(R.id.mineInfoLayout);
    }


    @Override
    public void setEvents() {
        index_url = Url.Yangtzeu_XueJI_Index1;
        url = Url.Yangtzeu_XueJI1;

        name.setText(nameStr);
        number.setText(idStr);

        presenter = new InfoPresenter(this, this);
        presenter.loadHeader();

        if (idStr.equals(UserManager.getInstance().getAccount())) {
            presenter.loadMineInfo();
        }

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.showChangeHeaderView();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("切换").setIcon(R.drawable.ic_change)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        YangtzeuUtils.showChooseModel(new OnResultListener<Integer>() {
                            @Override
                            public void onResult(Integer projectId) {
                                switch (projectId) {
                                    case 1:
                                        index_url = Url.Yangtzeu_XueJI_Index1;
                                        url = Url.Yangtzeu_XueJI1;
                                        presenter.loadMineInfo();
                                        break;
                                    case 2:
                                        index_url = Url.Yangtzeu_XueJI_Index2;
                                        url = Url.Yangtzeu_XueJI2;
                                        presenter.loadMineInfo();
                                        break;
                                }
                            }
                        });
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add("原文查看").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MyUtils.openUrl(InfoActivity.this, url, true);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public ImageView getHeader() {
        return headerView;
    }

    @Override
    public LinearLayout getMineInfoLayout() {
        return mineInfoLayout;
    }

    @Override
    public String getIndexUrl() {
        return index_url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
