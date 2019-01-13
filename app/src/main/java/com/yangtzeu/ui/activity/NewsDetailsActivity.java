package com.yangtzeu.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.easyandroidanimations.library.BounceAnimation;
import com.lib.loading.LVBlock;
import com.lib.x5web.X5DownloadListener;
import com.lib.x5web.X5WebView;
import com.tencent.smtt.sdk.WebSettings;
import com.yangtzeu.R;
import com.yangtzeu.presenter.NewsDetailsPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.NewsDetailsView;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.ShareUtils;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class NewsDetailsActivity extends BaseActivity implements NewsDetailsView {
    private LVBlock loading;
    private Toolbar toolbar;
    private NewsDetailsPresenter presenter;
    private String from_url;
    private RelativeLayout container;
    private X5WebView webView;
    private ImageView image;
    private TextView share;
    private TextView clear;
    private String share_path;
    private CardView cardView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.webkit.WebView.enableSlowWholeDocumentDraw();
        from_url = getIntent().getStringExtra("from_url");

        setContentView(R.layout.activity_news_details);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        container = findViewById(R.id.container);
        loading = findViewById(R.id.loading);
        toolbar = findViewById(R.id.toolbar);
        image = findViewById(R.id.image);
        clear = findViewById(R.id.clear);
        share = findViewById(R.id.share);
        cardView = findViewById(R.id.cardView);

    }

    @Override
    public void setEvents() {
        webView = new X5WebView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        container.addView(webView, 0);

        loading.setViewColor(getTheme().getResources().getColor(R.color.colorPrimary));
        loading.startAnim();

        presenter = new NewsDetailsPresenter(this, this);
        presenter.loadData();

        webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        webView.setDownloadListener(new X5DownloadListener(this));


        share_path = MyUtils.rootPath() + "A_Tool/Download/Image/" + toolbar.getTitle() + ".jpg";

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image.setImageBitmap(null);
                cardView.setVisibility(View.GONE);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(share_path);
                if (ObjectUtils.isNotEmpty(from_url))
                    ShareUtils.shareFile(NewsDetailsActivity.this, file);
                else ToastUtils.showShort(R.string.share_error);
                image.setImageBitmap(null);
                cardView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("分享").setIcon(R.drawable.ic_share).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Bitmap screenViewPicture = MyUtils.getScreenViewPicture(NewsDetailsActivity.this, getWindow().getDecorView(), webView);
                ImageUtils.save(screenViewPicture, share_path, Bitmap.CompressFormat.JPEG);
                MyUtils.saveImageToGallery(NewsDetailsActivity.this, share_path);

                cardView.setVisibility(View.VISIBLE);
                image.setImageBitmap(screenViewPicture);
                new BounceAnimation(cardView).setDuration(2000).animate();
                ToastUtils.showShort("图片保存成功");

                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add("查看原文").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MyUtils.openUrl(NewsDetailsActivity.this, from_url);
                return false;
            }
        });
        menu.add("浏览器中打开").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MyUtils.openBrowser(NewsDetailsActivity.this, from_url);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public String getFromUrl() {
        return from_url;
    }

    @Override
    public LVBlock getLoadingView() {
        return loading;
    }

    @Override
    public RelativeLayout getContainer() {
        return container;
    }

    @Override
    public X5WebView getWebView() {
        return webView;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }
}
