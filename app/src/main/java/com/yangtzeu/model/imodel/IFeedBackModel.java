package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.FeedBackView;

public interface IFeedBackModel {
    void sendSuggestion(Activity activity, FeedBackView view);

}
