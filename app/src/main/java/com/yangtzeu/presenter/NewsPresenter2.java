package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.NewsModel2;
import com.yangtzeu.ui.view.NewsView2;

public class NewsPresenter2 {
    private NewsModel2 model;
    private NewsView2 view;
    private Activity activity;

    public NewsPresenter2(Activity activity, NewsView2 view) {
        this.view = view;
        this.activity = activity;
        model = new NewsModel2();
    }

    public void loadNewsData() {

        //先获取条目起始的index
        OkHttp.do_Get(view.getUrlHeader() + "statlist.js", new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                try {
                    String s_rowCount =RegexUtils.getMatches("rowCount=(.*?);", response).get(0);
                    String s_pageCount = RegexUtils.getMatches("pageCount=(.*?);", response).get(0);
                    String s_allCount = RegexUtils.getMatches("totalPages=(.*?);", response).get(0);
                    int rowCount = Integer.parseInt(s_rowCount.substring(s_rowCount.indexOf("=")+1, s_rowCount.lastIndexOf(";")));
                    int pageCount = Integer.parseInt(s_pageCount.substring(s_pageCount.indexOf("=")+1, s_pageCount.lastIndexOf(";")));
                    int allCount = Integer.parseInt(s_allCount.substring(s_allCount.indexOf("=")+1, s_allCount.lastIndexOf(";")));

                    int start = (pageCount - rowCount % pageCount) % pageCount;
                    view.setStartIndex(start);

                    LogUtils.e(allCount, view.getAllPage());
                    if (view.getAllPage() == 0) {
                        view.setAllPage(allCount);
                        LogUtils.e(allCount);
                    }

                    model.loadNewsData(activity, view);
                } catch (NumberFormatException e) {
                    onFailure(e.toString());
                }
            }

            @Override
            public void onFailure(String error) {
                view.setStartIndex(0);

                model.loadNewsData(activity, view);
            }
        });
    }
}
