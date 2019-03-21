package com.yangtzeu.http.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

//请求头插值器
public final class RequestCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //在原来的request的基础上修改
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();

        builder.removeHeader("User-Agent");
        //修改UA
        builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0");
        builder.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        builder.addHeader("Connection", "keep-alive");
        builder.addHeader("Accept", "*/*");

        return chain.proceed(builder.build());
    }
}
