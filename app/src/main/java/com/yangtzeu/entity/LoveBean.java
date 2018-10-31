package com.yangtzeu.entity;

import java.io.Serializable;
import java.util.List;

public class LoveBean implements Serializable {

    /**
     * info : 表白查询成功
     * data : [{"id":"1","master":"王怀玉","master_id":"201603246","qq":"1223414335","description":"兰兰，歪腻","time":"2018-10-10 15:45:25","image":"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=316756213,1796810314&fm=27&gp=0.jpg","music":"http://fs.open.kugou.com/10b470d5c55b340a6a02ee93db0fb557/5bcc7247/G114/M05/00/05/sg0DAFnM4WmAFSzEADLMglmDGiE068.mp3","hide":"false","master_ta":"吴兰玲","qq_ta":"2440888027","replay":[{"id":"1","love_id":"1","user_id":"201603246","user_name":"王怀玉","content":"超级爱你","time":"2018-06-25"}]}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * master : 王怀玉
         * master_id : 201603246
         * qq : 1223414335
         * description : 兰兰，歪腻
         * time : 2018-10-10 15:45:25
         * image : https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=316756213,1796810314&fm=27&gp=0.jpg
         * music : http://fs.open.kugou.com/10b470d5c55b340a6a02ee93db0fb557/5bcc7247/G114/M05/00/05/sg0DAFnM4WmAFSzEADLMglmDGiE068.mp3
         * hide : false
         * master_ta : 吴兰玲
         * qq_ta : 2440888027
         * replay : [{"id":"1","love_id":"1","user_id":"201603246","user_name":"王怀玉","content":"超级爱你","time":"2018-06-25"}]
         */

        private String id;
        private String master;
        private String master_id;
        private String qq;
        private String description;
        private String time;
        private String image;
        private String music;
        private String hide;
        private String master_ta;
        private String qq_ta;
        private List<ReplayBean> replay;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getMusic() {
            return music;
        }

        public void setMusic(String music) {
            this.music = music;
        }

        public String getHide() {
            return hide;
        }

        public void setHide(String hide) {
            this.hide = hide;
        }

        public String getMaster_ta() {
            return master_ta;
        }

        public void setMaster_ta(String master_ta) {
            this.master_ta = master_ta;
        }

        public String getQq_ta() {
            return qq_ta;
        }

        public void setQq_ta(String qq_ta) {
            this.qq_ta = qq_ta;
        }

        public List<ReplayBean> getReplay() {
            return replay;
        }

        public void setReplay(List<ReplayBean> replay) {
            this.replay = replay;
        }

        public static class ReplayBean implements Serializable {
            /**
             * id : 1
             * love_id : 1
             * user_id : 201603246
             * user_name : 王怀玉
             * content : 超级爱你
             * time : 2018-06-25
             */

            private String id;
            private String love_id;
            private String user_id;
            private String user_name;
            private String content;
            private String time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getLove_id() {
                return love_id;
            }

            public void setLove_id(String love_id) {
                this.love_id = love_id;
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

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
