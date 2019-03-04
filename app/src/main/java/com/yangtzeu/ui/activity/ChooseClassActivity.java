package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangtzeu.R;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.ChooseClassPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.ChooseClassView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import androidx.appcompat.widget.Toolbar;

public class ChooseClassActivity extends BaseActivity implements ChooseClassView {
    private Toolbar toolbar;
    private LinearLayout chooseContainer;
    private TextView trip;
    private ChooseClassPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_class);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        trip = findViewById(R.id.trip);
        toolbar = findViewById(R.id.toolbar);
        chooseContainer = findViewById(R.id.chooseContainer);

    }

    @Override
    public void setEvents() {
        index_url = Url.Yangtzeu_ChooseClass_Index1;
        url = Url.Yangtzeu_ChooseClass1;

        presenter = new ChooseClassPresenter(this, this);
        presenter.getChooseClassInfo();
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
                                        index_url = Url.Yangtzeu_ChooseClass_Index1;
                                        url = Url.Yangtzeu_ChooseClass1;
                                        presenter.getChooseClassInfo();
                                        break;
                                    case 2:
                                        index_url = Url.Yangtzeu_ChooseClass_Index2;
                                        url = Url.Yangtzeu_ChooseClass2;
                                        presenter.getChooseClassInfo();
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
                MyUtils.openUrl(ChooseClassActivity.this, url, true);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public LinearLayout getContainer() {
        return chooseContainer;
    }

    @Override
    public TextView getTrip() {
        return trip;
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
