package com.yangtzeu.utils;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.receiver.PhonePermissionsReceiver;

/**
 * Created by Administrator on 2018/3/10.
 *
 */
public class DeviceUtils {
    @SuppressLint("StaticFieldLeak")
    private static DeviceUtils mDeviceUtils;

    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private Context mContext;

    public DeviceUtils(Context context){
        mContext=context;
        //获取设备管理服务
        devicePolicyManager=(DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //DeviceReceiver 继承自 DeviceAdminReceiver
        componentName=new ComponentName(context, PhonePermissionsReceiver.class);
    }

    public static DeviceUtils getInstance(Context context){
        if (mDeviceUtils ==null) {
            synchronized (DeviceUtils.class) {
                if (mDeviceUtils ==null) {
                    mDeviceUtils =new DeviceUtils(context);
                }
            }
        }
        return mDeviceUtils;
    }

    // 激活程序
    public void onActivate() {
        //判断是否激活  如果没有就启动激活设备
        if (!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, mContext.getString(R.string.permission_help));
            mContext.startActivity(intent);
        }else {
            ToastUtils.showShort(R.string.permission_ed);
        }
    }

    public boolean isActivate() {
        return devicePolicyManager.isAdminActive(componentName);
    }
    /**
     * 移除程序 如果不移除程序 APP无法被卸载
     */
    public void onRemoveActivate() {
        devicePolicyManager.removeActiveAdmin(componentName);

    }

    /**
     * 设置解锁方式 不需要激活就可以运行
     */
    public void startLockMethod() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
        mContext.startActivity(intent);
    }

    /**
     * 设置解锁方式
     */
    public void setLockMethod() {
        // PASSWORD_QUALITY_ALPHABETIC
        // 用户输入的密码必须要有字母（或者其他字符）。
        // PASSWORD_QUALITY_ALPHANUMERIC
        // 用户输入的密码必须要有字母和数字。
        // PASSWORD_QUALITY_NUMERIC
        // 用户输入的密码必须要有数字
        // PASSWORD_QUALITY_SOMETHING
        // 由设计人员决定的。
        // PASSWORD_QUALITY_UNSPECIFIED
        // 对密码没有要求。
        if (devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
            devicePolicyManager.setPasswordQuality(componentName,
                    DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
            mContext.startActivity(intent);
        }else {
            ToastUtils.showShort(R.string.please_permission);
        }
    }

    /**
     * 立刻锁屏
     */
    public void LockNow() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.lockNow();
        }else {
            ToastUtils.showShort(R.string.please_permission);
        }
    }

    /**
     * 设置多长时间后锁屏
     * @param time
     */
    public void LockByTime(long time) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.setMaximumTimeToLock(componentName, time);
        }else {
            ToastUtils.showShort(R.string.please_permission);
        }
    }

    /**
     * 恢复出厂设置
     */
    public void WipeData() {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
        }else {
            ToastUtils.showShort(R.string.please_permission);
        }
    }

    /**
     * 设置密码锁
     * @param password
     */
    public void setPassword(String password) {
        if (devicePolicyManager.isAdminActive(componentName)) {
            devicePolicyManager.resetPassword(password,
                    DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        }else {
            ToastUtils.showShort(R.string.please_permission);
        }
    }
}
