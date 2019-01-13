package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.NewsDetailsView;

public interface INewsDetailsModel {
    void loadData(Activity activity, NewsDetailsView view);
}
