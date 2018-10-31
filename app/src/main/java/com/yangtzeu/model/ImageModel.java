package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.model.imodel.IImageModel;
import com.yangtzeu.ui.view.MyImageView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.Objects;

public class ImageModel implements IImageModel {

    @Override
    public void showDialog(final Activity activity, MyImageView mView, final String title, final Object object) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.activity_image_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.style_dialog)
                .setView(view)
                .create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


        TextView save = view.findViewById(R.id.save_img);
        TextView share = view.findViewById(R.id.share);
        TextView clear = view.findViewById(R.id.clear);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MyUtils.shareText(activity,object.toString()  + "\n\n数据来自：" + Url.AppDownUrl);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (object != null && URLUtil.isNetworkUrl(object.toString()) ) {
                    MyUtils.saveFile(activity, object.toString(), "A_Tool/Download/Image/", FileUtils.getFileName(object.toString()));
                } else {
                    ToastUtils.showShort(R.string.cant_save);
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        Window window = dialog.getWindow();
        Objects.requireNonNull(window).setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.BottomToBottom);  //添加动画
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }

}
