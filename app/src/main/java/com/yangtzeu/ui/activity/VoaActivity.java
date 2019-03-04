package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.yangtzeu.R;
import com.yangtzeu.presenter.VoaPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.VoaView;
import com.yangtzeu.utils.MediaPlayUtil;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;

public class VoaActivity extends BaseActivity implements VoaView {
    private Toolbar toolbar;
    private LinearLayout slow_container;
    private LinearLayout normal_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voa);
        init();
        MyUtils.setToolbarBackToHome(this,toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        slow_container = findViewById(R.id.slow_container);
        normal_container = findViewById(R.id.normal_container);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setEvents() {
        VoaPresenter presenter = new VoaPresenter(this, this);
        presenter.getSlowVoaTitleList();
        presenter.getNormalVoaTitleList();
    }

    @Override
    public LinearLayout getNormalContainer() {
        return normal_container;
    }

    @Override
    public LinearLayout getSlowContainer() {
        return slow_container;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("关闭音频").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MediaPlayUtil instance = MediaPlayUtil.getInstance();
                if (instance.isPlaying()) {
                    instance.stop();
                }
                return true;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
}
