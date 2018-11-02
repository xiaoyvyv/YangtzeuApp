package com.yangtzeu.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.BannerBean;
import com.yangtzeu.entity.ManyBean;
import com.yangtzeu.entity.MarqueeBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IManyModel;
import com.yangtzeu.service.qq.QQService;
import com.yangtzeu.ui.adapter.ManyAdapter;
import com.yangtzeu.ui.view.ManyView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

public class ManyModel implements IManyModel {
    @Override
    public void loadMarqueeView(final Activity activity, final ManyView view) {
        OkHttp.do_Get(Url.Yangtzeu_App_Many_Notice, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                MarqueeBean beans = GsonUtils.fromJson(response, MarqueeBean.class);
                final List<MarqueeBean.DataBean> info = beans.getData();
                final List<String> info_str = new ArrayList<>();
                for (int i = 0; i < info.size(); i++) {
                    info_str.add(info.get(i).getInfo());
                }
                view.getNoticeView().start(info_str);
                view.getNoticeView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyUtils.openUrl(activity, info.get(view.getNoticeView().getIndex()).getUrl());
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
    public void loadBanner(final Activity activity, final ManyView view) {
        OkHttp.do_Get(Url.Yangtzeu_App_Many_Banner, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                BannerBean beans = GsonUtils.fromJson(response, BannerBean.class);
                final List<BannerBean.DataBean> info = beans.getData();
                final List<String> info_str = new ArrayList<>();
                for (int i = 0; i < info.size(); i++) {
                    info_str.add(info.get(i).getTitle());
                }
                view.getBGABanner().setData(info, info_str);
                view.getBGABanner().setAdapter(new BGABanner.Adapter<ImageView,BannerBean.DataBean>() {
                    @Override
                    public void fillBannerItem(BGABanner banner, ImageView itemView, BannerBean.DataBean model, int position) {
                        itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(activity).load(model.getImage()).into(itemView);
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
    public void fitAdapter(Activity activity, ManyView view) {
        final ManyAdapter adapter = new ManyAdapter(activity);
        view.getRecyclerView().setNestedScrollingEnabled(false);
        view.getRecyclerView().setAdapter(adapter);

        OkHttp.do_Get(Url.Yangtzeu_Many_Item, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                ManyBean bean = GsonUtils.fromJson(response, ManyBean.class);
                adapter.setData(bean);
                adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });
    }

    @Override
    public void loadQQLikeEvents(final Activity activity, ManyView view) {
        if (MyUtils.isAccessibilitySettingsOn(activity,QQService.class)) {
            ToastUtils.showShort(R.string.please_open_friend);
            try {
                Intent intent =activity.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort(R.string.no_qq_app);
            }
        } else {
            ToastUtils.showShort(R.string.qq_like_permission_error);
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setTitle(R.string.trip)
                    .setMessage(R.string.need_accessibility)
                    .setPositiveButton("打开", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            activity.startActivity(intent);
                            activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        }
                    })
                    .setNegativeButton(R.string.clear, null)
                    .create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }
}
