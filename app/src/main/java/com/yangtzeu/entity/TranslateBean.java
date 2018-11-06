package com.yangtzeu.entity;

public class TranslateBean {
    /**
     * data : I 'll eat first.
     * mp3 :  http://fanyi.baidu.com/gettts?lan=en&text=I+%27ll+eat+first.&spd=3&source=web
     */

    private String data;
    private String mp3;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }
}
