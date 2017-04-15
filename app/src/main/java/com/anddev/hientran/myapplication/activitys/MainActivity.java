package com.anddev.hientran.myapplication.activitys;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anddev.hientran.myapplication.R;
import com.anddev.hientran.myapplication.adapters.LogNumberAdapter;
import com.anddev.hientran.myapplication.adapters.PageFragmentAdapter;
import com.anddev.hientran.myapplication.databases.CommonDbMethod;

import com.anddev.hientran.myapplication.fragment.BlackListFragment;
import com.anddev.hientran.myapplication.fragment.LogFragment;
import com.anddev.hientran.myapplication.inbox.InboxService;
import com.anddev.hientran.myapplication.models.MobileData;
import com.anddev.hientran.myapplication.models.NumberData;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private FloatingActionButton floatbtnadd;
    private Activity activity;
    private Context context;
    private Resources resources;
    private String titleAction;
    private BlackListFragment blackListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;
        context = MainActivity.this;
        resources = context.getResources();
        titleAction = resources.getString(R.string.title_action);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        final PageFragmentAdapter viewAdapter = new PageFragmentAdapter(getFragmentManager(), MainActivity.this);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(viewAdapter);
        viewPager.setCurrentItem(0);

        Fragment fragment = viewAdapter.getItem(viewPager.getCurrentItem());
        if (fragment instanceof BlackListFragment) blackListFragment = (BlackListFragment) fragment;

        // TabLayout dùng để xet tile tab
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) floatbtnadd.hide();
                else floatbtnadd.show();
                Fragment fragment = viewAdapter.getItem(position);
                if (fragment instanceof LogFragment)((LogFragment) fragment).reloadWhenDataChanges();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        floatbtnadd = (FloatingActionButton) findViewById(R.id.fabtn_add);
        floatbtnadd.setOnClickListener(this);
    }


    // onclick addButton
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabtn_add:
                openActionDialog();
                return;
        }
    }



    // ============================================================================
    //             Xử Lý ActionDialog Tùy Chọn
    //============================================================================

    public void openActionDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setIcon(R.drawable.about);
        dialog.setTitle(R.string.title_dialong);
        String[] item_dialog = resources.getStringArray(R.array.item_dialog);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_selectable_list_item, item_dialog);
        dialog.setNegativeButton(R.string.discard_dialog_button_cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog.setAdapter(arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String canelButton = resources.getString(R.string.button_cancel);
                        if (which == 0) {
                            openDialogInbox(canelButton);
                        } else if (which == 1) {
                            openDialogLog(canelButton);
                        } else if (which == 2) {
                            openManualEntryDialog("Number", resources.getString(R.string.button_add), canelButton);
                        }
                    }
                });
        dialog.show();
    }

    // nhập số bằng tay
    private void openManualEntryDialog(String message, String okButton, String cancelButton) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_blacklist);
        dialog.setCanceledOnTouchOutside(false);

        TextView txtViewPopupMessage = (TextView) dialog.findViewById(R.id.txtViewPopupMessage);
        ImageButton imgBtnClose = (ImageButton) dialog.findViewById(R.id.imgBtnClose);
        final EditText editText = (EditText) dialog.findViewById(R.id.editText);

        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        btnAdd.setText(okButton);
        txtViewPopupMessage.setText(message);

        // khi click button dailog thêm number
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editText.getText().toString().trim();
                new CommonDbMethod(context).addToNumberBlacklist(getContactName(number), number);
                Log.i("Number" , "ten"+ getContactName(number));
                activity.setTitle(titleAction);
                dialog.dismiss();
                blackListFragment.updateListView();
            }
        });

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setText(cancelButton);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();
    }

    // mở dialog những số từ hộp thư đến (inbox )
    private void openDialogInbox(String cancelButton) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_inbox_lognumber);
        dialog.setCanceledOnTouchOutside(false);

        ListView listView = (ListView) dialog.findViewById(R.id.listViewInbox);
        final ArrayList<MobileData> mobileDatas = new InboxService().fetchInboxSms(context);

        final ArrayList<NumberData> numberDatas = new ArrayList<>();
        for (int i = 0; i < mobileDatas.size(); i++) {
            NumberData numberData = new NumberData();
            numberData.setSenderNumber(mobileDatas.get(i).getMobileNumber());
            numberDatas.add(numberData);
        }

        LogNumberAdapter inboxNumberAdapter = new LogNumberAdapter(activity, numberDatas);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        btnCancel.setText(cancelButton);
        listView.setAdapter(inboxNumberAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                new CommonDbMethod(context).addToNumberBlacklist(mobileDatas.get(position).getSmsThreadNo(), numberDatas.get(position).getSenderNumber());
                activity.setTitle(titleAction);
                blackListFragment.updateListView();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openDialogLog(String cancelButton) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_inbox_lognumber);
        dialog.setCanceledOnTouchOutside(false);

        TextView view = (TextView) dialog.findViewById(R.id.view);
        ListView listView = (ListView) dialog.findViewById(R.id.listViewInbox);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

        LogNumberAdapter inboxNumberAdapter = new LogNumberAdapter(activity, getCallDetails());
        btnCancel.setText(cancelButton);
        listView.setAdapter(inboxNumberAdapter);
        view.setText(R.string.title_logcall);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                new CommonDbMethod(context).addToNumberBlacklist(getCallDetails().get(position).getSenderNumber(), getCallDetails().get(position).getSenderNumber());
                activity.setTitle(titleAction);
                blackListFragment.updateListView();
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });

        dialog.show();
    }
    // truy vấn dữ liệu từ nhật ký cuộc gọi
    private ArrayList<NumberData> getCallDetails() {
        final ArrayList<NumberData> numberDatas = new ArrayList<>();
        try {
            StringBuffer sb = new StringBuffer();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, resources.getString(R.string.permission_not_granted), Toast.LENGTH_LONG).show();
            }
            Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            sb.append("Call Details :");
            while (managedCursor.moveToNext()) {
                String callType = managedCursor.getString(type);
                if (Integer.parseInt(callType) == CallLog.Calls.INCOMING_TYPE || Integer.parseInt(callType) == CallLog.Calls.MISSED_TYPE) {
                    String phNumber = managedCursor.getString(number);

                    String callDate = managedCursor.getString(date);
                    Date callDayTime = new Date(Long.valueOf(callDate));
                    String callDuration = managedCursor.getString(duration);
                    String dir = null;
                    int dircode = Integer.parseInt(callType);
                    switch (dircode) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            dir = "OUTGOING";
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            dir = "INCOMING";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            dir = "MISSED";
                            break;
                    }
                    NumberData numberData = new NumberData();
                    numberData.setSenderNumber(phNumber);
                    numberDatas.add(numberData);
                    sb.append("\n Phone Number:--- " + phNumber + " \n Call Type:--- " + dir + " \n Call Date:--- " + callDayTime + " \n Call duration in sec :--- " + callDuration);
                    sb.append("\n----------------------------------");
                }
            }
            managedCursor.close();
        } catch (Exception e) {
        }
        return numberDatas;
    }

    public String getContactName(String phoneNumber)
    {
        ContentResolver cr = getApplicationContext().getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null)
        {
            return null;
        }
        String contactName = "Unknown";
        if(cursor.moveToFirst())
        {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    //============================================================================
    //                              MENU
    //============================================================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_exit:
                openDialogExit();
                break;
            case R.id.menu_main_add_blacklist:
                openActionDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // dialog exit
    protected void openDialogExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(resources.getString(R.string.title_dialog_exit));
        builder.setNegativeButton(resources.getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(resources.getString(R.string.discard_dialog_button_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
                onDestroy();
            }
        });
        builder.show();
    }

}
