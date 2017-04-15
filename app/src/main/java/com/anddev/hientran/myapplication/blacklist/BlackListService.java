package com.anddev.hientran.myapplication.blacklist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anddev.hientran.myapplication.databases.CommonDbMethod;
import com.anddev.hientran.myapplication.models.MobileData;

import java.util.ArrayList;

/**
 * Created by HienTran on 9/23/2016.
 */

public class BlackListService {

    public ArrayList<MobileData> fetchBlackList() {
        ArrayList<MobileData> mobileDatas = new ArrayList<>();
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(CommonDbMethod.mPATH , null, SQLiteDatabase.OPEN_READWRITE);

            //Check, if the "fromAddr" exists in the BlackListDB
            Cursor c = db.query(CommonDbMethod.TABLE_BLACK_LIST, null, null, null, null, null, null);
            if (c.moveToFirst() && c.getCount() > 0) {
                while (!c.isAfterLast()) {
                    MobileData mobileData = new MobileData();
                    mobileData.setCallerName(c.getString(c.getColumnIndex("names")));
                    mobileData.setMobileNumber(c.getString(c.getColumnIndex("numbers")));
                    mobileData.setOtherString(c.getString(c.getColumnIndex("sms_id")));
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