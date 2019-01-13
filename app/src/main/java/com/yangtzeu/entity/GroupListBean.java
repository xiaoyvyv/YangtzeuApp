package com.yangtzeu.entity;

import java.util.List;

public class GroupListBean {
    /**
     * info : 加载成功
     * data : [{"id":"10000005","name":"新长大助手官方群","desc":"反馈学习交流群，禁止灌水！","create":"2019-01-12 15:42:21"}]
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
         * id : 10000005
         * name : 新长大助手官方群
         * desc : 反馈学习交流群，禁止灌水！
         * create : 2019-01-12 15:42:21
         */

        private String id;
        private String name;
        private String desc;
        private String create;

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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCreate() {
            return create;
        }

        public void setCreate(String create) {
            this.create = create;
        }
    }
}
