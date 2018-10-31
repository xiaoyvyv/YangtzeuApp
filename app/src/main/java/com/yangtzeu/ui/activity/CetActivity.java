package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CetActivity extends BaseActivity {
    private final static String TAG = "【CetActivity】";
    private Toolbar toolbar;
    private LinearLayout Cet_container;
    private ImageView yzm;
    private EditText et_name;
    private EditText et_number;
    private EditText et_yzm;
    private OkHttpClient okHttpClient;
    private String COOKIE;
    private String CET = "CET4_181_DANGCI";
    private ProgressDialog progress;
    private TextView trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cet);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        trip = findViewById(R.id.trip);
        Cet_container = findViewById(R.id.Cet_container);
        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        et_yzm = findViewById(R.id.et_yzm);
        yzm = findViewById(R.id.yzm);


    }

    @Override
    public void setEvents() {
        progress = new ProgressDialog(CetActivity.this);

        okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> list) {
                COOKIE = getCookie(list);
                LogUtils.e(COOKIE);
            }

            @Override
            public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                return new ArrayList<>();
            }
        }).build();


        yzm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getYzm();
            }
        });


        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.length() == 15) {
                    SPUtils.getInstance("user_info").put("cet_number", input);
                    getYzm();
                }
            }
        });

        String number = SPUtils.getInstance("user_info").getString("cat_number");
        et_number.setText(number);
        String name = SPUtils.getInstance("user_info").getString("cat_name");
        et_name.setText(name);


        getCetHistory();
    }

    private void getCetHistory() {
        OkHttp.do_Get(Url.Yangtzeu_Cet, new OnResultStringListener() {
            @Override
            public void onResponse(String s) {
                try {
                    GetCetGrade(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                if (trip != null)
                    trip.setText("请点击屏幕刷新");
                Cet_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCetHistory();
                        if (trip != null)
                            trip.setText("玩命加载中...\n请稍等!");
                    }
                });
            }
        });
    }


    private void getYzm() {
        et_yzm.setText(null);
        String s_number = et_number.getText().toString().trim();
        if (s_number.isEmpty()) {
            ToastUtils.showShort("请先输入准考证号");
            return;
        }
        String image = "http://cache.neea.edu.cn/Imgs.do?c=CET&ik=" + s_number + "&t=0." + MyUtils.getRand(100000) + "10045977614";
        final Request request = new Request.Builder()
                .url(image)
                .addHeader("Referer", "http://cet.neea.edu.cn/cet/")
                .addHeader("Accept", "*/*")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Host", "cache.neea.edu.cn")
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Cache-Control", "max-age=0")
                .get()
                .build();

        progress.setMessage("验证码加载中...");
        progress.show();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Message message = new Message();
                message.obj = "请检查网络连接";
                message.what = 404;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String image = Objects.requireNonNull(response.body()).string();
                image = image.substring(image.indexOf("\"") + 1, image.lastIndexOf("\""));
                final String finalImage = image;
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Glide.with(CetActivity.this).load(finalImage).into(yzm);
                    }
                });
            }
        });
    }

    public void getCet(View view) {
        String s_name = et_name.getText().toString().trim();
        String s_yzm = et_yzm.getText().toString().trim();
        String s_number = et_number.getText().toString().trim();
        SPUtils.getInstance("user_info").put("cat_name", s_name);
        SPUtils.getInstance("user_info").put("cat_number", s_number);

        if (s_number.isEmpty()) {
            ToastUtils.showShort("请输入准考证号");
            return;
        }
        if (s_name.isEmpty()) {
            ToastUtils.showShort("请输入姓名");
            return;
        }
        if (s_yzm.isEmpty()) {
            ToastUtils.showShort("请输入验证码");
            return;
        }
        RequestBody body = new FormBody.Builder()
                .add("v", s_yzm)
                .add("data", CET + "," + s_number + "," + s_name)
                .build();
        final Request request = new Request.Builder()
                .url("http://cache.neea.edu.cn/cet/query")
                .addHeader("Referer", "http://cet.neea.edu.cn/cet/")
                .addHeader("Accept", "/")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Host", "cache.neea.edu.cn")
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("Cookie", COOKIE)
                .post(body)
                .build();

        progress.setMessage("Cet成绩查询中...");
        progress.show();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Message message = new Message();
                message.obj = "请检查网络连接";
                message.what = 400;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String result = Objects.requireNonNull(response.body()).string();
                result = result.substring(result.indexOf("{") + 1, result.lastIndexOf("}") - 1);

                final String error = result.substring(result.lastIndexOf("'") + 1, result.length());
                final String finalResult = result;
                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        if (finalResult.contains("error")) {
                            Message message = new Message();
                            message.obj = error;
                            message.what = 400;
                            handler.sendMessage(message);
                        } else {
                            String list[] = finalResult.split(",");
                            String number = null;
                            String name = null;
                            String school = null;
                            String score = null;
                            String l = null;
                            String r = null;
                            String w = null;
                            try {
                                number = getItem(list[0]);
                                name = getItem(list[1]);
                                school = getItem(list[2]);
                                score = getItem(list[3]);
                                String t = getItem(list[4]);
                                l = getItem(list[5]);
                                r = getItem(list[6]);
                                w = getItem(list[7]);
                                String kyz = getItem(list[8]);
                                String kys = getItem(list[9]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            final String kind;
                            if (CET.contains("CET4")) {
                                kind = "英语四级";
                            } else {
                                kind = "英语六级";
                            }

                            final String details = "Cet类别：" + kind
                                    + "\n准考证号：" + number
                                    + "\n姓名：" + name
                                    + "\n学校：" + school
                                    + "\n最终成绩：" + score
                                    + "\n听力部分：" + l
                                    + "\n阅读部分：" + r
                                    + "\n写作部分：" + w;

                            LogUtils.i(details);

                            Message message = new Message();
                            message.obj = details;
                            message.what = 0;
                            handler.sendMessage(message);
                        }

                    }
                });

            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    getYzm();
                    final String text = msg.obj.toString();
                    AlertDialog dialog = new AlertDialog.Builder(CetActivity.this)
                            .setTitle("Cet成绩详情")
                            .setMessage(text)
                            .setPositiveButton("知道了", null)
                            .setNegativeButton("分享Cet成绩", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MyUtils.shareText(CetActivity.this, "给你看看我的Cet成绩噢!\n\n" + text + "\n\n数据来自：" + Url.AppDownUrl);

                                }
                            })
                            .create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    break;
                case 400:
                    getYzm();

                    progress.dismiss();
                    ToastUtils.showShort(msg.obj.toString());
                    break;
            }
        }
    };

    private String getItem(String string) {
        String item;
        if (string.contains("'")) {
            item = string.substring(string.indexOf("'") + 1, string.lastIndexOf("'"));
        } else {
            item = string.substring(string.indexOf(":") + 1, string.length());
        }
        return item;
    }

    private void GetCetGrade(String result) {
        Document document = Jsoup.parse(result);
        Elements Tbody_List = document.select("table.gridtable tbody#grid7017333742_data");
        Elements Tr_List = Tbody_List.get(0).select("tbody tr");

        Cet_container.removeAllViews();

        for (int i = 0; i < Tr_List.size(); i++) {
            LinearLayout ItemView2 = (LinearLayout) View.inflate(CetActivity.this, R.layout.activity_cet_item, null);
            CardView cardView = ItemView2.findViewById(R.id.Item);

            Cet_container.addView(ItemView2);
            String item[] = Tr_List.get(i).text().split(" ");
            Log.e(TAG, Arrays.toString(item));


            try {
                TextView name = ItemView2.findViewById(R.id.name);
                name.setText(item[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                TextView grade = ItemView2.findViewById(R.id.grade);
                if (Double.valueOf(item[1]) < 425) {
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.white_d));
                } else {
                    cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }
                grade.setText(item[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                TextView is_pass = ItemView2.findViewById(R.id.is_pass);
                is_pass.setText(item[2]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                TextView term = ItemView2.findViewById(R.id.term);
                term.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                term.setSingleLine(true);
                term.setSelected(true);
                term.setFocusable(true);
                term.setFocusableInTouchMode(true);
                term.setText(item[3]);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cet_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_cet:
                MyUtils.startActivity(new Intent(CetActivity.this, WebActivity.class)
                        .putExtra("from_url", Url.Yangtzeu_Cet_Add));
                break;
            case R.id.read_in_web:
                MyUtils.startActivity(new Intent(CetActivity.this, WebActivity.class)
                        .putExtra("from_url", Url.Yangtzeu_Cet));
                break;
            case R.id.join_in_web:
                MyUtils.startActivity(new Intent(CetActivity.this, WebActivity.class)
                        .putExtra("from_url", Url.Yangtzeu_Guan_Cet));
                break;
            case R.id.g_web:
                MyUtils.startActivity(new Intent(CetActivity.this, WebActivity.class)
                        .putExtra("from_url", "http://cet.neea.edu.cn/cet"));
                break;
            case R.id.g_web2:
                MyUtils.startActivity(new Intent(CetActivity.this, WebActivity.class)
                        .putExtra("from_url", "https://www.chsi.com.cn/cet/"));
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private String getCookie(List<Cookie> cookies) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < cookies.size(); i++) {
            stringBuilder.append(cookies.get(i));
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    public void setCet4(View view) {
        ((TextView) findViewById(R.id.cet4)).setTextColor(getResources().getColor(R.color.colorAccent));
        ((TextView) findViewById(R.id.cet6)).setTextColor(getResources().getColor(R.color.white_d));
        CET = "CET4_181_DANGCI";
    }

    public void setCet6(View view) {
        ((TextView) findViewById(R.id.cet4)).setTextColor(getResources().getColor(R.color.white_d));
        ((TextView) findViewById(R.id.cet6)).setTextColor(getResources().getColor(R.color.colorAccent));
        CET = "CET6_181_DANGCI";
    }

}
