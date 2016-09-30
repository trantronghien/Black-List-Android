package com.anddev.hientran.myapplication.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.anddev.hientran.myapplication.models.MobileData;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by HienTran on 9/17/2016.
 */

public class CommonDbMethod {
    Context context;
    // path databases
    public static final String mPATH = "/data/data/com.anddev.hientran.myapplication/databases/BlackListDB.db";

    public CommonDbMethod(Context context) {
        this.context = context;
    }

    // add blacklist
    public void addToNumberBlacklist(String name, String number) {
        if (number.length() == 0) {
            Toast.makeText(context, "Please fill up both the fields", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            SQLiteDatabase db;
            db = context.openOrCreateDatabase(mPATH, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            db.setVersion(1);
            db.setLocale(Locale.getDefault());
            db.setLockingEnabled(true);
            db.execSQL("create table IF NOT EXISTS SMS_BlackList(sms_id varchar(20), names varchar(20), numbers varchar(20) UNIQUE)");

            // sử lý number khi nếu có khoảng trắng
            String [] str = number.split("\\s+");
            String number_stadand = "";
            for (int i = 0; i < str.length; i++) {
                number_stadand += str[i];
            }
            Log.d("CommonDbMethod" , "insert number: " + number_stadand);

            ContentValues values = new ContentValues();
            // values.put("sms_id", id);
            values.put("names", name);
            values.put("numbers", number_stadand);
            //values.put("body", body);

            db.insert("SMS_BlackList", null, values);

        /*if (db.insert("SMS_BlackList", null, values) == -1){
            Log.d("addToSMS_BlackList", "3: blockingCodeForSMS ");
            Toast.makeText(context, name + " already exist in database\n Please try a new name!!", Toast.LENGTH_LONG).show();
            db.close();
            return;
        }*/

            Log.d("addToSMS_BlackList", "4: blockingCodeForSMS ");
            Log.d("addToSMS_BlackList", "5: blockingCodeForSMS ");

            db.close();
            Toast.makeText(context, number + " is added to blacklist ", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(context, "Error: Can't add number" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // delete blacklist
    public boolean deleteBlackListNumber(final String number, final String tableName) {
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(mPATH, null, SQLiteDatabase.OPEN_READWRITE);
            db.delete(tableName, "numbers" + " = ?", new String[] {number});
            db.close();
            return true;
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }
        return false;

    }
    // ============================================================================
    //                              Log Number
    //============================================================================


    public boolean deleteLogNumber(final String formattedDate, final String number, final String tableName) {
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(mPATH, null, SQLiteDatabase.OPEN_READWRITE);
            db.delete(tableName, "numbers" + " = ? and body = ?", new String[] {number.trim(), formattedDate.trim()});
            db.close();
        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }
        return false;
    }
}



