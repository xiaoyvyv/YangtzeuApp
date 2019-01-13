package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.smtt.sdk.WebSettings;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.INewsDetailsModel;
import com.yangtzeu.ui.view.NewsDetailsView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NewsDetailsModel implements INewsDetailsModel {

    @Override
    public void loadData(final Activity activity, final NewsDetailsView view) {
        OkHttp.do_Get(view.getFromUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getLoadingView().stopAnim();
                view.getContainer().removeView(view.getLoadingView());
                Document document = Jsoup.parse(response);
                document.select("#arc_next").remove();
                String title = document.select("head > title").text();

                view.getToolbar().setTitle(title);

                if (view.getFromUrl().contains(Url.Yangtzeu_JWC)) {
                    Elements div = document.select("#arc_r > div:nth-child(2)");
                    String header = MyUtils.readAssetsFile(activity, "html/jwc.html");
                    header = header.replace("my_title", title);
                    String html = header.replace("replace", String.valueOf(div));
                    view.getWebView().loadDataWithBaseURL(Url.Yangtzeu_JWC, html, "text/html", "utf-8", null);
                } else if (view.getFromUrl().contains(Url.Yangtzeu_News)) {
                    Elements div = document.select("div.newsTwoTxt.test1");
                    String header = MyUtils.readAssetsFile(activity, "html/news.html");
                    header = header.replace("my_title", title);
                    String html = header.replace("replace", String.valueOf(div));

                    String mp4 = document.select(".v_news_content > p:nth-child(1) > script:nth-child(1)").attr("vurl");
                    LogUtils.e(mp4);
                    if (!StringUtils.isEmpty(mp4)) {
                        ToastUtils.showLong("播放视频");
                        MyUtils.openUrl(activity, Url.Yangtzeu_News + mp4);
                    }
                    view.getWebView().getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                    view.getWebView().loadDataWithBaseURL(Url.Yangtzeu_JWC, html, "text/html", "utf-8", null);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.try_again);
            }
        });

    }
}
