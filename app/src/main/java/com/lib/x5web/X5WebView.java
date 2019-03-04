package com.lib.x5web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yangtzeu.R;
import com.yangtzeu.utils.MyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;


/**
 * Created by Administrator on 2018/4/9.
 *
 * @author 王怀玉
 * @explain X5WebView
 */

public class X5WebView extends WebView {
    private Context context;
    private Toolbar toolbar;
    private WebViewProgressBar progressBar;
    private X5LoadFinishListener x5LoadFinishListener;

    public static ValueCallback<Uri> uploadFile;
    public static ValueCallback<Uri[]> uploadFiles;
    private ImageView imageView;
    private AlertDialog dialog;


    public X5WebView(Context context) {
        super(context);
        this.context = context;
        this.getView().setClickable(true);
        SetUp();
        initWebViewSettings();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        this.getView().setClickable(true);
        SetUp();
        initWebViewSettings();
        //  WebStorage webStorage = WebStorage.getInstance();
    }

    public void setTitleAndProgressBar(Toolbar toolbar, WebViewProgressBar progressBar) {
        this.toolbar = toolbar;
        this.progressBar = progressBar;
    }

    public void setX5LoadFinishListener(X5LoadFinishListener x5LoadFinishListener) {
        this.x5LoadFinishListener = x5LoadFinishListener;
    }

    private void SetUp() {
        this.setWebViewClient(mWebViewClient);
        this.setWebChromeClient(mWebChromeClient);

        //WebView长按实事件
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                HitTestResult result = getHitTestResult();
                if (result != null) {
                    int type = result.getType();
                    if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                        final String longClickUrl = result.getExtra();
                        if (longClickUrl.startsWith("data:image/")) {
                            LogUtils.i("X5WebView捕获网址", longClickUrl);
                            ImageView imageView = new ImageView(context);
                            imageView.setMinimumHeight(ConvertUtils.dp2px(200));
                            imageView.setMinimumWidth(ConvertUtils.dp2px(200));
                            imageView.setImageBitmap(MyUtils.base64ToBitmap(longClickUrl));
                            dialog = new AlertDialog.Builder(context)
                                    .setTitle(R.string.trip)
                                    .setView(imageView)
                                    .setMessage(R.string.is_save_image)
                                    .setNegativeButton(R.string.clear, null)
                                    .setPositiveButton(R.string.bt_save, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                ToastUtils.showShort(context.getString(R.string.save_local) + ":\n\n" + MyUtils.rootPath() + "A_Tool/Download/Image/");
                                                String name = "file-" + System.currentTimeMillis();
                                                File avaterFile = new File(new File(MyUtils.rootPath() + "A_Tool/Download/Image/"), name + ".jpg");//设置文件名称
                                                if (avaterFile.exists()) {
                                                    boolean delete = avaterFile.delete();
                                                }
                                                boolean newFile = avaterFile.createNewFile();
                                                FileOutputStream fos = new FileOutputStream(avaterFile);
                                                MyUtils.base64ToBitmap(longClickUrl).compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                                fos.flush();
                                                fos.close();

                                                MyUtils.saveImageToGallery(context, MyUtils.rootPath() + "A_Tool/Download/Image/" + name + ".jpg");
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                                    .create();
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(false);
                        } else {
                            MyUtils.openImage(context, longClickUrl);
                        }
                        return true;
                    } else return false;
                } else return false;
            }
        });
        setDownloadListener(new X5DownloadListener(context));
    }

    //客户端设置
    WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String Url) {
            //return false 历史记录不会保存重定向的网页
            LogUtils.i("加载网页" + Url);
            if (!Url.startsWith("http")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                boolean isInstall = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
                if (isInstall) {
                    ToastUtils.showShort(R.string.open_app);
                    context.startActivity(intent);
                    return false;
                } else {
                    ToastUtils.showShort(R.string.no_open_app);
                    return true;
                }
            }
            //微信H5支付核心代码
            Map<String, String> extraHeaders = new HashMap<>();
            extraHeaders.put("Referer", getUrl());
            view.loadUrl(Url, extraHeaders);

            return super.shouldOverrideUrlLoading(view, Url);
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (progressBar != null)
                progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView webView, String url) {
            if (x5LoadFinishListener != null)
                x5LoadFinishListener.onLoadFinish(webView, progressBar, url);
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
        }
    };

    //辅助类
    WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView webView, String title) {
            if (title.isEmpty()) {
                title = context.getString(R.string.web_details);
            }
            setToolBarTitle(title);
        }

        @Override
        public void onReceivedIcon(WebView webView, Bitmap bitmap) {
            super.onReceivedIcon(webView, bitmap);
        }

        @Override
        public void onProgressChanged(WebView webView, int progress) {
            setProgress(progress);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            uploadFiles = valueCallback;
            chooseFiles();
            return true;
        }
    };

    public final static int CHOOSE_FILE = 120;

    private void chooseFiles() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        ((Activity) context).startActivityForResult(Intent.createChooser(i, "File Chooser"), CHOOSE_FILE);
    }

    private void setProgress(int progress) {
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
    }

    private void setToolBarTitle(String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);

        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        // 设置自适应屏幕，两者合用
        webSetting.setUseWideViewPort(true); //将图片调整到适合WebView的大小
        webSetting.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        // 缩放操作
        webSetting.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSetting.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSetting.setDisplayZoomControls(false); //隐藏原生的缩放控件


        // 其他细节操作
        webSetting.setAllowFileAccess(true); //设置可以访问文件
        webSetting.setAllowFileAccessFromFileURLs(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSetting.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSetting.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSetting.setGeolocationEnabled(true);
        webSetting.setBlockNetworkImage(false);
        webSetting.setSavePassword(true);
        webSetting.setSaveFormData(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setAppCachePath(MyUtils.rootPath() + "A_Tool/WebCache/"); //设置  Application Caches 缓存目录
        webSetting.setDatabaseEnabled(true);
        webSetting.setDatabasePath(MyUtils.rootPath() + "A_Tool/WebCache/");
        webSetting.setDomStorageEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);

        if (NetworkUtils.isConnected()) {
            webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            ToastUtils.showShort(R.string.no_internet);
            webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }

        //设置混合协议
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting.setMixedContentMode(2);
        }

    }

    public void showFreeRoom() {
        X5WebView.this.loadUrl("javascript:__doPostBack('btFreeRoom','')");
    }


}
