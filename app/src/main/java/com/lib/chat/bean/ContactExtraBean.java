package com.lib.chat.bean;

public class ContactExtraBean {
    /**
     * code : 200
     * message : success
     * data : {"userType":"USER","id":"$uuid1","name":"$appAccount1","timestamp":"$ts3","extra":"$extra","lastMessage":{"fromUuid":"$fromUuid3","fromAccount":"$fromAccount3","payload":"$payload3","sequence":"$sequence3","bizType":"$bizType"}}
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
         * userType : USER
         * id : $uuid1
         * name : $appAccount1
         * timestamp : $ts3
         * extra : $extra
         * lastMessage : {"fromUuid":"$fromUuid3","fromAccount":"$fromAccount3","payload":"$payload3","sequence":"$sequence3","bizType":"$bizType"}
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
             * fromUuid : $fromUuid3
             * fromAccount : $fromAccount3
             * payload : $payload3
             * sequence : $sequence3
             * bizType : $bizType
             */

            private String fromUuid;
            private String fromAccount;
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
