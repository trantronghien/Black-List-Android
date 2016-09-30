package com.anddev.hientran.myapplication.fragment;



import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import com.anddev.hientran.myapplication.R;
import com.anddev.hientran.myapplication.adapters.BlackListAdapter;
import com.anddev.hientran.myapplication.adapters.PageFragmentAdapter;
import com.anddev.hientran.myapplication.blacklist.BlackListPresenter;
import com.anddev.hientran.myapplication.blacklist.BlackListService;
import com.anddev.hientran.myapplication.interfaces.IBlacklistView;




/**
 * Created by HienTran on 9/23/2016.
 */


public class BlackListFragment extends Fragment implements IBlacklistView  {

    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    BlackListPresenter blackListPresenter;
    RelativeLayout relative_help;
    BlackListAdapter mAdapter;


    private void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyview_fragment_blacklist);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        relative_help = (RelativeLayout) rootView.findViewById(R.id.relative_help);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_blacklist, container, false);
        initView(rootView);
        blackListPresenter = new BlackListPresenter(this, new BlackListService());
        mAdapter = new BlackListAdapter(blackListPresenter.onSaveClick(), getActivity());
        setMessage(blackListPresenter.onSaveClick().size());
        mAdapter.setOnDataChangeListener(new BlackListAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(int size) {
                setMessage(blackListPresenter.onSaveClick().size());

            }
        });

        recyclerView.setAdapter(mAdapter);
        return rootView;
    }

    // todo: set VISIBLE help
    // hiện thị nội dụng trợ giúp trong fragment nếu Recyview rỗng
    public void setMessage(int size) {
        if (size > 0) {
           relative_help.setVisibility(View.INVISIBLE);
        } else {
           relative_help.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getSmsInfo() {

    }
    @Override
    public String getSmsName() {
        return null;
    }

    @Override
    public String getSmsNumber() {
        return null;
    }



}
