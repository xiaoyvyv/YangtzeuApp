package com.yangtzeu.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.webkit.URLUtil;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.io.IOException;
import java.net.Proxy;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class OkHttp {
    @SuppressLint("StaticFieldLeak")
    private static OkHttp okHttp;
    public static OkHttpClient okHttpClient;
    private static Handler UIHandler;
    private static String nullString = "";
    private static PersistentCookieJar cookieJar;
    private Context context;

    public Context getContext() {
        return context;
    }

    private OkHttp(Context context) {
        this.context = context;
    }

    public static void initOkHttp(Context context) {
        if (okHttp == null) {
            okHttp = new OkHttp(context);
        }
        if (okHttpClient == null) {
            if (cookieJar == null) {
                cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
            }
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .proxy(Proxy.NO_PROXY)
                    .cookieJar(cookieJar)
                    .build();
        }
        if (UIHandler == null) {
            UIHandler = new Handler(context.getMainLooper());
        }
    }

    public static void do_Get(final String url, final OnResultStringListener onResultStringListener) {
        LogUtils.i("Get--加载链接：" + url);

        final String cache_key = EncryptUtils.encryptMD5ToString("get:string" + url);
        if (!URLUtil.isNetworkUrl(url)) {
            if (onResultStringListener != null)
                onResultStringListener.onFailure("错误的网址：" + url);
            return;
        }

        if (!NetworkUtils.isConnected()) {
            if (onResultStringListener != null)
                onResultStringListener.onFailure(OkhttpError.ERROR_NO_INTERNET);

            boolean cache_okhttp = SPUtils.getInstance("app_info").getBoolean("cache_okhttp", true);
            if (!cache_okhttp || StringUtils.equals(url, Url.Yangtzeu_Login_Path)) {
                return;
            }
        }

        final Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onResultStringListener != null) {
                            String cache = CacheDiskUtils.getInstance(MyUtils.geCacheDir()).getString(cache_key);
                            if (ObjectUtils.isNotEmpty(cache) && !url.contains("10.10.240.250")) {
                                onResultStringListener.onResponse(cache);
                            } else {
                                onResultStringListener.onFailure(OkhttpError.ERROR_LOAD);
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                ResponseBody result = response.body();
                final String string = result != null ? result.string() : nullString;
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() != 200) {
                            if (onResultStringListener != null)
                                onResultStringListener.onFailure(OkhttpError.ERROR_FAILURE);
                            return;
                        }
                        if (onResultStringListener != null) {
                            if (string.contains(OkhttpError.ERROR_SCHOOL_HOST) && !url.contains(Url.Yangtzeu_App_ShowMessage)) {
                                onResultStringListener.onFailure(OkhttpError.ERROR_LOGIN_SCHOOL_INTERNET);
                            } else {
                                //缓存到sd卡
                                CacheDiskUtils.getInstance(MyUtils.geCacheDir()).put(cache_key, string);
                                onResultStringListener.onResponse(string);
                            }
                        }
                    }
                });
            }
        });
    }

    public static void do_Get(final String url, final OnResultBytesListener onResultListener) {
        LogUtils.i("Get--加载链接：" + url);

        final String cache_key = EncryptUtils.encryptMD5ToString("get:bytes" + url);

        if (!URLUtil.isNetworkUrl(url)) {
            if (onResultListener != null)
                onResultListener.onFailure("错误的网址：" + url);
            return;
        }

        if (!NetworkUtils.isConnected()) {
            if (onResultListener != null)
                onResultListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
            boolean cache_okhttp = SPUtils.getInstance("app_info").getBoolean("cache_okhttp", true);
            if (!cache_okhttp) {
                return;
            }
        }

        final Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onResultListener != null) {
                            byte[] cache = CacheDiskUtils.getInstance(MyUtils.geCacheDir()).getBytes(cache_key);
                            if (ObjectUtils.isNotEmpty(cache) && !url.contains("10.10.240.250")) {
                                onResultListener.onResponse(cache);
                            } else {
                                onResultListener.onFailure(OkhttpError.ERROR_LOAD);
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {

                final byte[] result = Objects.requireNonNull(response.body()).bytes();
                final String string = result != null ? new String(result) : nullString;
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() != 200) {
                            if (onResultListener != null)
                                onResultListener.onFailure(OkhttpError.ERROR_FAILURE);
                            return;
                        }
                        if (onResultListener != null) {
                            if (string.contains(OkhttpError.ERROR_SCHOOL_HOST)) {
                                onResultListener.onFailure(OkhttpError.ERROR_LOGIN_SCHOOL_INTERNET);
                            } else {
                                CacheDiskUtils.getInstance(MyUtils.geCacheDir()).put(cache_key, result);
                                onResultListener.onResponse(result);
                            }
                        }
                    }
                });
            }
        });
    }


    public static void do_Post(final Request request, final OnResultStringListener onResultStringListener) {

        FormBody body = (FormBody) request.body();
        StringBuilder temp = new StringBuilder();
        if (body != null) {
            for (int i = 0; i < body.size(); i++) {
                temp.append(body.name(i)).append(":").append(body.value(i)).append(";");
            }
        }
        LogUtils.i("Post--加载链接：" + request.url().toString());
        final String cache_key = EncryptUtils.encryptMD5ToString("post:string" + request.url().toString() + temp.toString());
        if (!NetworkUtils.isConnected()) {
            if (onResultStringListener != null)
                onResultStringListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
            boolean cache_okhttp = SPUtils.getInstance("app_info").getBoolean("cache_okhttp", true);
            if (!cache_okhttp || StringUtils.equals(request.url().toString(), Url.Yangtzeu_Login_Path)) {
                return;
            }
        }


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onResultStringListener != null) {
                            String cache = CacheDiskUtils.getInstance(MyUtils.geCacheDir()).getString(cache_key);
                            if (ObjectUtils.isNotEmpty(cache)) {
                                onResultStringListener.onResponse(cache);
                            } else {
                                onResultStringListener.onFailure(OkhttpError.ERROR_LOAD);
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {

                ResponseBody result = response.body();
                final String string = result != null ? result.string() : nullString;
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() != 200) {
                            if (onResultStringListener != null)
                                onResultStringListener.onFailure(OkhttpError.ERROR_FAILURE);
                            return;
                        }
                        if (onResultStringListener != null) {
                            if (string.contains(OkhttpError.ERROR_SCHOOL_HOST)) {
                                onResultStringListener.onFailure(OkhttpError.ERROR_LOGIN_SCHOOL_INTERNET);
                            } else {
                                CacheDiskUtils.getInstance(MyUtils.geCacheDir()).put(cache_key, string);
                                onResultStringListener.onResponse(string);
                            }
                        }
                    }
                });
            }
        });
    }


    public static PersistentCookieJar cookieJar() {
        return cookieJar;
    }
}
