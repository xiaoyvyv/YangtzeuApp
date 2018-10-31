package com.yangtzeu.entity;

import java.io.Serializable;
import java.util.List;

public class TripBean implements Serializable{
    /**
     * version : 1.0
     * data : [{"title":"","image":"http://"},{"title":"","image":"http://"}]
     */

    private String version;
    private List<DataBean> data;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * title :
         * image : http://
         */

        private String title;
        private String image;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
