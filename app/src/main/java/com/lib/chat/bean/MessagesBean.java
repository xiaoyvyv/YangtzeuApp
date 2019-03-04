package com.lib.chat.bean;


public class MessagesBean {
    /**
     * sequence : 155123027575068001
     * payload : "payload"
     * ts : 1551230275750
     * fromAccount : 201603250
     * toAccount : 201603246
     * bizType : TEXT
     * extra :
     */

    private String sequence;
    private String payload;
    private long ts;
    private String fromAccount;
    private String bizType;
    private String extra;
    //私聊记录特有
    private String toAccount;

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }


    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}