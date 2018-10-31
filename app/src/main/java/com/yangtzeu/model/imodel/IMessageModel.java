package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.MessageView;

public interface IMessageModel {
    void loadMessageData(Activity activity, MessageView view);
}
