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

        //缓存的Key
        final String cache_key = EncryptUtils.encryptMD5ToString("get:string" + url);

        //如果网址错误，则退出
        if (!URLUtil.isNetworkUrl(url)) {
            if (onResultStringListener != null)
                onResultStringListener.onFailure("错误的网址：" + url);
            return;
        }

        //如果没有网络，且设置为禁用缓存，则退出
        if (!NetworkUtils.isConnected()) {
            if (onResultStringListener != null) {
                boolean cache_okhttp = SPUtils.getInstance("app_info").getBoolean("cache_okhttp", true);
                if (!cache_okhttp || StringUtils.equals(url, Url.Yangtzeu_Login_Path)) {
                    onResultStringListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
                    return;
                } else {
                    String cache = CacheDiskUtils.getInstance(MyUtils.geCacheDir()).getString(cache_key);
                    if (ObjectUtils.isNotEmpty(cache) && !url.contains(OkhttpError.ERROR_SCHOOL_HOST)) {
                        onResultStringListener.onResponse(cache);
                    } else {
                        onResultStringListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
                    }
                }
            } else {
                LogUtils.i("未设置网络请求监听---Url：" + url);
            }
            return;
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
                            onResultStringListener.onFailure(OkhttpError.ERROR_LOAD);
                        } else {
                            LogUtils.i("未设置网络请求监听---Url：" + url);
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
                        //缓存到sd卡
                        CacheDiskUtils.getInstance(MyUtils.geCacheDir()).put(cache_key, string);

                        if (onResultStringListener != null) {
                            //返回码不为200即退出
                            if (response.code() != 200) {
                                onResultStringListener.onFailure(OkhttpError.ERROR_FAILURE);
                                return;
                            }

                            //如果返回结果包含校园网登录信息，且不为留言，则退出
                            if (string.contains(OkhttpError.ERROR_SCHOOL_HOST) && !url.contains(Url.Yangtzeu_App_ShowMessage)) {
                                onResultStringListener.onFailure(OkhttpError.ERROR_LOGIN_SCHOOL_INTERNET);
                                return;
                            }

                            //返回成功结果
                            onResultStringListener.onResponse(string);
                        } else {
                            LogUtils.i("未设置网络请求监听---Url：" + url);
                        }
                    }
                });
            }
        });
    }

    public static void do_Get(final String url, final OnResultBytesListener onResultListener) {
        LogUtils.i("Get--加载链接：" + url);

        //缓存的Key
        final String cache_key = EncryptUtils.encryptMD5ToString("get:bytes" + url);

        //如果网址错误，则退出
        if (!URLUtil.isNetworkUrl(url)) {
            if (onResultListener != null)
                onResultListener.onFailure("错误的网址：" + url);
            return;
        }

        //如果没有网络，且设置为禁用缓存，则退出
        if (!NetworkUtils.isConnected()) {
            if (onResultListener != null) {
                boolean cache_okhttp = SPUtils.getInstance("app_info").getBoolean("cache_okhttp", true);
                if (!cache_okhttp || StringUtils.equals(url, Url.Yangtzeu_Login_Path)) {
                    onResultListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
                    return;
                } else {
                    byte[] cache = CacheDiskUtils.getInstance(MyUtils.geCacheDir()).getBytes(cache_key);
                    if (ObjectUtils.isNotEmpty(cache) && !url.contains(OkhttpError.ERROR_SCHOOL_HOST)) {
                        onResultListener.onResponse(cache);
                    } else {
                        onResultListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
                    }
                }
            } else {
                LogUtils.i("未设置网络请求监听---Url：" + url);
            }
            return;
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
                            onResultListener.onFailure(OkhttpError.ERROR_LOAD);
                        } else {
                            LogUtils.i("未设置网络请求监听---Url：" + url);
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
                        //缓存到sd卡
                        CacheDiskUtils.getInstance(MyUtils.geCacheDir()).put(cache_key, result);

                        if (onResultListener != null) {
                            //返回码不为200即退出
                            if (response.code() != 200) {
                                onResultListener.onFailure(OkhttpError.ERROR_FAILURE);
                                return;
                            }

                            //如果返回结果包含校园网登录信息，且不为留言，则退出
                            if (string.contains(OkhttpError.ERROR_SCHOOL_HOST)) {
                                onResultListener.onFailure(OkhttpError.ERROR_LOGIN_SCHOOL_INTERNET);
                                return;
                            }

                            //返回成功结果
                            onResultListener.onResponse(result);
                        } else {
                            LogUtils.i("未设置网络请求监听---Url：" + url);
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

        if (!NetworkUtils.isConnected()) {
            if (onResultStringListener != null)
                onResultStringListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
            return;
        }


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onResultStringListener != null) {
                            onResultStringListener.onFailure(OkhttpError.ERROR_LOAD);
                        } else {
                            LogUtils.i("未设置网络请求监听---Url：" + request.url());
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
                        if (onResultStringListener != null) {
                            //返回码不为200即退出
                            if (response.code() != 200) {
                                onResultStringListener.onFailure(OkhttpError.ERROR_FAILURE);
                                return;
                            }

                            //如果返回结果包含校园网登录信息，且不为留言，则退出
                            if (string.contains(OkhttpError.ERROR_SCHOOL_HOST)) {
                                onResultStringListener.onFailure(OkhttpError.ERROR_LOGIN_SCHOOL_INTERNET);
                                return;
                            }

                            //返回成功结果
                            onResultStringListener.onResponse(string);
                        }else {
                            LogUtils.i("未设置网络请求监听---Url：" + request.url());
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
