package com.yangtzeu.presenter;

import android.app.Activity;
import android.content.DialogInterface;

import com.blankj.utilcode.util.TimeUtils;
import com.yangtzeu.model.SplashModel;
import com.yangtzeu.ui.view.SplashView;
import com.yangtzeu.utils.MyUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SplashPresenter {
    private SplashModel model;
    private SplashView view;
    private Activity activity;

    public SplashPresenter(Activity activity, SplashView view) {
        this.view = view;
        this.activity = activity;
        model = new SplashModel();
    }

    public void loadAdImage() {
        model.loadAdImage(activity, view);
    }

    public void loadPermission(final SplashModel.OnPermissionCallBack callBack) {
        // 获取当前小时
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        // 获取当前小时内分钟
        int min = cal.get(Calendar.MINUTE);
        //凌晨开始计时到当前的分钟数
        int minuteOfDay = hour * 60 + min;
        if (minuteOfDay < 200) {
            MyUtils.getAlert(activity, "已经凌晨过了，教务处系统在凌晨可能无法访问，你要继续操作吗？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    model.loadPermission(activity, callBack);
                }
            }).show();
        } else {
            model.loadPermission(activity, callBack);
        }
    }

    public void loadUser() {
        model.loadUser(activity, view);
    }

    public void checkCopyRight() {
        model.checkCopyRight(activity, view);
    }
}
