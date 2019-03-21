package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ad.OnAdListener;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lib.subutil.GsonUtils;
import com.lib.yun.Base64;
import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.xiaomi.ad.common.pojo.AdType;
import com.yangtzeu.R;
import com.yangtzeu.app.MyApplication;
import com.yangtzeu.entity.AnswerListBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.callback.StringCallBack;
import com.yangtzeu.model.imodel.IAnswerListModel;
import com.yangtzeu.ui.activity.WebActivity;
import com.yangtzeu.ui.view.AnswerListView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class AnswerListModel implements IAnswerListModel {
    private FrameLayout frameLayout;

    @Override
    public void loadAnswer(final Activity activity, final AnswerListView view) {
        Request request = OkHttp.getRequest(view.getFromUrl() + "api.php?key=" + getAnswerKey());
        OkHttp.getInstance().newCall(request).enqueue(new StringCallBack() {
            @Override
            public void onFinish(Call call, String response, boolean isResponseExist, boolean isCacheResponse) {
                LogUtils.e(response);
                AnswerListBean answerListBean = GsonUtils.fromJson(response, AnswerListBean.class);
                if (answerListBean.getCode() == 200) {
                    view.getContainer().removeAllViews();
                    final List<AnswerListBean.DataBean> data = answerListBean.getData();
                    for (int i = 0; i < data.size(); i++) {
                        @SuppressLint("InflateParams")
                        View item = LayoutInflater.from(activity).inflate(R.layout.activity_answer_list_item, null);
                        view.getContainer().addView(item);

                        ImageView image = item.findViewById(R.id.image);
                        TextView name = item.findViewById(R.id.name);
                        RelativeLayout onClick = item.findViewById(R.id.onClick);

                        MyUtils.loadImage(activity, image, Url.My_App_Home + data.get(i).getImage());
                        name.setText(data.get(i).getName());

                        final int finalI = i;
                        onClick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, WebActivity.class);
                                intent.putExtra("from_url", Url.My_App_Home + data.get(finalI).getUrl() + "?key=" + getAnswerKey());
                                intent.putExtra("isNoTitle", true);
                                intent.putExtra("isAnswer", true);
                                MyUtils.startActivity(intent);
                            }
                        });
                    }
                    view.getContainer().addView(loadBannerAd(activity, view), Integer.parseInt(MyUtils.getRand(data.size())));
                } else {
                    ToastUtils.showShort(R.string.try_again);
                }
            }
        });
    }

    private View loadBannerAd(Activity activity, AnswerListView view) {
        if (frameLayout != null) {
            return frameLayout;
        }
        frameLayout = new FrameLayout(activity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(54));
        frameLayout.setLayoutParams(params);

        try {
            view.setAdWorker(AdWorkerFactory.getAdWorker(activity, frameLayout, new OnAdListener() {
                @Override
                public void onResultListener(int state, Object info) {
                    LogUtils.i(state, info);
                    switch (state) {
                        case OnAdListener.AdDismissed:
                        case OnAdListener.AdFailed:
                            frameLayout.setVisibility(View.GONE);
                            break;
                    }

                }
            }, AdType.AD_BANNER));
            view.getAdWorker().loadAndShow(MyApplication.PORTRAIT_BANNER_POSITION_ID);
        } catch (Exception ignored) {}
        return frameLayout;
    }


    public static String getAnswerKey() {
        String base64_key;
        long nowMills = TimeUtils.getNowMills();
        String base64_nowMills = Base64.encodeToString(String.valueOf(nowMills).getBytes(), Base64.DEFAULT);
        String number = SPUtils.getInstance("user_info").getString("number");
        String base64_number = Base64.encodeToString(number.getBytes(), Base64.DEFAULT);
        String key = base64_nowMills + "#" + base64_number;
        base64_key = Base64.encodeToString(key.getBytes(), Base64.DEFAULT);
        return base64_key;
    }

}
