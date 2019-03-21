package com.yangtzeu.http.interceptor;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

//响应头插值器
public final class ResponseCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(chain.request()).newBuilder()
                .removeHeader("Pragma") //移除影响
                .removeHeader("Cache-Control") //移除影响
             //   .header("Cache-Control", "max-age=3600")
               // .header("Cache-Control", "max-st=3600")//设置3600秒
                .build();
    }
}

