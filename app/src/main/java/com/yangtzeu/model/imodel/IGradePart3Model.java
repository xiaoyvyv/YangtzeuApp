package com.yangtzeu.model.imodel;


import android.app.Activity;

import com.yangtzeu.ui.view.GradePartView3;

public interface IGradePart3Model {
    void loadPointData(Activity activity, GradePartView3 view);

    void parseAllGrade(Activity activity, GradePartView3 view, String result);
}
