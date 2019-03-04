package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.ChatView;

public interface IChatModel {
    void createGroup(Activity activity, ChatView view);

    void loadYangtzeuGroupInfo(Activity activity, ChatView view);
}
