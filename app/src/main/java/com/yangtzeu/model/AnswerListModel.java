package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.lib.subutil.GsonUtils;
import com.lib.yun.Base64;
import com.yangtzeu.R;
import com.yangtzeu.entity.AnswerListBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.callback.StringCallBack;
import com.yangtzeu.model.imodel.IAnswerListModel;
import com.yangtzeu.ui.activity.AnswerWebActivity;
import com.yangtzeu.ui.view.AnswerListView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.MyUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

public class AnswerListModel implements IAnswerListModel {
    private String currUrl;

    public static String encodeKey() {
        String base64_key;
        long nowMills = TimeUtils.getNowMills();
        String base64_nowMills = Base64.encodeToString(String.valueOf(nowMills).getBytes(), Base64.DEFAULT);
        String number = SPUtils.getInstance("user_info").getString("number");
        String base64_number = Base64.encodeToString(number.getBytes(), Base64.DEFAULT);
        String key = base64_nowMills + "#" + base64_number;
        base64_key = Base64.encodeToString(key.getBytes(), Base64.DEFAULT);
        return base64_key;
    }

    public static String decodeAnswerKey(String key) {
        byte[] decode_bytes = Base64.decode(key, Base64.DEFAULT);
        String decode_str = new String(decode_bytes, StandardCharsets.UTF_8);
        String[] split = decode_str.split("#");
        String split_time = split[0];
        byte[] time_bytes = Base64.decode(split_time.getBytes(), Base64.DEFAULT);
        String decode_time = new String(time_bytes, StandardCharsets.UTF_8);
        long timeMill = Long.parseLong(decode_time);
        if (timeMill > 15000) {
            return null;
        } else {
            return split[1];
        }
    }

    @Override
    public void loadAnswer(final Activity activity, final AnswerListView view) {
        Request request = OkHttp.getRequest(view.getFromUrl() + "api.php?key=" + encodeKey());
        OkHttp.getInstance().newCall(request).enqueue(new StringCallBack() {
            @Override
            public void onFinish(Call call, String response, boolean isResponseExist, boolean isCacheResponse) {
                AnswerListBean answerListBean = GsonUtils.fromJson(response, AnswerListBean.class);
                if (answerListBean.getCode() == 200) {
                    view.getContainer().removeAllViews();
                    final List<AnswerListBean.DataBean> data = answerListBean.getData();
                    for (int i = 0; i < data.size(); i++) {
                        if (i % 4 == 0) {
                            @SuppressLint("InflateParams")
                            View item = LayoutInflater.from(activity).inflate(R.layout.view_ad_container, null);
                            AdView adView = GoogleUtils.newBannerView(activity, AdSize.LARGE_BANNER);
                            adView.loadAd(GoogleUtils.getRequest());
                            FrameLayout frameLayout = item.findViewById(R.id.cardView);

                            frameLayout.addView(adView);
                            view.getContainer().addView(item);
                        }
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
                                currUrl = Url.My_App_Home + data.get(finalI).getUrl() + "?key=" + encodeKey();


                                enterAnswer(activity);
                            }
                        });
                    }
                    loadInsertAd(activity);
                } else {
                    ToastUtils.showShort(R.string.try_again);
                }
            }
        });
    }

    private void loadInsertAd(Activity activity) {

    }

    private void enterAnswer(Activity activity) {
        Intent intent = new Intent(activity, AnswerWebActivity.class);
        intent.putExtra("from_url", currUrl);
        intent.putExtra("isNoTitle", true);
        intent.putExtra("isAnswer", true);
        MyUtils.startActivity(intent);

        loadInsertAd(activity);
    }

}
