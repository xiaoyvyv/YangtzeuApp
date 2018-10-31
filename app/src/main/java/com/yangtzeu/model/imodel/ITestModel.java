package com.yangtzeu.model.imodel;


import android.app.Activity;

import com.yangtzeu.ui.view.TestView;

import org.jsoup.select.Elements;

public interface ITestModel {
    void getTestInfo(Activity activity, TestView view);

    void parseTestInfo(Activity activity, TestView view, Elements options) throws Exception;
}
