package com.lib.x5web;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.smtt.sdk.DownloadListener;
import com.yangtzeu.R;
import com.yangtzeu.ui.activity.DownloadActivity;
import com.yangtzeu.utils.DownloadUtils;
import com.yangtzeu.utils.MyUtils;

import java.net.URLDecoder;
import java.util.Objects;

/**
 * Created by Administrator on 2018/4/10.
 *
 * @author 王怀玉
 * @explain X5DownloadListener
 */

public class X5DownloadListener implements DownloadListener {
    private final ProgressDialog progressDialog;
    private Context context;
    private String FilePath;

    public X5DownloadListener(Context context) {
        this.context = context;

        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle(R.string.trip);
        progressDialog.setMessage(context.getString(R.string.download_ing));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCanceledOnTouchOutside(false);
    }
    private String fileName;
    @SuppressLint("SetTextI18n")
    @Override
    public void onDownloadStart(final String s, String s1, final String s2, String s3, long l) {
        // s 下载地址,s1 UA ,s2 文件名字 l文件大小
        String FileName = s2.substring(s2.lastIndexOf("=") + 1, s2.length());
        try {
            fileName = URLDecoder.decode(FileName, "utf-8");
            fileName = fileName.replace("\"", "");
        } catch (Exception ignored) {}

        if (fileName.isEmpty()) {
            fileName = s.substring(s.lastIndexOf("/") + 1, s.length());
        }
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.lastIndexOf("?"));
        }
        if (fileName.endsWith(".cn")) {
            fileName = fileName.replace(".cn", ".jpg");
        }
        @SuppressLint("InflateParams")
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.activity_web_down, null);
        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.style_dialog)
                .setView(view).create();
        dialog.show();

        TextView text = view.findViewById(R.id.text);
        TextView download = view.findViewById(R.id.download);
        text.setText(context.getString(R.string.file_name)+"：" + fileName + "\n\n"+context.getString(R.string.size)+"：" + MyUtils.FormatSize(context,String.valueOf(l)));

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String RandNum =EncryptUtils.encryptMD5ToString(MyUtils.getRand(10));
                RandNum = RandNum.substring(0, 5);
                RandNum = "文件_" + RandNum + "_";
                fileName = RandNum + fileName;

                //过SystemService 以获取 DownloadManager
                String SavePath = SPUtils.getInstance("app_info").getString("down_path", "A_Tool/Download/");
                MyUtils.createSDCardDir(SavePath);

                FilePath = Environment.getExternalStorageDirectory() + "/" + SavePath + fileName;

                progressDialog.show();

                //第二个是相对参数
                DownloadUtils.get().download(s, SavePath, fileName, new DownloadUtils.OnDownloadListener() {
                    @Override
                    public void onDownloadSuccess() {
                        progressDialog.dismiss();

                        AlertDialog dialog = new AlertDialog.Builder(context, R.style.style_dialog)
                                .setTitle(R.string.trip)
                                .setMessage(context.getString(R.string.download_dir)+"：\n\n" + FilePath)
                                .setNegativeButton(R.string.open_file, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MyUtils.openFile(context,FilePath);
                                    }
                                })
                                .setPositiveButton(R.string.download_manger, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(context, DownloadActivity.class);
                                        MyUtils.startActivity(intent);
                                    }
                                })
                                .create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                    }

                    @Override
                    public void onDownloading(int progress) {
                        progressDialog.setProgress(progress);
                        progressDialog.show();
                    }

                    @Override
                    public void onDownloadFailed(String error) {
                        progressDialog.dismiss();
                        ToastUtils.showShort(R.string.download_error);
                    }
                });
            }
        });


        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        Objects.requireNonNull(window).setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setWindowAnimations(R.style.BottomToBottom);  //添加动画
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth(); //设置宽度
        dialog.getWindow().setAttributes(lp);
    }
}