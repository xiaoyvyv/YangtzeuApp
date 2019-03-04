package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.ChatPartView1;

public interface IChatPart1Model {
    void setMessageListener(Activity activity, ChatPartView1 view);

    void loadContactHistory(Activity activity, ChatPartView1 view);
}
