package com.yangtzeu.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IVoaDetailsModel;
import com.yangtzeu.ui.view.VoaDetailsView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VoaDetailsModel implements IVoaDetailsModel {
    @Override
    public void loadData(final Activity activity, final VoaDetailsView view) {
        OkHttp.do_Get(view.getUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements content = document.select("#content");
                String time = content.select(".datetime").text();
                String mp3 = document.select("#mp3").attr("href");
                view.setMp3(mp3);
                Elements elements = content.select("p");

                view.getText().removeAllViews();
                for (final Element element1 : elements) {
                    String text = element1.text().trim();
                    if (StringUtils.isEmpty(text)) {
                        continue;
                    }

                    @SuppressLint("InflateParams")
                    View item = LayoutInflater.from(activity).inflate(R.layout.activity_voa_item, null);
                    view.getText().addView(item);
                    TextView title = item.findViewById(R.id.title);
                    title.setText(text);
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });




    }
}
