package com.yangtzeu.entity;

import java.util.ArrayList;
import java.util.List;

public class BannerBean {
    /**
     * version : 1.0
     * data : [{"title":"测试1","image":"http://img.ivsky.com/img/bizhi/co/201806/28/liuhaoran.jpg"},{"title":"测试2","image":"http://img.ivsky.com/img/tupian/co/201804/10/qingchen_de_lushui-018.jpg"},{"title":"测试3","image":"http://img.ivsky.com/img/bizhi/co/201806/28/wulei-001.jpg"}]
     */

    private String version;
    private List<DataBean> data= new ArrayList<>();

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 测试1
         * image : http://img.ivsky.com/img/bizhi/co/201806/28/liuhaoran.jpg
         * url : http://img.ivsky.com/img/bizhi/co/201806/28/liuhaoran.jpg
         */

        private String title;
        private String image;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
