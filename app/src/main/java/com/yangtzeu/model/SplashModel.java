package com.yangtzeu.model;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.AdBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.ISplashModel;
import com.yangtzeu.ui.activity.LoginActivity;
import com.yangtzeu.ui.activity.MainActivity;
import com.yangtzeu.ui.view.SplashView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.UserUtils;

import java.util.List;

public class SplashModel implements ISplashModel {
    private static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;//需要请求的权限
    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;//需要请求的权限
    private static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;//需要请求的权限
    private String[] permission = new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, READ_PHONE_STATE};
    private boolean[] canEnter = {false, false};

    @Override
    public void loadAdImage(final Activity activity, final SplashView view) {
        final String ad_url = SPUtils.getInstance("app_info").getString("ad_url");
        String ad_title = SPUtils.getInstance("app_info").getString("ad_title");
        final String ad_image = SPUtils.getInstance("app_info").getString("ad_image");
        Glide.with(activity).load(ad_image).into(view.getAdView());
        view.getAdView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtils.openBrowser(activity, ad_url);
            }
        });
        view.getAdTitle().setText(ad_title);
        OkHttp.do_Get(Url.Yangtzeu_AD, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                AdBean adBean = GsonUtils.fromJson(response, AdBean.class);
                String url = adBean.getResult().get(0).getUrl();
                SPUtils.getInstance("app_info").put("ad_url", url);

                String title = adBean.getResult().get(0).getTitle();
                SPUtils.getInstance("app_info").put("ad_title", title);

                String image = adBean.getResult().get(0).getImageurl();
                SPUtils.getInstance("app_info").put("ad_image", image);

                view.getAdTitle().setText(title);
                MyUtils.loadImage(activity, view.getAdView(), image);
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e("广告加载失败：" + error);
            }
        });

        new Handler(activity.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                canEnter[0] = true;
                checkCanIn();
            }
        }, 3000);
    }

    private void checkCanIn() {
        if (canEnter[0] && canEnter[1]) {
            ActivityUtils.finishAllActivities();
            MyUtils.startActivity(MainActivity.class);
        }
    }

    @Override
    public void loadPermission(Activity activity, final OnPermissionCallBack callBack) {
        boolean granted = PermissionUtils.isGranted(permission);
        if (granted) {
            callBack.OnGranted();
        } else {
            AlertDialog dialog = MyUtils.getAlert(activity, activity.getString(R.string.please_allow_sd_permission),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PermissionUtils.permission(permission)
                                    .callback(new PermissionUtils.FullCallback() {
                                        @Override
                                        public void onGranted(List<String> permissionsGranted) {
                                            callBack.OnGranted();
                                        }

                                        @Override
                                        public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                                            if (!permissionsDeniedForever.isEmpty()) {
                                                PermissionUtils.launchAppDetailsSettings();
                                                ToastUtils.showLong(R.string.please_allow_permission);
                                            }
                                        }
                                    }).request();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AppUtils.exitApp();
                        }
                    });
            dialog.show();
        }
    }

    @Override
    public void loadUser(Activity activity, SplashView view) {
        String number = SPUtils.getInstance("user_info").getString("number", "");
        String password = SPUtils.getInstance("user_info").getString("password", "");

        if (number.isEmpty() || password.isEmpty()) {
            new Handler(activity.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityUtils.finishAllActivities();
                    MyUtils.startActivity(LoginActivity.class);
                }
            }, 2000);
            return;
        }

        String[] user_info = {number, password};
        UserUtils.do_Login(activity, user_info, new UserUtils.OnLogResultListener() {
            @Override
            public void onSuccess(String result) {
                canEnter[1] = true;
                checkCanIn();
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(error);
                LogUtils.i(error);
                ActivityUtils.finishAllActivities();
                MyUtils.startActivity(LoginActivity.class);
            }
        });

    }

    public interface OnPermissionCallBack {
        void OnGranted();
    }
}
