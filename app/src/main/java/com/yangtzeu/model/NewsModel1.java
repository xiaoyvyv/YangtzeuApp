package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.lib.yun.UrlSafeBase64;
import com.yangtzeu.R;
import com.yangtzeu.database.DatabaseUtils;
import com.yangtzeu.entity.YzBannerBean;
import com.yangtzeu.model.imodel.INewsModel1;
import com.yangtzeu.ui.activity.JwcListActivity;
import com.yangtzeu.ui.fragment.HomePartFragment2;
import com.yangtzeu.ui.view.NewsView1;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.viewpager.widget.ViewPager;
import cn.bingoogolapple.bgabanner.BGABanner;

public class NewsModel1 implements INewsModel1 {

    @Override
    public void loadBanner(final Activity activity, NewsView1 view) {
        //加载内存的Banner
        List<String> title = new ArrayList<>();
        final List<String> url = new ArrayList<>();
        List<String> image = new ArrayList<>();
        List<YzBannerBean> list = DatabaseUtils.getHelper(activity, "banner.db").queryAll(YzBannerBean.class);
        if (ObjectUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                title.add(list.get(i).getTitle());
                url.add(list.get(i).getUrl());
                image.add(list.get(i).getImage());
            }
            view.getBGABanner().setData(image, title);
            view.getBGABanner().setAdapter(new BGABanner.Adapter<ImageView, String>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, String model, int position) {
                    itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                    MyUtils.loadImage(activity, itemView, model);
                }
            });
            view.getBGABanner().setDelegate(new BGABanner.Delegate() {
                @Override
                public void onBannerItemClick(BGABanner banner, View itemView, Object model, int position) {
                    MyUtils.openUrl(activity, url.get(position));
                }
            });
        }
        //获取最新的Banner
        YangtzeuUtils.getBanner(activity);

    }

    @Override
    public void fitGridView(final Activity activity, final NewsView1 view) {
        view.getSearchView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 修改回车键功能
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 先隐藏键盘
                    KeyboardUtils.hideSoftInput(view.getSearchView());
                    String input = view.getSearchView().getText().toString().trim();
                    if (!input.isEmpty()) {
                        view.getSearchView().setText(null);
                        String e_input = UrlSafeBase64.encodeToString(input);
                        LogUtils.e(e_input);
                        MyUtils.openUrl(activity, Url.Yangtzeu_News_Search + e_input);

                    } else ToastUtils.showShort(R.string.please_input);
                }
                return false;
            }
        });

        view.getGridView().setNumColumns(4);
        view.getGridView().setVerticalSpacing(30);
        view.getGridView().setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return view.getTitleArray().length;
            }

            @Override
            public Object getItem(int position) {
                return view.getTitleArray()[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @SuppressLint("InflateParams")
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    convertView = inflater.inflate(R.layout.fragment_news_layout1_item, null);
                    viewHolder.title = convertView.findViewById(R.id.Title);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.title.setText(view.getTitleArray()[position]);
                viewHolder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewPager viewPager = HomePartFragment2.mViewPager;
                        if (viewPager != null) {
                            viewPager.setCurrentItem(position + 1);
                        }
                        TabLayout tabLayout = HomePartFragment2.tabLayout;
                        if (tabLayout != null) {
                            Objects.requireNonNull(tabLayout.getTabAt(position + 1)).select();
                        }
                    }
                });

                return convertView;
            }

            class ViewHolder {
                TextView title;
            }
        });

    }

    @Override
    public void fitJwcGridView(final Activity activity, final NewsView1 view) {
        final String[] jwc_url = {Url.Yangtzeu_JWTZ, Url.Yangtzeu_BZSW, Url.Yangtzeu_JXDT, Url.Yangtzeu_JXJB, "http://jwc.yangtzeu.edu.cn/jwnews/jwxw/gjxx/",
                "http://jwc.yangtzeu.edu.cn/jwnews/jwgl/xlcx/", "http://jwc.yangtzeu.edu.cn/jwnews/kwgl/", "http://jwc.yangtzeu.edu.cn/jwnews/sjcx/sjjx/",
                "http://jwc.yangtzeu.edu.cn/jwnews/sjcx/xkjs/", "http://jwc.yangtzeu.edu.cn/jwnews/xzzx/xyxz/", "http://jwc.yangtzeu.edu.cn/jwnews/xzzx/jsxz/",
                "http://jwc.yangtzeu.edu.cn/jwnews/xzzx/xsxz/"};
        final String[] jwc_title = {"教务通知", "本周事务", "教学动态", "教学简报", "高教信息",
                "校历查询", "考务管理", "实践教学", "科学竞赛", "学院下载", "教师下载", "学生下载"};

        view.getJwcGridView().setNumColumns(4);
        view.getJwcGridView().setVerticalSpacing(30);
        view.getJwcGridView().setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return jwc_title.length;
            }

            @Override
            public Object getItem(int position) {
                return jwc_title[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @SuppressLint("InflateParams")
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(activity);
                    convertView = inflater.inflate(R.layout.fragment_news_layout1_item, null);
                    viewHolder.title = convertView.findViewById(R.id.Title);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.title.setText(jwc_title[position]);
                viewHolder.title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, JwcListActivity.class);
                        intent.putExtra("title", jwc_title[position]);
                        intent.putExtra("from_url", jwc_url[position]);
                        MyUtils.startActivity(intent);
                    }
                });

                return convertView;
            }

            class ViewHolder {
                TextView title;
            }
        });
    }

}
