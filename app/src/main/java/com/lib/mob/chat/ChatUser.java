package com.lib.mob.chat;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;


public class ChatUser  {
    private String id;
    private String name;
    private String icon;

    public ChatUser(String id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setIcon(@NonNull String bitmap) {
        icon = bitmap;
    }
}
