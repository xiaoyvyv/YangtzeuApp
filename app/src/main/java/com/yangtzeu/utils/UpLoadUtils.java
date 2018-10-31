package com.yangtzeu.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.lib.yun.Auth;
import com.qiniu.android.common.AutoZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.yangtzeu.listener.OnUpLoadListener;
import com.yangtzeu.url.Url;

import org.json.JSONObject;

import java.io.File;


public class UpLoadUtils {
    private static boolean cancel = false;

    public static void upLoadFile(String path, final OnUpLoadListener loadListener) {
        cancel = false;
        //压缩图片
        if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
            Bitmap bitmap = ImageUtils.bytes2Bitmap(FileIOUtils.readFile2BytesByStream(new File(path)));
            Bitmap bitmap1 = ImageUtils.compressBySampleSize(bitmap, 150, 300);
            String new_path = MyUtils.rootPath() + "A_Tool/Image/CP_" + FileUtils.getFileName(path);
            ImageUtils.save(bitmap1, new_path, Bitmap.CompressFormat.JPEG);
            path = new_path;
        }
        String token = Auth.create(Url.DefeatAK, Url.DefeatSK).uploadToken( Url.DefeatName);
        //设置服务器文件名
        String key = "shop_" + FileUtils.getFileName(path);
        Configuration config = new Configuration.Builder()
                .zone(AutoZone.autoZone)
                .build();
        UploadManager uploadManager = new UploadManager(config);

        UpCompletionHandler handler=new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info.isOK()) {
                    loadListener.onSuccess(Url.DefeatUrl + key);
                } else {
                    loadListener.onFailure(info.error);
                }
            }
        };

        UploadOptions options=new UploadOptions(null, null, false, new UpProgressHandler() {
            @Override
            public void progress(String key, double percent) {
                loadListener.onUploading((int) (percent * 100));
            }
        }, new UpCancellationSignal() {
            @Override
            public boolean isCancelled() {
                return cancel;
            }
        });

        uploadManager.put(path,key,token,handler,options);
    }

}
