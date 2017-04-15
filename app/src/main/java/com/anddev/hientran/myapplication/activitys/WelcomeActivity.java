package com.anddev.hientran.myapplication.activitys;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.anddev.hientran.myapplication.R;

public class WelcomeActivity extends AbsRuntimePermission {

    private static final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wecome);
        // cấp quyền nếu không cấp quyền với phiên bản 6.0 trở lên mới check quyền
        if(Build.VERSION.SDK_INT >= 23){
            requestAppPermissions(new String[]{
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.RECEIVE_SMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.READ_CONTACTS, },
                    R.string.msgPermission, REQUEST_PERMISSION);
        }
        else {
            new CountDownTimer(1000 , 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }
                @Override
                public void onFinish() {
                    Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }.start();
        }
    }


    // đã cho phép tất cả quyền đề xuất
    @Override
    public void onPermissionsGranted(int requestCode) {
        CountDownTimer countDownTimer = new CountDownTimer(1000 , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
    }

}
