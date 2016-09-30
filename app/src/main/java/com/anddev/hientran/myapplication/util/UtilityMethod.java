package com.anddev.hientran.myapplication.util;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.anddev.hientran.myapplication.R;
import com.anddev.hientran.myapplication.fragment.BlackListFragment;
import com.anddev.hientran.myapplication.fragment.LogFragment;

/**
 * Created by HienTran on 9/23/2016.
 */

//todo giao dịch Fragments
public class UtilityMethod {

        public static void blackListFragment(Activity activity) {
            android.app.Fragment fragment;
            fragment = new BlackListFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            FragmentManager frgManager = activity.getFragmentManager();
            android.app.FragmentTransaction ft = frgManager.beginTransaction();
            ft.replace(R.id.viewpager, fragment);
            ft.commit();
        }
        public static void logListFragment(Activity activity) {
            android.app.Fragment fragment;
            fragment = new LogFragment();
            Bundle args = new Bundle();
            fragment.setArguments(args);
            FragmentManager frgManager = activity.getFragmentManager();
            android.app.FragmentTransaction ft = frgManager.beginTransaction();
            ft.replace(R.id.viewpager, fragment);
            ft.commit();
        }


    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

}
