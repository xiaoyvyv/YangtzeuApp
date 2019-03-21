package com.yangtzeu.http;

import android.os.Handler;
import android.os.Looper;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.CookieCache;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.CookiePersistor;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.yangtzeu.http.callback.StringCallBack;
import com.yangtzeu.http.interceptor.RequestCacheInterceptor;
import com.yangtzeu.http.interceptor.ResponseCacheInterceptor;
import com.yangtzeu.url.Url;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;


public class OkHttp {
    private static volatile OkHttpClient okHttpClient;
    private static volatile PersistentCookieJar cookieJar;

    public OkHttp() {

    }

    public static OkHttpClient getInstance() {

        if (cookieJar == null) {
            synchronized (PersistentCookieJar.class) {
                if (cookieJar == null) {
                    CookieCache cache = new SetCookieCache();
                    CookiePersistor persist = new SharedPrefsCookiePersistor(Utils.getApp().getApplicationContext());
                    cookieJar = new PersistentCookieJar(cache, persist);
                }
            }
        }

        if (okHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new RequestCacheInterceptor())
                            .addNetworkInterceptor(new ResponseCacheInterceptor())
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(5, TimeUnit.SECONDS)
                            .writeTimeout(5 * 60, TimeUnit.SECONDS)
                            .cookieJar(cookieJar)
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    public static void do_Get(final String url, final OnResultStringListener onResultStringListener) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        do_Post(request, onResultStringListener);
    }

    public static void do_Post(final Request request, final OnResultStringListener onResultStringListener) {
        Call call = OkHttp.getInstance().newCall(request);
        StringCallBack stringCallBack = new StringCallBack() {
            @Override
            public void onFinish(Call call, String response, boolean isResponseExist, boolean isCacheResponse) {
                if (isResponseExist) {
                    if (onResultStringListener != null)
                        onResultStringListener.onResponse(response);
                } else {
                    if (onResultStringListener != null)
                        onResultStringListener.onFailure(response);

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            boolean offline_mode = SPUtils.getInstance("user_info").getBoolean("offline_mode", false);
                            if (offline_mode) {
                                ToastUtils.showLong("您当前处于离线模式且未找到缓存数据，若需获取最新数据请关闭离线模式！");
                            }
                        }
                    }, 500);
                }
            }
        };

        //离线模式
        if (request.url().toString().contains(Url.Yangtzeu_Base_Url)) {
            boolean offline_mode = SPUtils.getInstance("user_info").getBoolean("offline_mode", false);
            if (offline_mode) {
                stringCallBack.onFailure(call, new IOException("The jwc offline mode is opened"));
            } else {
                call.enqueue(stringCallBack);
            }
        } else {
            call.enqueue(stringCallBack);
        }
    }

    public static Request getRequest(String url) {
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    //Cookie管理
    public static PersistentCookieJar cookieJar() {
        return cookieJar;
    }
}
