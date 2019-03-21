package com.yangtzeu.entity;

import java.util.List;

public class AnswerListBean {
    /**
     * code : 200
     * message : 成功
     * data : [{"name":"新视野大学英语读写教程第一册答案(第三版)","url":"/yangtzeu/html/answer/answer_yy/duxie_1","image":"/yangtzeu/html/answer/answer_yy/duxie_1.jpg"},{"name":"新视野大学英语读写教程第二册答案(第三版)","url":"/yangtzeu/html/answer/answer_yy/duxie_2","image":"/yangtzeu/html/answer/answer_yy/duxie_2.jpg"},{"name":"新视野大学英语读写教程第三册答案(第三版)","url":"/yangtzeu/html/answer/answer_yy/duxie_3","image":"/yangtzeu/html/answer/answer_yy/duxie_3.jpg"},{"name":"新视野大学英语读写教程第四册答案(第三版)","url":"/yangtzeu/html/answer/answer_yy/duxie_4","image":"/yangtzeu/html/answer/answer_yy/duxie_4.jpg"},{"name":"新视野大学英语视听说教程第一册答案","url":"/yangtzeu/html/answer/answer_yy/shitinshuo_1","image":"/yangtzeu/html/answer/answer_yy/shitinshuo_1.jpg"},{"name":"新视野大学英语视听说教程第二册答案","url":"/yangtzeu/html/answer/answer_yy/shitinshuo_2","image":"/yangtzeu/html/answer/answer_yy/shitinshuo_2.jpg"},{"name":"新视野大学英语视听说教程第三册答案","url":"/yangtzeu/html/answer/answer_yy/shitinshuo_3","image":"/yangtzeu/html/answer/answer_yy/shitinshuo_3.jpg"},{"name":"新视野大学英语视听说教程第四册答案","url":"/yangtzeu/html/answer/answer_yy/shitinshuo_4","image":"/yangtzeu/html/answer/answer_yy/shitinshuo_4.jpg"},{"name":"大学英语 听说（学生用书）第一册","url":"/yangtzeu/html/answer/answer_yy/english_tingshuo_1","image":"/yangtzeu/html/answer/answer_yy/english_tingshuo_1.jpg"},{"name":"大学英语 听说（学生用书）第二册","url":"/yangtzeu/html/answer/answer_yy/english_tingshuo_2","image":"/yangtzeu/html/answer/answer_yy/english_tingshuo_2.jpg"},{"name":"大学英语 听说（学生用书）第三册","url":"/yangtzeu/html/answer/answer_yy/english_tingshuo_3","image":"/yangtzeu/html/answer/answer_yy/english_tingshuo_3.jpg"}]
     */

    private int code;
    private String message;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 新视野大学英语读写教程第一册答案(第三版)
         * url : /yangtzeu/html/answer/answer_yy/duxie_1
         * image : /yangtzeu/html/answer/answer_yy/duxie_1.jpg
         */

        private String name;
        private String url;
        private String image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
