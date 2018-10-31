package com.yangtzeu.entity;

import java.util.List;

public class MarqueeBean {

    /**
     * info : 成功
     * data : [{"info":"1.您好"},{"info":"2.我是小玉"},{"info":"3.欢迎你"},{"info":"4.请支持我一下下"},{"info":"5.非常感谢"},{"info":"6.爱你哦"}]
     */

    private String info;
    private List<DataBean> data;

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
         * info : 1.您好
         */

        private String info;
        private String url;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
