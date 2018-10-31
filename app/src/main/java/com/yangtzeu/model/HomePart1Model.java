package com.yangtzeu.model;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IHomePart1Model;
import com.yangtzeu.ui.view.HomePartView1;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


public class HomePart1Model implements IHomePart1Model {


    @Override
    public void loadData(final Activity activity, final HomePartView1 view) {
        OkHttp.do_Get(Url.Yangtzeu_JWC, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                analysisData(activity,view,response);
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
                final String url = elements_next.get(j).select("li a").attr("href");
                String title = elements_next.get(j).select("li a").text();

                TextView fragment_item_time = item.findViewById(R.id.notice_time);
                TextView fragment_item_title = item.findViewById(R.id.notice_title);
                TextView fragment_item_kind = item.findViewById(R.id.notice_kind);
                fragment_item_time.setText(time);
                fragment_item_title.setText(title);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyUtils.openUrl(activity,url);
                    }
                });
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
}
