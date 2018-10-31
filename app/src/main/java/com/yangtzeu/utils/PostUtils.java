package com.yangtzeu.utils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.url.Url;

import okhttp3.FormBody;
import okhttp3.Request;


/**
 * 发送邮件工具类
 */
public class PostUtils {
    public static void sendMessage(String qq, String text) {
        String url = Url.Yangtzeu_App_SendEmail + "?address=" + qq;
        FormBody formBody = new FormBody.Builder()
                .add("text", text)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                LogUtils.i(response);
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e("邮件提交失败");
            }
        });
    }

    public static void sendBug(String crashCause, String crashMessage) {
        String url = Url.Yangtzeu_App_SendEmail+"?address=1223414335@qq.com";
        String number = SPUtils.getInstance("user_info").getString("number");
        String name = SPUtils.getInstance("user_info").getString("name");
        String text = "<body>" +
                "<h3>新长大助手崩溃日志</h3 align=\"center\">" +
                "<h4>原因</h4>" +
                "<h5><font color=\"red\">" + crashCause + "</font></h5>" +
                "<h4>信息</h4>" +
                "<h5>" + crashMessage + "</h5>" +
                "<h4>用户</h4>" +
                "<h5>" + name+"："+ number+ "</h5>" +
                "</body>";

        FormBody formBody = new FormBody.Builder()
                .add("text", text)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                LogUtils.i("Bug提交成功");
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e("Bug提交失败");
            }
        });
    }
}
