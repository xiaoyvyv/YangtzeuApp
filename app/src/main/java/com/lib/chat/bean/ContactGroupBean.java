package com.lib.chat.bean;


import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class ContactGroupBean {
    /**
     * topicId : 17621378744713216
     * topicName : 长大助手官方群
     * ownerUuid : 17521175009951744
     * ownerAccount : 201603246
     * bulletin : null
     * extra : null
     */

    private String topicId;
    private String topicName;
    private String ownerUuid;
    private String ownerAccount;
    private Object bulletin;
    private Object extra;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }

    public Object getBulletin() {
        return bulletin;
    }

    public void setBulletin(Object bulletin) {
        this.bulletin = bulletin;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
}