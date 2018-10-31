package com.yangtzeu.http;

public interface OnResultStringListener {
    void onResponse(String response);
    void onFailure(String error);
}
