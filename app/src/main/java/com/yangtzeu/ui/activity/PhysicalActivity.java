package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultBytesListener;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.FragmentAdapter;
import com.yangtzeu.ui.fragment.PhysicalFragment;
import com.yangtzeu.ui.fragment.PhysicalAddFragment;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PhysicalActivity extends BaseActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tab_layout;
    public Handler handler;
    private PhysicalAddFragment physicalAddFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);
        init();

        handler = new Handler(getMainLooper());

        MyUtils.setToolbarBackToHome(this, toolbar);
    }


    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        tab_layout = findViewById(R.id.physical_tablayout);
        viewPager = findViewById(R.id.viewPager);

    }

    @Override
    public void setEvents() {
        showLogin();
    }


    public void showLogin() {
        String physical_number = SPUtils.getInstance("user_info").getString("number");
        String physical_password = SPUtils.getInstance("user_info").getString("physical_password", "12345678");

        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.activity_physical_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(PhysicalActivity.this)
                .setView(view)
                .create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        final TextInputEditText number = view.findViewById(R.id.number);
        number.setText(physical_number);
        final TextInputEditText password = view.findViewById(R.id.password);
        final TextInputEditText verifyView = view.findViewById(R.id.verifyView);
        final ImageView verifyImage = view.findViewById(R.id.verifyImage);


        password.setText(physical_password);
        Button login = view.findViewById(R.id.login);
        Button exit = view.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                PhysicalActivity.this.onBackPressed();
            }
        });

        getVerifyImage(verifyImage);
        verifyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyImage(verifyImage);
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

                String verify = Objects.requireNonNull(verifyView.getText()).toString().trim();
                if (verify.isEmpty()) {
                    verifyView.setError("请输入验证码");
                    verifyView.requestFocus();
                    return;
                }
                KeyboardUtils.hideSoftInput(PhysicalActivity.this);

                SPUtils.getInstance("user_info").put("physical_password", password_str);

                RequestBody body = new FormBody.Builder()
                        .add("password", password_str)
                        .add("Submit", "立即登陆")
                        .add("userid", number_str)
                        .add("usertype", "student")
                        .add("code", verify)
                        .build();

                Request request = new Request.Builder()
                        .url(Url.Yangtzeu_Physical_Login)
                        .post(body)
                        .build();

                OkHttp.do_Post(request, new OnResultStringListener() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("学号或密码输入不正确")) {
                            ToastUtils.showShort("学号或密码输入不正确\nTrip：若已修完全部实验则无需该操作");
                        } else if (response.contains("我的实验预约")) {
                            ToastUtils.showShort("登录成功");
                            dialog.dismiss();
                            getData();
                        } else ToastUtils.showShort("未知错误");
                    }

                    @Override
                    public void onFailure(String error) {
                        dialog.dismiss();
                        ToastUtils.showShort("登录失败");
                    }
                });

            }
        });
    }

    //获取验证码
    private void getVerifyImage(final ImageView verifyImage) {
        OkHttp.do_Get(Url.Yangtzeu_Physical_Verify, new OnResultBytesListener() {
            @Override
            public void onResponse(byte[] bytes) {
                Bitmap bitmap = ImageUtils.bytes2Bitmap(bytes);
                verifyImage.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }

    private void getData() {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.clear();
        physicalAddFragment = PhysicalAddFragment.newInstance(Url.Yangtzeu_Physical_Add);
        adapter.addFragment("添加实验预约", physicalAddFragment);
        PhysicalFragment physicalFragment = PhysicalFragment.newInstance(Url.Yangtzeu_Physical_Grade);
        adapter.addFragment("已选实验预约", physicalFragment);
        PhysicalFragment physicalFragment2 = PhysicalFragment.newInstance(Url.Yangtzeu_Physical_Delete);
        adapter.addFragment("删除实验预约", physicalFragment2);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tab_layout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("资料下载").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                MyUtils.openUrl(PhysicalActivity.this, "http://phylab.yangtzeu.edu.cn/jpkc/resources/onews.asp?id=156");
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add("地点说明").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                List<Cookie> cookieList = OkHttp.cookieJar().loadForRequest(Objects.requireNonNull(HttpUrl.get(URI.create(Url.Yangtzeu_Physical_Login))));
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < cookieList.size(); i++) {
                    builder.append(cookieList.get(i));
                    builder.append(";");
                }
                String physical_cookie = builder.toString();
                LogUtils.i("物理实验Cookie：" + physical_cookie);

                SPUtils.getInstance("user_info").put("physical_cookie",physical_cookie);
                MyUtils.openUrl(PhysicalActivity.this, "http://10.10.16.16/index.php/Home/Student/exproom", physical_cookie);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (physicalAddFragment != null) {
            boolean isDel = physicalAddFragment.hideList();
            if (!isDel) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
