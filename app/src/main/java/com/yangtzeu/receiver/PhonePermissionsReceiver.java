package com.yangtzeu.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;


/**
 * Created by Administrator on 2018/3/10.
 *
 */

public class PhonePermissionsReceiver extends DeviceAdminReceiver {
    @Override
    public void onEnabled(Context context, Intent intent) {
        // 设备管理：可用
        ToastUtils.showShort(R.string.permission_success);
    }

    @Override
    public void onDisabled(final Context context, Intent intent) {
        // 设备管理：不可用
        ToastUtils.showShort(R.string.permission_clear);
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        super.onDisableRequested(context, intent);
           /* // 这里处理 不可编辑设备。这里可以造成死机状态
            Intent intent2 = new Intent(context, NoticeSetting.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
            context.stopService(intent);// 是否可以停止*/

        return "取消激活后，《远离手机》功能将无法使用！";
    }

    @Override
    public void onPasswordChanged(Context context, Intent intent) {
        // 设备管理：密码己经改变

    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        LogUtils.e("尝试解锁屏幕", "失败");
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        LogUtils.e("尝试解锁屏幕", "成功");
    }
}
