package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.BannerBean;
import com.yangtzeu.entity.HotAnswerBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IAnswerLayout1Model;
import com.yangtzeu.ui.activity.WebActivity;
import com.yangtzeu.ui.view.AnswerLayout1View;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.GoogleUtils;
import com.yangtzeu.utils.MyUtils;

import java.util.Collections;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

public class AnswerLayout1Model implements IAnswerLayout1Model {

    @Override
    public void loadBanner(final Activity activity, final AnswerLayout1View view) {
        OkHttp.do_Get(Url.Yangtzeu_App_Answer_Banner, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                BannerBean beans = GsonUtils.fromJson(response, BannerBean.class);
                final List<BannerBean.DataBean> info = beans.getData();
                //随机广告顺序排序
                Collections.shuffle(info);
                Collections.shuffle(info);
                view.getBanner().setData(info, null);
                view.getBanner().setAdapter(new BGABanner.Adapter<ImageView, BannerBean.DataBean>() {
                    @Override
                    public void fillBannerItem(BGABanner banner, ImageView itemView, BannerBean.DataBean model, int position) {
                        itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(activity).load(model.getImage()).into(itemView);
                    }
                });
                view.getBanner().setDelegate(new BGABanner.Delegate() {
                    @Override
                    public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                        MyUtils.openUrl(activity, info.get(position).getUrl());
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });

    }

    @Override
    @SuppressLint("InflateParams")
    public void loadHotAnswer(final Activity activity, final AnswerLayout1View view) {
        OkHttp.do_Get(Url.Yangtzeu_App_Hot_Answer, new OnResultStringListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                HotAnswerBean beans = GsonUtils.fromJson(response, HotAnswerBean.class);
                final List<HotAnswerBean.DataBean> info = beans.getData();
                view.getContainer().removeAllViews();
                for (int i = 0; i < info.size(); i++) {
                    View item = LayoutInflater.from(activity).inflate(R.layout.view_hot_answer_item, null);
                    view.getContainer().addView(item);

                    TextView name = item.findViewById(R.id.name);
                    TextView message = item.findViewById(R.id.message);
                    TextView author = item.findViewById(R.id.author);
                    LinearLayout onClick = item.findViewById(R.id.onClick);

                    name.setText(info.get(i).getTitle());
                    message.setText(info.get(i).getMessage());
                    author.setText("来源：" + info.get(i).getAuthor());
                    name.setText(info.get(i).getTitle());

                    final String url = info.get(i).getUrl();
                    onClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, WebActivity.class);
                            intent.putExtra("from_url", url);
                            intent.putExtra("isNoTitle", true);
                            intent.putExtra("isAnswer", true);
                            MyUtils.startActivity(intent);
                        }
                    });
                }

                AdView adView1 = GoogleUtils.newBannerView(activity, AdSize.LARGE_BANNER);
                adView1.loadAd(GoogleUtils.getRequest());
                AdView adView2 = GoogleUtils.newBannerView(activity, AdSize.LARGE_BANNER);
                adView2.loadAd(GoogleUtils.getRequest());
                LinearLayout googleView = view.getBottomContainer();
                googleView.addView(adView1);
                googleView.addView(adView2);
            }

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
    }


}
