package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yangtzeu.R;
import com.yangtzeu.presenter.VoaDetailsPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.VoaDetailsView;
import com.yangtzeu.utils.MediaPlayUtil;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;

public class VoaDetailsActivity extends BaseActivity implements VoaDetailsView {
    private Toolbar toolbar;
    private VoaDetailsPresenter presenter;
    private LinearLayout text;
    private FloatingActionButton play;
    private String from_url;
    private String mp3 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("title");
        from_url = getIntent().getStringExtra("from_url");
        if (from_url == null) {
            ToastUtils.showShort(R.string.no_data);
        } else {
            setContentView(R.layout.activity_voa_details);
            init();
            toolbar.setTitle(title);
            MyUtils.setToolbarBackToHome(this, toolbar);
        }
    }

        @Override
        public void findViews () {
            toolbar = findViewById(R.id.toolbar);
            text = findViewById(R.id.text);
            play = findViewById(R.id.play);


        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setEvents () {
            presenter = new VoaDetailsPresenter(this, this);
            presenter.loadData();

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mp3 == null) {
                        ToastUtils.showShort(R.string.wait_load_finish);
                    } else {
                        MediaPlayUtil.getInstance().play(mp3);
                    }
                }
            });
        }

        @Override
        public LinearLayout getText () {
            return text;
        }

        @Override
        public String getUrl () {
            return from_url;
        }

    @Override
    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }
}
