package com.yangtzeu.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.URLUtil;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class OkHttpBak {
    @SuppressLint("StaticFieldLeak")
    private static OkHttpBak okHttp;

    private static final String User_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0";
    public static OkHttpClient okHttpClient;
    private static Handler UIHandler;
    private static String nullString = "";
    private static PersistentCookieJar cookieJar;
    private Context context;

    public Context getContext() {
        return context;
    }

    private OkHttpBak(Context context) {
        this.context = context;
    }

    public static void initOkHttp(Context context) {
        if (okHttp == null) {
            okHttp = new OkHttpBak(context);
        }
        if (okHttpClient == null) {
            if (cookieJar == null) {
                cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
            }

            @Nullable
            File cacheDir = new File(MyUtils.createSDCardDir("A_Tool/Cache/OkHttp/"));

            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new RequestCacheInterceptor())
                    .addNetworkInterceptor(new ResponseCacheInterceptor())
                    .connectTimeout(8 * 1000, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(5 * 60, TimeUnit.SECONDS)
                    .cookieJar(cookieJar)
                    .cache(new Cache(cacheDir, 10 * 1024 * 1024))
                    .proxy(Proxy.NO_PROXY)
                    .proxySelector(new ProxySelector() {
                        @Override
                        public List<Proxy> select(URI uri) {
                            return Collections.singletonList(Proxy.NO_PROXY);
                        }

                        @Override
                        public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                        }
                    })
                    .build();
        }

        if (UIHandler == null) {
            UIHandler = new Handler(context.getMainLooper());
        }
    }


    public static void do_Get(final String url, final OnResultStringListener onResultStringListener) {
        Log.i("OkHttp-do_Get()", "Get--加载链接：OnResultStringListener：" + url);

        //如果网址错误，则退出
        if (!URLUtil.isNetworkUrl(url)) {
            if (onResultStringListener != null)
                onResultStringListener.onFailure("错误的网址：" + url);
            return;
        }

        final Request request = new Request.Builder()
                .url(url)
                .build();

        Headers headers = request.headers();
        LogUtils.e(headers);


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onResultStringListener != null) {
                            //如果没有网络，则退出
                            if (!NetworkUtils.isConnected()) {
                                onResultStringListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
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
                        if (onResultStringListener != null) {
                            //如果返回结果包含校园网登录信息，且不为留言，则退出
                            if (string.contains(OkhttpError.ERROR_SCHOOL_HOST) && !url.contains(Url.Yangtzeu_App_ShowMessage)) {
                                onResultStringListener.onFailure(OkhttpError.ERROR_LOGIN_SCHOOL_INTERNET);
                                return;
                            }

                            //返回码不成功即退出
                            if (response.isSuccessful()) {
                                //返回成功结果
                                onResultStringListener.onResponse(string);
                            } else {
                                onResultStringListener.onFailure(OkhttpError.ERROR_FAILURE);
                            }

                        }
                    }
                });
            }
        });
    }


    public static void do_Get(final String url, final OnResultBytesListener onResultListener) {
        Log.i("OkHttp-do_Get()", "Get--加载链接：OnResultBytesListener：" + url);

        //如果网址错误，则退出
        if (!URLUtil.isNetworkUrl(url)) {
            if (onResultListener != null)
                onResultListener.onFailure("错误的网址：" + url);
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
                            //如果没有网络，且设置为禁用缓存，则退出
                            if (!NetworkUtils.isConnected()) {
                                onResultListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
                            } else {
                                onResultListener.onFailure(OkhttpError.ERROR_LOAD);
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                byte[] result;
                if (response.body() != null) {
                    result = response.body().bytes();
                } else {
                    onResultListener.onFailure(OkhttpError.ERROR_FAILURE);
                    return;
                }
                final String string = result != null ? new String(result) : nullString;

                final byte[] finalResult = result;
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (onResultListener != null) {

                            //如果返回结果包含校园网登录信息，且不为留言，则退出
                            if (string.contains(OkhttpError.ERROR_SCHOOL_HOST)) {
                                onResultListener.onFailure(OkhttpError.ERROR_LOGIN_SCHOOL_INTERNET);
                                return;
                            }

                            //返回码不成功即退出
                            if (response.isSuccessful()) {
                                //返回成功结果
                                onResultListener.onResponse(finalResult);
                            } else {
                                onResultListener.onFailure(OkhttpError.ERROR_FAILURE);
                            }
                        }

                    }
                });
            }
        });
    }

    public static void do_Post(final Request request, final OnResultStringListener onResultStringListener) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull final IOException e) {
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onResultStringListener != null) {
                            if (!NetworkUtils.isConnected()) {
                                onResultStringListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
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
                String text = Jsoup.parse(string).text();
                if (text.contains("过快点击")) {
                    LogUtils.e("过快点击");

                }
                UIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (onResultStringListener != null) {
                            //如果返回结果包含校园网登录信息，且不为留言，则退出
                            if (string.contains(OkhttpError.ERROR_SCHOOL_HOST)) {
                                onResultStringListener.onFailure(OkhttpError.ERROR_LOGIN_SCHOOL_INTERNET);
                                return;
                            }

                            //返回码不成功即退出
                            if (response.isSuccessful()) {
                                //返回成功结果
                                onResultStringListener.onResponse(string);
                            } else {
                                onResultStringListener.onFailure(OkhttpError.ERROR_FAILURE);
                            }

                        }
                    }
                });
            }
        });
    }


    //请求头插值器
    public static final class RequestCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //在原来的request的基础上修改
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            //修改UA
            builder.removeHeader("User-Agent");
            builder.addHeader("User-Agent", User_Agent);
            builder.addHeader("Cache-Control", "max-age=10");

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxStale(10, TimeUnit.SECONDS)
                    .build();
            builder.cacheControl(cacheControl);

         /*   //无网下强制缓存，Post方式下强制网络
            if (NetworkUtils.isConnected() || request.method().equals("POST")) {
                Log.v("请求头插值器", "强制网络");
                builder.cacheControl(CacheControl.FORCE_NETWORK);
            } else {
                Log.v("请求头插值器", "强制本地");
                builder.cacheControl(CacheControl.FORCE_CACHE);
            }*/
            Request newRequest = builder.build();

            Headers headers = newRequest.headers();
            LogUtils.e(headers);
            return chain.proceed(newRequest);
        }
    }

    //响应头插值器
    public static final class ResponseCacheInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            return chain.proceed(chain.request()).newBuilder()
                    .removeHeader("Pragma") //移除影响
                    .removeHeader("Cache-Control") //移除影响
                    .header("Cache-Control", "max-age=5")//设置10秒
                    .build();
        }
    }


    //Cookie管理
    public static PersistentCookieJar cookieJar() {
        return cookieJar;
    }
}
