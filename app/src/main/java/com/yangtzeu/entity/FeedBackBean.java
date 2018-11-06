package com.yangtzeu.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedBackBean {

    /**
     * info : 建议查询成功
     * data : [{"id":"12","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"测试","email":"1223414335@qq.com","time":"18-11-03 03:45:24 pm"},{"id":"11","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"你好","email":"1223414335@qq.com","time":"18-11-03 03:44:17 pm"},{"id":"10","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"你不发","email":"1223414335@qq.com","time":"18-11-03 03:34:21 pm"},{"id":"9","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"你好","email":"1223414335@qq.com","time":"18-11-03 03:29:20 pm"},{"id":"8","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"CC","email":"1223414335@qq.com","time":"18-11-03 03:27:29 pm"},{"id":"7","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"CC","email":"1223414335@qq.com","time":"18-11-03 03:26:02 pm"},{"id":"6","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"1223414335","email":"1223414335@qq.com","time":"18-11-03 03:24:37 pm"},{"id":"5","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"能够","email":"1223414335@qq.com","time":"18-11-03 03:20:24 pm"},{"id":"4","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"能够","email":"1223414335@qq.com","time":"18-11-03 03:20:23 pm"},{"id":"3","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"那你说你是","email":"1223414335@qq.com","time":"18-11-03 03:14:24 pm"},{"id":"2","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"你好","email":"1223414335@qq.com","time":"18-11-03 03:12:57 pm"},{"id":"1","class":"机械11606","master":"王怀玉","master_id":"201603246","content":"CC","email":"1223414335@qq.com","time":"18-11-03 03:03:57 pm"}]
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
         * id : 12
         * class : 机械11606
         * master : 王怀玉
         * master_id : 201603246
         * content : 测试
         * email : 1223414335@qq.com
         * time : 18-11-03 03:45:24 pm
         */

        private String id;
        @SerializedName("class")
        private String classX;
        private String master;
        private String master_id;
        private String content;
        private String email;
        private String time;
        private String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public String getMaster() {
            return master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public String getMaster_id() {
            return master_id;
        }

        public void setMaster_id(String master_id) {
            this.master_id = master_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
