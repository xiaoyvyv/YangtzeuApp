package com.yangtzeu.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.SPUtils;


public class AppIconUtils {
    private PackageManager mPackageManager;
    //默认组件
    private ComponentName componentName0;
    private ComponentName componentName1;
    private ComponentName componentName2;

    /**
     * 设置默认图标生效
     */

    private void enableComponentName1() {
        enableComponent(componentName0);
        disableComponent(componentName1);
        disableComponent(componentName2);
    }

    /**
     * 设置第icon1图标生效
     */

    private void enableComponentName2() {
        disableComponent(componentName0);
        enableComponent(componentName1);
        disableComponent(componentName2);
    }

    /**
     * 设置第icon2图标生效
     */
    private void enableComponentName3() {
        disableComponent(componentName0);
        disableComponent(componentName1);
        enableComponent(componentName2);
    }

    /**
     * 启动组件
     *
     * @param componentName 组件名
     */
    private void enableComponent(ComponentName componentName) { //此方法用以启用和禁用组件，会覆盖Androidmanifest文件下定义的属性
        mPackageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * 禁用组件
     * <p>
     * *@param componentName 组件名
     */
    private void disableComponent(ComponentName componentName) {
        mPackageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    //最后调用
    public void pmTest(Activity context) {
        // 获取到包管理类实例
        mPackageManager = context.getPackageManager();
        // 根据全限定名创建一个组件，即activity-alias 节点下的name：HomeActivity2 对应的组件
        componentName0 = new ComponentName(context.getBaseContext(), "com.yangtzeu.HomeActivity0");
        componentName1 = new ComponentName(context.getBaseContext(), "com.yangtzeu.HomeActivity1");
        componentName2 = new ComponentName(context.getBaseContext(), "com.yangtzeu.HomeActivity2");

        String action = SPUtils.getInstance("app_info").getString("app_icon", "app_icon_0");

        //从后台获取到应该使用那一个组件，或者根据时间来判断
        if ("app_icon_1".equals(action)) {
            enableComponentName2();
        } else if ("app_icon_2".equals(action)) {
            enableComponentName3();
        } else {
            enableComponentName1();
        }
    }
}