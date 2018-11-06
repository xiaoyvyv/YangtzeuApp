package com.yangtzeu.entity;

import com.blankj.utilcode.util.TimeUtils;

public class CollectionBean {
    private String title;
    private String url;
    private String time;
    private long id;

    public CollectionBean() {
        id = TimeUtils.getNowMills();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
