package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IPlanPart2Model;
import com.yangtzeu.ui.activity.PlanActivity;
import com.yangtzeu.ui.view.PlanPartView2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import okhttp3.FormBody;
import okhttp3.Request;

public class PlanPart2Model implements IPlanPart2Model {

    private static String plan_id;

    @Override
    public void showPlanList(final Activity activity, final PlanPartView2 view) {
        final LinearLayout container = view.getContainer();
        OkHttp.do_Get(view.getMajorModeUrl(), new OnResultStringListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                view.getRefresh().finishRefresh();
                Document document = Jsoup.parse(response);
                Elements trs = document.select("tbody#planList_data>tr");
                container.removeAllViews();
                for (int i = 0; i < trs.size(); i++) {
                    Elements tds = trs.get(i).select("tr td");

                    String title;
                    String id;
                    String bind_info;

                    bind_info = tds.get(7).text();
                    title = tds.get(4).text();
                    id = tds.get(0).select("td>input").attr("value");

                    if (StringUtils.isEmpty(title)) title = "--";
                    if (StringUtils.isEmpty(id)) id = "0";
                    if (StringUtils.isEmpty(bind_info)) bind_info = "--";

                    @SuppressLint("InflateParams")
                    View dialog_view = LayoutInflater.from(activity).inflate(R.layout.view_plan_item, null);
                    Button bt_unbind = dialog_view.findViewById(R.id.bt_unbind);
                    Button bt_bind = dialog_view.findViewById(R.id.bt_bind);
                    TextView text = dialog_view.findViewById(R.id.text);

                    container.addView(dialog_view);
                    text.setText(title);

                    final String finalId = id;
                    if (bind_info.contains("已绑定")) {
                        bt_unbind.setVisibility(View.VISIBLE);
                        bt_bind.setVisibility(View.GONE);
                        bt_unbind.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                plan_id = finalId;
                                unbindPlan(activity, view);
                            }
                        });

                    } else {
                        bt_bind.setVisibility(View.VISIBLE);
                        bt_unbind.setVisibility(View.GONE);

                        bt_bind.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                plan_id = finalId;
                                bindPlan(activity, view);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                view.getRefresh().finishRefresh();
                ToastUtils.showShort(error);
            }
        });

    }

    @Override
    public void unbindPlan(final Activity activity, final PlanPartView2 view) {
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
    public void bindPlan(final Activity activity, final PlanPartView2 view) {
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

                if (PlanActivity.tablayout != null) {
                    TabLayout.Tab tab = PlanActivity.tablayout.getTabAt(0);
                    if (tab != null) {
                        tab.select();
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(error);
            }
        });
    }
}
