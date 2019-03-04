package com.lib.chat.bean;

import java.util.List;

public class MessageHistoryBean {
    /**
     * code : 200
     * message : success
     * data : {"appId":2882303761517953315,"row":1,"timestamp":1551230275750,"messages":[{"sequence":"155123027575068001","payload":"eyJtc2dJZCI6IkFGN1QxLTAiLCJwYXlsb2FkIjpbLTI2LC0xMjAsLTExMSwtMjYsLTk5LC05MSwtMjQsLTEyMSwtODYsNTAsNDgsNDksNTQsNDgsNTAsNTYsNDksNDhdLCJ0aW1lc3RhbXAiOjE1NTEyMzAyNzUwMjQsInZlcnNpb24iOjB9","ts":1551230275750,"fromAccount":"201603250","toAccount":"201603246","bizType":"TEXT","extra":""}]}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * appId : 2882303761517953315
         * row : 1
         * timestamp : 1551230275750
         * messages : [{"sequence":"155123027575068001","payload":"eyJtc2dJZCI6IkFGN1QxLTAiLCJwYXlsb2FkIjpbLTI2LC0xMjAsLTExMSwtMjYsLTk5LC05MSwtMjQsLTEyMSwtODYsNTAsNDgsNDksNTQsNDgsNTAsNTYsNDksNDhdLCJ0aW1lc3RhbXAiOjE1NTEyMzAyNzUwMjQsInZlcnNpb24iOjB9","ts":1551230275750,"fromAccount":"201603250","toAccount":"201603246","bizType":"TEXT","extra":""}]
         */

        private long appId;
        private int row;
        private long timestamp;
        private List<MessagesBean> messages;

        //群聊记录特有
        private long topicId;

        public long getAppId() {
            return appId;
        }

        public void setAppId(long appId) {
            this.appId = appId;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public List<MessagesBean> getMessages() {
            return messages;
        }

        public void setMessages(List<MessagesBean> messages) {
            this.messages = messages;
        }

        public long getTopicId() {
            return topicId;
        }

        public void setTopicId(long topicId) {
            this.topicId = topicId;
        }
    }
}
