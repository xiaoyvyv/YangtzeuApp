package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IVoaModel;
import com.yangtzeu.ui.activity.VoaListActivity;
import com.yangtzeu.ui.view.VoaView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VoaModel implements IVoaModel {
    @Override
    public void getVoaTitleList(final Activity activity, final VoaView view) {
        OkHttp.do_Get(Url.Yangtzeu_Voa_Home, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements uls = document.select("#left_nav > ul");
                if (uls.size() > 2) {
                    Elements elements = uls.get(1).select("ul > li > a");
                    view.getSlowContainer().removeAllViews();
                    for (final Element element1 : elements) {
                        @SuppressLint("InflateParams")
                        View item = LayoutInflater.from(activity).inflate(R.layout.activity_voa_item, null);
                        view.getSlowContainer().addView(item);
                        TextView title = item.findViewById(R.id.title);
                        LinearLayout onclick = item.findViewById(R.id.onclick);

                        title.setText(element1.text());
                        final String url = Url.Yangtzeu_Voa_Home + element1.attr("href");
                        onclick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, VoaListActivity.class);
                                intent.putExtra("title", element1.text());
                                intent.putExtra("from_url", url);
                                MyUtils.startActivity(intent);
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });


    }

    @Override
    public void getNormalVoaTitleList(final Activity activity, final VoaView view) {
        OkHttp.do_Get(Url.Yangtzeu_Voa_Home, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                final Elements uls = document.select("#left_nav > ul");
                if (uls.size() > 1) {
                    Elements elements = uls.get(0).select("ul > li > a");
                    view.getNormalContainer().removeAllViews();
                    for (final Element element1 : elements) {
                        @SuppressLint("InflateParams")
                        View item = LayoutInflater.from(activity).inflate(R.layout.activity_voa_item, null);
                        view.getNormalContainer().addView(item);
                        TextView title = item.findViewById(R.id.title);
                        LinearLayout onclick = item.findViewById(R.id.onclick);

                        title.setText(element1.text());
                        final String url = Url.Yangtzeu_Voa_Home + element1.attr("href");
                        onclick.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(activity, VoaListActivity.class);
                                intent.putExtra("title", element1.text());
                                intent.putExtra("from_url", url);
                                MyUtils.startActivity(intent);
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });

    }
}
