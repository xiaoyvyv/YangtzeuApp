package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.tabs.TabLayout;
import com.lib.yun.UrlSafeBase64;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.INewsModel1;
import com.yangtzeu.ui.activity.JwcListActivity;
import com.yangtzeu.ui.fragment.HomePartFragment2;
import com.yangtzeu.ui.view.NewsView1;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.viewpager.widget.ViewPager;
import cn.bingoogolapple.bgabanner.BGABanner;

public class NewsModel1 implements INewsModel1 {

    @Override
    public void loadBanner(final Activity activity, final NewsView1 view) {
        OkHttp.do_Get(Url.Yangtzeu_Url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                List<String> title = new ArrayList<>();
                final List<String> url = new ArrayList<>();
                List<String> image = new ArrayList<>();

                Document doc = Jsoup.parse(response);
                Elements elements = doc.select("div.iban div.bd ul li");
                for (int i = 0; i < elements.size(); i++) {
                    String image_style = elements.get(i).attr("style");
                    String image_url = Url.Yangtzeu_Url + image_style.substring(image_style.indexOf("(") + 1, image_style.indexOf(")"));
                    String context_url = elements.get(i).select("li a").attr("href");
                    title.add("长江大学热点资讯");
                    image.add(image_url);
                    url.add(context_url);
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

            @Override
            public void onFailure(String error) {
                LogUtils.e(error);
            }
        });
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
        final String[] jwc_url = {Url.Yangtzeu_JWTZ, Url.Yangtzeu_BZSW, Url.Yangtzeu_JXDT, Url.Yangtzeu_JXJB, "http://jwc.yangtzeu.edu.cn/jwxw/gjxx.htm",
                "http://jwc.yangtzeu.edu.cn/jwgl/xlcx.htm", "http://jwc.yangtzeu.edu.cn/kwgl/kwgl.htm", "http://jwc.yangtzeu.edu.cn/sjcx/sjjx.htm",
                "http://jwc.yangtzeu.edu.cn/sjcx/xkjs.htm", "http://jwc.yangtzeu.edu.cn/xzzx/xyxz.htm", "http://jwc.yangtzeu.edu.cn/xzzx/jsxz.htm",
                "http://jwc.yangtzeu.edu.cn/xzzx/xsxz.htm"};
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
