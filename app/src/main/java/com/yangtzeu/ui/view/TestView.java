package com.yangtzeu.ui.view;

import android.app.ProgressDialog;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.ui.adapter.FragmentAdapter;
import com.yangtzeu.ui.fragment.TestFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public interface TestView {
     Toolbar getToolbar();
     TabLayout  getTabLayout();
     FrameLayout getContainer();
     FragmentAdapter  getFragmentAdapter();
     ProgressDialog  getProgressDialog();
     FragmentManager getManager();

      List<Fragment> getFragments();
}
