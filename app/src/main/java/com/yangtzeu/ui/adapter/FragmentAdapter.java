package com.yangtzeu.ui.adapter;


import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by 2016 on 2017/12/24.
 *
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mFragments_name;
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<>();
        mFragments_name = new ArrayList<>();
    }

    public void addFragment(String title,Fragment fragment) {
        mFragments.add(fragment);
        mFragments_name.add(title);
    }
    public void clear() {
        mFragments.clear();
        mFragments_name.clear();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int arg0) {
        return mFragments.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return mFragments.hashCode();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments_name.get(position);
    }
}