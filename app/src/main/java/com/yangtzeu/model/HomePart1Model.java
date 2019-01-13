package com.yangtzeu.model;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.MainNotice;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IHomePart1Model;
import com.yangtzeu.ui.activity.NewsDetailsActivity;
import com.yangtzeu.ui.view.HomePartView1;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class HomePart1Model implements IHomePart1Model {


    @Override
    public void loadData(final Activity activity, final HomePartView1 view) {
        OkHttp.do_Get(Url.Yangtzeu_JWC, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                analysisData(activity, view, response);
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }

    @Override
    public void analysisData(final Activity activity, HomePartView1 view, String data) {
        View rootView = view.getRootView();
        Document document = Jsoup.parse(data);
        Elements elements = document.select("div.f_div");
        LinearLayout container1 = rootView.findViewById(R.id.jwc_jwtz);
        LinearLayout container2 = rootView.findViewById(R.id.jwc_bzsw);
        LinearLayout container3 = rootView.findViewById(R.id.jwc_jwdt);
        LinearLayout container4 = rootView.findViewById(R.id.jwc_jxjb);

        container1.removeAllViews();
        container2.removeAllViews();
        container3.removeAllViews();
        container4.removeAllViews();

        for (int i = 0; i < elements.size(); i++) {
            Elements elements_next = elements.get(i).select("div ul li");

            for (int j = 0; j < elements_next.size() - 1; j++) {
                View item = View.inflate(activity, R.layout.fragment_home_part1_item, null);
                item.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.alpha_to1_750));

                String time = elements_next.get(j).select("li span").text();
                final String url = Url.Yangtzeu_JWC + elements_next.get(j).select("li a").attr("href");
                String title = elements_next.get(j).select("li a").text();

                TextView fragment_item_time = item.findViewById(R.id.notice_time);
                TextView fragment_item_title = item.findViewById(R.id.notice_title);
                TextView fragment_item_kind = item.findViewById(R.id.notice_kind);
                View fragment_item_view = item.findViewById(R.id.view);

                fragment_item_time.setText(time);
                fragment_item_title.setText(title);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (url.contains(Url.Yangtzeu_JWC) || url.contains(Url.Yangtzeu_News)) {
                            Intent intent = new Intent(activity, NewsDetailsActivity.class);
                            intent.putExtra("from_url", url);
                            MyUtils.startActivity(intent);
                        } else {
                            MyUtils.openUrl(activity, url);
                        }
                    }
                });

                //最后一条间隔线取消
                if (j == elements_next.size() - 2) {
                    fragment_item_view.setBackgroundColor(Color.TRANSPARENT);
                }
                switch (i) {
                    case 0:
                        fragment_item_kind.setText(activity.getResources().getString(R.string.jwc_notice));
                        container1.addView(item);
                        break;
                    case 1:
                        fragment_item_kind.setText(activity.getResources().getString(R.string.jwc_event));
                        container2.addView(item);
                        break;
                    case 2:
                        fragment_item_kind.setText(activity.getResources().getString(R.string.jwc_activity));
                        container3.addView(item);
                        break;
                    case 3:
                        fragment_item_kind.setText(activity.getResources().getString(R.string.jwc_news_paper));
                        container4.addView(item);
                        break;
                }

            }
        }
    }

    @Override
    public void loadNotice(final Activity activity, final HomePartView1 view) {
        YangtzeuUtils.getAlertNotice(activity);

        OkHttp.do_Get(Url.Yangtzeu_AppMainNotice, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                int old_version = SPUtils.getInstance("app_info").getInt("main_notice_version", 0);

                MainNotice mainNotice = GsonUtils.fromJson(response, MainNotice.class);

                final int version = mainNotice.getVersion();
                final String title = mainNotice.getTitle();
                final String message = mainNotice.getMessage();
                final String clickUrl = mainNotice.getClickUrl();
                final boolean canClose = mainNotice.isCanClose();
                view.getNoticeTitle().setText(title);
                view.getNoticeText().setText(message);
                view.getNoticeText().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!StringUtils.isEmpty(clickUrl)) {
                            MyUtils.openUrl(activity, clickUrl);
                        }
                    }
                });
                view.getNoticeText().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (canClose) {
                            SPUtils.getInstance("app_info").put("main_notice_version", version);
                            view.getNoticeLayout().setVisibility(View.GONE);
                        }
                        return true;
                    }
                });

                if (version > old_version || !canClose) {
                    view.getNoticeLayout().setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }
}
