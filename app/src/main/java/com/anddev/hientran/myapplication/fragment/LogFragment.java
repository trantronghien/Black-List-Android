package com.anddev.hientran.myapplication.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.anddev.hientran.myapplication.R;
import com.anddev.hientran.myapplication.activitys.MainActivity;
import com.anddev.hientran.myapplication.adapters.LogAdapter;
import com.anddev.hientran.myapplication.adapters.LogNumberAdapter;
import com.anddev.hientran.myapplication.blockedlist.BlockedListPresenter;
import com.anddev.hientran.myapplication.blockedlist.BlockedListService;
import com.anddev.hientran.myapplication.databases.CommonDbMethod;
import com.anddev.hientran.myapplication.inbox.InboxService;
import com.anddev.hientran.myapplication.interfaces.IBlockedListView;
import com.anddev.hientran.myapplication.models.MobileData;
import com.anddev.hientran.myapplication.models.NumberData;
import com.anddev.hientran.myapplication.util.UtilityMethod;

import java.util.ArrayList;

/**
 * Created by HienTran on 9/23/2016.
 */
@SuppressLint("ValidFragment")
public class LogFragment extends Fragment implements IBlockedListView {

    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    TextView textView;
    BlockedListPresenter blockedListPresenter;

    private void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv);
        textView = (TextView) rootView.findViewById(R.id.textView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_log, container, false);
        initView(rootView);
        blockedListPresenter = new BlockedListPresenter(this, new BlockedListService());
        LogAdapter mAdapter = new LogAdapter(blockedListPresenter.onFetchClick(), getActivity());
        recyclerView.setAdapter(mAdapter);
        setEmptyMessage(blockedListPresenter.onFetchClick().size());

        mAdapter.setOnDataChangeListener(new LogAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(int size) {
                setEmptyMessage(size);
            }
        });

        return rootView;
    }

    public void reloadWhenDataChanges(){
        //snippet will call the onCreateView Method of the Fragment.
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    @Override
    public String getSmsName() {
        return null;
    }

    @Override
    public String getSmsNumber() {
        return null;
    }
    public void setEmptyMessage(int size) {
        if (size > 0) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }
}
