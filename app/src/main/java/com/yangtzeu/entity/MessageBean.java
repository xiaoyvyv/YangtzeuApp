package com.yangtzeu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageBean implements Serializable {

    /**
     * info : 查询成功
     * data : [{"id":"1","text":"内测版本开通啦","time":"2018-10-03","from":"admin","to":"all","read":"false","mobile":"15298246925"}]
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

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * text : 内测版本开通啦
         * time : 2018-10-03
         * from : admin
         * to : all
         * read : false
         * mobile : 15298246925
         */

        private String id;
        private String text;
        private String time;
        private String from;
        private String from_number;
        private String to;
        private String read;


        public String getFrom_number() {
            return from_number;
        }

        public void setFrom_number(String from_number) {
            this.from_number = from_number;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getRead() {
            return read;
        }

        public void setRead(String read) {
            this.read = read;
        }

    }
}
