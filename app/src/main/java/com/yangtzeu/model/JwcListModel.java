package com.yangtzeu.model;

import android.app.Activity;
import android.util.ArraySet;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.entity.JwcListBean;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IJwcListModel;
import com.yangtzeu.ui.view.JwcListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JwcListModel implements IJwcListModel {
    @Override
    public void loadData(Activity activity, final JwcListView view) {
        final int ole_index = view.getAdapter().getItemCount();
        OkHttp.do_Get(view.getUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                view.getSmartRefreshLayout().finishRefresh();
                view.getSmartRefreshLayout().finishLoadMore();
                Document document = Jsoup.parse(response);
                String[] page_text = document.select("#pages").text().split(" ");
                String all_page = page_text[0].substring(page_text[0].indexOf("共") + 1, page_text[0].indexOf("页"));
                if (view.getAllPage() == 0) {
                    view.setAllPage(all_page);
                }


                Elements list = document.select("div#list_r ul li");

                List<JwcListBean> beans = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    String time = list.get(i).select("li span").text();
                    String title = list.get(i).select("li a").text();
                    String url = list.get(i).select("li a").attr("href");
                    JwcListBean bean = new JwcListBean();
                    bean.setTime(time);
                    bean.setKind(view.getKind());
                    bean.setUrl(url);
                    bean.setTitle(title);
                    beans.add(bean);
                }
                view.getAdapter().setData(beans);
                view.getAdapter().notifyItemRangeChanged(ole_index, view.getAdapter().getItemCount());
            }

            @Override
            public void onFailure(String error) {
                view.getSmartRefreshLayout().finishRefresh();
                view.getSmartRefreshLayout().finishLoadMore();
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }
}
