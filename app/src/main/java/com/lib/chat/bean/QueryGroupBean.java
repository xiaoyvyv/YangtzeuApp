package com.lib.chat.bean;

import java.util.List;

public class QueryGroupBean {
    /**
     * code : 200
     * message : success
     * data : [{"topicId":"17621378744713216","topicName":"长大助手官方群","ownerUuid":"17521175009951744","ownerAccount":"201603246","bulletin":null,"extra":null},{"topicId":"17621516447907840","topicName":"长大助手官方群","ownerUuid":"17521175009951744","ownerAccount":"201603246","bulletin":null,"extra":null},{"topicId":"17622370076852224","topicName":"长大助手官方群","ownerUuid":"17521175009951744","ownerAccount":"201603246","bulletin":null,"extra":null},{"topicId":"17622401211170816","topicName":"长大助手官方群","ownerUuid":"17521175009951744","ownerAccount":"201603246","bulletin":null,"extra":null}]
     */

    private int code;
    private String message;
    private List<ContactGroupBean> data;

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

    public List<ContactGroupBean> getData() {
        return data;
    }

    public void setData(List<ContactGroupBean> data) {
        this.data = data;
    }

}
