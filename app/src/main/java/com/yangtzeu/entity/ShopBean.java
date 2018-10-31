package com.yangtzeu.entity;

import java.util.List;

public class ShopBean {


    /**
     * info : 商品查询成功
     * data : [{"id":"5","name":"一个超级好用的网球拍","description":"网球拍出售","master":"玉","master_id":"201603246","phone":"15298246925","price":"80","type":"新鲜","qq":"1223414335","wechat":"whysbelief","image":"https://img.alicdn.com/tfscom/TB1XedSp_qWBKNjSZFxXXcpLpXa.jpg_q90.jpg","time":"18-10-18 07:41:51 pm","replay":[{"id":"1","good_id":"5","user_id":"201603246","user_name":"王怀玉","content":"这个东西特别好"}]}]
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
         * id : 5
         * name : 一个超级好用的网球拍
         * description : 网球拍出售
         * master : 玉
         * master_id : 201603246
         * phone : 15298246925
         * price : 80
         * type : 新鲜
         * qq : 1223414335
         * wechat : whysbelief
         * image : https://img.alicdn.com/tfscom/TB1XedSp_qWBKNjSZFxXXcpLpXa.jpg_q90.jpg
         * time : 18-10-18 07:41:51 pm
         * replay : [{"id":"1","good_id":"5","user_id":"201603246","user_name":"王怀玉","content":"这个东西特别好"}]
         */

        private String id;
        private String name;
        private String description;
        private String master;
        private String master_id;
        private String phone;
        private String price;
        private String type;
        private String qq;
        private String wechat;
        private String image;
        private String time;
        private List<ReplayBean> replay;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<ReplayBean> getReplay() {
            return replay;
        }

        public void setReplay(List<ReplayBean> replay) {
            this.replay = replay;
        }

        public static class ReplayBean {
            /**
             * id : 1
             * good_id : 5
             * user_id : 201603246
             * user_name : 王怀玉
             * content : 这个东西特别好
             */

            private String id;
            private String good_id;
            private String user_id;
            private String user_name;
            private String content;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getGood_id() {
                return good_id;
            }

            public void setGood_id(String good_id) {
                this.good_id = good_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
