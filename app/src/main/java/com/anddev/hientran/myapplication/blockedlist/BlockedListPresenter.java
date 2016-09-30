package com.anddev.hientran.myapplication.blockedlist;


import com.anddev.hientran.myapplication.interfaces.IBlockedListView;
import com.anddev.hientran.myapplication.models.MobileData;

import java.util.ArrayList;


/**
 * Created by HienTran on 9/17/2016.
 */
public class BlockedListPresenter {
    IBlockedListView blacklistView;
    BlockedListService blackListService;

    public BlockedListPresenter(IBlockedListView blacklistView, BlockedListService blackListService) {
        this.blacklistView = blacklistView;
        this.blackListService = blackListService;
    }

    public ArrayList<MobileData> onFetchClick() {
        String smsName = blacklistView.getSmsName();
        String smsNumber = blacklistView.getSmsNumber();

        return blackListService.getSmsInfo();
    }

}
