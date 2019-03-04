package com.lib.chat.bean;

import java.util.List;

public class ContactBean {
    /**
     * code : 200
     * message : success
     * data : [{"userType":"USER","id":"17521175009951744","name":"201603246","timestamp":"1551245251665","extra":"","lastMessage":{"fromUuid":"17521175009951744","fromAccount":"201603246","toUuid":"17521175009951744","toAccount":"201603246","payload":"5oiR5p2l6IeqMjAxNjAyODEw\r\n","sequence":"155124525166568001","bizType":"TEXT"}},{"userType":"USER","id":"17521194337304576","name":"201602810","timestamp":"1551245249921","extra":"","lastMessage":{"fromUuid":"17521175009951744","fromAccount":"201603246","toUuid":"17521194337304576","toAccount":"201602810","payload":"5oiR5p2l6IeqMjAxNjAzMjQ2\r\n","sequence":"155124524992105001","bizType":"TEXT"}},{"userType":"USER","id":"17542664337752064","name":"201603250","timestamp":"1551230275750","extra":"","lastMessage":{"fromUuid":"17542664337752064","fromAccount":"201603250","toUuid":"17521175009951744","toAccount":"201603246","payload":"eyJtc2dJZCI6IkFGN1QxLTAiLCJwYXlsb2FkIjpbLTI2LC0xMjAsLTExMSwtMjYsLTk5LC05MSwt\r\nMjQsLTEyMSwtODYsNTAsNDgsNDksNTQsNDgsNTAsNTYsNDksNDhdLCJ0aW1lc3RhbXAiOjE1NTEy\r\nMzAyNzUwMjQsInZlcnNpb24iOjB9\r\n","sequence":"155123027575068001","bizType":"TEXT"}}]
     */

    private int code;
    private String message;
    private List<ContactData> data;

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

    public List<ContactData> getData() {
        return data;
    }

    public void setData(List<ContactData> data) {
        this.data = data;
    }

    public static class ContactData {

        /**
         * userType : USER
         * id : 17521175009951744
         * name : 201603246
         * timestamp : 1551245251665
         * extra :
         * lastMessage : {"fromUuid":"17521175009951744","fromAccount":"201603246","toUuid":"17521175009951744","toAccount":"201603246","payload":"5oiR5p2l6IeqMjAxNjAyODEw\r\n","sequence":"155124525166568001","bizType":"TEXT"}
         */

        private String userType;
        private String id;
        private String name;
        private String timestamp;
        private String extra;
        private LastMessageBean lastMessage;

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

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

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public LastMessageBean getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(LastMessageBean lastMessage) {
            this.lastMessage = lastMessage;
        }

        public static class LastMessageBean {
            /**
             * fromUuid : 17521175009951744
             * fromAccount : 201603246
             * toUuid : 17521175009951744
             * toAccount : 201603246
             * payload : 5oiR5p2l6IeqMjAxNjAyODEw
             * sequence : 155124525166568001
             * bizType : TEXT
             */

            private String fromUuid;
            private String fromAccount;
            private String toUuid;
            private String toAccount;
            private String payload;
            private String sequence;
            private String bizType;

            public String getFromUuid() {
                return fromUuid;
            }

            public void setFromUuid(String fromUuid) {
                this.fromUuid = fromUuid;
            }

            public String getFromAccount() {
                return fromAccount;
            }

            public void setFromAccount(String fromAccount) {
                this.fromAccount = fromAccount;
            }

            public String getToUuid() {
                return toUuid;
            }

            public void setToUuid(String toUuid) {
                this.toUuid = toUuid;
            }

            public String getToAccount() {
                return toAccount;
            }

            public void setToAccount(String toAccount) {
                this.toAccount = toAccount;
            }

            public String getPayload() {
                return payload;
            }

            public void setPayload(String payload) {
                this.payload = payload;
            }

            public String getSequence() {
                return sequence;
            }

            public void setSequence(String sequence) {
                this.sequence = sequence;
            }

            public String getBizType() {
                return bizType;
            }

            public void setBizType(String bizType) {
                this.bizType = bizType;
            }
        }
    }
}
