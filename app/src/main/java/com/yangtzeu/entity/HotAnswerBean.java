package com.yangtzeu.entity;

import java.util.List;

public class HotAnswerBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 高等数学上册
         * message : 同济大学出版社（第七版）
         * url : http://101.132.108.0/yangtzeu/html/answer/answer_sx/gaoshu_1/
         * author : 小编
         */

        private String title;
        private String message;
        private String url;
        private String author;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
