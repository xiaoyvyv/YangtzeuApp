package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.lib.touch.DragView;
import com.lib.x5web.WebViewProgressBar;
import com.lib.x5web.X5JavaScriptFunction;
import com.lib.x5web.X5WebView;
import com.yangtzeu.R;
import com.yangtzeu.model.AnswerListModel;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.utils.GoogleUtils;

public class AnswerWebActivity extends BaseActivity {
    private X5WebView mWebView;
    private WebViewProgressBar progress;
    private String from_url;
    private FrameLayout web_container;
    private DragView ic_close;
    private LinearLayout googleView;
    private boolean isExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        ScreenUtils.setFullScreen(this);
        from_url = getIntent().getStringExtra("from_url");
        setContentView(R.layout.activity_answer_web);
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebView.getSettings().setJavaScriptEnabled(false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void findViews() {
        mWebView = new X5WebView(this);
        progress = findViewById(R.id.web_progress);
        web_container = findViewById(R.id.web_container);
        ic_close = findViewById(R.id.ic_close);
        googleView = findViewById(R.id.googleView);

    }

    @SuppressLint({"SetJavaScriptEnabled", "RestrictedApi"})
    @Override
    public void setEvents() {
        mWebView.getSettings().setUserAgent(AnswerListModel.encodeKey());

        ic_close.setPadding(ConvertUtils.dp2px(15));
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnswerWebActivity.this.finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        web_container.addView(mWebView, 0, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        progress.setProgressColor(Color.GREEN);

        mWebView.setTitleAndProgressBar(null, progress);

        mWebView.addJavascriptInterface(new X5JavaScriptFunction(AnswerWebActivity.this), "android");
        if (from_url != null)
            mWebView.loadUrl(from_url);


        AdView adView1 = GoogleUtils.newBannerView(this, AdSize.LARGE_BANNER);
        adView1.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView1);
        AdView adView2 = GoogleUtils.newBannerView(this, AdSize.LARGE_BANNER);
        adView2.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            Exit();
        }
    }

    @SuppressLint("HandlerLeak")
    private void Exit() {
        if (!isExit) {
            isExit = true;
            ToastUtils.showShort(R.string.double_close_web);
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    isExit = false;
                }
            }.sendEmptyMessageDelayed(0, 2000); // 利用handler延迟发送更改状态信息
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
