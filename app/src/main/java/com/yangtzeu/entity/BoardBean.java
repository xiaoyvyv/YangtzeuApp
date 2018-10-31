package com.yangtzeu.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/2/26.
 * @author 王怀玉
 * @explain 留言板的实体类
 */

public class BoardBean {

    /**
     * code : 200
     * result : [{"id":"257","留言内容":"补考在哪里搞？找了半天，结果就一个通知，有个屁用。","班级":"经济11602","头像":"1223414335","昵称":"谢嘉伟","个签":"201603907","背景":"@ffffff∩http://","时间":"2018-09-06 03:44:13","回复":[{"id":"162","p_id":"257","回复内容":"那是教务处刚出来的栏目，给你一个通知就不错了","昵称":"王怀玉","时间":"2018-09-06 03:48:21"}]}]
     */

    private String code;
    private List<ResultBean> result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * id : 257
         * 留言内容 : 补考在哪里搞？找了半天，结果就一个通知，有个屁用。
         * 班级 : 经济11602
         * 头像 : 1223414335
         * 昵称 : 谢嘉伟
         * 个签 : 201603907
         * 背景 : @ffffff∩http://
         * 时间 : 2018-09-06 03:44:13
         * 回复 : [{"id":"162","p_id":"257","回复内容":"那是教务处刚出来的栏目，给你一个通知就不错了","昵称":"王怀玉","时间":"2018-09-06 03:48:21"}]
         */

        private String id;
        private String 留言内容;
        private String 班级;
        private String 头像;
        private String 昵称;
        private String 个签;
        private String 背景;
        private String 时间;
        private List<回复Bean> 回复;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String get留言内容() {
            return 留言内容;
        }

        public void set留言内容(String 留言内容) {
            this.留言内容 = 留言内容;
        }

        public String get班级() {
            return 班级;
        }

        public void set班级(String 班级) {
            this.班级 = 班级;
        }

        public String get头像() {
            return 头像;
        }

        public void set头像(String 头像) {
            this.头像 = 头像;
        }

        public String get昵称() {
            return 昵称;
        }

        public void set昵称(String 昵称) {
            this.昵称 = 昵称;
        }

        public String get个签() {
            return 个签;
        }

        public void set个签(String 个签) {
            this.个签 = 个签;
        }

        public String get背景() {
            return 背景;
        }

        public void set背景(String 背景) {
            this.背景 = 背景;
        }

        public String get时间() {
            return 时间;
        }

        public void set时间(String 时间) {
            this.时间 = 时间;
        }

        public List<回复Bean> get回复() {
            return 回复;
        }

        public void set回复(List<回复Bean> 回复) {
            this.回复 = 回复;
        }

        public static class 回复Bean {
            /**
             * id : 162
             * p_id : 257
             * 回复内容 : 那是教务处刚出来的栏目，给你一个通知就不错了
             * 昵称 : 王怀玉
             * 时间 : 2018-09-06 03:48:21
             */

            private String id;
            private String p_id;
            private String 回复内容;
            private String 昵称;
            private String 时间;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getP_id() {
                return p_id;
            }

            public void setP_id(String p_id) {
                this.p_id = p_id;
            }

            public String get回复内容() {
                return 回复内容;
            }

            public void set回复内容(String 回复内容) {
                this.回复内容 = 回复内容;
            }

            public String get昵称() {
                return 昵称;
            }

            public void set昵称(String 昵称) {
                this.昵称 = 昵称;
            }

            public String get时间() {
                return 时间;
            }

            public void set时间(String 时间) {
                this.时间 = 时间;
            }
        }
    }
}