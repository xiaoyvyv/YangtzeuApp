package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IPingJiaoModel;
import com.yangtzeu.ui.view.PingJiaoView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import okhttp3.FormBody;
import okhttp3.Request;

public class PingJiaoModel implements IPingJiaoModel {

    @Override
    public void loadData(final Activity activity, final PingJiaoView view) {
        String term_id = SPUtils.getInstance("user_info").getString("term_id", Url.Default_Term);

        FormBody formBody = new FormBody.Builder()
                .add("project.id", "1")
                .add("semester.id", term_id)
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(view.getUrl())
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                view.getContainer().removeAllViews();

                Document document = Jsoup.parse(response);
                Elements trs = document.select("table.gridtable>tbody>tr");
                if (trs.size() < 2) {
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.trip)
                            .setMessage("未到评教本学期时间或年份选择错误！")
                            .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    activity.onBackPressed();
                                }
                            })
                            .setNegativeButton("网页查看", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MyUtils.openUrl(activity, view.getUrl());
                                    activity.finish();
                                }
                            })
                            .setNeutralButton("恢复默认学期", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SPUtils.getInstance("user_info").put("term_id", Url.Default_Term);
                                    loadData(activity, view);
                                }
                            })
                            .show();
                    return;
                }

                for (int i = 0; i < trs.size(); i++) {
                    @SuppressLint("InflateParams")
                    View item = LayoutInflater.from(activity).inflate(R.layout.activity_pingjiao_item, null);
                    view.getContainer().addView(item);
                    TextView class_name = item.findViewById(R.id.class_name);
                    TextView wj_kind = item.findViewById(R.id.wj_kind);
                    TextView wj_name = item.findViewById(R.id.wj_name);
                    TextView wj_teacher = item.findViewById(R.id.wj_teacher);
                    TextView wj_text = item.findViewById(R.id.wj_text);
                    View view = item.findViewById(R.id.view);
                    LinearLayout onClick = item.findViewById(R.id.onClick);

                    Elements tds = trs.get(i).select("tr td");
                    LogUtils.e(tds);

                    String className = tds.get(1).text();
                    if (StringUtils.isEmpty(className)) className = "";

                    String classKind = tds.get(2).text();
                    if (StringUtils.isEmpty(classKind)) classKind = "";

                    String classTeacher = tds.get(3).text();
                    if (StringUtils.isEmpty(classTeacher)) classTeacher = "";

                    String wj_Name = tds.get(4).text();
                    if (StringUtils.isEmpty(wj_Name)) wj_Name = "";

                    String wj_CaoZuo = tds.get(5).text();
                    if (StringUtils.isEmpty(wj_CaoZuo)) wj_CaoZuo = "";

                    final String wj_Url = Url.Yangtzeu_Base_Url + tds.get(5).select("td>a").attr("href");

                    class_name.setText(className);
                    wj_kind.setText("课程类型：" + classKind);
                    wj_name.setText("问卷名称：" + wj_Name);
                    wj_teacher.setText("课程老师：" + classTeacher);
                    wj_text.setText("操作：" + wj_CaoZuo);

                    if (wj_CaoZuo.equals("评教完成")) {
                        view.setBackgroundColor(Color.GREEN);
                        wj_text.setTextColor(Color.GREEN);

                        final String finalClassTeacher = classTeacher;
                        onClick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyUtils.getAlert(activity, "给" + finalClassTeacher + "老师评教已经完成", null).show();
                            }
                        });
                    } else {
                        view.setBackgroundColor(Color.RED);
                        wj_text.setTextColor(Color.RED);
                        onClick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MyUtils.openUrl(activity, wj_Url, true);
                            }
                        });
                    }
                }


            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });
    }
}
