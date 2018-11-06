package com.yangtzeu.entity;

public class WeiBoBean {
    /**
     * retCode : 200
     * retDesc : 成功
     * data : {"text":"笑疯！改编版《沙漠骆驼》，歌词什么还蛮搭的！😂\n\n\n\n\n\n","cover":"http://wx3.sinaimg.cn/large/0065K79ily8fwwd815ew8j30a00hoq4y.jpg","video":"http://f.us.sinaimg.cn/003Kld6glx07oWj1cOsU01040200hzRv0k010.mp4?label=mp4_hd&template=360x636.24.0&Expires=1541349729&ssig=RHLQaltdBR&KID=unistore,video"}
     * succ : true
     */

    private int retCode;
    private String retDesc;
    private DataBean data;
    private boolean succ;

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getRetDesc() {
        return retDesc;
    }

    public void setRetDesc(String retDesc) {
        this.retDesc = retDesc;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSucc() {
        return succ;
    }

    public void setSucc(boolean succ) {
        this.succ = succ;
    }

    public static class DataBean {
        /**
         * text : 笑疯！改编版《沙漠骆驼》，歌词什么还蛮搭的！😂






         * cover : http://wx3.sinaimg.cn/large/0065K79ily8fwwd815ew8j30a00hoq4y.jpg
         * video : http://f.us.sinaimg.cn/003Kld6glx07oWj1cOsU01040200hzRv0k010.mp4?label=mp4_hd&template=360x636.24.0&Expires=1541349729&ssig=RHLQaltdBR&KID=unistore,video
         */

        private String text;
        private String cover;
        private String video;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }
    }
}
