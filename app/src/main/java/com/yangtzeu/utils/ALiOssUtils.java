package com.yangtzeu.utils;


import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.yangtzeu.listener.OnUpLoadListener;
import com.yangtzeu.url.Url;

import java.io.File;

import androidx.annotation.RequiresApi;

public class ALiOssUtils {
    private static Handler handler;
    private static OSS oss;
    private static final String bucketName = "whysroom";
    private static final String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    private static final String urlHeader = "https://whysroom.oss-cn-beijing.aliyuncs.com/";

    public static void initOSSClient(Context context) {
        handler = new Handler(context.getMainLooper());
        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(Url.Yangtzeu_App_STS);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(8); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        oss = new OSSClient(context, endpoint, credentialProvider, conf);
    }


    public static OSSAsyncTask upLoadFile(String key, File file, OnUpLoadListener resultListener) {
        if (ObjectUtils.isEmpty(key)) {
            resultListener.onFailure("未设置文件的key");
            return null;
        }

        if (ObjectUtils.isEmpty(file)) {
            resultListener.onFailure("未选择文件");
            return null;
        }
        return upLoadFile(key, file.getPath(), resultListener);
    }

    public static OSSAsyncTask upLoadFile(final String key, String file_path, final OnUpLoadListener loadListener) {
        if (ObjectUtils.isEmpty(key)) {
            loadListener.onFailure("未设置文件的key");
            LogUtils.e("未设置文件的key");
            return null;
        }
        if (ObjectUtils.isEmpty(new File(file_path)) || ObjectUtils.isEmpty(file_path)) {
            loadListener.onFailure("未选择文件");
            LogUtils.e("未选择文件");
            return null;
        }

        //下面3个参数依次为bucket名，Object名，上传文件路径
        PutObjectRequest put = new PutObjectRequest(bucketName, key, file_path);

        // 异步上传，可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                final int progress = (int) (currentSize / (totalSize * 1.0f) * 100);
                if (loadListener != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            loadListener.onUploading(progress);
                        }
                    });
                }
            }
        });
        LogUtils.e("开始上传");
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 上传成功
                        String url = urlHeader + key;
                        loadListener.onSuccess(url);
                        LogUtils.e("上传成功");
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, final ClientException clientException, final ServiceException serviceException) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // 请求异常
                        if (clientException != null) {
                            // 本地异常如网络异常等
                            loadListener.onFailure(clientException.getMessage());
                        }
                        if (serviceException != null) {
                            // 服务异常
                            loadListener.onFailure(serviceException.getMessage());
                        }
                        LogUtils.e("上传服务异常");
                    }
                });
            }
        });
        return ossAsyncTask;
    }

}
