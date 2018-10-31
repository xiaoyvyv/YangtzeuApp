package com.yangtzeu.entity;

import com.lib.mob.chat.ChatUser;

public class ChatBean {
    public static final int NOT_READ = 0;
    private ChatUser user;
    private String text;
    private String time;
    private int state;
    private int background;
    private String image;
    private boolean right;
    private int type;

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }


    public String getText() {
        return text;
    }

    public ChatUser getUser() {
        return user;
    }

    public String getTime() {
        return time;
    }

    public int getState() {
        return state;
    }

    public int getBackground() {
        return background;
    }

    public String getImage() {
        return image;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(ChatUser user) {
        this.user = user;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class Type {
        public static final int TEXT = 0;
    }

    public static class Builder{
        ChatBean chatBean;
        public Builder(){
            chatBean = new ChatBean();
        }

        public Builder setText(String text) {
            chatBean.setText(text);
            return this;
        }

        public Builder setUser(ChatUser user) {
            chatBean.setUser(user);
            return this;
        }

        public Builder setTime(String time) {
            chatBean.setTime(time);
            return this;
        }

        public Builder setState(int state) {
            chatBean.setState(state);
            return this;
        }

        public Builder setBackground(int background) {
            chatBean.setBackground(background);
            return this;
        }

        public Builder setImage(String image) {
            chatBean.setImage(image);
            return this;
        }

        public Builder setType(int type) {
            chatBean.setType(type);
            return this;
        }

        public Builder setRight(boolean isRight) {
            chatBean.setRight(isRight);
            return this;
        }

        public ChatBean build() {
            return chatBean;
        }

    }
}
