package com.yangtzeu.utils;

import android.graphics.Bitmap;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.yangtzeu.listener.OnUpLoadListener;

import java.io.File;

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
        String key = file_header + "/" + FileUtils.getFileMD5ToString(path) + "." + FileUtils.getFileExtension(path);

        ALiOssUtils.upLoadFile(key, path, loadListener);

    }

}
