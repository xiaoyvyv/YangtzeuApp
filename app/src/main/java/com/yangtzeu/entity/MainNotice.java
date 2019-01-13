package com.yangtzeu.entity;

public class MainNotice {

    /**
     * version : 1
     * title : 软件通知公告
     * message : 软件通知公告，软件通知公告，软件通知公告，软件通知公告，软件通知公告
     * clickUrl :
     * canClose : true
     */

    private int version;
    private String title;
    private String message;
    private String clickUrl;
    private boolean canClose;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public boolean isCanClose() {
        return canClose;
    }

    public void setCanClose(boolean canClose) {
        this.canClose = canClose;
    }
}
