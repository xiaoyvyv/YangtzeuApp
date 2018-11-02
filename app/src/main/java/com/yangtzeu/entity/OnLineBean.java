package com.yangtzeu.entity;

import java.util.ArrayList;
import java.util.List;

public class OnLineBean {

    /**
     * info : 在线人数查询成功
     * size : 3
     * data : [{"id":"13","name":"王怀玉","number":"201603248","time":"1539789077"}]
     */

    private String info;
    private int size;
    private List<DataBean> data= new ArrayList<>();

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 13
         * name : 王怀玉
         * number : 201603248
         * time : 1539789077
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
