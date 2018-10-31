package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yangtzeu.R;
import com.yangtzeu.presenter.ChooseClassPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.ChooseClassView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;

public class ChooseClassActivity extends BaseActivity implements ChooseClassView {
    private Toolbar toolbar;
    private LinearLayout chooseContainer;
    private TextView trip;

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
        ChooseClassPresenter presenter = new ChooseClassPresenter(this, this);
        presenter.getChooseClassInfo();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("原文查看").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MyUtils.openUrl(ChooseClassActivity.this, Url.Yangtzeu_ChooseClass, true);
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
}
