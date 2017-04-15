package com.anddev.hientran.myapplication.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.anddev.hientran.myapplication.R;
import com.anddev.hientran.myapplication.databases.CommonDbMethod;
import com.anddev.hientran.myapplication.models.MobileData;


import java.util.ArrayList;

/**
 * Created by HienTran on 9/24/2016.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private ArrayList<MobileData> mDataset;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtName;
        private TextView mTxtAge;
        private TextView mTxtNumber;

        private CardView cardView;

        public ViewHolder(View v) {
            super(v);
            mTxtName = (TextView) v.findViewById(R.id.person_name);
            mTxtAge = (TextView) v.findViewById(R.id.person_age);
            mTxtNumber = (TextView) v.findViewById(R.id.person_number);
            cardView = (CardView) v.findViewById(R.id.cv);
        }
    }


    public LogAdapter(ArrayList<MobileData> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }


    @Override
    public LogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loglist, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        MobileData mobileData = mDataset.get(position);

        holder.mTxtName.setText(mobileData.getCallerName());
        holder.mTxtNumber.setText(mobileData.getMobileNumber());
        holder.mTxtAge.setText(mobileData.getOtherString());

        holder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(context.getResources().getString(R.string.title_delete_log))
                        .setMessage(context.getResources().getString(R.string.message_dialog_delete_log))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(context.getString(R.string.discard_dialog_button_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteLog(position);
                            }})
                        .setNegativeButton(context.getString(R.string.discard_dialog_button_no), null).show();
            }

        });

    }

    private void deleteLog(int position) {
        new CommonDbMethod(context)
                .deleteLogNumber(mDataset.get(position).getOtherString(), mDataset.get(position).getMobileNumber(), CommonDbMethod.TABLE_BLOCKED_LIST);
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
        doButtonOneClickActions();

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void doButtonOneClickActions() {
        if(mOnDataChangeListener != null){
            mOnDataChangeListener.onDataChanged(mDataset.size());
        }
    }

    OnDataChangeListener mOnDataChangeListener;
    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener){
        mOnDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener{
        void onDataChanged(int size);
    }

}
