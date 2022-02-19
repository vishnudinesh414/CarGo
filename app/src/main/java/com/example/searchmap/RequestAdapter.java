package com.example.searchmap;

import android.app.Activity;
import android.content.Context;

import android.location.Address;
import android.location.Geocoder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;


public class RequestAdapter extends BaseAdapter {
    public ListViewClickInterface listViewClickInterface;
    public Context mContext;
    int layout;
    ArrayList<String> rootList;
    public String[] mTitle = {"hello","hello","hello","hello","hello","hello"};
    ArrayList<String> routeKeys ;

    public RequestAdapter(Context mContext,int layout,ArrayList<String> rootList,ArrayList<String> routeKeys,ListViewClickInterface listViewClickInterface) {
        this.mContext = mContext;
        this.layout=layout;
        this.rootList = rootList;
        this.listViewClickInterface = listViewClickInterface;
        this.routeKeys=routeKeys;
    }
    @Override
    public int getCount() {
        return rootList.size();
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
        ArrayList<String> outer = new ArrayList<String>();
        int i= 0;
        RecordHolder holder;
        View row = convertView;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layout, parent, false);
            holder = new RecordHolder();
            holder.textView= row.findViewById(R.id.textView);
            row.setTag(holder);
        }
        else
        {
            holder = (RecordHolder) row.getTag();
        }
//        requestActivity.getRouteKeys.add(routeKeys.get(position));
        holder.textView.setText(rootList.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listViewClickInterface.onItemClick(position, routeKeys.get(position));
            }
        });
        return row;

    }
    static class RecordHolder {
        TextView textView;
    }


}
