package com.anddev.hientran.myapplication.inbox;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.anddev.hientran.myapplication.models.MobileData;

import java.util.ArrayList;

/**
 * Created by HienTran on 9/24/2016.
 */

// truy vấn lấy số đã gửi từ SMS
public class InboxService {
    public ArrayList<MobileData> fetchInboxSms(Context context) {
        ArrayList<MobileData> mobileDatas = new ArrayList<>();

        Uri inboxURI = Uri.parse("content://sms/inbox");

        // List required columns
        String[] reqCols = new String[]{"_id", "address", "body", "thread_id"};

        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = context.getContentResolver();
       /* Cursor c = cr.query(inboxURI, new String[]{"_id", "DISTINCT address", "body", "thread_id"}, //DISTINCT
                "address IS NOT NULL) GROUP BY (address", null, null);*/

        // Fetch Inbox SMS Message from Built-in Content Provider
        Cursor c = cr.query(inboxURI, reqCols, null, null, null);


        if (c.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                MobileData mobileData = new MobileData();

                for(int idx=0;idx < c.getColumnCount();idx++)
                {
                    msgData += "****" + c.getColumnName(idx) + ":" + c.getString(idx);
                    Log.i("***mm", "*** " + msgData);

                    if (idx == 1) {
                        mobileData.setMobileNumber(c.getString(idx));
                    }

                    if (idx == 2) {
                        mobileData.setOtherString(c.getString(idx));
                    }

                    if (idx == 3) {
                        mobileData.setCallerName(c.getString(idx));
                    }
                    if (idx == 4) {
                        mobileData.setSmsId(c.getString(idx));
                    }

                }

                mobileDatas.add(mobileData);


            } while (c.moveToNext());

        } else {
            // empty box, no SMS
        }

        return mobileDatas;
    }
}
