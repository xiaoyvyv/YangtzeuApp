package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.ChatPartView2;

public interface IChatPart2Model {
    void loadContacts(Activity activity, ChatPartView2 view);

    void addAdmin(Activity activity, ChatPartView2 view);
}
