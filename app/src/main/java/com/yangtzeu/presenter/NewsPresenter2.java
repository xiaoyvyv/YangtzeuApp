package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.yangtzeu.model.NewsModel1;
import com.yangtzeu.model.NewsModel2;
import com.yangtzeu.ui.view.NewsView1;
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
        model.loadNewsData(activity, view);
    }
}
