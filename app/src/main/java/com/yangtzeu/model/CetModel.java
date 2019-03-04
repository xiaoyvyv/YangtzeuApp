package com.yangtzeu.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.CetCardBean;
import com.yangtzeu.entity.CetDateBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultBytesListener;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.ICetModel;
import com.yangtzeu.ui.view.CetView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CetModel implements ICetModel {

    @Override
    public void initData(final Activity activity, final CetView view) {
        ToastUtils.showShort("正在初始化查询日期");
        OkHttp.do_Get(Url.Yangtzeu_Cet_Date, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                ToastUtils.showShort("初始化成功");
                view.getQueryGradeView().setVisibility(View.VISIBLE);
                try {
                    String json = response.substring(response.indexOf("=") + 1, response.lastIndexOf(";"));
                    CetDateBean dateBean = GsonUtils.fromJson(json, CetDateBean.class);
                    List<CetDateBean.RdsubBean> beans = dateBean.getRdsub();

                    view.getCetTitle().setText(dateBean.getSubn());

                    if (ObjectUtils.isNotEmpty(beans)) {
                        for (final CetDateBean.RdsubBean bean : beans) {
                            LogUtils.e(bean.getName(), bean.getCode(), bean.getEn(), bean.getTab());
                            if (bean.getCode().contains("CET4")) {
                                view.setCetInfo(bean.getTab());
                                view.getCet4DateView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        view.setCetInfo(bean.getTab());
                                        view.getCet4DateView().setTextColor(activity.getResources().getColor(R.color.colorAccent));
                                        view.getCet6DateView().setTextColor(activity.getResources().getColor(R.color.white_d));
                                    }
                                });
                                continue;
                            }

                            if (bean.getCode().contains("CET6")) {
                                view.getCet6DateView().setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        view.setCetInfo(bean.getTab());
                                        view.getCet4DateView().setTextColor(activity.getResources().getColor(R.color.white_d));
                                        view.getCet6DateView().setTextColor(activity.getResources().getColor(R.color.colorAccent));
                                    }
                                });
                            }
                        }
                    }

                    addTextWatcher(activity, view);
                } catch (Exception e) {
                    ToastUtils.showShort("表单初始化失败");
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort("CET成绩查询初始化失败");
            }
        });
    }

    @Override
    public void getCetHistoryGrade(final Activity activity, final CetView view) {
        OkHttp.do_Get(view.getUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String result) {
                Document document = Jsoup.parse(result);
                Elements tbody_List = document.select("table.gridtable>tbody");
                if (!tbody_List.isEmpty() && tbody_List.size() > 1) {
                    Elements trList = tbody_List.get(1).select("tbody tr");

                    view.getCetHistoryContainer().removeAllViews();

                    for (int i = 0; i < trList.size(); i++) {
                        String text = trList.get(i).text();
                        if (StringUtils.isEmpty(text)) {
                            ToastUtils.showShort("CET历史成绩：" + activity.getString(R.string.no_data));
                            continue;
                        }

                        LinearLayout ItemView2 = (LinearLayout) View.inflate(activity, R.layout.activity_cet_item, null);
                        LinearLayout cardView = ItemView2.findViewById(R.id.item);
                        view.getCetHistoryContainer().addView(ItemView2);

                        String item[] = text.split(" ");
                        try {
                            TextView name = ItemView2.findViewById(R.id.name);
                            name.setText(item[0]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            TextView grade = ItemView2.findViewById(R.id.grade);
                            if (Double.valueOf(item[1]) < 425) {
                                cardView.setBackgroundColor(activity.getResources().getColor(R.color.white_d));
                            } else {
                                cardView.setBackgroundColor(activity.getResources().getColor(R.color.white));
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
                } else {
                    ToastUtils.showShort("CET历史成绩：" + activity.getString(R.string.no_data));
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });
    }

    @Override
    public void addTextWatcher(final Activity activity, final CetView view) {
        //自动填充姓名和准考证号
        String name = SPUtils.getInstance("user_info").getString("cet_name");
        view.getStudentName().setText(name);
        String number = SPUtils.getInstance("user_info").getString("cet_number");
        view.getCetNumber().setText(number);

        if (!StringUtils.isEmpty(number)) {
            getGradeYzm(activity, view);
        }

        view.getCetNumber().addTextChangedListener(new TextWatcher() {
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
                    getGradeYzm(activity, view);
                }
            }
        });
    }

    @Override
    public void getGradeYzm(final Activity activity, final CetView view) {
        view.getYzmEditView().setText(null);
        String s_number = view.getCetNumber().getText().toString().trim();
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

        final ProgressDialog progress = MyUtils.getProgressDialog(activity, "验证码加载中...");
        progress.show();


        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    String image = Objects.requireNonNull(response);
                    image = image.substring(image.indexOf("\"") + 1, image.lastIndexOf("\""));
                    Glide.with(activity).load(image).into(view.getYzmImage());
                } catch (Exception e) {
                    ToastUtils.showShort(response);
                }
            }

            @Override
            public void onFailure(String error) {
                progress.dismiss();
                ToastUtils.showShort(R.string.load_error);
            }
        });
    }

    @Override
    public void getNowTermGrade(final Activity activity, final CetView view) {
        KeyboardUtils.hideSoftInput(activity);

        String s_name = view.getStudentName().getText().toString().trim();
        String s_number = view.getCetNumber().getText().toString().trim();
        String s_yzm = view.getYzmEditView().getText().toString().trim();

        SPUtils.getInstance("user_info").put("cet_name", s_name);
        SPUtils.getInstance("user_info").put("cet_number", s_number);

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
                .add("data", view.getCetInfo() + "," + s_number + "," + s_name)
                .build();
        LogUtils.e("data", view.getCetInfo() + "," + s_number + "," + s_name);

        final Request request = new Request.Builder()
                .url("http://cache.neea.edu.cn/cet/query")
                .addHeader("Referer", "http://cet.neea.edu.cn/cet/")
                .addHeader("Accept", "/")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Host", "cache.neea.edu.cn")
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Cache-Control", "max-age=0")
                .post(body)
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String result) {
                //重新刷新验证码
                getGradeYzm(activity, view);
                result = result.substring(result.indexOf("{") + 1, result.lastIndexOf("}") - 1);

                final String error = result.substring(result.lastIndexOf("'") + 1);
                final String finalResult = result;
                new Handler(activity.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (finalResult.contains("error")) {
                            ToastUtils.showLong(error);
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
                            if (view.getCetInfo().contains("CET4")) {
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

                            AlertDialog dialog = new AlertDialog.Builder(activity)
                                    .setTitle("Cet成绩详情")
                                    .setMessage(details)
                                    .setPositiveButton("知道了", null)
                                    .setNegativeButton("分享Cet成绩", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            MyUtils.shareText(activity, "给你看看我的Cet成绩噢!\n\n" + details + "\n\n数据来自：" + Url.AppDownUrl);
                                        }
                                    })
                                    .create();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                        }

                    }
                });

            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });

    }

    @Override
    public void getCardYzm(final Activity activity, final CetView view) {
        String id_number = SPUtils.getInstance("user_info").getString("id_number");
        view.getCardNumber().setText(id_number);
        String name = SPUtils.getInstance("user_info").getString("cet_name");
        view.getCardName().setText(name);

        OkHttp.do_Get(Url.Yangtzeu_Cet_Card_Yzm, new OnResultBytesListener() {
            @Override
            public void onResponse(byte[] bytes) {
                Glide.with(activity).load(bytes).into(view.getCardYzmImage());
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    public void getCardInfo(final Activity activity, final CetView view) {
        KeyboardUtils.hideSoftInput(activity);

        String card_name = view.getCardName().getText().toString().trim();
        String card_number = view.getCardNumber().getText().toString().trim();
        String card_yzm = view.getCardYzm().getText().toString().trim();

        if (card_name.isEmpty()) {
            ToastUtils.showShort("请先输入姓名");
            return;
        }
        if (card_number.isEmpty()) {
            ToastUtils.showShort("请先输入身份证号");
            return;
        }
        if (card_yzm.isEmpty()) {
            ToastUtils.showShort("请先输入验证码");
            return;
        }
        SPUtils.getInstance("user_info").put("id_number", card_number);
        FormBody formBody = new FormBody.Builder()
                .add("provinceCode", "42")
                .add("IDTypeCode", "1")
                .add("Name", card_name)
                .add("IDNumber", card_number)
                .add("verificationCode", card_yzm)
                .build();
        Request request = new Request.Builder()
                .addHeader("Referer", "http://cet-bm.neea.edu.cn/Home/QuickPrintTestTicket")
                .post(formBody)
                .url("http://cet-bm.neea.edu.cn/Home/ToQuickPrintTestTicket")
                .build();
        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                LogUtils.e(response);
                //刷新验证码
                view.getCardYzm().setText(null);
                getCardYzm(activity, view);
                try {
                    CetCardBean cetCardBean = GsonUtils.fromJson(response, CetCardBean.class);
                    String message = cetCardBean.getMessage();
                    if (message.contains("[")) {
                        message = message.replace("[", "");
                        message = message.replace("]", "");
                        CetCardBean.MessageBean messageBean = GsonUtils.fromJson(message, CetCardBean.MessageBean.class);
                        final String url = "http://cet-bm.neea.edu.cn/Home/DownTestTicket?SID=" + messageBean.getSID();
                        MyUtils.getAlert(activity, "查询成功，是否下载准考证？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyUtils.openUrl(activity, url);
                            }
                        }).show();
                    } else {
                        MyUtils.getAlert(activity, message, null).show();
                    }
                } catch (Exception e) {
                    ToastUtils.showShort(R.string.load_error);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });
    }

    private String getItem(String string) {
        String item;
        if (string.contains("'")) {
            item = string.substring(string.indexOf("'") + 1, string.lastIndexOf("'"));
        } else {
            item = string.substring(string.indexOf(":") + 1);
        }
        return item;
    }

}
