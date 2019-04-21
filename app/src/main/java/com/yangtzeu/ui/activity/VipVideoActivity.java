package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.lib.subutil.GsonUtils;
import com.lib.x5web.X5WebView;
import com.lib.yun.StringUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.VipVideoBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.callback.StringCallBack;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.MyUtils;

import okhttp3.Call;
import okhttp3.Request;

public class VipVideoActivity extends BaseActivity {
    private Toolbar toolbar;
    private X5WebView webView;
    private TextView vip_line;
    private TextView vip_url;
    private TextView back;
    private Button play;
    private String nowApi;
    private String v_url;
    private RewardedVideoAd rewardedVideoAd;
    private LinearLayout googleView;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_video);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webView);
        play = findViewById(R.id.play);
        vip_line = findViewById(R.id.vip_line);
        vip_url = findViewById(R.id.vip_url);
        back = findViewById(R.id.back);
        googleView = findViewById(R.id.googleView);
        scrollView = findViewById(R.id.scrollView);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void setEvents() {
        webView.loadUrl("https://m.v.qq.com/x/channel/select/movie");
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP)
                    scrollView.requestDisallowInterceptTouchEvent(false);
                else
                    scrollView.requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        //加载VIP视频接口
        Request request = OkHttp.getRequest(Url.Yangtzeu_App_Vip_Video);
        OkHttp.getInstance().newCall(request).enqueue(new StringCallBack() {
            @Override
            public void onFinish(Call call, String response, boolean isResponseExist, boolean isCacheResponse) {
                VipVideoBean bean = GsonUtils.fromJson(response, VipVideoBean.class);
                final PopupMenu popupMenu = new PopupMenu(VipVideoActivity.this, vip_line);
                for (int i = 0; i < bean.getList().size(); i++) {
                    VipVideoBean.ListBean listBean = bean.getList().get(i);
                    final String api = listBean.getApi();
                    final String name = listBean.getName();
                    popupMenu.getMenu().add(name).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            nowApi = api;
                            vip_line.setText(name);
                            return true;
                        }
                    });
                    if (i == 0) {
                        vip_line.setText(bean.getList().get(0).getName());
                        nowApi = bean.getList().get(0).getApi();
                    }
                }
                vip_line.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupMenu.show();
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    VipVideoActivity.super.onBackPressed();
                }
            }
        });
        loadVideoAd();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StringUtils.isEmpty(nowApi)) {
                    ToastUtils.showShort("接口未加载成功，请重试");
                    return;
                }
                String trim = vip_url.getText().toString().trim();
                if (StringUtils.isEmpty(trim)) {
                    ToastUtils.showShort("请输入VIP视频地址后重试");
                    return;
                }
                v_url = nowApi + trim;

                //广告没有准备好
                if (!GoogleUtils.showRewardedVideoAd()) {
                    GoogleUtils.showInterstitialAd(new OnResultListener<Boolean>() {
                        @Override
                        public void onResult(Boolean s) {
                            loadVideoAd();
                            MyUtils.openBrowser(VipVideoActivity.this, v_url);
                        }
                    });
                }
            }
        });

        AdView adView1 = GoogleUtils.newBannerView(this, AdSize.LARGE_BANNER);
        adView1.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView1);
        AdView adView2 = GoogleUtils.newBannerView(this, AdSize.LARGE_BANNER);
        adView2.loadAd(GoogleUtils.getRequest());
        googleView.addView(adView2);
    }

    private void loadVideoAd() {
        rewardedVideoAd = GoogleUtils.loadRewardedVideoAd(new GoogleUtils.MyRewardedListener() {
            @Override
            public void onRewardedVideoAdClosed() {
                MyUtils.openBrowser(VipVideoActivity.this, v_url);
                loadVideoAd();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                GoogleUtils.loadInterstitialAd();
            }
        });
    }

    public void OnVideoWeb(View view) {
        switch (view.getId()) {
            case R.id.tx_video:
                webView.loadUrl("https://m.v.qq.com/x/channel/select/movie");
                break;
            case R.id.aqy_video:
                webView.loadUrl("http://m.iqiyi.com/dianying/");
                break;
            case R.id.yk_video:
                webView.loadUrl("https://movie.youku.com/");
                break;
            case R.id.copy_url:
                vip_url.setText(webView.getUrl());
                break;
        }
    }

    @Override
    public void onResume() {
        if (rewardedVideoAd != null)
            rewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        if (rewardedVideoAd != null)
            rewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (rewardedVideoAd != null)
            rewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}
