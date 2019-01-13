package com.yangtzeu.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.http.ProgressHelper;
import com.yangtzeu.http.ProgressRequestBody;
import com.yangtzeu.http.ProgressRequestListener;
import com.yangtzeu.listener.OnUpLoadListener;
import com.yangtzeu.url.Url;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UpLoadUtils {

    public static void upLoadFile(String path, String file_header, final OnUpLoadListener loadListener) {
        //压缩图片
        if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
            Bitmap bitmap = ImageUtils.bytes2Bitmap(FileIOUtils.readFile2BytesByStream(new File(path)));
            Bitmap bitmap1 = ImageUtils.compressBySampleSize(bitmap, 300, 500);
            String new_path = MyUtils.rootPath() + "A_Tool/Image/CP_" + FileUtils.getFileName(path);
            ImageUtils.save(bitmap1, new_path, Bitmap.CompressFormat.JPEG);
            path = new_path;
        }
        String key = file_header + FileUtils.getFileMD5ToString(path) + "." + FileUtils.getFileExtension(path);

        ALiOssUtils.upLoadFile(key, path, loadListener);

    }


    /*
    public static void upLoadFile(String path, String header, final OnUpLoadListener loadListener) {
        //压缩图片
        if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
            Bitmap bitmap = ImageUtils.bytes2Bitmap(FileIOUtils.readFile2BytesByStream(new File(path)));
            Bitmap bitmap1 = ImageUtils.compressBySampleSize(bitmap, 500, 500);
            String new_path = MyUtils.rootPath() + "A_Tool/Image/CP_" + FileUtils.getFileName(path);
            ImageUtils.save(bitmap1, new_path, Bitmap.CompressFormat.JPEG);
            path = new_path;
        }

        File file = new File(path);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("save_header", header)
                .addFormDataPart("img", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"),
                        FileIOUtils.readFile2BytesByStream(file)));

        //有多个图片就用for循环添加即可
        MultipartBody body = builder.build();

        ProgressRequestBody progressRequestBody = ProgressHelper.addProgressRequestListener(body, new ProgressRequestListener() {
            @Override
            public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                int progress = (int) ((100 * bytesWritten) / contentLength);
                LogUtils.e("进度："+progress);
                loadListener.onUploading(progress);
            }
        });


        Request request = new Request.Builder()
                .url(Url.Url_Upload)
                .post(progressRequestBody)
                .build();

        OkHttp.do_Post(request, new OnResultStringListener() {
            @Override
            public void onResponse(final String response) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MessageBean messageBean = GsonUtils.fromJson(response, MessageBean.class);
                        loadListener.onSuccess(messageBean.getInfo());
                    }
                }, 200);
            }

            @Override
            public void onFailure(String error) {
                loadListener.onFailure(error);
            }
        });
    }
    */
}
