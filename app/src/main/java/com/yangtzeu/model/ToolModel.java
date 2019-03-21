package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.FileBean;
import com.yangtzeu.model.imodel.IToolModel;
import com.yangtzeu.service.qq.QQService;
import com.yangtzeu.ui.view.ToolView;
import com.yangtzeu.utils.MyUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToolModel implements IToolModel {

    @Override
    public void setQQlikeEvent(final Activity activity, ToolView view) {
        if (MyUtils.isAccessibilitySettingsOn(activity, QQService.class)) {
            ToastUtils.showShort(R.string.please_open_friend);
            try {
                Intent intent = activity.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort(R.string.no_qq_app);
            }
        } else {
            ToastUtils.showShort(R.string.qq_like_permission_error);
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(R.string.trip)
                    .setMessage(R.string.need_accessibility)
                    .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                    })
                    .setNegativeButton(R.string.clear, null)
                    .create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void getCeHuiData(Activity activity, ToolView view) {
        String sdCardDir = MyUtils.createSDCardDir("A_Tool/QQ_Cache/");
        List<File> files = new ArrayList<>();
        MyUtils.listFilesInDir(files, sdCardDir);
        Collections.sort(files, new MyUtils.FileComparator2());
        List<FileBean> fileBeans = new ArrayList<>();
        int count = files.size();
        if (count > 100) {
            count = 100;
        }
        for (int i = 0; i < count; i++) {
            File file = files.get(i);
            FileBean fileBean = new FileBean();
            String name = FileUtils.getFileName(file);
            String path = file.getAbsolutePath();
            long time = file.lastModified();
            String size = FileUtils.getFileSize(file);
            String type = FileUtils.getFileExtension(file);

            fileBean.setName(name);
            fileBean.setPath(path);
            fileBean.setSize(size);
            fileBean.setType(type);
            fileBean.setTime(TimeUtils.millis2String(time));

            fileBeans.add(fileBean);
        }

        if (ObjectUtils.isEmpty(fileBeans)) {
            ToastUtils.showShort("未找到数据，请确保QQ图片监听正在运行");
            return;
        }
        view.getAdapter().setData(fileBeans);
        view.getAdapter().notifyItemRangeChanged(0, view.getAdapter().getItemCount());
    }
}
