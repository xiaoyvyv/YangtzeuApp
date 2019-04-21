package com.yangtzeu.entity;

public class LevelBean {

    /**
     * msg : 学币入账成功：1个
     * code : 200
     * data : {"id":2,"size":221,"empirical":221,"uid":"201602810","level":"1","level_name":"Lv1：黑铁-学水"}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2
         * size : 221
         * empirical : 221
         * uid : 201602810
         * level : 1
         * level_name : Lv1：黑铁-学水
         */

        private int id;
        private int size;
        private int empirical;
        private String uid;
        private String level;
        private String level_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getEmpirical() {
            return empirical;
        }

        public void setEmpirical(int empirical) {
            this.empirical = empirical;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getLevel_name() {
            return level_name;
        }

        public void setLevel_name(String level_name) {
            this.level_name = level_name;
        }
    }
}
