package com.yangtzeu.utils;

import android.app.Activity;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.yangtzeu.listener.OnResultListener;

public class GoogleUtils {
    public static boolean DEBUG = true;
    private static InterstitialAd interstitialAd;
    private static RewardedVideoAd rewardedVideoAd;

    public static void init(Context context) {
        MobileAds.initialize(context, "ca-app-pub-4388416572498943~4994986559");
    }

    public static AdView newBannerView(Activity activity, AdSize adSize) {
        AdView adView = new AdView(activity);
        if (DEBUG) {
            adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        } else {
            adView.setAdUnitId("ca-app-pub-4388416572498943/5031479726");
        }
        adView.setAdSize(adSize);
        return adView;
    }

    public static AdRequest getRequest() {
        AdRequest.Builder build = new AdRequest.Builder();
        if (DEBUG) {
            build.addTestDevice("B72597D4041AEA5C937B5E25285F41C8");
        }
        return build.build();
    }

    /**
     * 加载插屏
     */
    public static void loadInterstitialAd() {
        interstitialAd = new InterstitialAd(Utils.getApp().getApplicationContext());
        if (DEBUG) {
            interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        } else {
            interstitialAd.setAdUnitId("ca-app-pub-4388416572498943/9517643359");
        }
        interstitialAd.loadAd(getRequest());
    }

    /**
     * 显示插屏
     *
     * @param listener 显示插屏回调
     */
    public static void showInterstitialAd(final OnResultListener<Boolean> listener) {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    listener.onResult(true);
                }

                @Override
                public void onAdClosed() {
                    listener.onResult(true);
                }
            });
            interstitialAd.show();
        } else {
            listener.onResult(true);
        }
    }


    /**
     * 加载激励视频广告
     */
    public static RewardedVideoAd loadRewardedVideoAd(RewardedVideoAdListener listener) {
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(Utils.getApp().getApplicationContext());
        rewardedVideoAd.setRewardedVideoAdListener(listener);
        if (DEBUG) {
            rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", GoogleUtils.getRequest());
        } else {
            rewardedVideoAd.loadAd("ca-app-pub-4388416572498943/2121411200", GoogleUtils.getRequest());
        }
        return rewardedVideoAd;
    }

    /**
     * 显示激励视频广告
     */
    public static boolean showRewardedVideoAd() {
        if (rewardedVideoAd != null && rewardedVideoAd.isLoaded()) {
            rewardedVideoAd.show();
            return true;
        } else {
            return false;
        }
    }


    public static class MyRewardedListener implements RewardedVideoAdListener {

        @Override
        public void onRewardedVideoAdLoaded() {

        }

        @Override
        public void onRewardedVideoAdOpened() {

        }

        @Override
        public void onRewardedVideoStarted() {

        }

        @Override
        public void onRewardedVideoAdClosed() {

        }

        @Override
        public void onRewarded(RewardItem rewardItem) {

        }

        @Override
        public void onRewardedVideoAdLeftApplication() {

        }

        @Override
        public void onRewardedVideoAdFailedToLoad(int i) {

        }

        @Override
        public void onRewardedVideoCompleted() {

        }
    }
}
