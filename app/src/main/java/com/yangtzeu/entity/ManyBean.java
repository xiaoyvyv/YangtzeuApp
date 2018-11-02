package com.yangtzeu.entity;


import java.util.ArrayList;
import java.util.List;

public class ManyBean {

    /**
     * info : 加载成功
     * data : [{"title":"科学上网","url":"https://g.vvvip.top/","icon":""},{"title":"科学上网","url":"https://g.vvvip.top/","icon":""}]
     */

    private String info;
    private List<DataBean> data= new ArrayList<>();

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 科学上网
         * url : https://g.vvvip.top/
         * icon :
         */

        private String title;
        private String url;
        private String icon;

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

        public String getIcon() {
            return icon;
        }


        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
