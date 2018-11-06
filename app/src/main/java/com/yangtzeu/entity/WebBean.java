package com.yangtzeu.entity;

import java.util.List;

public class WebBean {
    private List<WebListBean> webList;

    public List<WebListBean> getWebList() {
        return webList;
    }

    public void setWebList(List<WebListBean> webList) {
        this.webList = webList;
    }

    public static class WebListBean {
        /**
         * title : 长江大学官网
         * url : http://www.yangtzeu.edu.cn/
         */

        private String title;
        private String url;

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
    }
}
