package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.model.SplashModel;
import com.yangtzeu.ui.view.SplashView;

public interface ISplashModel {
    void loadAdImage(Activity activity, SplashView view);


    void loadPermission(Activity activity, SplashModel.OnPermissionCallBack callBack);

    void loadUser(Activity activity, SplashView view);

    void checkCopyRight(Activity activity, SplashView view);

}
