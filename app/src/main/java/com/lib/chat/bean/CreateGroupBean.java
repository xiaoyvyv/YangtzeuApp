package com.lib.chat.bean;

import java.io.Serializable;
import java.util.List;

public class CreateGroupBean implements Serializable {
    /**
     * code : 200
     * message : success
     * data : {"appId":"2882303761517953315","topicInfo":{"topicId":"17621378744713216","topicName":"长大助手官方群","ownerUuid":"17521175009951744","ownerAccount":"201603246","bulletin":null,"extra":null},"members":[{"uuid":"17521175009951744","account":"201603246"}]}
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

    public static class DataBean implements Serializable {
        /**
         * appId : 2882303761517953315
         * topicInfo : {"topicId":"17621378744713216","topicName":"长大助手官方群","ownerUuid":"17521175009951744","ownerAccount":"201603246","bulletin":null,"extra":null}
         * members : [{"uuid":"17521175009951744","account":"201603246"}]
         */

        private String appId;
        private ContactGroupBean topicInfo;
        private List<MembersBean> members;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public ContactGroupBean getTopicInfo() {
            return topicInfo;
        }

        public void setTopicInfo(ContactGroupBean topicInfo) {
            this.topicInfo = topicInfo;
        }

        public List<MembersBean> getMembers() {
            return members;
        }

        public void setMembers(List<MembersBean> members) {
            this.members = members;
        }


        public static class MembersBean implements Serializable {
            /**
             * uuid : 17521175009951744
             * account : 201603246
             */

            private String uuid;
            private String account;

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }
        }
    }
}
