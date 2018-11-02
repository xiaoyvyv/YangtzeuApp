package com.yangtzeu.entity;

import java.util.ArrayList;
import java.util.List;

public class AdBean {

    private List<ResultBean> result = new ArrayList<>();

    public AdBean() {
        result.add(new ResultBean());
    }


    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * title : 广告招租
         * url : http://p04pfl7p6.bkt.clouddn.com/App/Me_bg4.jpg
         * imageurl : http://p04pfl7p6.bkt.clouddn.com/App/Me_bg4.jpg
         */

        private String title;
        private String url;
        private String imageurl;

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

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }
    }
}
