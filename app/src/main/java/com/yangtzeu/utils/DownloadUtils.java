package com.yangtzeu.utils;

import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/3/29.
 *
 * @author 王怀玉
 * @explain DownloadUtils
 */
public class DownloadUtils {
    private static DownloadUtils downloadUtils;
    private OkHttpClient okHttpClient;

    public static DownloadUtils get() {
        if (downloadUtils == null) {
            downloadUtils = new DownloadUtils();
        }
        return downloadUtils;
    }

    private DownloadUtils() {
        okHttpClient = new OkHttpClient.Builder().build();
    }

    /**
     * @param url 下载连接
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String saveDir, final String filename, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 下载失败
                        listener.onDownloadFailed(e.toString());
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len ;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = Objects.requireNonNull(response.body()).byteStream();
                    long total = Objects.requireNonNull(response.body()).contentLength();
                    File file = new File(savePath, filename);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        final int progress = (int) (sum * 1.0f / total * 100);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // 下载中
                                listener.onDownloading(progress);
                            }
                        });
                    }
                    fos.flush();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 下载完成
                            listener.onDownloadSuccess();
                        }
                    });
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 下载失败
                        listener.onDownloadFailed(e.toString());
                    }
                });
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException ignored) {}
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException ignored) {}
                }
            }
        });
    }

    /**
     * @param request 下载请求头
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void downloadWithRequest(final Request request, final String saveDir, final String filename, final OnDownloadListener listener) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 下载失败
                        listener.onDownloadFailed(e.toString());
                    }
                });
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len ;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = Objects.requireNonNull(response.body()).byteStream();
                    long total = Objects.requireNonNull(response.body()).contentLength();
                    File file = new File(savePath, filename);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        final int progress = (int) (sum * 1.0f / total * 100);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                // 下载中
                                listener.onDownloading(progress);
                            }
                        });
                    }
                    fos.flush();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 下载完成
                            listener.onDownloadSuccess();
                        }
                    });
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 下载失败
                            listener.onDownloadFailed(e.toString());
                        }
                    });
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException ignored) {}
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException ignored) {}
                }
            }
        });
    }
    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    private Handler mHandler = new Handler();

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(String error);
    }
}