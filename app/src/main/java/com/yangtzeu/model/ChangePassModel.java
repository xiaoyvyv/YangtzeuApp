package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.snackbar.Snackbar;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IChangePassModel;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.ui.view.ChangePassView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.Request;

public class ChangePassModel implements IChangePassModel {


    @Override
    public void changePassEvent(final Activity activity, final ChangePassView view) {
        KeyboardUtils.hideSoftInput(view.PassDone());
        String Account = Objects.requireNonNull(view.ACC().getText()).toString().trim();
        final String PasswordOld = Objects.requireNonNull(view.PassOld().getText()).toString().trim();
        final String PasswordNew = Objects.requireNonNull(view.PassNew().getText()).toString().trim();
        String PasswordDone = Objects.requireNonNull(view.PassDone().getText()).toString().trim();
        if (!Account.isEmpty() && !PasswordOld.isEmpty() && !PasswordNew.isEmpty() && !PasswordDone.isEmpty()) {
            if (PasswordNew.equals(PasswordDone)) {
                if (PasswordNew.length() <= 6) {
                    Snackbar.make(view.getToolbar(), "新密码必须大于6位！", Snackbar.LENGTH_LONG).show();
                } else if (!MyUtils.isContainAll(PasswordNew)) {
                    Snackbar.make(view.getToolbar(), "新密码必须包含大、小写字母及数字！", Snackbar.LENGTH_LONG).show();
                } else {
                    //清空旧密码
                    SPUtils.getInstance("user_info").put("password", "");
                    //延时0.5秒修改密码
                    new Handler(activity.getMainLooper()).postDelayed(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            changePasswordStep1(activity, view, PasswordOld, PasswordNew);
                        }
                    }, 500);
                }
            } else {
                Snackbar.make(view.getToolbar(), "两次新密码输入不同，请重试！", Snackbar.LENGTH_LONG).show();
            }
        } else
            Snackbar.make(view.getToolbar(), "请输入学号或密码！", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void changePasswordStep1(final Activity activity, final ChangePassView view, final String passwordOld, final String passwordNew) {
        ToastUtils.showShort(R.string.loading);
        OkHttp.do_Get(Url.Yangtzeu_Change_Password, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                ToastUtils.showShort(R.string.loading);
                Document document = Jsoup.parse(response);
                Elements string = document.select("li.foot input");
                String value = string.attr("value");
                LogUtils.i("学生ID：" + value);
                changePasswordStep2(activity, view, passwordOld, passwordNew, value);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }

    @Override
    public void changePasswordStep2(final Activity activity, final ChangePassView view, final String passwordOld, final String passwordNew, final String value) {
        String StudentNum = SPUtils.getInstance("user_info").getString("number");
        FormBody formBody = new FormBody.Builder()
                .add("oldPassword", passwordOld)
                .add("password", passwordNew)
                .add("repeatedPassword", passwordNew)
                .add("mail", StudentNum + "@yangtzeu.edu.com")
                .add("user.id", value)
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(Url.Yangtzeu_Change_Password)
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                if (response.contains("请不要过快点击")) {
                    changePasswordStep2(activity, view, passwordOld, passwordNew, value);
                } else if (response.contains("保存成功")) {
                    //设置Cookie不可用
                    SPUtils.getInstance("user_info").put("online", false);
                    //清空旧Cookie
                    SPUtils.getInstance("user_info").put("cookie", "");
                    AlertDialog dialog = MyUtils.getAlert(activity, "密码修改成功！\n请牢记你的新密码！\n\nPassword：" + passwordNew,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityUtils.finishAllActivities();
                                    MyUtils.startActivity(LoginActivity.class);
                                }
                            });
                    dialog.show();
                } else if (response.contains("原密码不正确")) {
                    AlertDialog dialog = MyUtils.getAlert(activity, "原密码不正确", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    //设置Cookie不可用
                    SPUtils.getInstance("user_info").put("online", false);
                    MyUtils.getAlert(activity, "Cookie过期，请先登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityUtils.finishAllActivities();
                            MyUtils.startActivity(LoginActivity.class);
                        }
                    }).show();
                }
            }
            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.change_pass_error);
            }
        });
    }
}
