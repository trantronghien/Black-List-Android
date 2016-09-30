package com.anddev.hientran.myapplication.activitys;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.CallLog;
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
import com.anddev.hientran.myapplication.adapters.BlackListAdapter;
import com.anddev.hientran.myapplication.adapters.LogNumberAdapter;
import com.anddev.hientran.myapplication.adapters.PageFragmentAdapter;
import com.anddev.hientran.myapplication.blacklist.BlackListPresenter;
import com.anddev.hientran.myapplication.blacklist.BlackListService;
import com.anddev.hientran.myapplication.databases.CommonDbMethod;

import com.anddev.hientran.myapplication.fragment.BlackListFragment;
import com.anddev.hientran.myapplication.fragment.LogFragment;
import com.anddev.hientran.myapplication.inbox.InboxService;
import com.anddev.hientran.myapplication.models.MobileData;
import com.anddev.hientran.myapplication.models.NumberData;
import com.anddev.hientran.myapplication.util.UtilityMethod;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public FloatingActionButton floatbtnadd;
    Activity activity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = MainActivity.this;
        context = MainActivity.this;
        // giao dịch fragment



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PageFragmentAdapter(getFragmentManager(), MainActivity.this));
        viewPager.setCurrentItem(0, true);  // xét vị trí tab mặc định khi chương trình chạy

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position != 0){
                    floatbtnadd.hide();
                }
                else {
                    floatbtnadd.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // TabLayout dùng để xet tile tab
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

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
        dialog.setTitle("Thêm Số Vào Danh Sách Đen");
        String[] item_dialog = {"  Từ Tin Nhắn", "  Từ Nhật Ký Cuộc Gọi", "  Tự Nhập"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_selectable_list_item, item_dialog);
//            arrayAdapter.add("     From Inbox");
//            arrayAdapter.add("     From Log");
//            arrayAdapter.add("     Manual Entry");


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

                        if (which == 0) {
                            openDialogInbox("Hủy Bỏ");
                        } else if (which == 1) {
                            openDialogLog("Hủy Bỏ");
                        } else if (which == 2) {
                            openManualEntryDialog("Number", "Thêm", "Hủy Bỏ");
                        }
                    }
                });
        dialog.show();
    }

    // nhập số bằng tay
    String titleAction = "Danh Sách Chặn";
    ArrayList<MobileData> data = new ArrayList<>();
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
                new CommonDbMethod(context).addToNumberBlacklist("", editText.getText().toString().trim());
                activity.setTitle(titleAction);
                dialog.dismiss();
                // FIXME: 9/28/2016
                data = new BlackListService().getSmsInfo();
                new BlackListAdapter(data , context);

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
        view.setText("Nhật Ký");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                new CommonDbMethod(context).addToNumberBlacklist(getCallDetails().get(position).getSenderNumber(), getCallDetails().get(position).getSenderNumber());
                // UtilityMethod.blackListFragment(activity);
                activity.setTitle(titleAction);
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
                Toast.makeText(activity, "Quyền chưa được cấp", Toast.LENGTH_SHORT).show();
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

    // ẩn hiện menu
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);
//        menu.findItem(R.id.filter).setVisible(true);
//        menu.findItem(R.id.export).setVisible(true);
//        menu.findItem(R.id.import0).setVisible(true);
//        activity.setOnMenuItemClickListener(this);
//    }


    // dialog exit
    protected void openDialogExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bạn Thực Sự Muốn Thoát ?");
        builder.setNegativeButton("Cannel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do notthing
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
                onDestroy();
            }
        });
        builder.show();
    }
}
