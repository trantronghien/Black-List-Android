package com.anddev.hientran.myapplication.blacklist;

import com.anddev.hientran.myapplication.interfaces.IBlacklistView;
import com.anddev.hientran.myapplication.models.MobileData;

import java.util.ArrayList;

/**
 * Created by HienTran on 9/23/2016.
 */


public class BlackListPresenter {
    private IBlacklistView blacklistView;
    private BlackListService blackListService;

    public BlackListPresenter(IBlacklistView blacklistView, BlackListService blackListService) {
        this.blacklistView = blacklistView;
        this.blackListService = blackListService;
    }


    public ArrayList<MobileData> onSaveClick() {
        String smsName = blacklistView.getSmsName();
        String smsNumber = blacklistView.getSmsNumber();
        return blackListService.fetchBlackList();
    }
}
