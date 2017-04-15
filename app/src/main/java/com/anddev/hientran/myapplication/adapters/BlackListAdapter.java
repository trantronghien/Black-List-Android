package com.anddev.hientran.myapplication.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anddev.hientran.myapplication.R;
import com.anddev.hientran.myapplication.databases.CommonDbMethod;
import com.anddev.hientran.myapplication.interfaces.IBlacklistView;
import com.anddev.hientran.myapplication.models.MobileData;

import java.util.ArrayList;

/**
 * Created by HienTran on 9/23/2016.
 */

public class BlackListAdapter extends RecyclerView.Adapter<BlackListAdapter.ViewHolder> {

    private ArrayList<MobileData> mDataset;
    private Context context;

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTxtNumber;
        public TextView mTxtName;
        public ImageButton btnDelete;

        public ViewHolder(View v) {
            super(v);
            mTxtName = (TextView) v.findViewById(R.id.person_name);
            mTxtNumber = (TextView) v.findViewById(R.id.person_number);
            btnDelete = (ImageButton) v.findViewById(R.id.btnDelete);
        }
    }

    public BlackListAdapter(ArrayList<MobileData> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }


    // Create new views back list
    @Override
    public BlackListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blacklist, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MobileData mobileData = mDataset.get(position);
        holder.mTxtNumber.setText(mobileData.getMobileNumber().toString());
        holder.mTxtName.setText(mobileData.getCallerName().toString());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.title_delete_item))
                        .setMessage(context.getString(R.string.msg_delete_item))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(context.getString(R.string.discard_dialog_button_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                deleteItem(position);
                                doButtonOneClickActions();
                            }
                        })
                        .setNegativeButton(context.getString(R.string.discard_dialog_button_no), null).show();
            }
        });
    }

    public void deleteItem(int position) {
        CommonDbMethod commonDbMethod = new CommonDbMethod(context);
        commonDbMethod.deleteBlackListNumber(mDataset.get(position).getMobileNumber(), CommonDbMethod.TABLE_BLACK_LIST);
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public MobileData getItem(int position){
        return mDataset.get(position);
    }

    private void doButtonOneClickActions() {
        if (mOnDataChangeListener != null) {
            mOnDataChangeListener.onDataChanged(mDataset.size());
            notifyDataSetChanged();
        }
    }

    OnDataChangeListener mOnDataChangeListener;

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }

    public interface OnDataChangeListener {
        void onDataChanged(int size);
    }
}


