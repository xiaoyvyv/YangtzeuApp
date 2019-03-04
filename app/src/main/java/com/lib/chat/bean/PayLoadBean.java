package com.lib.chat.bean;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;

public class PayLoadBean {
    private int version = 0;
    private String msgId;
    private String number;
    private String name;
    private String text;
    private String qq;
    private String background;
    private long timestamp;

    public PayLoadBean(String msgId, String text, String background) {
        this.msgId = msgId;
        this.text = text;
        this.background = background;
        this.timestamp = TimeUtils.getNowMills();
        this.qq= SPUtils.getInstance("user_info").getString("qq");
        this.number = SPUtils.getInstance("user_info").getString("number");
        this.name = SPUtils.getInstance("user_info").getString("name");
    }

    PayLoadBean(String msgId, String number, String name, String text, String qq, String background) {
        this.msgId = msgId;
        this.number = number;
        this.name = name;
        this.text = text;
        this.qq = qq;
        this.background = background;
        this.timestamp = TimeUtils.getNowMills();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
