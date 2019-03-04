package com.yangtzeu.ui.view;




import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.ui.adapter.FragmentAdapter;
import com.yangtzeu.url.Url;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

public interface GradeView {

    FragmentAdapter getAdapter();
    ViewPager getViewPager();

    TabLayout getTabLayout();

    Toolbar getToolbar();


}
