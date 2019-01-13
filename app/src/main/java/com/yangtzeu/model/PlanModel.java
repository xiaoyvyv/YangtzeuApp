package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.yun.StringUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IPlanModel;
import com.yangtzeu.ui.view.PlanView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import okhttp3.FormBody;
import okhttp3.Request;

public class PlanModel implements IPlanModel {
    private String plan_id;
    public static String plan_term = "1";

    @Override
    public void isPlanExist(final Activity activity, final PlanView view) {
        OkHttp.do_Get(Url.Yangtzeu_Personal_Plan, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                if (response.contains("还没有属于您的培养计划")) {
                    MyUtils.getAlert(activity, "还没有属于您的培养计划，点击确定去绑定你的专业培养计划", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showPlanList(activity, view);
                        }
                    }).show();
                } else {
                    loadPlan(activity, view);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(error);
            }
        });
    }


    @SuppressLint("InflateParams")
    @Override
    public void showPlanList(final Activity activity, final PlanView view) {
        View dialog_view = LayoutInflater.from(activity).inflate(R.layout.activity_plan_dialog, null);
        final RadioGroup container = dialog_view.findViewById(R.id.container);

        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(dialog_view)
                .setNeutralButton(R.string.clear, null)
                .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtils.isEmpty(plan_id)) {
                            ToastUtils.showShort("请先选择培养方案");
                            return;
                        }
                        bindPlan(activity, view);
                    }
                })
                .setNegativeButton("解绑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (StringUtils.isEmpty(plan_id)) {
                            ToastUtils.showShort("请先选择培养方案");
                            return;
                        }
                        unbindPlan(activity, view);
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        OkHttp.do_Get(Url.Yangtzeu_Major_Mode, new OnResultStringListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements trs = document.select("tbody#planList_data>tr");
                container.removeAllViews();
                for (int i = 0; i < trs.size(); i++) {
                    Elements tds = trs.get(i).select("tr td");

                    String title;
                    String id;
                    String isbind;

                    isbind = tds.get(7).text();
                    title = tds.get(4).text();
                    id = tds.get(0).select("td>input").attr("value");

                    if (StringUtils.isEmpty(title)) title = "--";
                    if (StringUtils.isEmpty(id)) id = "0";
                    if (StringUtils.isEmpty(isbind)) isbind = "--";

                    RadioButton button = new RadioButton(activity);
                    container.addView(button);
                    button.setTextSize(15);
                    button.setGravity(Gravity.CENTER);
                    button.setText(title + "-----" + isbind);

                    final String finalId = id;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            plan_id = finalId;
                        }
                    });

                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(error);
            }
        });

    }

    @Override
    public void unbindPlan(Activity activity, PlanView view) {
        FormBody formBody = new FormBody.Builder()
                .add("planId", plan_id)
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .url("http://jwc3.yangtzeu.edu.cn/eams/stdMajorPlan!unbind.action")
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                String info = document.select("div.actionMessage").text();
                ToastUtils.showShort(info);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(error);
            }
        });
    }

    @Override
    public void bindPlan(final Activity activity, final PlanView view) {
        FormBody formBody = new FormBody.Builder()
                .add("planId", plan_id)
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .url("http://jwc3.yangtzeu.edu.cn/eams/stdMajorPlan!binding.action")
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                String info = document.select("div.actionMessage").text();
                ToastUtils.showShort(info);
                loadPlan(activity, view);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(error);
            }
        });
    }

    @Override
    public void loadPlan(final Activity activity, final PlanView view) {
        final ProgressDialog alert = MyUtils.getProgressDialog(activity, activity.getString(R.string.loading));
        alert.show();

        OkHttp.do_Get(Url.Yangtzeu_Me_Mode_Details, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);

                Elements script = document.select("script");

                //如果没有培养计划就去绑定
                String actionMessage = document.select("div.actionMessage").text();
                if (actionMessage.contains("没有个人培养计划")) {
                    isPlanExist(activity, view);
                    alert.dismiss();
                    return;
                }

                String end_url;
                try {
                    end_url = RegexUtils.getMatches("myDraftPersonalPlan!info.action?(.*?)&style=Default&terms=", script.toString()).get(0);
                } catch (Exception e) {
                    end_url = "myDraftPersonalPlan!info.action?planId=0&style=Default&terms=";
                }
                String url = "http://jwc3.yangtzeu.edu.cn/eams/" + end_url+ plan_term;
                OkHttp.do_Get(url, new OnResultStringListener() {
                    @Override
                    public void onResponse(String response) {
                        alert.dismiss();
                        Document document = Jsoup.parse(response);
                        view.getToolbar().setTitle("第"+plan_term+"学期培养计划");
                        String plan = document.select("div#PrintA").toString();
                        String html = MyUtils.readAssetsFile(activity, "html/plan.html");
                        html = html.replace("replace", plan);
                        view.getWebView().loadDataWithBaseURL(Url.Yangtzeu_Base_Url, html, "text/html", "utf-8", null);

                    }

                    @Override
                    public void onFailure(String error) {
                        ToastUtils.showShort(error);
                        alert.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                alert.dismiss();
                ToastUtils.showShort(error);
            }
        });

    }

}
