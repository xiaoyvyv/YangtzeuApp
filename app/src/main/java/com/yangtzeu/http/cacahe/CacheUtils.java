package com.yangtzeu.http.cacahe;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.io.File;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;

public class CacheUtils {

    private static File getCacheDir() {
        return new File(MyUtils.createSDCardDir("A_Tool/Cache/MyOkHttpCache/"));
    }

    private static String callToKey(Call call) {
        StringBuilder params = new StringBuilder();
        Request request = call.request();
        params.append(request.url());
        if (request.body() != null)
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();

                int size = body.size();
                for (int i = 0; i < size; i++) {
                    if (body.name(i).equals("password")) continue;
                    if (body.name(i).equals("ids")) continue;
                    params.append(body.name(i));
                    params.append("=");
                    params.append(body.value(i));
                    params.append("&");
                }
                params.append("number=");
                params.append(SPUtils.getInstance("user_info").getString("number"));
                params.append("&term=");
                params.append(SPUtils.getInstance("user_info").getString("term_id", Url.Default_Term));
            }
        return EncryptUtils.encryptMD5ToString(params.toString());
    }

    public static void put(Call call, String string) {
        CacheDiskUtils.getInstance(getCacheDir()).put(callToKey(call), string);
    }

    public static String getStringCache(Call call) {
        //返回取得缓存
        return CacheDiskUtils.getInstance(getCacheDir()).getString(callToKey(call), null);
    }

    public static void put(Call call, byte[] bytes) {
        CacheDiskUtils.getInstance(getCacheDir()).put(callToKey(call), bytes);
    }

    public static byte[] getByteCache(Call call) {
        //返回取得缓存
        return CacheDiskUtils.getInstance(getCacheDir()).getBytes(callToKey(call), null);
    }
}
