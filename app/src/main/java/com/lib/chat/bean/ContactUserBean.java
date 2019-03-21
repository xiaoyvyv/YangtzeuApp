package com.lib.chat.bean;


import android.annotation.SuppressLint;

import com.blankj.utilcode.util.TimeUtils;

import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;

@SuppressLint("SimpleDateFormat")
public class ContactUserBean {
    private String name;
    private String number;
    private String qq = "default_header";
    private String note = "这个人很懒，什么都没留下～";
    private String time;

    public ContactUserBean() {
        time = TimeUtils.getNowString(new SimpleDateFormat("yyyy-mm-dd"));
    }

    ContactUserBean(String number, String name, String qq, String note) {
        this.name = name;
        this.note = note;
        this.number = number;
        this.qq = qq;
        this.time = TimeUtils.getNowString(new SimpleDateFormat("yyyy-mm-dd"));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(@NonNull String number) {
        this.number = number;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
