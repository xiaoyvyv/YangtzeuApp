package com.yangtzeu.listener;

public interface OnUpLoadListener {
    void onUploading(int progress);
    void onSuccess(String path);
    void onFailure(String error);
}
