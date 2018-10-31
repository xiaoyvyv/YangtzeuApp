package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.MyImageView;

public interface IImageModel {
    void showDialog(Activity activity, MyImageView view, String title, Object object);
}
