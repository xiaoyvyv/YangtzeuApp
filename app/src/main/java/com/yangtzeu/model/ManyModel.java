package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.lib.calculator.calculator.CalculatorActivity;
import com.lib.subutil.GsonUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.BannerBean;
import com.yangtzeu.entity.ManyBean;
import com.yangtzeu.entity.MarqueeBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IManyModel;
import com.yangtzeu.ui.activity.AnswerActivity;
import com.yangtzeu.ui.activity.LockActivity;
import com.yangtzeu.ui.activity.LoveActivity;
import com.yangtzeu.ui.activity.ShopActivity;
import com.yangtzeu.ui.activity.ToolActivity;
import com.yangtzeu.ui.activity.WebListActivity;
import com.yangtzeu.ui.adapter.ManyAdapter;
import com.yangtzeu.ui.view.ManyView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.NotificationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.bingoogolapple.bgabanner.BGABanner;

public class ManyModel implements IManyModel, View.OnClickListener {
    private Activity activity;
    private ManyView view;

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
                //随机广告顺序排序
                Collections.shuffle(info);
                final List<String> info_str = new ArrayList<>();
                for (int i = 0; i < info.size(); i++) {
                    info_str.add(info.get(i).getTitle());
                }
                view.getBGABanner().setData(info, info_str);
                view.getBGABanner().setAdapter(new BGABanner.Adapter<ImageView, BannerBean.DataBean>() {
                    @Override
                    public void fillBannerItem(BGABanner banner, ImageView itemView, BannerBean.DataBean model, int position) {
                        itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                        Glide.with(activity).load(model.getImage()).into(itemView);
                    }
                });
                view.getBGABanner().setDelegate(new BGABanner.Delegate() {
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
    public void fitAdapter(final Activity activity, final ManyView view) {
        final ManyAdapter adapter = new ManyAdapter(activity);
        view.getRecyclerView().setNestedScrollingEnabled(false);
        view.getRecyclerView().setAdapter(adapter);

        OkHttp.do_Get(Url.Yangtzeu_Many_Item, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                ManyBean bean = GsonUtils.fromJson(response, ManyBean.class);
                int spanCount = bean.getSpanCount();
                view.getRecyclerView().setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
                adapter.setSpanCount(spanCount);
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
    @SuppressLint("InflateParams")
    public void loadItemLayout(Activity activity, ManyView view) {
        this.activity = activity;
        this.view = view;

        View rootView1 = view.getItemLayout();

        rootView1.findViewById(R.id.lock).setOnClickListener(this);
        rootView1.findViewById(R.id.shop).setOnClickListener(this);
        rootView1.findViewById(R.id.answer).setOnClickListener(this);
        rootView1.findViewById(R.id.love).setOnClickListener(this);
        rootView1.findViewById(R.id.cut_off).setOnClickListener(this);
        rootView1.findViewById(R.id.like).setOnClickListener(this);
        rootView1.findViewById(R.id.web).setOnClickListener(this);
        rootView1.findViewById(R.id.calculator).setOnClickListener(this);


        /*
        View rootView2 = LayoutInflater.from(activity).inflate(R.layout.fragment_many_banner_item2, null);
        rootView2.findViewById(R.id.ruler).setOnClickListener(this);
        rootView2.findViewById(R.id.compass).setOnClickListener(this);
        rootView2.findViewById(R.id.translate).setOnClickListener(this);
        rootView2.findViewById(R.id.kg).setOnClickListener(this);
        rootView2.findViewById(R.id.game).setOnClickListener(this);
        rootView2.findViewById(R.id.matrix).setOnClickListener(this);
        rootView2.findViewById(R.id.voa).setOnClickListener(this);
        rootView2.findViewById(R.id.chat).setOnClickListener(this);
        */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shop:
                MyUtils.startActivity(ShopActivity.class);
                break;
            case R.id.answer:
                MyUtils.startActivity(AnswerActivity.class);
                break;
            case R.id.love:
                MyUtils.startActivity(LoveActivity.class);
                break;
            case R.id.cut_off:
                MyUtils.openUrl(activity, Url.Yangtzeu_App_Quan, true);
                break;
            case R.id.lock:
                boolean isEnable = NotificationUtils.isNotificationEnabled(activity);
                if (isEnable) {
                    MyUtils.startActivity(LockActivity.class);
                } else {
                    MyUtils.getAlert(activity, "检测到您未允许本App的锁屏通知权限，建议您允许！\n\n请勾选允许锁屏通知\n\n权限用于在锁屏界面显示手机锁定剩余时间\n\n若不需要显示请点击取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NotificationUtils.toNotificationSetting(activity);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyUtils.startActivity(LockActivity.class);
                        }
                    }).show();
                }
                break;
            case R.id.like:
                MyUtils.startActivity(ToolActivity.class);
                break;
            case R.id.web:
                PopupMenu menu = new PopupMenu(activity, v);
                menu.getMenu().add("长大网站集").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        MyUtils.startActivity(WebListActivity.class);
                        return true;
                    }
                });
                menu.getMenu().add("电脑软件集").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(activity, WebListActivity.class);
                        intent.putExtra("from_url", Url.Yangtzeu_All_Web_Soft);
                        intent.putExtra("title", activity.getString(R.string.soft_list));
                        MyUtils.startActivity(intent);
                        return true;
                    }
                });
                menu.show();
                break;
            case R.id.calculator:
                MyUtils.startActivity(CalculatorActivity.class);
                break;

            /*case R.id.compass:
                break;
            case R.id.translate:
                MyUtils.startActivity(TranslateActivity.class);
                break;
            case R.id.kg:
                MyUtils.startActivity(KgActivity.class);
                break;
            case R.id.ruler:
                MyUtils.startActivity(RulerActivity.class);
                break;
            case R.id.game:
                MyUtils.startActivity(GameActivity.class);
                break;
            case R.id.matrix:
                MyUtils.startActivity(MatrixActivity.class);
                break;
            case R.id.voa:
                MyUtils.startActivity(VoaActivity.class);
                break;*/
        }
    }
}
