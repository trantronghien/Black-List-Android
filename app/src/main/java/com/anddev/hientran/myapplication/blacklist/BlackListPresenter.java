package com.anddev.hientran.myapplication.blacklist;

import android.view.View;

import com.anddev.hientran.myapplication.interfaces.IBlacklistView;
import com.anddev.hientran.myapplication.models.MobileData;

import java.util.ArrayList;

/**
 * Created by HienTran on 9/23/2016.
 */


public class BlackListPresenter {
    IBlacklistView blacklistView;
    BlackListService blackListService;

    public BlackListPresenter(IBlacklistView blacklistView, BlackListService blackListService) {
        this.blacklistView = blacklistView;
        this.blackListService = blackListService;
    }


    public ArrayList<MobileData> onSaveClick() {
        String smsName = blacklistView.getSmsName();
        String smsNumber = blacklistView.getSmsNumber();
        return blackListService.getSmsInfo();
    }
}
