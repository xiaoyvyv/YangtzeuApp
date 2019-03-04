package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.lib.x5web.WebViewProgressBar;
import com.lib.x5web.X5WebView;
import com.lib.yun.StringUtils;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;

public class AnswerListActivity extends BaseActivity {
    private Toolbar toolbar;
    private String from_url;
    private FrameLayout container;
    private X5WebView webView;
    private WebViewProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        from_url = getIntent().getStringExtra("from_url");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_list);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.slow_container);
        progress = findViewById(R.id.progress);
    }


    @Override
    public void setEvents() {
        webView = new X5WebView(this);
        container.addView(webView, 0);
        webView.setTitleAndProgressBar(toolbar, progress);
        webView.setOnLongClickListener(null);
        if (StringUtils.isNotEmpty(from_url))
            webView.loadUrl(from_url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
