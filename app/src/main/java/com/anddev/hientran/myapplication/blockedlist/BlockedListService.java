package com.anddev.hientran.myapplication.blockedlist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.anddev.hientran.myapplication.databases.CommonDbMethod;
import com.anddev.hientran.myapplication.models.MobileData;

import java.util.ArrayList;

/**
 * Created by HienTran on 9/17/2016.
 */
public class BlockedListService {

    public ArrayList<MobileData> getSmsInfo() {
        return fetchBlockedList();
    }

    private ArrayList<MobileData> fetchBlockedList() {
        ArrayList<MobileData> mobileDatas = new ArrayList<>();

        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(CommonDbMethod.mPATH, null, SQLiteDatabase.OPEN_READWRITE);

            //Check, if the "fromAddr" exists in the BlackListDB
            Cursor c = db.query("sms_blocked", null, null, null, null, null, " id DESC");
            //Log.i("ifBlockedDeleteSMS", "c.moveToFirst(): " + c.moveToFirst() + "  c.getCount(): " + c.getCount());

            if (c.moveToFirst() && c.getCount() > 0) {
                while (!c.isAfterLast()) {
                    MobileData mobileData = new MobileData();
                    mobileData.setCallerName(c.getString(c.getColumnIndex("names")));
                    mobileData.setMobileNumber(c.getString(c.getColumnIndex("numbers")));
                    mobileData.setOtherString(c.getString(c.getColumnIndex("body")));
                    mobileDatas.add(mobileData);
                    c.moveToNext();
                }
                c.close();

            }

            db.close();

        } catch (Exception e) {

        }

        return mobileDatas;
    }
}