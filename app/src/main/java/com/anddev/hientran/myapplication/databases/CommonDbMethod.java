package com.anddev.hientran.myapplication.databases;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.anddev.hientran.myapplication.R;
import com.anddev.hientran.myapplication.adapters.BlackListAdapter;
import com.anddev.hientran.myapplication.models.MobileData;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by HienTran on 9/17/2016.
 */

public class CommonDbMethod {
    private Context context;

    // path databases
    public static final String mPATH = "/data/data/com.anddev.hientran.myapplication/databases/BlackListDB.db";
    public static final String TABLE_BLACK_LIST = "BlackList";
    public static final String TABLE_BLOCKED_LIST = "BlockedList"; // log call
    public static final int IDEX_BLACKLIST_NAME = 1;
    public static final int IDEX_BLACKLIST_NUMBER = 2;

    public CommonDbMethod(Context context) {
        this.context = context;
    }

    // add blacklist
    @SuppressLint("StringFormatInvalid")
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
            db.execSQL("create table IF NOT EXISTS " +TABLE_BLACK_LIST + "(sms_id varchar(20), names varchar(255), numbers varchar(20) UNIQUE)");

            // sử lý number khi nếu có khoảng trắng
            String [] str = number.split("\\s+");
            String number_stadand = "";
            for (int i = 0; i < str.length; i++) {
                number_stadand += str[i];
            }

            ContentValues values = new ContentValues();
            values.put("names", name);
            values.put("numbers", number_stadand);
            //values.put("body", body);

            db.insert(CommonDbMethod.TABLE_BLACK_LIST , null, values);
            db.close();
            Toast.makeText(context, context.getResources().getString(R.string.add_to_blacklist_success , number), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, context.getResources().getString(R.string.add_to_blacklist_error , e.getMessage()), Toast.LENGTH_LONG).show();
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



