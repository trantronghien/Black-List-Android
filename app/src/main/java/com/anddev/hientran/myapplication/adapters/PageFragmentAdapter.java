package com.anddev.hientran.myapplication.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;


import com.anddev.hientran.myapplication.R;
import com.anddev.hientran.myapplication.fragment.AboutFragments;
import com.anddev.hientran.myapplication.fragment.BlackListFragment;
import com.anddev.hientran.myapplication.fragment.LogFragment;

/**
 * Created by HienTran on 9/23/2016.
 */

public class PageFragmentAdapter extends FragmentPagerAdapter {

    private String[] tabTitles;
    private Fragment[] fragments;

    public PageFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);

        tabTitles = context.getResources().getStringArray(R.array.tabTitles);
        fragments = new Fragment[]{new BlackListFragment(), new LogFragment() , new AboutFragments()};
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

}

