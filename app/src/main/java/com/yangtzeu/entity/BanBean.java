package com.yangtzeu.entity;

import java.util.List;

public class BanBean {

    /**
     * info : 查询成功
     * data : [{"id":"2","name":"why","number":"201603246","time":"2018-10-06"}]
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
         * id : 2
         * name : why
         * number : 201603246
         * time : 2018-10-06
         */

        private String id;
        private String name;
        private String number;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
