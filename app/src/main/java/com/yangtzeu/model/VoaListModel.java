package com.yangtzeu.model;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.imodel.IVoaListModel;
import com.yangtzeu.ui.view.VoaListView;
import com.yangtzeu.url.Url;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class VoaListModel implements IVoaListModel {

    @Override
    public void loadData(Activity activity, final VoaListView view) {
        String url;
        if (view.is_archiver()) {
            url = Url.Yangtzeu_Voa_Home + view.getFromUrlHeader() + view.getPage() + "_archiver.html";
        } else {
            url=Url.Yangtzeu_Voa_Home + view.getFromUrlHeader() + view.getPage() + ".html";
        }


        final int old_index = view.getAdapter().getItemCount();
        OkHttp.do_Get(url, new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                Document document = Jsoup.parse(response);
                Elements elements = document.select("#list >ul >li > a");
                view.getAdapter().setData(elements);
                view.getAdapter().notifyItemRangeChanged(old_index, view.getAdapter().getItemCount());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });
    }
}
