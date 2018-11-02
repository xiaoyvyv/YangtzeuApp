package com.yangtzeu.entity;

import java.util.ArrayList;
import java.util.List;

public class AppBean {

    /**
     * size : 2
     * appList : [{"name":"新长大助手","icon":"","message":"成绩查询，选课，课表","url":"https://www.coolapk.com/apk/com.yangtzeu"},{"name":"新长大助手","icon":"","message":"成绩查询，选课，课表","url":"https://www.coolapk.com/apk/com.yangtzeu"}]
     */

    private int size;
    private List<AppListBean> appList= new ArrayList<>();

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<AppListBean> getAppList() {
        return appList;
    }

    public void setAppList(List<AppListBean> appList) {
        this.appList = appList;
    }

    public static class AppListBean {
        /**
         * name : 新长大助手
         * icon :
         * message : 成绩查询，选课，课表
         * url : https://www.coolapk.com/apk/com.yangtzeu
         */

        private String name;
        private String icon;
        private String message;
        private String url;
        private String author;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
