package com.yangtzeu.ui.view;

import android.widget.EditText;
import android.widget.GridView;

import cn.bingoogolapple.bgabanner.BGABanner;

public interface NewsView1 {
    BGABanner getBGABanner();

    EditText getSearchView();
    GridView getGridView();
    GridView getJwcGridView();

    String[] getTitleArray();

}
