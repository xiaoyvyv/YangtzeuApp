package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.ChatPartView3;

public interface IChatPart3Model {
    void loadContacts(Activity activity, ChatPartView3 view);

    void addAdmin(Activity activity, ChatPartView3 view);
}
