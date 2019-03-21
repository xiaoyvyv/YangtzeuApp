package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ad.OnAdVideoListener;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.lib.x5web.X5WebView;
import com.lib.yun.StringUtils;
import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IRewardVideoAdWorker;
import com.xiaomi.ad.common.pojo.AdType;
import com.yangtzeu.R;
import com.yangtzeu.app.MyApplication;
import com.yangtzeu.entity.VipVideoBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.callback.StringCallBack;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;
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
    private IRewardVideoAdWorker mAdWorker;

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
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setEvents() {
        loadAd();
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


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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

                    if (mAdWorker.isReady()) {
                        mAdWorker.show();
                    } else {
                        MyUtils.openBrowser(VipVideoActivity.this, v_url);
                    }
                } catch (Exception e) {
                    MyUtils.openBrowser(VipVideoActivity.this, v_url);
                }
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
        webView.loadUrl("https://m.v.qq.com/x/channel/select/movie");
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

    public void loadAd() {
        try {
            mAdWorker = AdWorkerFactory.getRewardVideoAdWorker(this, MyApplication.PORTRAIT_VIDEO_POSITION_ID, AdType.AD_REWARDED_VIDEO);
            mAdWorker.setListener(new OnAdVideoListener() {
                @Override
                public void onResultListener(int state, Object info) {
                    switch (state) {
                        case OnAdVideoListener.onAdPresent:
                            ToastUtils.showLong("视频播放完即可观看VIP视频或点击下载跳过广告");
                            break;
                        case OnAdVideoListener.onAdClick:
                            MyUtils.openBrowser(VipVideoActivity.this, v_url);
                            break;
                        case OnAdVideoListener.onAdDismissed:
                            try {
                                mAdWorker.recycle();
                                loadAd();
                            } catch (Exception ignored) {}
                            MyUtils.openBrowser(VipVideoActivity.this, v_url);
                            break;
                        case OnAdVideoListener.onAdFailed:
                            MyUtils.openBrowser(VipVideoActivity.this, v_url);
                            break;
                    }
                }
            });
            mAdWorker.recycle();
            mAdWorker.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        try {
            mAdWorker.recycle();
            super.onDestroy();
        } catch (Exception e) {
            super.onDestroy();
        }
    }
}
