package com.anddev.hientran.myapplication.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anddev.hientran.myapplication.R;
import com.anddev.hientran.myapplication.activitys.MainActivity;

/**
 * Created by HienTran on 9/23/2016.
 */
public class AboutFragments extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // hide FloatButton
       View rootview = inflater.inflate(R.layout.fragment_about , container , false);
        return rootview;
    }

}
