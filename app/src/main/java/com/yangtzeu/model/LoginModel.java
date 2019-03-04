package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.chat.common.UserManager;
import com.xiaomi.mimc.MIMCUser;
import com.yangtzeu.R;
import com.yangtzeu.app.MyApplication;
import com.yangtzeu.database.DatabaseUtils;
import com.yangtzeu.entity.UserBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.ILoginModel;
import com.yangtzeu.ui.activity.MainActivity;
import com.yangtzeu.ui.view.LoginView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.UserUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Objects;

public class LoginModel implements ILoginModel {

    @Override
    public void loadLoginEvent(final Activity activity, final LoginView view) {
        String number = SPUtils.getInstance("user_info").getString("number");
        String password = SPUtils.getInstance("user_info").getString("password");
        view.getNumberView().setText(number);
        view.getPassWordView().setText(password);


        view.getLoginButton().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ToastUtils.showShort("您未登录强制进入应用，将会导致大部分功能无法使用！");
                MyUtils.startActivity(MainActivity.class);
                activity.finish();

                //通用监听
                MyApplication.setMessListener();
                MIMCUser mimcUser = UserManager.getInstance().newUser(null);
                mimcUser.login();
                return true;
            }
        });


        view.getLoginButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_number = Objects.requireNonNull(view.getNumberView().getText()).toString().trim();
                String user_password = Objects.requireNonNull(view.getPassWordView().getText()).toString().trim();
                if (user_number.isEmpty()) {
                    ToastUtils.showShort(R.string.input_number);
                    return;
                }
                if (user_password.isEmpty()) {
                    ToastUtils.showShort(R.string.input_pass);
                    return;
                }

                KeyboardUtils.hideSoftInput(view.getPassWordView());

                SPUtils.getInstance("user_info").put("number", user_number);
                SPUtils.getInstance("user_info").put("password", user_password);

                ToastUtils.showLong(R.string.login_ing);
                view.getLoginButton().setEnabled(false);

                String UserInfo[] = {user_number, user_password};
                UserUtils.do_Login(activity, UserInfo, new UserUtils.OnLogResultListener() {
                    @Override
                    public void onSuccess(String result) {
                        loginSuccess(activity, view, result);
                    }

                    @Override
                    public void onFailure(String error) {
                        view.getLoginButton().setEnabled(true);
                        loginFailure(activity, view, error);
                    }
                });

            }
        });


    }

    @Override
    public void loadHistory(final Activity activity, final LoginView loginView) {
        loginView.getHistoryView().setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                final List<UserBean> userBeans = DatabaseUtils.getHelper("user.db").queryAll(UserBean.class);
                if (ObjectUtils.isNotEmpty(userBeans)) {
                    @SuppressLint("InflateParams")
                    View view = activity.getLayoutInflater().inflate(R.layout.view_user_history, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    LinearLayout layout = view.findViewById(R.id.slow_container);
                    for (int i = 0; i < userBeans.size(); i++) {
                        @SuppressLint("InflateParams")
                        View m_item = activity.getLayoutInflater().inflate(R.layout.view_user_history_item, null);
                        TextView title = m_item.findViewById(R.id.title);
                        TextView bt = m_item.findViewById(R.id.bt);
                        TextView bt_2 = m_item.findViewById(R.id.bt_2);
                        title.setText(userBeans.get(i).getName() + "：" + userBeans.get(i).getNumber());
                        final int finalI = i;
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                ToastUtils.showShort("你选择了：" + userBeans.get(finalI).getName());
                                loginView.getNumberView().setText(userBeans.get(finalI).getNumber());
                                loginView.getPassWordView().setText(userBeans.get(finalI).getPassowrd());
                            }
                        });
                        bt_2.setVisibility(View.VISIBLE);
                        bt_2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyUtils.getAlert(activity, activity.getString(R.string.is_delete_history), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface d, int which) {
                                        dialog.dismiss();
                                        UserBean userBean = DatabaseUtils.getHelper( "user.db")
                                                .queryById(UserBean.class, userBeans.get(finalI).getNumber());
                                        DatabaseUtils.getHelper( "user.db").delete(userBean);
                                        ToastUtils.showShort(R.string.delete_success);
                                    }
                                }).show();
                            }
                        });
                        layout.addView(m_item);
                    }

                } else {
                    ToastUtils.showShort(R.string.no_user_history);
                }
            }
        });
    }

    private void loginFailure(final Activity activity, LoginView view, String error) {
        ToastUtils.showShort(error);
        view.getLoginButton().setText(R.string.login_again);
        view.getLoginButton().setEnabled(true);
        SPUtils.getInstance("user_info").put("online", false);
        if (error.contains("教务处网络过载")) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(R.string.trip)
                    .setMessage(R.string.can_login_please_open_web)
                    .setPositiveButton(R.string.know, null)
                    .setNegativeButton(R.string.try_to_open, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyUtils.openBrowser(activity, Url.Yangtzeu_Login_Path);
                        }
                    })
                    .create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else if (error.equals("用户封停")) {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle("管理员温馨提示")
                    .setMessage("由于系统检测到你在使用本App的过程中，出现过不文明行为！\n\n给予封停该学号的处分！\n解封时间待定！")
                    .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ActivityUtils.finishAllActivities();
                            AppUtils.exitApp();
                        }
                    })
                    .create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    private void loginSuccess(Activity activity, LoginView view, String result) {
        ToastUtils.showShort(R.string.login_success);
        Document document = Jsoup.parse(result);
        Elements form = document.select("form");
        String id = form.text();
        id = id.replace(" ", "\n\n");
        SPUtils.getInstance("user_info").put("online", true);
        view.getLoginButton().setText(R.string.login_success);
        view.getLoginButton().setEnabled(false);
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.welcome)
                .setMessage(activity.getString(R.string.your_id) + "：" + id)
                .setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityUtils.finishAllActivities();
                        MyUtils.startActivity(MainActivity.class);
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setCancelable(false);

    }
}
