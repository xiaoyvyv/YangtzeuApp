package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.lib.x5web.WebViewProgressBar;
import com.lib.x5web.X5LoadFinishListener;
import com.lib.x5web.X5WebView;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebView;
import com.yangtzeu.R;
import com.yangtzeu.entity.CardInfoBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
public class CardCenterActivity extends BaseActivity implements View.OnClickListener {
    private static String CardCenterCookie = "";
    private String __VIEWSTATE;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private TextView card_id;
    private TextView card_tran_money;
    private TextView card_money;
    private X5WebView x5web;

    //首页
    public String home = "http://ykt.yangtzeu.edu.cn/Main.aspx";
    //挂失
    public String toaccountLose_url = Url.Yangtzeu_Card_Home+":8090/pcard/pcard/toaccountLose.action?";
    //解挂
    public String toaccountUnLose_url = Url.Yangtzeu_Card_Home+":8090/pcard/pcard/toaccountUnLose.action?";
    //修改密码
    public String toaccountCpwd_url = Url.Yangtzeu_Card_Home+":8090/pcard/pcard/toaccountCpwd.action?";
    //流水查询
    public String acchistrjn_url = Url.Yangtzeu_Card_Home+":8090/pcard/pcard/acchistrjn.action?";
    //银行卡充值校园卡
    public String cardfer_url = Url.Yangtzeu_Card_Home+":8090/pcard/pcard/cardfer.action?";
    //银行卡绑定校园卡
    public String bankbund_url = Url.Yangtzeu_Card_Home+":8090/pcard/pcard/bankbund.action?";
    //银行卡解绑校园卡
    public String bankunbund_url = Url.Yangtzeu_Card_Home+":8090/pcard/pcard/bankunbund.action?";
    //银行卡余额查询
    public String bankacc_url = Url.Yangtzeu_Card_Home+":8090/pcard/pcard/bankacc.action?";
    //安全信息修改
    public String SafeSet =  Url.Yangtzeu_Card_Home+"/Account/SafeSet.aspx?";
    //我的信息
    public String MyAccount =  Url.Yangtzeu_Card_Home+"/Account/MyAccount.aspx?";
    private WebViewProgressBar progress;
    private boolean isFirst = true;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_info:
                openRequestUrl(MyAccount);
                break;
            case R.id.acchistrjn_url:
                openRequestUrl(acchistrjn_url);
                break;
            case R.id.SafeSet:
                openRequestUrl(SafeSet);
                break;
            case R.id.bankacc_url:
                openRequestUrl(bankacc_url);
                break;
            case R.id.bankunbund_url:
                openRequestUrl(bankunbund_url);
                break;
            case R.id.bankbund_url:
                openRequestUrl(bankbund_url);
                break;
            case R.id.cardfer_url:
                openRequestUrl(cardfer_url);
                break;
            case R.id.toaccountCpwd_url:
                openRequestUrl(toaccountCpwd_url);
                break;
            case R.id.toaccountUnLose_url:
                openRequestUrl(toaccountUnLose_url);
                break;
            case R.id.toaccountLose_url:
                openRequestUrl(toaccountLose_url);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("浏览器中打开").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String TempCookie = SPUtils.getInstance("user_info").getString("school_card_cookie");
                TempCookie = TempCookie.substring(TempCookie.indexOf("=") + 1, TempCookie.lastIndexOf(";"));
                MyUtils.openBrowser(CardCenterActivity.this, home + "?f" + TempCookie);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_center);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);

    }


    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        card_id = findViewById(R.id.card_id);
        card_tran_money = findViewById(R.id.card_tran_money);
        card_money = findViewById(R.id.card_money);
        x5web = findViewById(R.id.x5web);
        progress = findViewById(R.id.progress);


    }

    @Override
    public void setEvents() {

        fab.setEnabled(false);
        x5web.setTitleAndProgressBar(toolbar, progress);


        if (ObjectUtils.isEmpty(CardCenterCookie)) {
            getHideState();
        } else {
            getInfo();
            fab.setEnabled(true);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ObjectUtils.isEmpty(CardCenterCookie)) {
                    showLogin(__VIEWSTATE);
                } else {
                    ToastUtils.showShort("当前用户：" + CardCenterCookie);
                }
            }
        });

    }

    private void getHideState() {
        final ProgressDialog progressDialog = new ProgressDialog(CardCenterActivity.this);
        progressDialog.setMessage("表单获取中，请稍等");
        progressDialog.setTitle("提示");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttp.do_Get(Url.Yangtzeu_CenterCard_Login, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Document doc = Jsoup.parse(response);
                __VIEWSTATE = doc.select("input#__VIEWSTATE").attr("value").trim();
                if (ObjectUtils.isEmpty(__VIEWSTATE)) {
                    AlertDialog dialog = new AlertDialog.Builder(CardCenterActivity.this)
                            .setTitle("提示")
                            .setMessage("数据出错，请链接到校园网后访问\n\nTrip：零点后服务器可能会关闭，可在正常工作时间内重新尝试该操作！")
                            .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    CardCenterActivity.this.onBackPressed();
                                }
                            })
                            .create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    return;
                }
                showLogin(__VIEWSTATE);
                fab.setEnabled(true);
            }

            @Override
            public void onFailure(String error) {
                progressDialog.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(CardCenterActivity.this)
                        .setTitle("提示")
                        .setMessage("数据出错，请链接到校园网后访问")
                        .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CardCenterActivity.this.onBackPressed();
                            }
                        })
                        .create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void showLogin(final String __VIEWSTATE) {
        final String number_str = SPUtils.getInstance("user_info").getString("number");
        final String password_str = SPUtils.getInstance("user_info").getString("card_password");
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.activity_card_center_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(CardCenterActivity.this)
                .setView(view)
                .create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final TextInputEditText number = view.findViewById(R.id.number);
        number.setText(number_str);
        final TextInputEditText password = view.findViewById(R.id.password);

        password.setText(password_str);
        Button login = view.findViewById(R.id.login);
        Button exit = view.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                CardCenterActivity.this.onBackPressed();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number_str = Objects.requireNonNull(number.getText()).toString().trim();
                if (number_str.isEmpty()) {
                    number.setError("请输入学号");
                    number.requestFocus();
                    return;
                }
                String password_str = Objects.requireNonNull(password.getText()).toString().trim();
                if (password_str.isEmpty()) {
                    password.setError("请输入密码");
                    password.requestFocus();
                    return;
                }

                final ProgressDialog progressDialog = new ProgressDialog(CardCenterActivity.this);
                progressDialog.setMessage("登录中，请稍等");
                progressDialog.setTitle("提示");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                SPUtils.getInstance("user_info").put("number", number_str);
                SPUtils.getInstance("user_info").put("card_password", password_str);

                RequestBody body = new FormBody.Builder()
                        .add("__VIEWSTATE", __VIEWSTATE)
                        .add("loginId", number_str)
                        .add("loginPwd", password_str)
                        .add("loginType", "sno")
                        .build();

                LogUtils.e(__VIEWSTATE, number_str, password_str);
                Request request = new Request.Builder()
                        .url(Url.Yangtzeu_CenterCard_Login)
                        .post(body)
                        .build();

                OkHttp.do_Post(request, new OnResultStringListener() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.contains("学工号或密码错误")) {
                            ToastUtils.showShort("学工号或密码错误");
                        } else if (response.contains("个人信息")) {
                            List<Cookie> cookieList = OkHttp.cookieJar().loadForRequest(Objects.requireNonNull(HttpUrl.get(URI.create(Url.Yangtzeu_CenterCard_Login))));
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < cookieList.size(); i++) {
                                builder.append(cookieList.get(i));
                                builder.append(";");
                            }
                            String school_card_cookie = builder.toString();
                            LogUtils.i("一卡通Cookie：" + school_card_cookie);
                            CardCenterCookie = school_card_cookie;
                            SPUtils.getInstance("user_info").put("school_card_cookie", school_card_cookie);
                            getInfo();
                            ToastUtils.showShort("登录成功");
                            dialog.dismiss();
                        } else if (response.contains("登录方式")) {
                            ToastUtils.showShort("登录过期，请重新登录");
                            showLogin(__VIEWSTATE);
                        } else {
                            ToastUtils.showShort("未知错误");
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                        progressDialog.dismiss();
                        ToastUtils.showShort("登录失败");
                    }
                });
            }
        });
    }

    private void openRequestUrl(String url) {
        try {
            String TempCookie = CardCenterCookie;
            TempCookie = TempCookie.substring(TempCookie.indexOf("=") + 1, TempCookie.lastIndexOf(";"));
            LogUtils.e(url + TempCookie);
            x5web.loadUrl(url + TempCookie);
            x5web.setX5LoadFinishListener(new X5LoadFinishListener() {
                @Override
                public void onLoadFinish(WebView webView, WebViewProgressBar progressBar, String s) {
                    if (!isFirst) {
                        x5web.setVisibility(View.VISIBLE);
                    }
                    isFirst = false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getInfo() {
        String cookie_list[] = CardCenterCookie.split(";");
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        //设置cookie
        for (String mCookie : cookie_list) {
            if (URLUtil.isNetworkUrl(home)) {
                MyUtils.syncX5Cookie(MyUtils.getHost(home), mCookie);
                LogUtils.i("X5WebView同步Cookie", mCookie);
            }
        }
        openRequestUrl(cardfer_url);

        OkHttp.do_Get( Url.Yangtzeu_Card_Home+"/Account/Operator.ashx?cmd=getaccounts", new OnResultStringListener() {
            @Override
            public void onResponse(String result) {
                result = result.replace("[", "");
                result = result.replace("]", "");
                Gson gson = new Gson();
                LogUtils.e(result);
                try {
                    CardInfoBean infoBean = gson.fromJson(result, CardInfoBean.class);
                    String card_id_str = infoBean.getACCOUNT();
                    double card_money_str = Double.parseDouble(infoBean.getEBAGAMT());
                    double card_tran_money_str = Double.parseDouble(infoBean.getTMPBALANCE());

                    card_id.setText("校园卡卡号：" + card_id_str);
                    card_money.setText("校园卡余额：" + (card_money_str / 100));
                    card_tran_money.setText("校园卡过渡余额：" + (card_tran_money_str / 100));

                } catch (Exception e) {
                    CardCenterCookie = "";
                    getHideState();
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort("加载失败");

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (x5web.getVisibility() == View.VISIBLE) {
            x5web.setVisibility(View.GONE);
        } else super.onBackPressed();
    }

}
