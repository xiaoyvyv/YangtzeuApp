package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputEditText;
import com.lib.gramophone.GramophoneView;
import com.yangtzeu.R;
import com.yangtzeu.presenter.KGPresident;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.KGView;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.MediaPlayUtil;
import com.yangtzeu.utils.MyUtils;

import java.util.Objects;

public class KgActivity extends BaseActivity implements KGView {
    private Toolbar toolbar;
    private TextInputEditText music_link;
    private GramophoneView image;
    private Button play;
    private Button download;
    private KGPresident president;
    private TextView true_path;
    private LinearLayout googleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kg);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        music_link = findViewById(R.id.music_link);
        play = findViewById(R.id.play);
        download = findViewById(R.id.download);
        image = findViewById(R.id.image);
        true_path = findViewById(R.id.true_path);
        googleView = findViewById(R.id.googleView);
    }

    @Override
    public void setEvents() {
        president = new KGPresident(this, this);
        play.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                president.playMusic();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                president.downloadMusic();
            }
        });

        AdView adView1 = GoogleUtils.newBannerView(this, AdSize.LARGE_BANNER);
        adView1.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView1);
        AdView adView2 = GoogleUtils.newBannerView(this, AdSize.LARGE_BANNER);
        adView2.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(R.string.help).setIcon(R.drawable.ic_about)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        MyUtils.getAlert(KgActivity.this,
                                getString(R.string.kg_help), null).show();
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public String getKgLink() {
        return Objects.requireNonNull(music_link.getText()).toString().trim();
    }

    @Override
    public TextView getTruePath() {
        return true_path;
    }

    @Override
    public GramophoneView getMusicView() {
        return image;
    }

    @Override
    protected void onDestroy() {
        MediaPlayUtil.getInstance().stop();
        super.onDestroy();
    }
}
