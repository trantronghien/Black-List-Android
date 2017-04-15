package com.anddev.hientran.myapplication.broadcast;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.anddev.hientran.myapplication.R;
import com.anddev.hientran.myapplication.activitys.MainActivity;
import com.anddev.hientran.myapplication.databases.CommonDbMethod;
import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.anddev.hientran.myapplication.databases.CommonDbMethod.TABLE_BLOCKED_LIST;

/**
 * Created by HienTran on 9/23/2016.
 */

public class BlockingProcessReceiver extends BroadcastReceiver {

    private Context context;
    private final String EDITOR = "Call" ;
    private final String TAG_LOG = "BlockingProcessReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        // SharedPreferences.Editor đổi tượng Editor để cho phép chỉnh sửa dữ liệu
        SharedPreferences.Editor editor = context.getSharedPreferences(EDITOR , Context.MODE_PRIVATE).edit();

        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        // nếu không phải trang thái điện thoại thì bỏ qua
        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
            return;
        else {
            Log.d("check status call" , "đang có cuộc gọi ");
            // trang thái cuộc gọi đang chuông
            if(telephony.getCallState() == telephony.CALL_STATE_RINGING) {
                SharedPreferences prefs = context.getSharedPreferences("status_file", Context.MODE_PRIVATE);

                int idName = prefs.getInt("idName", 0); //0 is the default value.
                if (idName == 1) {
                    Log.i("No Need block", ".......");
                } else {
                    Log.i("Ringing" , ".......");
                    // Lấy Số cuộc gọi đến
                    String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy hh:mm:ss");

                    Calendar c = Calendar.getInstance();
                    String formattedDate = df.format(c.getTime());
                    checkBlackList(number, context, formattedDate);
                    Log.i("income number", "" + number);
                }
                // trạng thái cuộc gọi không hoạt động
            } else if (telephony.getCallState() == telephony.CALL_STATE_IDLE) {

                editor.putInt("idName", 0);
                editor.commit();
                // không có cuộc gọi nào đang chờ hoặc đang được giữ
            } else if (telephony.getCallState() == telephony.CALL_STATE_OFFHOOK) {
                editor.putInt("idName", 1);
                editor.commit();
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void disconnectPhoneiTelephony(Context context) {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void saveIncomingBlockedNumber(Context context, String name, String number, String formattedDate) {
        try {
            SQLiteDatabase db;
            db = context.openOrCreateDatabase(CommonDbMethod.mPATH, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            db.setVersion(1);
            db.setLocale(Locale.getDefault());
            db.setLockingEnabled(true);
            db.execSQL("create table IF NOT EXISTS "+ TABLE_BLOCKED_LIST +"(id integer primary key autoincrement, names varchar(20), numbers varchar(20), body varchar(250))");

            ContentValues values = new ContentValues();
            values.put("names", name);
            values.put("numbers", number);
            values.put("body", formattedDate);
            db.insert(CommonDbMethod.TABLE_BLOCKED_LIST, null, values);
            db.close();

        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    /**
     * kiểm tra số truyền vào có nằm trong blocked list hay không nếu có dừng
     * cuộc gọi luư log và push thông báo
     * @param mobileNumber
     * @param context
     * @param createdDate
     */
    private void checkBlackList(final String mobileNumber, final Context context, String createdDate) {
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(CommonDbMethod.mPATH, null, SQLiteDatabase.OPEN_READWRITE);

            //Kiểm tra số đó có nằm trong database không
            Cursor c = db.query(CommonDbMethod.TABLE_BLACK_LIST, null, "numbers=?", new String[] { mobileNumber }, null, null, null);
//            Cursor cursor = db.query(CommonDbMethod.TABLE_BLACK_LIST, null, null , null , null, null, null);


            if (c.moveToFirst() && c.getCount() > 0) {
                String name = c.getString(c.getColumnIndex("names"));
                AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                manager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                disconnectPhoneiTelephony(context);
                saveIncomingBlockedNumber(context, name , mobileNumber, createdDate);
                pushNotification(mobileNumber);

                manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

                c.close();
                db.close();
                Toast.makeText(context, "" + mobileNumber + " đang bị chặn", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e){
            Log.e("SMSBlocking", " " + e.getMessage());
        }

    }

    // Đặt Thông Báo Trên Thanh Trang Thái
    private void pushNotification(String fromAddress){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Call Blocked")
                .setContentText(fromAddress);

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, mBuilder.build());
    }

}
