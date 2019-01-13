package com.yangtzeu.model;

import android.app.Activity;
import android.webkit.URLUtil;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.NewsBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.INewsModel2;
import com.yangtzeu.ui.view.NewsView2;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NewsModel2 implements INewsModel2 {


    @Override
    public void loadNewsData(Activity activity, final NewsView2 view) {
        final int old_index = view.getNewsAdapter().getItemCount();
        OkHttp.do_Get(view.getUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getSmartRefreshLayout().finishLoadMore();
                view.getSmartRefreshLayout().finishRefresh();
                if (view.isRefresh()) {
                    view.getData().clear();
                    view.getNewsAdapter().clearData();
                }
                try {
                    Document document = Jsoup.parse(response);
                    Elements li = document.select("div.newsTwoTxt ul").get(0).select("ul li");


                    String now_page = document.select("span.p_no_d").text();
                    //如果当前页面为第一页，则从0开始显示
                    if (now_page.equals("1")) {
                        view.setStartIndex(0);
                    }

                    for (int i = view.getStartIndex(); i < view.getStartIndex() + 30 && i < li.size(); i++) {
                        String title = li.get(i).select("li a").text();
                        String url = li.get(i).select("li a").attr("href");
                        if (!URLUtil.isNetworkUrl(url)) {
                            url = "http://news.yangtzeu.edu.cn/" + url;
                        }

                        String time = li.get(i).select("li span").text();

                        NewsBean bean = new NewsBean();
                        bean.setTilte(title);
                        bean.setKind(view.getKind());
                        bean.setUrl(url);
                        bean.setTime(time);
                        view.getData().add(bean);
                    }
                    view.getNewsAdapter().setDate(view.getData());
                    view.getNewsAdapter().notifyItemRangeChanged(old_index, view.getNewsAdapter().getItemCount());

                } catch (Exception e) {
                    ToastUtils.showShort(R.string.no_more);
                }
            }

            @Override
            public void onFailure(String error) {
                view.getSmartRefreshLayout().finishLoadMore();
                view.getSmartRefreshLayout().finishRefresh();
                LogUtils.e(error);
            }
        });

    }

}
