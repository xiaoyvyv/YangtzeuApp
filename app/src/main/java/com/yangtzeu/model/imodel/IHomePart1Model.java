package com.yangtzeu.model.imodel;


import android.app.Activity;

import com.yangtzeu.ui.view.GradePartView1;
import com.yangtzeu.ui.view.HomePartView1;

public interface IHomePart1Model {

    void loadData(Activity activity, HomePartView1 view);
    void analysisData(Activity activity, HomePartView1 view, String data);

    void loadNotice(Activity activity, HomePartView1 view);
}
