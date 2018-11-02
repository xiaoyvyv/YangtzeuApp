package com.yangtzeu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageBean implements Serializable {
    private List<ImageItem> imageItems= new ArrayList<>();

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    public static class ImageItem implements Serializable {
        private String title;
        private Object object;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

    public static ImageBean getImageBean(String[] trips,String[] imageObject) {
        ImageBean imageBean = new ImageBean();
        List<ImageItem> list = new ArrayList<>();
        for (int i = 0; i < imageObject.length; i++) {
            ImageItem imageItem = new ImageItem();
            imageItem.setObject(imageObject[i]);
            imageItem.setTitle(trips[i]);

            list.add(imageItem);
        }
        imageBean.setImageItems(list);
        return imageBean;
    }

    public static String[] toStringArray(String... string) {
        String[] str = new String[string.length];
        System.arraycopy(string, 0, str, 0, string.length);
        return str;
    }

}