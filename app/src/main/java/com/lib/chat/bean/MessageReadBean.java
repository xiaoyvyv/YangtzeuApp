package com.lib.chat.bean;

public class MessageReadBean {
    //统计未读消息的对象唯一id（私聊为对方id，群聊为群id）
    private long id;
    //未读数目
    private long unReadSize = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUnReadSize() {
        return unReadSize;
    }

    public void setUnReadSize(long unReadSize) {
        this.unReadSize = unReadSize;
    }

}
