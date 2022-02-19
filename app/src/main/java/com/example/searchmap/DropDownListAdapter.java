package com.example.searchmap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class DropDownListAdapter extends BaseAdapter {

    public DropDownClickInterface dropDownClickInterface;
    private Context mContext;
    int layout;
    public ArrayList<String> mTitle;
    public DropDownListAdapter(Context c, int layout, ArrayList<String> category,DropDownClickInterface dropDownClickInterface) {
        this.mContext = c;
        this.layout=layout;
        this.mTitle=category;
        this.dropDownClickInterface = dropDownClickInterface;
    }

    @Override
    public int getCount() {
        return mTitle.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RecordHolder holder=null;
        View row = convertView;
        if(convertView ==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layout, parent, false);
            holder = new RecordHolder();
            holder.textView=(TextView) row.findViewById(R.id.textView2);
//            holder.textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dropDownClickInterface.onItemClick(position);
//                }
//            });
            row.setTag(holder);
        }
        else{
            holder = (RecordHolder) row.getTag();
        }
        holder.textView.setText(mTitle.get(position));
        return row;
    }
    static class RecordHolder {
        TextView textView;

    }
}
