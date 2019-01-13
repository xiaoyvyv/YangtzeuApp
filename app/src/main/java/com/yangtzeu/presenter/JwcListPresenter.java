package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.RegexUtils;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.JwcListModel;
import com.yangtzeu.ui.view.JwcListView;

public class JwcListPresenter {
    private Activity activity;
    private JwcListView view;
    private JwcListModel mode;

    public JwcListPresenter(Activity activity, JwcListView view) {
        this.activity = activity;
        this.view = view;
        mode = new JwcListModel();
    }

    public void loadData() {
        //先获取条目起始的index
        OkHttp.do_Get(view.getUrlHeader() + "statlist.js", new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                try {
                    String s_rowCount =RegexUtils.getMatches("rowCount=(.*?);", response).get(0);
                    String s_pageCount = RegexUtils.getMatches("pageCount=(.*?);", response).get(0);
                    int rowCount = Integer.parseInt(s_rowCount.substring(s_rowCount.indexOf("=")+1, s_rowCount.lastIndexOf(";")));
                    int pageCount = Integer.parseInt(s_pageCount.substring(s_pageCount.indexOf("=")+1, s_pageCount.lastIndexOf(";")));
                    int start = (pageCount - rowCount % pageCount) % pageCount;
                    view.setStartIndex(start);
                    mode.loadData(activity, view);
                } catch (NumberFormatException e) {
                    onFailure(e.toString());
                }
            }

            @Override
            public void onFailure(String error) {
                view.setStartIndex(0);
                mode.loadData(activity, view);
            }
        });
    }
}
