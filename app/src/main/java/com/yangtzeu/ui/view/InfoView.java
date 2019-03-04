package com.yangtzeu.ui.view;


import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yangtzeu.ui.view.base.BaseView;

public interface InfoView extends BaseView {
    ImageView getHeader();
    LinearLayout getMineInfoLayout();
}
