package com.yangtzeu.model;


import android.app.Activity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.lib.x5web.WebViewProgressBar;
import com.lib.x5web.X5LoadFinishListener;
import com.lib.x5web.X5WebView;
import com.tencent.smtt.sdk.WebView;
import com.yangtzeu.R;
import com.yangtzeu.model.imodel.IHomePart3Model;
import com.yangtzeu.ui.view.HomePartView3;

import androidx.appcompat.widget.Toolbar;

public class HomePart3Model implements IHomePart3Model {

    private String new_book = "http://calis.yangtzeu.edu.cn:8000/opac/search?isFacet=false&view=mobile&libcode=&limitDays=120&queryType=search_newpublications&searchType=newpub";
    private String search_book = "http://calis.yangtzeu.edu.cn:8000/opac/weixin/account/service/index?a=/action";
    private String top_book = "http://calis.yangtzeu.edu.cn:8000/opac/ranking/bookLoanRank?view=jquerymobile&bookType=1";
    private String lib_home = "http://calis.yangtzeu.edu.cn:8000/opac/m/reader/space";

    @Override
    public void loadView(Activity activity, HomePartView3 view) {
        FrameLayout container = view.getContainer();
        WebViewProgressBar progressBar = view.getProgressBar();
        Toolbar toolbar = view.getToolbar();
        final X5WebView webView = view.getWebView();

        container.addView(webView, 0);
        webView.setTitleAndProgressBar(toolbar, progressBar);
        webView.loadUrl(lib_home);
        webView.setX5LoadFinishListener(new X5LoadFinishListener() {
            @Override
            public void onLoadFinish(WebView webView, WebViewProgressBar progressBar, String s) {
                if (webView.getUrl().contains(lib_home)) {
                    webView.clearHistory();
                }
            }
        });
        toolbar.inflateMenu(R.menu.lib_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        webView.loadUrl(lib_home);
                        break;
                    case R.id.search_book:
                        webView.loadUrl(search_book);
                        break;
                    case R.id.top_book:
                        webView.loadUrl(top_book);
                        break;
                    case R.id.new_book:
                        webView.loadUrl(new_book);
                        break;
                }

                return false;
            }
        });
    }
}
