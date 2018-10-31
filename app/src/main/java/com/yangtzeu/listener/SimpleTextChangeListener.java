package com.yangtzeu.listener;

import android.text.Editable;
import android.text.TextWatcher;

public class SimpleTextChangeListener implements TextWatcher {
    private OnResultListener<Editable> listener;
    public SimpleTextChangeListener(OnResultListener<Editable> listener) {
        this.listener = listener;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        listener.onResult(s);
    }
}
