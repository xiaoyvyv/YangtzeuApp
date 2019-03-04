package com.yangtzeu.model;

import android.app.Activity;
import android.content.DialogInterface;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IPlanPart1Model;
import com.yangtzeu.ui.activity.PlanActivity;
import com.yangtzeu.ui.fragment.PlanPartFragment1;
import com.yangtzeu.ui.view.PlanPartView1;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class PlanPart1Model implements IPlanPart1Model {
    @Override
    public void loadPlan(final Activity activity, final PlanPartView1 view) {
        view.getWebView().loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        view.getWebView().clearHistory();
        OkHttp.do_Get(Url.Yangtzeu_Me_Mode_Details, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements script = document.select("script");
                //如果没有培养计划就去绑定
                String actionMessage = document.select("div.actionMessage").text();
                if (actionMessage.contains("没有个人培养计划")) {
                    MyUtils.getAlert(activity, "没有个人培养计划，请先绑定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (PlanActivity.tablayout != null) {
                                TabLayout.Tab tab = PlanActivity.tablayout.getTabAt(1);
                                if (tab != null) {
                                    tab.select();
                                }
                            }
                        }
                    }).show();
                    return;
                }

                String end_url;
                try {
                    end_url = RegexUtils.getMatches("myDraftPersonalPlan!info.action?(.*?)&style=Default&terms=", script.toString()).get(0);
                } catch (Exception e) {
                    ToastUtils.showShort("培养计划解析失败！");
                    return;
                }

                String url = "http://jwc3.yangtzeu.edu.cn/eams/" + end_url + PlanPartFragment1.plan_term;
                OkHttp.do_Get(url, new OnResultStringListener() {
                    @Override
                    public void onResponse(String response) {

                        Document document = Jsoup.parse(response);

                        String plan = document.select("div#PrintA").toString();
                        String html = MyUtils.readAssetsFile(activity, "html/plan.html");
                        html = html.replace("replace", plan);
                        view.getWebView().loadDataWithBaseURL(Url.Yangtzeu_Base_Url, html, "text/html", "utf-8", null);
                    }

                    @Override
                    public void onFailure(String error) {
                        ToastUtils.showShort(error);
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(error);
            }
        });
    }
}
