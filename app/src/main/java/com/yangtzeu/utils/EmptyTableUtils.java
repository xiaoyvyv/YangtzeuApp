package com.yangtzeu.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.WebActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class EmptyTableUtils {
    private static ProgressDialog alertDialog;
    private static String Login_Path = "http://jwc2.yangtzeu.edu.cn:8080/login.aspx";


    public static void startGetForm(final Context context) {
        alertDialog = MyUtils.getProgressDialog(context, context.getString(R.string.loading));
        alertDialog.show();
        OkHttp.do_Get(Login_Path, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                try {
                    Document document = Jsoup.parse(response);
                    Elements VIEW_STATE = document.select("input#__VIEWSTATE");
                    Elements EVENT_VALIDATION = document.select("input#__EVENTVALIDATION");
                    String Form1 = VIEW_STATE.get(0).attr("value");
                    String Form2 = EVENT_VALIDATION.get(0).attr("value");
                    startLogin(context, Login_Path, Form1, Form2);
                } catch (Exception e) {
                    LogUtils.e("空教室查询错误：" + e);
                }
            }

            @Override
            public void onFailure(String error) {
                alertDialog.dismiss();
                LogUtils.e("空教室表单错误");
            }
        });
    }

    private static void startLogin(final Context context, String path, String form1, String form2) {

        //创建登录表单
        FormBody formBody = new FormBody.Builder()
                .add("__VIEWSTATE", form1)
                .add("__EVENTVALIDATION", form2)
                .add("txtUid", "201603246")
                .add("txtPwd", "xy981229")
                .add("btLogin", "µÇÂ¼")
                .add("selKind", "1")
                .build();

        Request request = new Request.Builder()
                .url(path)
                .post(formBody)
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document doc = Jsoup.parse(response);
                //截取 班级 姓名 学号
                String loginInfo = doc.select("div#lbPrompt").text();
                if (!loginInfo.isEmpty()) {
                    loginSuccess(context);
                }
            }

            @Override
            public void onFailure(String error) {
                alertDialog.dismiss();
            }
        });
    }

    private static void loginSuccess(Context context) {
        getCookie();
        alertDialog.dismiss();
        String cookie = SPUtils.getInstance("user_info").getString("old_cookie");
        //保存cookie
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("from_url", "http://jwc2.yangtzeu.edu.cn:8080/FreeRoom.aspx");
        intent.putExtra("isKJS", true);
        intent.putExtra("cookie", cookie);
        MyUtils.startActivity(intent);
    }

    private static void getCookie() {
        List<Cookie> cookieList = OkHttp.cookieJar().loadForRequest(Objects.requireNonNull(HttpUrl.get(URI.create(Login_Path))));
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cookieList.size(); i++) {
            builder.append(cookieList.get(i));
            builder.append(";");
        }

        //截取有用的Cookie
        String old_cookie = builder.toString();
        SPUtils.getInstance("user_info").put("old_cookie", old_cookie);

        LogUtils.i("登录成功", "旧教务处登录Cookie：" + old_cookie);
    }
}
