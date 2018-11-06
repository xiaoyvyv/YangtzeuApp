package com.yangtzeu.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.lib.mob.im.IMManager;
import com.yangtzeu.database.DatabaseUtils;
import com.yangtzeu.entity.BanBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OkhttpError;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.url.Url;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class UserUtils {
    private final static String language = "zh_CN";
    private static Handler handler;

    public static void do_Login(final Context context, String[] userInfo, final OnLogResultListener logResultListener) {
        if (userInfo.length != 2 || context == null) {
            if (logResultListener != null)
                logResultListener.onFailure("登录信息错误");
            return;
        }
        handler = new Handler(context.getMainLooper());

        final String userNumber = userInfo[0];
        final String userPassword = userInfo[1];

        List<BanBean.DataBean> ban_user = DatabaseUtils.getHelper(context, "ban_user.db").queryAll(BanBean.DataBean.class);
        if (ObjectUtils.isNotEmpty(ban_user)) {
            for (int i = 0; i < ban_user.size(); i++) {
                if (ban_user.get(i).getNumber().equals(userNumber)) {
                    if (logResultListener != null)
                        logResultListener.onFailure("用户封停");
                    SPUtils.getInstance("user_info").remove("cookie");
                    SPUtils.getInstance("user_info").put("online", false);
                    return;
                }
            }
            LogUtils.e("封号用户数量：" + ban_user.size());
        }

        OkHttp.do_Get(Url.Yangtzeu_Login_Path, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                if (response.contains("我的账户")) {
                    if (logResultListener != null)
                        logResultListener.onSuccess(response);
                    loginSuccess();
                } else if (response.contains("用户名") && response.contains("密码")) {
                    Document document = Jsoup.parse(response);
                    Elements scripts = document.select("script");
                    String last_scripts = scripts.get(scripts.size() - 1).toString();
                    String str = last_scripts.substring(last_scripts.indexOf("+") - 50, last_scripts.lastIndexOf("+"));
                    String login_key = str.substring(str.indexOf("'") + 1, str.lastIndexOf("'"));
                    String login_encode_pass = EncryptUtils.encryptSHA1ToString(login_key + userPassword).toLowerCase();
                    LogUtils.i("密码加密前缀：" + login_key, "密码加密完成：" + login_encode_pass);
                    login(userNumber, login_encode_pass, logResultListener);
                } else {
                    LogUtils.e(response);
                }
            }

            @Override
            public void onFailure(String error) {
                //重定向校园网登录页面，错误的信息页面
                if (error.contains(OkhttpError.ERROR_LOGIN_SCHOOL_INTERNET) || error.contains("10.157.16.67")) {
                    ToastUtils.showLong("请先登录校园网");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://10.151.0.249/a30.htm"));
                    context.startActivity(intent);
                    return;
                }


                if (logResultListener != null) {
                    if (NetworkUtils.isConnected()) {
                        logResultListener.onFailure("表单参数获取错误，请稍后再试");
                    } else {
                        logResultListener.onFailure(OkhttpError.ERROR_NO_INTERNET);
                    }
                }
            }
        });
    }


    private static void login(final String number, final String e_code_pass, final OnLogResultListener onLoginResultListener) {
        FormBody formBody = new FormBody.Builder()
                .add("username", number)
                .add("password", e_code_pass)
                .add("encodedPassword", "")
                .add("session_locale", language)
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(Url.Yangtzeu_Login_Path)
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String result) {
                //登录成功
                if (result.contains("我的账户")) {
                    if (onLoginResultListener != null)
                        onLoginResultListener.onSuccess(result);
                    loginSuccess();
                    return;
                }
                //登录失败的错误原因
                if (result.contains("请不要过快点击")) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            login(number, e_code_pass, onLoginResultListener);
                        }
                    }, 1000);
                    return;
                }

                MyUtils.mVibrator(ActivityUtils.getTopActivity(), 200);
                //清除登录失败的cookie
                OkHttp.cookieJar().clear();
                OkHttp.cookieJar().clearSession();

                if (result.contains("密码错误")) {
                    if (onLoginResultListener != null)
                        onLoginResultListener.onFailure("密码错误");
                } else if (result.contains("账户不存在")) {
                    if (onLoginResultListener != null)
                        onLoginResultListener.onFailure("账户不存在");
                } else if (result.contains("验证码不正确")) {
                    if (onLoginResultListener != null)
                        onLoginResultListener.onFailure("验证码不正确");
                } else {
                    if (onLoginResultListener != null)
                        onLoginResultListener.onFailure("登录未知错误");
                }
            }

            @Override
            public void onFailure(String error) {
                if (onLoginResultListener != null)
                    onLoginResultListener.onFailure("教务处网络过载，请稍后再试");
            }
        });
    }

    private static void loginSuccess() {
        HashMap<String, String> cookieStr = new HashMap<>();
        List<Cookie> CookieList = OkHttp.cookieJar().loadForRequest(Objects.requireNonNull(HttpUrl.get(URI.create(Url.Yangtzeu_Login_Path))));
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < CookieList.size(); i++) {
            builder.append(CookieList.get(i));
            builder.append(";");
        }

        //截取有用的Cookie
        String list[] = builder.toString().split(";");
        for (String string : list) {
            if (string.contains("JSESSIONID")) {
                cookieStr.put("JSESSIONID", string + ";");
            } else if (string.contains("GSESSIONID")) {
                cookieStr.put("GSESSIONID", string + ";");
            } else if (string.contains("adc-ck-jwxt_pools")) {
                cookieStr.put("pools", string + ";");
            }
        }

        String term_id = SPUtils.getInstance("user_info").getString("term_id", Url.Default_Term);
        cookieStr.put("semester", "semester.id=" + term_id + ";");

        String mCookie = cookieStr.get("JSESSIONID") +
                cookieStr.get("GSESSIONID") +
                cookieStr.get("semester") +
                cookieStr.get("pools");

        SPUtils.getInstance("user_info").put("online", true);
        SPUtils.getInstance("user_info").put("cookie", mCookie);
        LogUtils.i("登录成功", "登录界面Cookie：" + mCookie);

        IMManager.loginIM();
    }


    public static void do_Logout(final Activity activity) {
        final ProgressDialog progressDialog = MyUtils.getProgressDialog(activity, "注销中...");
        progressDialog.show();

        //设置密码空
        SPUtils.getInstance("user_info").remove("password");
        //设置cookie空
        SPUtils.getInstance("user_info").remove("cookie");
        //设置名字空
        SPUtils.getInstance("user_info").remove("name");
        //设置学号空
        SPUtils.getInstance("user_info").remove("number");
        //设置班级空
        SPUtils.getInstance("user_info").remove("class");
        //设置Cookie失效
        SPUtils.getInstance("user_info").put("online", false);

        FileUtils.deleteAllInDir(MyUtils.rootPath() + "A_Tool/Cache/");

        OkHttp.do_Get(Url.Yangtzeu_Out, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                ActivityUtils.finishAllActivities();
                MyUtils.startActivity(LoginActivity.class);
                OkHttp.cookieJar().clear();
                OkHttp.cookieJar().clearSession();
            }

            @Override
            public void onFailure(String error) {
                progressDialog.dismiss();
                ActivityUtils.finishAllActivities();
                MyUtils.startActivity(LoginActivity.class);
            }
        });
    }

    public static void do_Logout_Simple(final Activity activity, final OnLogResultListener logResultListener) {
        final ProgressDialog progressDialog = MyUtils.getProgressDialog(activity, "注销中...");
        progressDialog.show();
        //设置cookie空
        SPUtils.getInstance("user_info").remove("cookie");
        //设置名字空
        SPUtils.getInstance("user_info").remove("name");
        //设置班级空
        SPUtils.getInstance("user_info").remove("class");
        //设置Cookie失效
        SPUtils.getInstance("user_info").put("online", false);

        FileUtils.deleteAllInDir(MyUtils.rootPath() + "A_Tool/Cache/");

        OkHttp.do_Get(Url.Yangtzeu_Out, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (logResultListener != null) {
                    logResultListener.onSuccess(response);
                }
                OkHttp.cookieJar().clear();
                OkHttp.cookieJar().clearSession();
            }

            @Override
            public void onFailure(String error) {
                progressDialog.dismiss();
                if (logResultListener != null) {
                    logResultListener.onSuccess(error);
                }
            }
        });
    }

    public interface OnLogResultListener {
        void onSuccess(String response);

        void onFailure(String error);
    }
}
