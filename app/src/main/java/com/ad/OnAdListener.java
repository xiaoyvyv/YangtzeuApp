package com.ad;

import com.miui.zeus.mimo.sdk.listener.MimoAdListener;

public abstract class OnAdListener implements MimoAdListener {
    public final static int AdPresent = 0;
    public final static int AdClick = 1;
    public final static int AdDismissed = 2;
    public final static int AdFailed = 3;
    public final static int AdLoaded = 4;
    public final static int StimulateSuccess = 5;

    public abstract void onResultListener(int state, Object info);

    @Override
    public void onAdPresent() {
        onResultListener(AdPresent, "onAdPresent");
    }

    @Override
    public void onAdClick() {
        onResultListener(AdClick, "onAdClick");
    }

    @Override
    public void onAdDismissed() {
        onResultListener(AdDismissed, "onAdDismissed");
    }

    @Override
    public void onAdFailed(String s) {
        onResultListener(AdFailed, s);
    }

    @Override
    public void onAdLoaded(int i) {
        onResultListener(AdLoaded, i);
    }

    @Override
    public void onStimulateSuccess() {
        onResultListener(StimulateSuccess, "onStimulateSuccess");
    }
}
