package com.yangtzeu.model.imodel;


import android.app.Activity;

import com.yangtzeu.ui.view.GradePartView1;
import com.yangtzeu.ui.view.GradePartView2;

public interface IGradePart2Model {
    void loadPointData(Activity activity, GradePartView2 view);

    void parseAllGrade(Activity activity, GradePartView2 view, String result);
}
